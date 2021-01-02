package ir.faez.assignment2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.async.GetUsersAsyncTask;
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
    private List<User> users;

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
        getUsers();
    }

    private void init() {
        // initializing binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // get all users from DB
        getUsers();
        // invoke Listeners
        invokeOnClickListeners();
    }

    private void getUsers() {
        GetUsersAsyncTask getUsersAsyncTask = new GetUsersAsyncTask(this, new DbResponse<List<User>>() {
            @Override
            public void onSuccess(List<User> loadedUsers) {
                users = loadedUsers;
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(MainActivity.this, R.string.cantGetUsersFromDb, Toast.LENGTH_SHORT).show();
            }
        });
        getUsersAsyncTask.execute();
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
        loadData();

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

    private void loadData() {

        currUser = getUserByUsername(binding.usernameEdt.getText().toString().trim());
        // init username and password fields
        userName = binding.usernameEdt.getText().toString();
        password = binding.passwordEdt.getText().toString();
    }

    private User getUserByUsername(String userName) {

        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }
}


