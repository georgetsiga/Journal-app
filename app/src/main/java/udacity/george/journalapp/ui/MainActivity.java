package udacity.george.journalapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import udacity.george.journalapp.R;
import udacity.george.journalapp.adapters.JournalAdapter;
import udacity.george.journalapp.base.AppExecutors;
import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.JournalEntries;
import udacity.george.journalapp.utilities.entity.Users;
import udacity.george.journalapp.utilities.viewmodels.JournalBookViewModel;

import android.arch.lifecycle.LiveData;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private JournalAdapter mJournalAdapter;
    private  Boolean mDoubleBackToExitPressedOnce = false;
    private AppDatabase mDb;
    private TextView mWelcome;
    private ImageView mUserImage;
    private String mImageLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWelcome = findViewById(R.id.text_welcome);
        mUserImage = findViewById(R.id.image_user);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mJournalAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mJournalAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<JournalEntries> journalEntries = mJournalAdapter.getJournalEntries();
                        mDb.journalEntriesDao().deleteEntry(journalEntries.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent journalIntent = new Intent(MainActivity.this, JournalActivity.class);
                startActivity(journalIntent);
            }
        });
        mDb = AppDatabase.getInstance(getApplicationContext());
        getUserDetails();
        setupViewModel();
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

    private void setupViewModel() {
        JournalBookViewModel viewModel = ViewModelProviders.of(this).get(JournalBookViewModel.class);
        viewModel.getJournalEntries().observe(this, new Observer<List<JournalEntries>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntries> taskEntries) {
                mJournalAdapter.setJournalEntries(taskEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, JournalActivity.class);
        intent.putExtra(JournalActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_log_out) {
            try {
                LiveData<List<Users>> liveDataList = mDb.usersDao().loadAllUsers();
                liveDataList.observe(MainActivity.this, new Observer<List<Users>>() {
                    @Override
                    public void onChanged(@Nullable final List<Users> currentUsers) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(currentUsers != null){
                                    for (Users u : currentUsers) {
                                        u.setIsLoggedIn(0);
                                        mDb.usersDao().updateUser(u);
                                    }
                                }
                            }
                        });

                    }
                });
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getUserDetails(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Users user = mDb.usersDao().getUserByLoginStatusId(1);
                if(user != null){
                    mWelcome.setText(String.format("%s %s",user.getFirstName(),user.getLastName())) ;
                    mImageLocation = user.getImageUrl();
                    loadImage();
                }
            }
        });
    }

    private void loadImage(){
        try {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    Picasso.get()
                            .load(mImageLocation)
                            .into(mUserImage);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
