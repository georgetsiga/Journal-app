package udacity.george.journalapp.utilities.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.Users;

public class UsersViewModel extends AndroidViewModel {
    private static final String TAG = UsersViewModel.class.getSimpleName();

    private final LiveData<List<Users>> users;

    public UsersViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        users = database.usersDao().loadAllUsers();
    }

    public LiveData<List<Users>> getUsers() {
        return users;
    }
}
