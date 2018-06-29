package udacity.george.journalapp.utilities.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import udacity.george.journalapp.utilities.entity.JournalEntries;

@Dao
public interface JournalEntriesDao {
    @Query("SELECT * FROM journal ORDER BY updated_at desc")
    LiveData<List<JournalEntries>> loadAllEntries();

    @Insert
    void insertEntry(JournalEntries journalEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(JournalEntries journalEntries);

    @Delete
    void deleteEntry(JournalEntries journalEntries);

    @Query("SELECT * FROM journal WHERE id = :id")
    LiveData<JournalEntries> loadEntryById(int id);
}
