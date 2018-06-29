package udacity.george.journalapp.utilities.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.JournalEntries;

public class JournalBookViewModel extends AndroidViewModel {
    private static final String TAG = UsersViewModel.class.getSimpleName();

    private final LiveData<List<JournalEntries>> journalEntries;

    public JournalBookViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        journalEntries = database.journalEntriesDao().loadAllEntries();
    }

    public LiveData<List<JournalEntries>> getJournalEntries() {
        return journalEntries;
    }
}
