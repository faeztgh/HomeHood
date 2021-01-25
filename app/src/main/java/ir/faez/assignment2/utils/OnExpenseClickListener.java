package ir.faez.assignment2.utils;


import ir.faez.assignment2.data.model.Expense;

public interface OnExpenseClickListener {
    void onItemRemoved(Expense expense, int position);

    void onItemPayed(Expense expense, int position);


}
