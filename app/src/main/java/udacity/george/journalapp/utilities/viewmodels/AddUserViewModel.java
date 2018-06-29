package udacity.george.journalapp.utilities.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import udacity.george.journalapp.utilities.database.AppDatabase;
import udacity.george.journalapp.utilities.entity.Users;

public class AddUserViewModel  extends ViewModel {
   private final LiveData<Users> usersLiveData;
    public AddUserViewModel(AppDatabase database, int taskId) {
        usersLiveData = database.usersDao().loadUserById(taskId);
    }

    public LiveData<Users> getUsersLiveData() {
        return usersLiveData;
    }
}
