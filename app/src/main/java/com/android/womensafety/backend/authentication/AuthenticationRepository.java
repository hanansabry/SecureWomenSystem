package com.android.womensafety.backend.authentication;

import com.android.womensafety.model.User;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationRepository {

    interface RegistrationCallback {
        void onSuccessfulRegistration(FirebaseUser firebaseUser);

        void onFailedRegistration(String errmsg);
    }

    interface LoginCallback {
        void onSuccessLogin(FirebaseUser firebaseUser);

        void onFailedLogin(String errmsg);
    }

    void registerNewUser(User user, RegistrationCallback callback);

    void login(String email, String password, LoginCallback callback);

}
