package udacity.george.journalapp.utilities.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "journal")
public class JournalEntries {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String contents;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public JournalEntries(int id, String title, String contents, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.updatedAt = updatedAt;
    }

    @Ignore
    public JournalEntries(String title, String contents, Date updatedAt) {
        this.title = title;
        this.contents = contents;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
