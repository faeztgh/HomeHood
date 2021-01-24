package ir.faez.assignment2.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Expense implements Serializable {
    // Instance fields
    @PrimaryKey
    @NonNull
    @SerializedName("ObjectId")
    private String id;
    private String ownerId;
    private String title;
    private String amount;
    private String paymentDate;
    private String expenseType;
    private String status;

    //constructors
    public Expense(String id, String ownerId, String title, String amount, String paymentDate, String expenseType, String status) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expenseType = expenseType;
        this.status = status;
    }

    @Ignore
    public Expense(String ownerId, String title, String amount, String paymentDate, String expenseType, String status) {
        this.ownerId = ownerId;
        this.title = title;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expenseType = expenseType;
        this.status = status;
    }

    //-------------------------- Accessors -------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
