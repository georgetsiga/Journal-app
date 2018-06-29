package udacity.george.journalapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import udacity.george.journalapp.R;
import udacity.george.journalapp.base.AppExecutors;
import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.JournalEntries;
import udacity.george.journalapp.utilities.entity.Users;
import udacity.george.journalapp.utilities.viewmodelfactories.AddJournalEntryViewModelFactory;
import udacity.george.journalapp.utilities.viewmodels.AddJournalEntryViewModel;

public class JournalActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraJournalId";
    public static final String INSTANCE_TASK_ID = "instanceJournalId";
    private static final int DEFAULT_TASK_ID = -1;
    EditText mEditTextTitle, mEditTextContents;
    Button mButtonSave;
    private AppDatabase mDb;
    TextView mWelcome;

    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());
        mWelcome = findViewById(R.id.text_welcome);

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButtonSave.setText(R.string.update_button);
            mWelcome.setText(R.string.text_edit_entry);
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                AddJournalEntryViewModelFactory factory = new AddJournalEntryViewModelFactory(mDb, mTaskId);
                final AddJournalEntryViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddJournalEntryViewModel.class);

                viewModel.getJournalEntriesLiveData().observe(this, new Observer<JournalEntries>() {
                    @Override
                    public void onChanged(@Nullable JournalEntries taskEntry) {
                        viewModel.getJournalEntriesLiveData().removeObserver(this);
                        populateUI(taskEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
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
                liveDataList.observe(JournalActivity.this, new Observer<List<Users>>() {
                    @Override
                    public void onChanged(@Nullable final List<Users> currentUsers) {
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (currentUsers != null) {
                                    for (Users u : currentUsers) {
                                        u.setIsLoggedIn(0);
                                        mDb.usersDao().updateUser(u);
                                    }
                                }
                            }
                        });

                    }
                });
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mEditTextTitle = findViewById(R.id.edit_title);
        mEditTextContents = findViewById(R.id.edit_contents);

        mButtonSave = findViewById(R.id.button_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void populateUI(JournalEntries journalEntries) {
        if (journalEntries == null) {
            return;
        }

        mEditTextTitle.setText(journalEntries.getTitle());
        mEditTextContents.setText(journalEntries.getContents());
    }

    public void onSaveButtonClicked() {
        String title = mEditTextTitle.getText().toString();
        String contents = mEditTextContents.getText().toString();
        Date date = new Date();

        final JournalEntries journalEntries = new JournalEntries(title, contents, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTaskId == DEFAULT_TASK_ID) {
                    // insert new task
                    mDb.journalEntriesDao().insertEntry(journalEntries);
                } else {
                    //update task
                    journalEntries.setId(mTaskId);
                    mDb.journalEntriesDao().updateEntry(journalEntries);
                }
                finish();
            }
        });
    }
}
