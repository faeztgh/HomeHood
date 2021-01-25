package ir.faez.assignment2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.ExpenseCudAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.databinding.ActivityNewExpenseBinding;
import ir.faez.assignment2.network.NetworkHelper;
import ir.faez.assignment2.utils.Action;
import ir.faez.assignment2.utils.Result;
import ir.faez.assignment2.utils.ResultListener;
import ir.faez.assignment2.utils.Status;

//import ir.faez.assignment2.data.async.ExpenseCudAsyncTask;


public class NewExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "NEW_EXPENSE";
    private static String expenseTypeSp;
    private ActivityNewExpenseBinding binding;
    private String titleEt;
    private String amountEt;
    private String paymentDateTv;
    private NetworkHelper networkHelper;

    public static String getExpenseTypeSp() {
        return expenseTypeSp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    // initializing things that should be handled at start
    private void init() {
        // initializing binding
        binding = ActivityNewExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        spinnerHandler();
        invokeOnClickListeners();

        // initializing networkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

    }

    // invoke OnClickListeners
    private void invokeOnClickListeners() {
        binding.calendarIv.setOnClickListener(this);
        binding.newExpenseSaveBtn.setOnClickListener(this);
        binding.newExpenseBackIv.setOnClickListener(this);
    }

    // handling spinner
    private void spinnerHandler() {
        Spinner spinner = findViewById(R.id.newExpense_expenseType_sp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expenseTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String chosen = parent.getItemAtPosition(position).toString();
        expenseTypeSp = chosen;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //----------------------------- Implementing View.OnclickListener ------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newExpense_save_btn:
                saveBtnHandler();
                break;

            case R.id.calendar_iv:
                calendarHandler();
                break;

            case R.id.newExpense_back_iv:
                finish();
                break;
            default:
                Toast.makeText(NewExpenseActivity.this,
                        R.string.inappropriateInput,
                        Toast.LENGTH_SHORT).show();
        }
    }


    // handling save button
    // make and object from expense and send back to expense activity
    // with result and finishing activity
    private void saveBtnHandler() {
        titleEt = binding.newExpenseTitleEt.getText().toString();
        amountEt = binding.newExpenseAmountEt.getText().toString();
        paymentDateTv = binding.newExpenseDatePickerTv.getText().toString();

        final Expense exp = new Expense(MainActivity.currUser.getId(), titleEt, amountEt,
                paymentDateTv, expenseTypeSp, Status.expenses);
        if (!titleEt.isEmpty() && !amountEt.isEmpty() && !paymentDateTv.isEmpty()) {
//
            // implementing Network
            networkHelper.insertExpense(exp, MainActivity.currUser, new ResultListener<Expense>() {
                @Override
                public void onResult(Result<Expense> result) {
                    Log.d(TAG, "Result of inserting expense in server: " + result);
                    Error error = result != null ? result.getError() : null;
                    Expense resultExp = result != null ? result.getItem() : null;

                    if (result == null || resultExp == null || error != null) {
                        String errorMsg = error != null ? error.getMessage() : getString(R.string.somethingWentWrongOnInsert);
                        Toast.makeText(NewExpenseActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    exp.setId(resultExp.getId());


                    // implementing db

                    ExpenseCudAsyncTask expenseCudAsyncTask =
                            new ExpenseCudAsyncTask(getApplicationContext(), Action.INSERT_ACTION,
                                    new DbResponse<Expense>() {
                                @Override
                                public void onSuccess(Expense expense) {
                                    Toast.makeText(NewExpenseActivity.this, R.string.newExpenseAdded,
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(NewExpenseActivity.this, R.string.cantAddNewExpense, Toast.LENGTH_SHORT).show();

                                }
                            });

                    expenseCudAsyncTask.execute(exp);
                }
            });


        } else {
            binding.newExpenseTitleEt.setError("Fill!");
            binding.newExpenseAmountEt.setError("Fill!");
            binding.newExpenseDatePickerTv.setError("Fill!");
        }

    }

    // handling Calendar and DatePickerDialog
    private void calendarHandler() {
        Calendar calendar;
        DatePickerDialog datePickerDialog;

        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);


        datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, (datePicker, year1, month1, dayOfMonth) -> binding.newExpenseDatePickerTv.setText(day + " - " + month + " - " + year), day, month, year);
        datePickerDialog.show();
    }
}

