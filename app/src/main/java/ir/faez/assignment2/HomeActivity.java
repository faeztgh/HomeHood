package ir.faez.assignment2;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.assignment2.databinding.ActivityHomeBinding;
import ir.faez.assignment2.databinding.DialogWindowBinding;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHomeBinding binding;
    private String fullName;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String mobile;
    private String address;
    private String confirmationType = "";
    private boolean emailCheckbox;
    private boolean smsCheckbox;
    private int numberOfUnits;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();


    }

    // initializing things that should be handled at start
    private void init() {
        // initializing binding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // invoke Listeners
        invokeOnClickListeners();
    }

    // invoke Listeners
    private void invokeOnClickListeners() {
        binding.reportsBtn.setOnClickListener(this);
        binding.profileBtn.setOnClickListener(this);
        binding.announcementsBtn.setOnClickListener(this);
        binding.expensesBtn.setOnClickListener(this);
        binding.paymentsBtn.setOnClickListener(this);
        binding.exitBtn.setOnClickListener(this);
    }


//-------------------------------- Implementing Buttons --------------------------------------------

    private void invokeReportBtn() {
        Toast.makeText(this,
                "this option is not implemented yet!",
                Toast.LENGTH_SHORT).show();
    }


    private void invokeProfileBtn() {
        loadData();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogWindowBinding dialogWindowBinding = DialogWindowBinding.inflate(getLayoutInflater());
        View view = dialogWindowBinding.getRoot();
        builder.setView(view);

        dialogWindowImpl(dialogWindowBinding, builder);


    }


    private void invokeExitBtn() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }

    private void invokeAnnouncementsBtn() {
        Toast.makeText(this,
                "this option is not implemented yet!",
                Toast.LENGTH_SHORT).show();



    }

    private void invokeExpensesBtn() {

        Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);
        startActivity(intent);
    }

    private void invokePaymentsBtn() {
        Intent intent = new Intent(getApplicationContext(), PaymentsActivity.class);
        startActivity(intent);

    }

    //------------------------------------------- Listeners ----------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reports_btn:
                invokeReportBtn();
                break;
            case R.id.profile_btn:
                invokeProfileBtn();
                break;
            case R.id.announcements_btn:
                invokeAnnouncementsBtn();
                break;
            case R.id.expenses_btn:
                invokeExpensesBtn();
                break;
            case R.id.payments_btn:
                invokePaymentsBtn();
                break;
            case R.id.exit_btn:
                invokeExitBtn();
                break;
            default:
                Toast.makeText(this,
                        "Inappropriate Input!",
                        Toast.LENGTH_SHORT).show();
        }
    }


    //------------------------------------- Others -------------------------------------------------
    private void loadData() {
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getApplicationContext());
        firstName = preferencesManager.get(PreferencesManager.PREF_KEY_NAME, "");
        lastName = preferencesManager.get(PreferencesManager.PREF_KEY_LASTNAME, "");
        fullName = firstName + " " + lastName;
        userName = preferencesManager.get(PreferencesManager.PREF_KEY_USERNAME, "");
        email = preferencesManager.get(PreferencesManager.PREF_KEY_EMAIL, "");
        mobile = preferencesManager.get(PreferencesManager.PREF_KEY_MOBILE, "");
        address = preferencesManager.get(PreferencesManager.PREF_KEY_ADDRESS, "");
        numberOfUnits = preferencesManager.get(PreferencesManager.PREF_KEY_NUMBER_OF_UNITS, 0);
        emailCheckbox = preferencesManager.get(PreferencesManager.PREF_KEY_EMAIL_CHKBX, true);
        smsCheckbox = preferencesManager.get(PreferencesManager.PREF_KEY_SMS_CHKBX, true);
        if (emailCheckbox) {
            confirmationType += " Email ";
        }
        if (smsCheckbox) {
            confirmationType += " SmS ";
        }
    }


    @SuppressLint("SetTextI18n")
    private void dialogWindowImpl(DialogWindowBinding dialogWindowBinding, AlertDialog.Builder builder) {
        dialogWindowBinding.fullNameTv.setText(dialogWindowBinding.fullNameTv.getText() + " : " + fullName);
        dialogWindowBinding.usernameTv.setText(dialogWindowBinding.usernameTv.getText() + " : " + userName);
        dialogWindowBinding.emailTv.setText(dialogWindowBinding.emailTv.getText() + " : " + email);
        dialogWindowBinding.mobileTv.setText(dialogWindowBinding.mobileTv.getText() + " : " + mobile);
        dialogWindowBinding.addressTv.setText(dialogWindowBinding.addressTv.getText() + " : " + address);
        dialogWindowBinding.numberOfUnitsTv.setText(dialogWindowBinding.numberOfUnitsTv.getText() + " : " + numberOfUnits);
        dialogWindowBinding.confirmationTypeTv.setText(dialogWindowBinding.confirmationTypeTv.getText() + " : " + confirmationType);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialogWindowBinding.closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
