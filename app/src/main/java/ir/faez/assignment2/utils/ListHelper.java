package ir.faez.assignment2.utils;

import java.util.ArrayList;
import java.util.List;

import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.utils.Status;

public class ListHelper {

    private static ListHelper sInstance;

    private List<Expense> mExpensesList;
    private List<Expense> mPaymentsList;

    private ListHelper() {
        mExpensesList = new ArrayList<>();
        mPaymentsList = new ArrayList<>();
    }

    public synchronized static ListHelper getInstance() {
        if (sInstance == null) {
            sInstance = new ListHelper();
        }
        return sInstance;
    }

    public boolean insertExpense(Expense expense) {
        return mExpensesList.add(expense);
    }

    public boolean insertPayment(Expense expense) {
        return mPaymentsList.add(expense);
    }

    public boolean removeExpense(Expense expense) {
        return mExpensesList.remove(expense);
    }

    public boolean removePayment(Expense expense) {
        return mPaymentsList.remove(expense);
    }

    public List<Expense> getExpensesList() {
        return mExpensesList;
    }

    public List<Expense> getPaymentsList() {
        return mPaymentsList;
    }

    public List<Expense> getTotal() {
        List<Expense> result = new ArrayList<>();
        result.addAll(mExpensesList);
        result.addAll(mPaymentsList);
        return result;
    }

    public void setTotal(List<Expense> all) {

        for (Expense expense : all) {
            if (expense.getStatus().equals(Status.payments)) {
                mPaymentsList.add(expense);
            } else {
                mExpensesList.add(expense);
            }
        }
    }

    public boolean changeExpenseToPayment(Expense expense) {
        return removeExpense(expense) && insertPayment(expense);
    }

    public boolean changePaymentToExpense(Expense payment) {
        return removePayment(payment) && insertExpense(payment);
    }
}
