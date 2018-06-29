package udacity.george.journalapp.utilities.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.JournalEntries;

public class AddJournalEntryViewModel  extends ViewModel {
    // COMPLETED (6) Add a task member variable for the TaskEntry object wrapped in a LiveData
    private final LiveData<JournalEntries> journalEntriesLiveData;

    // COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public AddJournalEntryViewModel(AppDatabase database, int taskId) {
        journalEntriesLiveData = database.journalEntriesDao().loadEntryById(taskId);
    }

    // COMPLETED (7) Create a getter for the task variable
    public LiveData<JournalEntries> getJournalEntriesLiveData() {
        return journalEntriesLiveData;
    }
}
