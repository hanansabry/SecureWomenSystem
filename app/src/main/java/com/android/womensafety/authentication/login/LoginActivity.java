package com.android.womensafety.authentication.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.womensafety.Injection;
import com.android.womensafety.MainActivity;
import com.android.womensafety.R;
import com.android.womensafety.authentication.register.RegisterActivity;
import com.android.womensafety.backend.authentication.AuthenticationRepository;
import com.android.womensafety.configuration.StepOnePickContact;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements AuthenticationRepository.LoginCallback {

    private LoginPresenter presenter;
    private TextInputLayout emailTextInput, passwordTextInput;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this, Injection.provideAuthenticationRepository());
        initializeView();
    }

    private void initializeView() {
        emailTextInput = findViewById(R.id.email_text_input_edittext);
        passwordTextInput = findViewById(R.id.password_text_input_edittext);

        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
            }
        });
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordTextInput.setError(null);
                passwordTextInput.setErrorEnabled(false);
            }
        });

        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_bar);
    }

    public void OnRegisterClicked(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onLoginClicked(View view) {
        showProgressBar();

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (presenter.validateLoginData(email, password)) {
            presenter.login(email, password, this);
        } else {
            hideProgressBar();
        }
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }

    public void setEmailInputTextErrorMessage() {
        emailTextInput.setError("Email can't be empty");
    }

    public void setPasswordInputTextErrorMessage() {
        passwordTextInput.setError("Password can't be empty");
    }

    @Override
    public void onSuccessLogin(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "Welcome, " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        goToMainActivity();
    }

    private void goToMainActivity() {
        if (isAppFirstUse()) {
            Intent intent = new Intent(this, StepOnePickContact.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        }
    }

    @Override
    public void onFailedLogin(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show();
    }

    public boolean isAppFirstUse() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean firstUse = sharedPreferences.getBoolean("FIRST_USE", true);
        if (firstUse) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRST_USE", false);
            editor.apply();
        }
         return firstUse;
    }
}
