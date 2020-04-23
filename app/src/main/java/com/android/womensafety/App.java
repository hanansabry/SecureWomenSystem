package com.android.womensafety;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        if (sharedPreferences.getBoolean("FIRST_USE", true)) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("FIRST_USE", false);
//        }
    }
}
