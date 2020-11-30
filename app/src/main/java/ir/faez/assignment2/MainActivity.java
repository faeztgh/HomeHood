package ir.faez.assignment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.faez.assignment2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private String userName;
    private String password;
    private ActivityMainBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getPreferences(Context.MODE_PRIVATE);


        init();
    }

    private void init() {
        // initializing binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // invoke Listeners
        invokeOnClickListeners();
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
            Toast.makeText(this, "UserName field is EMPTY!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean isPasswordValid() {
        if (!binding.passwordEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            changeHint(binding.passwordEdt);
            Toast.makeText(this, "Password field is EMPTY!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean isAuth() {
        loadData();
        if (isUsernameValid()
                && isPasswordValid()
                && binding.usernameEdt.getText().toString().equals(userName)
                && binding.passwordEdt.getText().toString().equals(password)) {
            return true;
        } else {
            Toast.makeText(MainActivity.this,
                    "Wrong username or password!",
                    Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Inappropriate Input!", Toast.LENGTH_SHORT).show();
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
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getApplicationContext());
        userName = preferencesManager.get(PreferencesManager.PREF_KEY_USERNAME, "");
        password = preferencesManager.get(PreferencesManager.PREF_KEY_PASSWORD, "");

    }
}


