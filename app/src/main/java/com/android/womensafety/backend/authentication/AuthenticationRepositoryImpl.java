package com.android.womensafety.backend.authentication;

import com.android.womensafety.backend.users.UsersRepository;
import com.android.womensafety.backend.users.UsersRepositoryImpl;
import com.android.womensafety.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private final FirebaseAuth auth;

    public AuthenticationRepositoryImpl() {
        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void registerNewUser(final User user, final RegistrationCallback callback) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            user.setId(firebaseUser.getUid());

                            UsersRepositoryImpl firebaseUsersRepository = new UsersRepositoryImpl();
                            firebaseUsersRepository.insertNewUser(user, new UsersRepository.UserInsertionCallback() {
                                @Override
                                public void onUserInsertedSuccessfully() {
                                    callback.onSuccessfulRegistration(task.getResult().getUser());
                                }

                                @Override
                                public void onUserInsertedFailed(String errmsg) {
                                    firebaseUser.delete();
                                    callback.onFailedRegistration(errmsg);
                                }
                            });
                        } else {
                            if (task.getException() != null) {
                                callback.onFailedRegistration(task.getException().getMessage());
                            } else {
                                callback.onFailedRegistration("Something wrong is happened! Please try again..");
                            }
                        }
                    }
                });
    }

    @Override
    public void login(String email, String password, final LoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccessLogin(task.getResult().getUser());
                        } else {
                            if (task.getException() != null) {
                                callback.onFailedLogin(task.getException().getMessage());
                            } else {
                                callback.onFailedLogin("Something wrong is happened! Please try again..");
                            }
                        }
                    }
                });
    }
}
