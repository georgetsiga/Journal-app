package udacity.george.journalapp.utilities.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import udacity.george.journalapp.base.DateConverter;
import udacity.george.journalapp.utilities.dao.JournalEntriesDao;
import udacity.george.journalapp.utilities.dao.UsersDao;
import udacity.george.journalapp.utilities.entity.JournalEntries;
import udacity.george.journalapp.utilities.entity.Users;

@Database(entities = {JournalEntries.class, Users.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "journal_book";
    private static AppDatabase sInstance;

    public abstract UsersDao usersDao();
    public abstract JournalEntriesDao journalEntriesDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }
}
