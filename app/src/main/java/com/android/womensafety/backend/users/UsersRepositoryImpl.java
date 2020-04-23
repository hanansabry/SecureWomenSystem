package com.android.womensafety.backend.users;

import com.android.womensafety.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class UsersRepositoryImpl implements UsersRepository {

    public static String USERS_COLLECTION = "users";
    private final DatabaseReference mDatabase;

    public UsersRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(USERS_COLLECTION);
    }

    @Override
    public void insertNewUser(User user, final UserInsertionCallback callback) {
        String userId = user.getId();
        mDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onUserInsertedSuccessfully();
                } else {
                    callback.onUserInsertedFailed(task.getException().getMessage());
                }
            }
        });
    }
}
