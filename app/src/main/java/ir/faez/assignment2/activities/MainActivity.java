package ir.faez.assignment2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.GetSpecificUserAsyncTask;
import ir.faez.assignment2.data.async.UserCudAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.User;
import ir.faez.assignment2.databinding.ActivityMainBinding;
import ir.faez.assignment2.network.NetworkHelper;
import ir.faez.assignment2.utils.Action;
import ir.faez.assignment2.utils.Result;
import ir.faez.assignment2.utils.ResultListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SIGNIN";
    private static final int REQUEST_CODE = 1;
    public static User currUser;
    private String userName;
    private String password;
    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    private NetworkHelper networkHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserByState();

        init();
    }


    private void init() {
        // initializing NetworkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        // initializing binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // invoke Listeners
        invokeOnClickListeners();
    }


    private void getUserByState() {

        GetSpecificUserAsyncTask getSpecificUserAsyncTask = new GetSpecificUserAsyncTask(this,
                Action.GET_BY_STATE_ACTION, new DbResponse<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null && user.isLoggedIn().equals("true")) {
                    currUser = user;
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    currUser = null;
                }
            }

            @Override
            public void onError(Error error) {
            }
        });
        getSpecificUserAsyncTask.execute("true");

    }


    private void invokeOnClickListeners() {
        binding.signInBtn.setOnClickListener(this);
        binding.registerBtn.setOnClickListener(this);
    }

    //-------------------------------- Implementing Buttons --------------------------------------------
    private void registerBtn() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }


    private void signInBtn() {
        loadUserPass();


        if (currUser == null) {

            if (isAuth()) {

                // implementing online Auth
                final User finalUser = new User(userName, password);
                networkHelper.signinUser(finalUser, new ResultListener<User>() {
                    @Override
                    public void onResult(Result<User> result) {
                        Error error = (result != null) ? result.getError() : null;
                        User resultUser = (result != null) ? result.getItem() : null;
                        if ((result == null) || (error != null) || (resultUser == null)) {
                            String errMsg = (error != null) ? error.getMessage() : getString(R.string.cantSignInError);
                            Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        finalUser.setId(resultUser.getId());
                        finalUser.setSessionToken(resultUser.getSessionToken());
                        finalUser.setName(resultUser.getName());
                        finalUser.setLastName(resultUser.getLastName());
                        finalUser.setPhoneNo(resultUser.getPhoneNo());
                        finalUser.setEmail(resultUser.getEmail());
                        finalUser.setUsername(resultUser.getUsername());
                        finalUser.setPassword(resultUser.getPassword());
                        finalUser.setFullAddress(resultUser.getFullAddress());
                        finalUser.setNumberOfUnit(resultUser.getNumberOfUnit());
                        finalUser.setSmsChkbx(resultUser.isSmsChkbx());
                        finalUser.setEmailChkbx(resultUser.isEmailChkbx());
                        finalUser.setIsLoggedIn("true");

                        // implementing user insertion into database
                        UserCudAsyncTask userCudAsyncTask = new UserCudAsyncTask(getApplicationContext(),
                                Action.INSERT_ACTION, new DbResponse<User>() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null) {
                                    currUser = resultUser;
                                    Intent intent = new Intent(getApplicationContext(),
                                            HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                    userName = "";
                                    password = "";
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(MainActivity.this,
                                        R.string.cantSignInError, Toast.LENGTH_SHORT).show();
                            }
                        });
                        userCudAsyncTask.execute(finalUser);
                    }
                });
            }
        }


    }
    // ----------------------------------- Check Validations ---------------------------------------

    private boolean isUsernameValid() {
        if (!binding.usernameEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.usernameEdt);
            Toast.makeText(this, R.string.emptyUsernameField, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean isPasswordValid() {
        if (!binding.passwordEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.passwordEdt);
            Toast.makeText(this, R.string.emptyPasswordField, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean isAuth() {
        if (isUsernameValid() && isPasswordValid()) {
            return true;
        } else {
            return false;
        }
    }


    private void loadUserPass() {
        userName = binding.usernameEdt.getText().toString().trim();
        password = binding.passwordEdt.getText().toString().trim();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn_btn:
                signInBtn();
                break;
            case R.id.register_btn:
                registerBtn();
                break;
            default:
                Toast.makeText(this, R.string.inappropriateInput, Toast.LENGTH_SHORT).show();
        }
    }


    //------------------------------------- Others -------------------------------------------------
    private void changeHint(EditText editText) {
        if (editText.getHint().toString().trim().charAt(0) != '*') {
            editText.setHint("*" + editText.getHint().toString());
        }
        editText.setHintTextColor(Color.RED);
    }


}


