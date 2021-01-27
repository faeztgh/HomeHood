package ir.faez.assignment2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.ExpenseCudAsyncTask;
import ir.faez.assignment2.data.async.GetExpensesAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.network.NetworkHelper;
import ir.faez.assignment2.utils.Action;
import ir.faez.assignment2.utils.ExpenseAdapter;
import ir.faez.assignment2.utils.ListHelper;
import ir.faez.assignment2.utils.OnExpenseClickListener;
import ir.faez.assignment2.utils.Result;
import ir.faez.assignment2.utils.ResultListener;
import ir.faez.assignment2.utils.Status;


public class ExpensesActivity extends AppCompatActivity implements View.OnClickListener,
        OnExpenseClickListener {

    private static final String TAG = "EXPENSE_ACTIVITY";
    private static final int REQUEST_CODE = 1;
    private final String activityName = "EXPENSES";
    NetworkHelper networkHelper;
    private ImageView addNewExpenseIv;
    private ImageView backIv;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> allExpenses;
    private ListHelper listHelper;
    private List<Expense> currUserExpenses;


    //------------------------- Accessors------------------------------

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
        recyclerViewInit();
    }

    // initializing things that should be handled at start
    private void init() {
        if (currUserExpenses == null) {
            currUserExpenses = new ArrayList<>();
        }
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        listHelper = ListHelper.getInstance();
        getAllExpenses();
        invokeOnClickListeners();

    }


    private void getAllExpenses() {
        GetExpensesAsyncTask getExpensesAsyncTask = new
                GetExpensesAsyncTask(this, new DbResponse<List<Expense>>() {
            @Override
            public void onSuccess(List<Expense> loadedExpenses) {
                allExpenses = loadedExpenses;
                // gather all expenses belong to current user logged in and check the type
                gatherCurrUserExpenses();
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(ExpensesActivity.this, R.string.cantGetExpensesFromDb,
                        Toast.LENGTH_SHORT).show();
            }
        });
        if (MainActivity.currUser.getId() != null) {
            getExpensesAsyncTask.execute(MainActivity.currUser.getId());
        }
    }


    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.expenses_recycler_view);
        expenseAdapter = new ExpenseAdapter(this, currUserExpenses, this,
                activityName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);
    }



    // Double check for user specific expenses
    private void gatherCurrUserExpenses() {

        currUserExpenses = new ArrayList<>();
        if (allExpenses != null) {
            for (Expense exp : allExpenses) {
                if (exp != null) {
                    if (exp.getOwnerId().equals(MainActivity.currUser.getId())
                            && exp.getStatus().equals(Status.expenses)) {
                        currUserExpenses.add(exp);
                    }
                }
            }
        }

        recyclerViewInit();

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


    private void invokeOnClickListeners() {
        addNewExpenseIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
    }

    //------------------ OnExpenseListener Interface implementation --------------------------------




    @Override
    public void onItemRemoved(Expense expense, int position) {
        expense = currUserExpenses.get(position);

        if (expense != null) {
            //implementing Network
            Expense finalExpense = expense;
            networkHelper.deleteExpense(expense, MainActivity.currUser, new ResultListener<Expense>() {
                @Override
                public void onResult(Result<Expense> result) {
                    Log.d(TAG, "Result of deleting expense in server:  " + result);
                    Error error = result != null ? result.getError() : null;
                    Expense resultExp = result != null ? result.getItem() : null;

                    if (result == null || resultExp == null || error != null) {
                        String errorMsg = error != null ? error.getMessage() : getString(R.string.somethingWentWrongOnDelete);
                        Toast.makeText(ExpensesActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // removing from DB
                    ExpenseCudAsyncTask expenseCudAsyncTask =
                            new ExpenseCudAsyncTask(ExpensesActivity.this,
                            Action.DELETE_ACTION, new DbResponse<Expense>() {
                        @Override
                        public void onSuccess(Expense expense) {
                            //remove from ui
                            listHelper.removeExpense(expense);
                            expenseAdapter.removeItem(position);
                            Toast.makeText(ExpensesActivity.this, R.string.itemRemoved,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(ExpensesActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    expenseCudAsyncTask.execute(finalExpense);


                }
            });
        }


    }

    @Override
    public void onItemPayed(Expense expense, int position) {
        expense = currUserExpenses.get(position);
        expense.setStatus(Status.payments);
        if (expense != null) {
            // Implementing Network
            Expense finalExpense = expense;
            networkHelper.updateExpense(expense, MainActivity.currUser, new ResultListener<Expense>() {
                @Override
                public void onResult(Result<Expense> result) {
                    Log.d(TAG, "Result of updating expense status in server:  " + result);
                    Error error = result != null ? result.getError() : null;
                    Expense resultExp = result != null ? result.getItem() : null;

                    if (result == null || resultExp == null || error != null) {
                        String errorMsg = error != null ? error.getMessage() : getString(R.string.somethingWentWrongOnUpdate);
                        Toast.makeText(ExpensesActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // if server operation was successful, then ->
                    // implementing update expense status to db

                    ExpenseCudAsyncTask expenseCudAsyncTask = new ExpenseCudAsyncTask(ExpensesActivity.this,
                            Action.UPDATE_ACTION, new DbResponse<Expense>() {
                        @Override
                        public void onSuccess(Expense expense) {
                            listHelper.changeExpenseToPayment(expense);
                            expenseAdapter.removeItem(position);
                            Toast.makeText(ExpensesActivity.this, R.string.itemPayedSuccessfully,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(ExpensesActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    expenseCudAsyncTask.execute(finalExpense);
                }
            });
        }
    }
}
