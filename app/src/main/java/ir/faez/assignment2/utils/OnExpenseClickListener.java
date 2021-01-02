package ir.faez.assignment2.utils;


import ir.faez.assignment2.data.model.Expense;

public interface OnExpenseClickListener {
    void onItemRemoved(int position);

    void onItemPayed(int position);

}
