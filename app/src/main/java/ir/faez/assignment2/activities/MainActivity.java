package ir.faez.assignment2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.GetSpecificUserAsyncTask;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.model.User;
import ir.faez.assignment2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    public static User currUser;
    private String userName;
    private String password;
    private ActivityMainBinding binding;
    private SharedPreferences preferences;//TODO


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(Context.MODE_PRIVATE);

        init();
    }

    // it will read new user that register right now.
    @Override
    protected void onRestart() {
        super.onRestart();
        // get all users from DB

    }

    private void init() {
        // initializing binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // invoke Listeners
        invokeOnClickListeners();

        binding.usernameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                getUser(binding.usernameEdt.getText().toString().trim());

            }
        });
    }

    private void getUser(String username) {
        GetSpecificUserAsyncTask getSpecificUserAsyncTask = new GetSpecificUserAsyncTask(this, new DbResponse<User>() {
            @Override
            public void onSuccess(User user) {
                currUser = user;
            }

            @Override
            public void onError(Error error) {
            }
        });
        getSpecificUserAsyncTask.execute(username);
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

        if (isAuth()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
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
        loadUserPass();

        if (currUser != null) {

            if (isUsernameValid() && isPasswordValid()) {

                if (userName.equals(currUser.getUserName()) && password.equals(currUser.getPassword())) {
                    return true;
                } else {
                    Toast.makeText(this, R.string.wrongUserNameOrPassword, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(MainActivity.this,
                        R.string.wrongUserNameOrPassword,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, R.string.userNotExist, Toast.LENGTH_SHORT).show();
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


