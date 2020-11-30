package ir.faez.assignment2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Calendar;

import ir.faez.assignment2.databinding.ActivityNewExpenseBinding;


public class NewExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static final String EXTRA_MESSAGE = "EXPENSE_OBJ";
    private static String expenseTypeSp;
    private ActivityNewExpenseBinding binding;
    private String titleEt;
    private String amountEt;
    private String paymentDateTv;

    public static String getExpenseTypeSp() {
        return expenseTypeSp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

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
                        "Inappropriate input!",
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

        if (!titleEt.isEmpty() && !amountEt.isEmpty() && !paymentDateTv.isEmpty()) {
            Expense exp = new Expense(titleEt, amountEt, paymentDateTv, expenseTypeSp);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MESSAGE, (Serializable) exp);
            setResult(RESULT_OK, intent);
            finish();
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

