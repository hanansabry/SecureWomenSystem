package com.android.womensafety.backend.users;


import com.android.womensafety.model.User;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    void insertNewUser(User user, UserInsertionCallback callback);
}
