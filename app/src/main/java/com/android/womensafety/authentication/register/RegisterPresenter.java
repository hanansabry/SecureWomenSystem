package com.android.womensafety.authentication.register;


import com.android.womensafety.backend.authentication.AuthenticationRepository;
import com.android.womensafety.model.User;

public class RegisterPresenter {

    private final RegisterActivity view;
    private final AuthenticationRepository repository;

    public RegisterPresenter(RegisterActivity view, AuthenticationRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void registerNewUser(User user, AuthenticationRepository.RegistrationCallback callback) {
        repository.registerNewUser(user, callback);
    }

    public boolean validateUserData(User user) {
        boolean validate = true;
        if (user.getName() == null || user.getName().isEmpty()) {
            validate = false;
            view.setNameInputTextErrorMessage();
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            validate = false;
            view.setPasswordInputTextErrorMessage("Password can't be empty");
        }

        if (user.getPassword().length() < 6) {
            validate = false;
            view.setPasswordInputTextErrorMessage("Password must be at least 6 characters");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            validate = false;
            view.setEmailInputTextErrorMessage();
        }

        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            validate = false;
            view.setPhoneInputTextErrorMessage();
        }
        return validate;
    }
}
