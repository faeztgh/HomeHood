package ir.faez.assignment2;

import java.io.Serializable;

public class Expense implements Serializable {
    // Instance fields
    private String title;
    private String amount;
    private String paymentDate;
    private String expenseType;


    //constructor
    public Expense(String title, String amount, String paymentDate, String expenseType) {
        this.title = title;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expenseType = expenseType;
    }

    //-------------------------- Accessors -------------------------------------
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
}
