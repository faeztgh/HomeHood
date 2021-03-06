package ir.faez.assignment2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.model.User;
import ir.faez.assignment2.databinding.ActivitySignupBinding;
import ir.faez.assignment2.network.NetworkHelper;
import ir.faez.assignment2.utils.Result;
import ir.faez.assignment2.utils.ResultListener;


public class SignupActivity
        extends AppCompatActivity
        implements View.OnFocusChangeListener, View.OnClickListener {

    private static final String TAG = "SIGNUP";
    private static final int LOCATION_REQUEST_CODE = 1;
    private ActivitySignupBinding binding;
    private double latitude;
    private double longitude;
    private NetworkHelper networkHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    private void init() {

        // initializing NetworkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());
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
        binding.locationIv.setOnClickListener(this);
    }

    private void invokeOnFocusListeners() {
        binding.mobileEdt.setOnFocusChangeListener(this);
        binding.passwordEdt.setOnFocusChangeListener(this);
        binding.confirmPasswordEdt.setOnFocusChangeListener(this);
        binding.emailEdt.setOnFocusChangeListener(this);

    }


    // ----------------------------------- Check Validations ---------------------------------------

    private boolean isLatLngValid() {
        if (latitude != 0 && longitude != 0) {
            return true;
        } else {
            Toast.makeText(this, R.string.chooseLocation, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

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
            binding.passwordEdt.setError("Password length must be 6 or more");

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
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                double[] res = data.getDoubleArrayExtra("LATLNG");

                // set result to the class filed
                this.latitude = res[0];
                this.longitude = res[1];


            }
        }
    }

//

    private void registerBtn() {

        String name = binding.firstnameEdt.getText().toString().trim();
        String lastName = binding.lastnameEdt.getText().toString().trim();
        String phoneNo = binding.mobileEdt.getText().toString().trim();
        String email = binding.emailEdt.getText().toString().trim();
        String userName = binding.usernameEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();
        String address = binding.addressEdt.getText().toString();
        String numberOfUnits = binding.numberOfUnitsEdt.getText().toString().trim();
        boolean smsChkbx = binding.emailChkbx.isChecked();
        boolean emailChkbx = binding.smsChkbx.isChecked();


        int numOfUnits = 0;
        // validate number of units
        if (!numberOfUnits.equals("")) {
            numOfUnits = Integer.parseInt(numberOfUnits);
        } else {
            Toast.makeText(this, R.string.fillNumberOfUnits, Toast.LENGTH_SHORT).show();
        }


        // validate fields
        if (isInputsFilled()
                && isEmailValid()
                && isMobileValid()
                && isPasswordValid()
                && isConfirmPasswordValid()
                && isLatLngValid()) {


//            implementing Server Signup
            User user = new User(name, lastName, phoneNo, email, userName, password, address
                    , numOfUnits, smsChkbx, emailChkbx, this.latitude, this.longitude);

            networkHelper.signupUser(user, new ResultListener<User>() {
                @Override
                public void onResult(Result<User> result) {
                    Log.d(TAG, "Result of signing user up in server" + result);
                    Error error = (result != null) ? result.getError() : null;

                    if ((result == null) || (error != null)) {
                        String errMsg = (error != null) ? error.getMessage() : getString(R.string.cantSignUpError);
                        Toast.makeText(SignupActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(SignupActivity.this, R.string.successfulRegister, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
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
                            R.string.inappropriateInput,
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
                        R.string.inappropriateInput,
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


}

