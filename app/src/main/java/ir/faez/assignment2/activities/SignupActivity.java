package ir.faez.assignment2.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.CudAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.User;
import ir.faez.assignment2.databinding.ActivitySignupBinding;
import ir.faez.assignment2.utils.Action;
import ir.faez.assignment2.utils.PreferencesManager;

import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_ADDRESS;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_CONFIRM_PASSWORD;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_EMAIL;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_EMAIL_CHKBX;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_LASTNAME;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_MOBILE;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_NAME;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_NUMBER_OF_UNITS;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_PASSWORD;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_SMS_CHKBX;
import static ir.faez.assignment2.utils.PreferencesManager.PREF_KEY_USERNAME;


public class SignupActivity
        extends AppCompatActivity
        implements View.OnFocusChangeListener, View.OnClickListener {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    private void init() {
        // initializing binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // invoke Listeners
        invokeOnFocusListeners();
        invokeOnClickListeners();
    }


    private void invokeOnClickListeners() {
        binding.confirmBtn.setOnClickListener(this);
        binding.locationIv.setOnClickListener(this);
        binding.signUpBackIv.setOnClickListener(this);
    }

    private void invokeOnFocusListeners() {
        binding.mobileEdt.setOnFocusChangeListener(this);
        binding.passwordEdt.setOnFocusChangeListener(this);
        binding.confirmPasswordEdt.setOnFocusChangeListener(this);
        binding.emailEdt.setOnFocusChangeListener(this);
    }


    // ----------------------------------- Check Validations ---------------------------------------
    private boolean isMobileValid() {
        if (binding.mobileEdt.getText().toString().length() >= 10
                && !binding.mobileEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.mobileEdt);
            Toast.makeText(SignupActivity.this, "Wrong mobile input!"
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isEmailValid() {
        //pattern src: https://www.tutorialspoint.com/how-to-check-email-address-validation-in-android-on-edit-text

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (binding.emailEdt.getText().toString().trim().matches(emailPattern)
                && !binding.emailEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.emailEdt);
            Toast.makeText(SignupActivity.this,
                    "Invalid Email format!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isPasswordValid() {
        if (binding.passwordEdt.getText().toString().length() >= 6
                && !binding.passwordEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.passwordEdt);
            Toast.makeText(SignupActivity.this,
                    "password length must be 6 or more",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isConfirmPasswordValid() {
        if (binding.confirmPasswordEdt.getText().toString()
                .equals(binding.passwordEdt.getText().toString())
                && !binding.confirmPasswordEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.confirmPasswordEdt);
            Toast.makeText(SignupActivity.this,
                    "Confirm password not match!"
                    , Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private boolean isInputsFilled() {
        if (!binding.firstnameEdt.getText().toString().trim().isEmpty()
                && !binding.lastnameEdt.getText().toString().trim().isEmpty()
                && !binding.firstnameEdt.getText().toString().trim().isEmpty()
                && !binding.usernameEdt.getText().toString().trim().isEmpty()
                && !binding.addressEdt.getText().toString().trim().isEmpty()
                && !binding.numberOfUnitsEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            Toast.makeText(SignupActivity.this,
                    "Please Complete The Form",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }


//-------------------------------- Implementing Buttons --------------------------------------------

    private void locationIvBtn() {
        Toast.makeText(this, "this option is not implemented yet!"
                , Toast.LENGTH_SHORT).show();
    }


    private void clearFormBtn() {


        binding.mobileEdt.setText("");
        binding.firstnameEdt.setText("");
        binding.lastnameEdt.setText("");
        binding.usernameEdt.setText("");
        binding.passwordEdt.setText("");
        binding.confirmPasswordEdt.setText("");
        binding.emailEdt.setText("");
        binding.addressEdt.setText("");
        binding.numberOfUnitsEdt.setText("");
        binding.emailChkbx.setChecked(false);
        binding.smsChkbx.setChecked(false);

    }

    private void revertHints() {
        revertChangeHint(binding.mobileEdt);
        revertChangeHint(binding.firstnameEdt);
        revertChangeHint(binding.lastnameEdt);
        revertChangeHint(binding.usernameEdt);
        revertChangeHint(binding.passwordEdt);
        revertChangeHint(binding.confirmPasswordEdt);
        revertChangeHint(binding.emailEdt);
        revertChangeHint(binding.addressEdt);

    }


    private void registerBtn() {
        String name = binding.firstnameEdt.getText().toString().trim();
        String lastName = binding.lastnameEdt.getText().toString().trim();
        String phoneNo = binding.mobileEdt.getText().toString().trim();
        String email = binding.emailEdt.getText().toString().trim();
        String userName = binding.usernameEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordEdt.getText().toString().trim();
        String address = binding.addressEdt.getText().toString();
        int numberOfUnits = Integer.parseInt(binding.numberOfUnitsEdt.getText().toString().trim());
        boolean smsChkbx = binding.emailChkbx.isChecked();
        boolean emailChkbx = binding.smsChkbx.isChecked();

        if (isInputsFilled()
                && isEmailValid()
                && isMobileValid()
                && isPasswordValid()
                && isConfirmPasswordValid()) {

            PreferencesManager preferencesManager = PreferencesManager.getInstance(getApplicationContext());

            preferencesManager.put(PREF_KEY_NAME, name);
            preferencesManager.put(PREF_KEY_LASTNAME, lastName);
            preferencesManager.put(PREF_KEY_MOBILE, phoneNo);
            preferencesManager.put(PREF_KEY_EMAIL, email);
            preferencesManager.put(PREF_KEY_USERNAME, userName);
            preferencesManager.put(PREF_KEY_PASSWORD, password);
            preferencesManager.put(PREF_KEY_CONFIRM_PASSWORD, confirmPassword);
            preferencesManager.put(PREF_KEY_ADDRESS, address);
            preferencesManager.put(PREF_KEY_NUMBER_OF_UNITS, numberOfUnits);
            preferencesManager.put(PREF_KEY_EMAIL_CHKBX, emailChkbx);
            preferencesManager.put(PREF_KEY_SMS_CHKBX, smsChkbx);
            Toast.makeText(this, "You Registered Successfully!", Toast.LENGTH_LONG).show();


            finish();


            //** Implementing database **//


            CudAsyncTask cudAsyncTask = new CudAsyncTask(this, Action.INSERT_ACTION, new DbResponse<User>() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(SignupActivity.this, R.string.successfulRegister, Toast.LENGTH_SHORT).show();
                    //TODO for test

                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(SignupActivity.this, R.string.cantSignUpError, Toast.LENGTH_SHORT).show();
                }
            });
            User user = new User(name, lastName, phoneNo, email, userName, password, address, numberOfUnits, smsChkbx, emailChkbx);
            cudAsyncTask.execute(user);

        }


    }

    //------------------------------------------- Listeners ----------------------------------------
    //Implementing OnFocusChange
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.email_edt:
                    isEmailValid();
                    break;
                case R.id.mobile_edt:
                    isMobileValid();
                    break;
                case R.id.password_edt:
                    isPasswordValid();
                    break;
                case R.id.confirm_password_edt:
                    isConfirmPasswordValid();
                    break;

                default:
                    Toast.makeText(SignupActivity.this,
                            "Inappropriate input!",
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Implementing OnClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_btn:
                registerBtn();
                break;

            case R.id.location_iv:
                locationIvBtn();
                break;

            case R.id.signUp_back_iv:
                finish();
                break;

            default:
                Toast.makeText(this,
                        "Inappropriate Input!",
                        Toast.LENGTH_SHORT).show();
        }
    }

    //------------------------------------- Others -------------------------------------------------
    private void changeHint(EditText editText) {
        if (editText.getHint().toString().trim().charAt(0) != '*') {
//            editText.setHint("*" + editText.getHint().toString());
//            editText.setHintTextColor(Color.RED);
            editText.setError("Wrong Input");
        }
    }

    private void revertChangeHint(EditText editText) {
        if (editText.getHint().toString().trim().charAt(0) == '*') {
            editText.setHint(editText.getHint().toString().replace('*', ' '));
            editText.setHintTextColor(Color.argb(150, 0, 0, 128));

        }
    }
}

