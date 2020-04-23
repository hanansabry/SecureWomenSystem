package com.android.womensafety;


import com.android.womensafety.backend.authentication.AuthenticationRepository;
import com.android.womensafety.backend.authentication.AuthenticationRepositoryImpl;

public class Injection {


    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }
}
