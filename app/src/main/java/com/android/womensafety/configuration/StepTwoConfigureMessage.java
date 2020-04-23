package com.android.womensafety.configuration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.womensafety.MainActivity;
import com.android.womensafety.R;

public class StepTwoConfigureMessage extends AppCompatActivity {

    private EditText messageContentEditText;
    private boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_message);

        messageContentEditText = findViewById(R.id.message_body_edittext);
        messageContentEditText.setText(getSavedMessage());

        if (getIntent().getExtras() != null) {
            edit = getIntent().getExtras().getBoolean("EDIT");
            if (edit) {
                Button next = findViewById(R.id.next);
                next.setText("DONE");
            }
        }
    }

    public void onNextClicked(View view) {
        if (messageContentEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must enter message content", Toast.LENGTH_LONG).show();
        } else {
            //save message into database then go to main activity
            saveMessage();
            if (edit) {
                finish();
            } else {
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
            }
        }
    }

    private void saveMessage() {
        EditText messageEditText = findViewById(R.id.message_body_edittext);
        String message = messageEditText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MSG", message);
        editor.apply();
    }

    private String getSavedMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedPreferences.getString("MSG", null);
    }
}
