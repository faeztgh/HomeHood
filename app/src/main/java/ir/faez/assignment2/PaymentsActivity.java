package ir.faez.assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.faez.assignment2.databinding.ActivityPaymentsBinding;

public class PaymentsActivity extends AppCompatActivity implements View.OnClickListener, OnExpenseClickListener {

    private static List<Expense> paymentsExpenseList;
    private final String activityName = "PAYMENTS";
    ActivityPaymentsBinding binding;
    private ExpenseAdapter expenseAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        init();
    }

    // initializing things that should be handled at start
    private void init() {
        binding = ActivityPaymentsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (paymentsExpenseList == null) {
            paymentsExpenseList = new ArrayList<>();
        }

        invokeOnClickListeners();
        recyclerViewInit();
    }

    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.payments_recycler_view);
        expenseAdapter = new ExpenseAdapter(this, paymentsExpenseList,
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
                        "Inappropriate input!",
                        Toast.LENGTH_SHORT).show();
                break;
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
        Toast.makeText(this, "Item Checked Successfully!", Toast.LENGTH_SHORT).show();
    }

    //------------------------ Accessors --------------------------
    public static List<Expense> getPaymentsExpenseList() {
        if (paymentsExpenseList == null) {
            paymentsExpenseList = new ArrayList<>();
        }
        return paymentsExpenseList;
    }
}
