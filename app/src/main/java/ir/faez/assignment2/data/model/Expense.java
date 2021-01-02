package ir.faez.assignment2.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Expense implements Serializable {
    // Instance fields
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long ownerId;
    private String title;
    private String amount;
    private String paymentDate;
    private String expenseType;


    //constructors
    public Expense(long id, long ownerId, String title, String amount, String paymentDate, String expenseType) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expenseType = expenseType;
    }

    @Ignore
    public Expense(long ownerId, String title, String amount, String paymentDate, String expenseType) {
        this.ownerId = ownerId;
        this.title = title;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expenseType = expenseType;
    }

    //-------------------------- Accessors -------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getOwnerId() {
        return ownerId;
    }
}
