package udacity.george.journalapp.utilities.viewmodelfactories;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.viewmodels.AddUserViewModel;

public class AddUserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mUserId;

    public AddUserViewModelFactory(AppDatabase database, int userId) {
        mDb = database;
        mUserId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddUserViewModel(mDb, mUserId);
    }
}
