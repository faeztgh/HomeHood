package ir.faez.assignment2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.GetExpensesAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.databinding.ActivityPaymentsBinding;
import ir.faez.assignment2.utils.ExpenseAdapter;
import ir.faez.assignment2.utils.OnExpenseClickListener;
import ir.faez.assignment2.utils.Status;

public class PaymentsActivity extends AppCompatActivity implements View.OnClickListener, OnExpenseClickListener {

    private static List<Expense> paymentsList;
    private final String activityName = "PAYMENTS";
    ActivityPaymentsBinding binding;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> allExpenses;

    //------------------------ Accessors --------------------------


    public static List<Expense> getPaymentsList() {
        if (paymentsList == null) {
            paymentsList = new ArrayList<>();
        }
        return paymentsList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    // initializing things that should be handled at start
    private void init() {
        binding = ActivityPaymentsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (paymentsList == null) {
            paymentsList = new ArrayList<>();
        }
        getAllExpenses();
        invokeOnClickListeners();
        recyclerViewInit();
    }

    private void getAllExpenses() {
        GetExpensesAsyncTask getExpensesAsyncTask = new GetExpensesAsyncTask(this, new DbResponse<List<Expense>>() {
            @Override
            public void onSuccess(List<Expense> loadedExpenses) {
                allExpenses = loadedExpenses;
                // gather all expenses belong to current user logged in
                gatherCurrUserPayments();
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(PaymentsActivity.this, R.string.cantGetExpensesFromDb, Toast.LENGTH_SHORT).show();
            }
        });
        getExpensesAsyncTask.execute();
    }

    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.payments_recycler_view);
        expenseAdapter = new ExpenseAdapter(this, paymentsList,
                this, activityName);
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
    @Override
    public void onItemRemoved(int position) {
        expenseAdapter.removeItem(position);
        Toast.makeText(this, R.string.itemRemoved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemPayed(int position) {
        expenseAdapter.payItem(position, activityName);
        Toast.makeText(this, R.string.itemCheckedSuccessfully, Toast.LENGTH_SHORT).show();
    }

    private void gatherCurrUserPayments() {

        paymentsList = new ArrayList<>();
        if (allExpenses != null) {
            for (Expense exp : allExpenses) {
                if (exp != null) {
                    if (exp.getOwnerId() == MainActivity.currUser.getId() && exp.getStatus().equals(Status.payments)) {
                        paymentsList.add(exp);
                    }
                }
            }
        }


        recyclerViewInit();

    }
}
