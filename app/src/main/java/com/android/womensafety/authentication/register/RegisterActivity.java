package com.android.womensafety.authentication.register;

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
import com.android.womensafety.authentication.login.LoginActivity;
import com.android.womensafety.backend.authentication.AuthenticationRepository;
import com.android.womensafety.configuration.StepOnePickContact;
import com.android.womensafety.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements AuthenticationRepository.RegistrationCallback {

    private RegisterPresenter presenter;
    private TextInputLayout nameTextInput, passwordTextInput, emailTextInput, phoneTextInput;
    private EditText nameEditText, familyNumberEditText, passwordEditText, emailEditText, phoneEditText;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new RegisterPresenter(this, Injection.provideAuthenticationRepository());
        initializeView();
    }

    private void initializeView() {
        nameTextInput = findViewById(R.id.name_text_input_edittext);
        passwordTextInput = findViewById(R.id.password_text_input_edittext);
        emailTextInput = findViewById(R.id.email_text_input_edittext);
        phoneTextInput = findViewById(R.id.phone_text_input_edittext);

        nameEditText = findViewById(R.id.name_edit_text);
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameTextInput.setError(null);
                nameTextInput.setErrorEnabled(false);
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
        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
            }
        });
        phoneEditText = findViewById(R.id.phone_edit_text);
        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneTextInput.setError(null);
                phoneTextInput.setErrorEnabled(false);
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        registerButton = findViewById(R.id.register);
    }

    private User getUserInputData() {
        User user = new User();
        user.setName(nameEditText.getText().toString().trim());
        user.setPassword(passwordEditText.getText().toString().trim());
        user.setEmail(emailEditText.getText().toString().trim());
        user.setPhone(phoneEditText.getText().toString().trim());
        return user;
    }

    public void setNameInputTextErrorMessage() {
        nameTextInput.setError("Name can't be empty");
    }

    public void setPasswordInputTextErrorMessage(String errorMessage) {
        passwordTextInput.setError(errorMessage);
    }

    public void setEmailInputTextErrorMessage() {
        emailTextInput.setError("Email can't be empty");
    }

    public void setPhoneInputTextErrorMessage() {
        phoneTextInput.setError("Phone can't be empty");
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
    }

    public void OnRegisterClicked(View view) {
        showProgressBar();
        User user = getUserInputData();
        if (presenter.validateUserData(user)) {
            presenter.registerNewUser(user, this);
        } else {
            hideProgressBar();
        }
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onSuccessfulRegistration(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "Registration is successfully completed\n Welcome " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        goToMainActivity();
    }

    @Override
    public void onFailedRegistration(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, "Registration Failed\n" + errmsg, Toast.LENGTH_LONG).show();
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
