package udacity.george.journalapp.utilities.viewmodelfactories;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.viewmodels.AddJournalEntryViewModel;

public class AddJournalEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mJournalEntryId;

    public AddJournalEntryViewModelFactory(AppDatabase database, int journalEntryId) {
        mDb = database;
        mJournalEntryId = journalEntryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddJournalEntryViewModel(mDb, mJournalEntryId);
    }
}
