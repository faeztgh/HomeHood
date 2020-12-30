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

    public User(long id, String name, String lastName, String phoneNo, String email, String userName, String password, String fullAddress, int numberOfUnit, boolean smsChkbx, boolean emailChkbx) {
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
    }

    @Ignore
    public User(String name, String lastName, String phoneNo, String email, String userName, String password, String fullAddress, int numberOfUnit, boolean smsChkbx, boolean emailChkbx) {

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
