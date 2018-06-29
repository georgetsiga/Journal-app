package udacity.george.journalapp.utilities.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import udacity.george.journalapp.utilities.entity.Users;
@Dao
public interface UsersDao {
    @Query("SELECT * FROM users ORDER BY created_at desc")
    LiveData<List<Users>> loadAllUsers();

    @Insert
    void insertUser(Users journalBook);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(Users journalBook);

    @Delete
    void deleteUser(Users journalBook);

    @Query("SELECT * FROM users WHERE googleId = :id")
    LiveData<Users> loadUserById(int id);

    @Query("SELECT * FROM users WHERE isLoggedIn = :isLoggedIn")
    Users getUserByLoginStatusId(int isLoggedIn);
}
