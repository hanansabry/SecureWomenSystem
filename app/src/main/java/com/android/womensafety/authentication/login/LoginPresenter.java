package com.android.womensafety.authentication.login;

import com.android.womensafety.backend.authentication.AuthenticationRepository;

public class LoginPresenter {

    private final LoginActivity view;
    private final AuthenticationRepository repository;

    public LoginPresenter(LoginActivity view, AuthenticationRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void login(String email, String password, AuthenticationRepository.LoginCallback callback) {
        repository.login(email, password, callback);
    }

    public boolean validateLoginData(String email, String password) {
        boolean validate = true;
        if (email == null || email.isEmpty()) {
            validate = false;
            view.setEmailInputTextErrorMessage();
        }

        if (password == null || password.isEmpty()) {
            validate = false;
            view.setPasswordInputTextErrorMessage();
        }
        return validate;
    }


}
