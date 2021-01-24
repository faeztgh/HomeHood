package ir.faez.assignment2.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("ObjectId")
    private String id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String lastName;
    @ColumnInfo
    private String phoneNo;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String username;
    @ColumnInfo
    private String password;
    @ColumnInfo
    private String fullAddress;
    @ColumnInfo
    private String sessionToken;
    @ColumnInfo
    private int numberOfUnit;
    @ColumnInfo
    private boolean smsChkbx;
    @ColumnInfo
    private boolean emailChkbx;
    @ColumnInfo
    private double latitude;
    @ColumnInfo
    private double longitude;


    public User(String id, String name, String lastName, String phoneNo, String email, String username
            , String password, String fullAddress, int numberOfUnit, boolean smsChkbx
            , boolean emailChkbx, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullAddress = fullAddress;
        this.numberOfUnit = numberOfUnit;
        this.smsChkbx = smsChkbx;
        this.emailChkbx = emailChkbx;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public User(String name, String lastName, String phoneNo, String email, String username
            , String password, String fullAddress, int numberOfUnit, boolean smsChkbx
            , boolean emailChkbx, double latitude, double longitude) {

        this.name = name;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullAddress = fullAddress;
        this.numberOfUnit = numberOfUnit;
        this.smsChkbx = smsChkbx;
        this.emailChkbx = emailChkbx;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }


    public String getPhoneNo() {
        return phoneNo;
    }


    public String getEmail() {
        return email;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getFullAddress() {
        return fullAddress;
    }


    public int getNumberOfUnit() {
        return numberOfUnit;
    }


    public boolean isSmsChkbx() {
        return smsChkbx;
    }


    public boolean isEmailChkbx() {
        return emailChkbx;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
