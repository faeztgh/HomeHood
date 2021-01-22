package ir.faez.assignment2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.GetExpensesAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.utils.ExpenseAdapter;
import ir.faez.assignment2.utils.OnExpenseClickListener;
import ir.faez.assignment2.utils.Status;


public class ExpensesActivity extends AppCompatActivity implements View.OnClickListener, OnExpenseClickListener {

    private static final int REQUEST_CODE = 1;
    private static List<Expense> expensesList;
    private final String activityName = "EXPENSES";
    private ImageView addNewExpenseIv;
    private ExpenseAdapter expenseAdapter;
    private ImageView backIv;
    private List<Expense> allExpenses;

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

    //refresh the list after new expense created
    @Override
    protected void onRestart() {
        super.onRestart();
        getAllExpenses();

    }

    // initializing things that should be handled at start
    private void init() {
        if (expensesList == null) {
            expensesList = new ArrayList<>();
        }
        getAllExpenses();
        invokeOnClickListeners();

    }


    private void getAllExpenses() {
        GetExpensesAsyncTask getExpensesAsyncTask = new GetExpensesAsyncTask(this, new DbResponse<List<Expense>>() {
            @Override
            public void onSuccess(List<Expense> loadedExpenses) {
                allExpenses = loadedExpenses;
                // gather all expenses belong to current user logged in and check the type
                gatherCurrUserExpenses();
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(ExpensesActivity.this, R.string.cantGetExpensesFromDb, Toast.LENGTH_SHORT).show();
            }
        });
        getExpensesAsyncTask.execute(MainActivity.currUser.getId());
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
                        R.string.inappropriateInput,
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }

    // opening NewExpense Activity to adding new expense item
    private void addNewExpenseBtn() {
        Intent intent = new Intent(this, NewExpenseActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    //------------------ OnExpenseListener Interface implementation --------------------------------
    @Override
    public void onItemRemoved(int position) {

        expenseAdapter.removeItem(position);
        Toast.makeText(this, R.string.itemRemoved, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemPayed(int position) {
        expenseAdapter.payItem(position, activityName);
        Toast.makeText(this, R.string.itemPayedSuccessfully, Toast.LENGTH_SHORT).show();

    }


    private void gatherCurrUserExpenses() {

        expensesList = new ArrayList<>();
        if (allExpenses != null) {
            for (Expense exp : allExpenses) {
                if (exp != null) {
                    if (exp.getOwnerId() == MainActivity.currUser.getId() && exp.getStatus().equals(Status.expenses)) {
                        expensesList.add(exp);
                    }
                }
            }
        }


        recyclerViewInit();

    }
}
