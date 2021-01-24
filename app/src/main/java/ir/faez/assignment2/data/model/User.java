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
    @SerializedName("objectId")
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
    private String isLoggedIn;


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

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public int getNumberOfUnit() {
        return numberOfUnit;
    }

    public void setNumberOfUnit(int numberOfUnit) {
        this.numberOfUnit = numberOfUnit;
    }

    public boolean isSmsChkbx() {
        return smsChkbx;
    }

    public void setSmsChkbx(boolean smsChkbx) {
        this.smsChkbx = smsChkbx;
    }

    public boolean isEmailChkbx() {
        return emailChkbx;
    }

    public void setEmailChkbx(boolean emailChkbx) {
        this.emailChkbx = emailChkbx;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(String loggedIn) {
        isLoggedIn = loggedIn;
    }
}
