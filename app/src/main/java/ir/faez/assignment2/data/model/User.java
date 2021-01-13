package ir.faez.assignment2.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String lastName;
    private String phoneNo;
    private String email;
    private String userName;
    private String password;
    private String fullAddress;
    private int numberOfUnit;
    private boolean smsChkbx;
    private boolean emailChkbx;
    private double latitude;
    private double longitude;


    public User(long id, String name, String lastName, String phoneNo, String email, String userName
            , String password, String fullAddress, int numberOfUnit, boolean smsChkbx
            , boolean emailChkbx, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.fullAddress = fullAddress;
        this.numberOfUnit = numberOfUnit;
        this.smsChkbx = smsChkbx;
        this.emailChkbx = emailChkbx;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public User(String name, String lastName, String phoneNo, String email, String userName
            , String password, String fullAddress, int numberOfUnit, boolean smsChkbx
            , boolean emailChkbx, double latitude, double longitude) {

        this.name = name;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.fullAddress = fullAddress;
        this.numberOfUnit = numberOfUnit;
        this.smsChkbx = smsChkbx;
        this.emailChkbx = emailChkbx;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
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


    public String getUserName() {
        return userName;
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
}
