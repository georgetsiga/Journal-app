package udacity.george.journalapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

import udacity.george.journalapp.R;
import udacity.george.journalapp.base.AppExecutors;
import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.Users;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount mAccount;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int DEFAULT_TASK_ID = -1;
    public static final String INSTANCE_TASK_ID = "instanceLoginId";
    private  Boolean mDoubleBackToExitPressedOnce = false;

    private AppDatabase mDb;
    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDb = AppDatabase.getInstance(getApplicationContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }
signInButton = findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(this);
        checkSignInStatus();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                signIn();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onDestroy();
            killProcess();
        }
        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit the App", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDoubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void killProcess(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void checkSignInStatus() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Users user = mDb.usersDao().getUserByLoginStatusId(1);
                if (user == null) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();

                    mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                            .enableAutoManage(LoginActivity.this, LoginActivity.this)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                            .build();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Date date = new Date();
            final Users user = new Users(account.getId(), account.getGivenName(), account.getFamilyName(), account.getEmail(),String.valueOf(account.getPhotoUrl()), 1, date);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.usersDao().insertUser(user);
                    finish();
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }
    }

    private void handleSignResult(GoogleSignInResult result) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + result.isSuccess());
        if (result.isSuccess()) {
            mAccount = result.getSignInAccount();
            updateUI(mAccount);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(mAccount);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
