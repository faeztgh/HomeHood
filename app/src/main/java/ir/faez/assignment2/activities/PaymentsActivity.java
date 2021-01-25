package ir.faez.assignment2.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import ir.faez.assignment2.databinding.ActivityPaymentsBinding;
import ir.faez.assignment2.network.NetworkHelper;
import ir.faez.assignment2.utils.Action;
import ir.faez.assignment2.utils.ExpenseAdapter;
import ir.faez.assignment2.utils.ListHelper;
import ir.faez.assignment2.utils.OnExpenseClickListener;
import ir.faez.assignment2.utils.Result;
import ir.faez.assignment2.utils.ResultListener;
import ir.faez.assignment2.utils.Status;

public class PaymentsActivity extends AppCompatActivity implements View.OnClickListener,
        OnExpenseClickListener {

    private static final String TAG = "PAYMENTS_ACTIVITY";
    private final String activityName = "PAYMENTS";
    ActivityPaymentsBinding binding;

    private ExpenseAdapter expenseAdapter;
    private List<Expense> allExpenses;
    private ListHelper listHelper;
    private List<Expense> currUserPayments;
    private NetworkHelper networkHelper;

    //------------------------ Accessors --------------------------


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    // initializing things that should be handled at start
    private void init() {

        if (currUserPayments == null) {
            currUserPayments = new ArrayList<>();
        }

        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        binding = ActivityPaymentsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        listHelper = ListHelper.getInstance();


        getAllExpenses();
        invokeOnClickListeners();
        recyclerViewInit();

    }

    private void getAllExpenses() {
        GetExpensesAsyncTask getExpensesAsyncTask = new
                GetExpensesAsyncTask(this, new DbResponse<List<Expense>>() {
            @Override
            public void onSuccess(List<Expense> loadedExpenses) {
                allExpenses = loadedExpenses;
                // gather all expenses belong to current user logged in and check the type
                gatherCurrUserPayments();
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(PaymentsActivity.this, R.string.cantGetExpensesFromDb,
                        Toast.LENGTH_SHORT).show();
            }
        });
        if (MainActivity.currUser.getId() != null) {
            getExpensesAsyncTask.execute(MainActivity.currUser.getId());
        }
    }

    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.payments_recycler_view);
        expenseAdapter = new ExpenseAdapter(this, currUserPayments, this,
                activityName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);
    }

    // invoke OnClickListeners
    private void invokeOnClickListeners() {
        binding.paymentsBackIv.setOnClickListener(this);
    }

    //----------------------------- Implementing View.OnclickListener ------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payments_back_iv:
                finish();
                break;
            default:
                Toast.makeText(PaymentsActivity.this,
                        R.string.inappropriateInput,
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //------------------ OnExpenseListener Interface implementation --------------------------------


    private void gatherCurrUserPayments() {

        currUserPayments = new ArrayList<>();
        if (allExpenses != null) {
            for (Expense exp : allExpenses) {
                if (exp != null) {
                    if (exp.getOwnerId().equals(MainActivity.currUser.getId())
                            && exp.getStatus().equals(Status.payments)) {
                        currUserPayments.add(exp);
                    }
                }
            }
        }

        recyclerViewInit();

    }

    @Override
    public void onItemRemoved(Expense expense, int position) {
        expense = currUserPayments.get(position);

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
                        Toast.makeText(PaymentsActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // removing from DB
                    ExpenseCudAsyncTask expenseCudAsyncTask = new ExpenseCudAsyncTask(PaymentsActivity.this,
                            Action.DELETE_ACTION, new DbResponse<Expense>() {
                        @Override
                        public void onSuccess(Expense expense) {
                            //remove from ui
                            listHelper.removeExpense(expense);
                            expenseAdapter.removeItem(position);
                            Toast.makeText(PaymentsActivity.this, R.string.itemRemoved,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(PaymentsActivity.this, error.getMessage(),
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
        expense = currUserPayments.get(position);
        expense.setStatus(Status.expenses);
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
                        Toast.makeText(PaymentsActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // if server operation was successful, then ->
                    // implementing update expense status to db

                    ExpenseCudAsyncTask expenseCudAsyncTask = new ExpenseCudAsyncTask(PaymentsActivity.this,
                            Action.UPDATE_ACTION, new DbResponse<Expense>() {
                        @Override
                        public void onSuccess(Expense expense) {
                            listHelper.changePaymentToExpense(expense);
                            expenseAdapter.removeItem(position);
                            Toast.makeText(PaymentsActivity.this, R.string.itemCheckedSuccessfully,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(PaymentsActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    expenseCudAsyncTask.execute(finalExpense);

                }
            });
        }
    }
}
