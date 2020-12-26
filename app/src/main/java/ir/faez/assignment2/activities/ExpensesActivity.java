package ir.faez.assignment2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.faez.assignment2.utils.ExpenseAdapter;
import ir.faez.assignment2.R;
import ir.faez.assignment2.beans.Expense;
import ir.faez.assignment2.utils.OnExpenseClickListener;


public class ExpensesActivity extends AppCompatActivity implements View.OnClickListener, OnExpenseClickListener {

    private static final int REQUEST_CODE = 1;
    private static List<Expense> expensesList;
    private final String activityName = "EXPENSES";
    private ImageView addNewExpenseIv;
    private ExpenseAdapter expenseAdapter;
    private ImageView backIv;

    //------------------------- Accessors------------------------------
    public static List<Expense> getExpensesList() {
        if (expensesList == null) {
            return expensesList = new ArrayList<>();
        }
        return expensesList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        addNewExpenseIv = findViewById(R.id.addNewExpense_iv);
        backIv = findViewById(R.id.expenses_back_iv);
        init();
    }

    // initializing things that should be handled at start
    private void init() {
        if (expensesList == null) {
            expensesList = new ArrayList<>();
        }
        invokeOnClickListeners();
        recyclerViewInit();
    }

    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.expenses_recycler_view);
        expenseAdapter = new ExpenseAdapter(this, expensesList, this, activityName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);
    }

    private void invokeOnClickListeners() {
        addNewExpenseIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
    }

    //----------------------------- Implementing View.OnclickListener ------------------------------
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addNewExpense_iv:
                addNewExpenseBtn();
                break;
            case R.id.expenses_back_iv:
                finish();
                break;
            default:
                Toast.makeText(ExpensesActivity.this,
                        "Inappropriate input!",
                        Toast.LENGTH_SHORT).show();
                break;


        }

    }

    // opening NewExpense Activity to adding new expense item
    private void addNewExpenseBtn() {
        Intent intent = new Intent(this, NewExpenseActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    // getting expense object from NewExpenseActivity and add it to expenseList
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    Expense expRes = (Expense) data.getSerializableExtra("EXPENSE_OBJ");

                    if (expensesList == null) {
                        expensesList = new ArrayList<>();
                    }
                    expensesList.add(expensesList.size(), expRes);
                    recyclerViewInit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------ OnExpenseListener Interface implementation --------------------------------
    @Override
    public void onItemRemoved(int position) {
        expenseAdapter.removeItem(position);
        Toast.makeText(this, "Item Removed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemPayed(int position) {
        expenseAdapter.payItem(position, activityName);
        Toast.makeText(this, "Item Payed Successfully!", Toast.LENGTH_SHORT).show();

    }
}
