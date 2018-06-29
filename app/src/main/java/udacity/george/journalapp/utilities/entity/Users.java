package udacity.george.journalapp.utilities.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
@Entity(tableName="users")
public class Users {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String googleId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String imageUrl;
    private int isLoggedIn;
    @ColumnInfo(name = "created_at")
    private Date createdAt;


    public Users(int id, String googleId, String firstName, String lastName, String emailAddress,String imageUrl, int isLoggedIn, Date createdAt) {
        this.id = id;
        this.googleId = googleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.imageUrl = imageUrl;
        this.isLoggedIn = isLoggedIn;
        this.createdAt = createdAt;
    }
    @Ignore
    public Users(String googleId, String firstName, String lastName, String emailAddress,String imageUrl, int isLoggedIn, Date createdAt) {
        this.googleId = googleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.imageUrl = imageUrl;
        this.isLoggedIn = isLoggedIn;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
