package com.android.womensafety.configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.womensafety.BuildConfig;
import com.android.womensafety.MainActivity;
import com.android.womensafety.R;
import com.android.womensafety.model.Contact;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

public class StepOnePickContact extends AppCompatActivity {

    private static final int PICK_CONTACT = 10;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private EditText nameEditText, phoneEditText;
    private ContactsAdapter contactsAdapter;
    private boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);

        nameEditText = findViewById(R.id.person_name_edittext);
        phoneEditText = findViewById(R.id.person_phone_edittext);

        if (getIntent().getExtras() != null) {
            edit = getIntent().getExtras().getBoolean("EDIT");
            if (edit) {
                Button next = findViewById(R.id.next);
                next.setText("DONE");
            }
        }

        setupContactsRecyclerView(getAddedContacts());
    }

    public void onNextClicked(View view) {
        if (getAddedContacts().size() == 0) {
            Toast.makeText(this, "You must choose at least one person", Toast.LENGTH_LONG).show();
        } else {
            if (edit) {
                finish();
            } else {
                //step 2 : configure your message
                startActivity(new Intent(this, StepTwoConfigureMessage.class));
            }
        }
    }

    public void onPickContactClicked(View view) {
        //get current location
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            openContactsToPick();
        }
    }

    private void openContactsToPick() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (PICK_CONTACT):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                nameEditText.setText(TempNameHolder);

                                phoneEditText.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
        }
    }

    public void onDoneClicked(View view) {
        String personName = nameEditText.getText().toString();
        String personPhone = phoneEditText.getText().toString();

        if (personName.isEmpty() || personPhone.isEmpty()) {
            Toast.makeText(this, "You must enter name and phone", Toast.LENGTH_SHORT).show();
        } else if (getAddedContacts().size() == 2) {
            Toast.makeText(this, "You can't add more than 2 persons", Toast.LENGTH_SHORT).show();
        } else {
            Contact contact = new Contact(personName, personPhone);
            addNewContact(contact);
            contactsAdapter.addContact(contact);
        }
    }

    private void addNewContact(Contact contact) {
        SharedPreferences sharedPreferences = getSharedPreferences("CONTACTS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(contact.getPhone(), contact.getName());
        editor.apply();
    }


    private ArrayList<Contact> getAddedContacts() {
        SharedPreferences sharedPreferences = getSharedPreferences("CONTACTS", Context.MODE_PRIVATE);
        HashMap<String, String> contacts = (HashMap<String, String>) sharedPreferences.getAll();

        ArrayList<Contact> myContacts = new ArrayList<>();
        for (String phone : contacts.keySet()) {
            String name = contacts.get(phone);
            Contact c = new Contact(name, phone);
            myContacts.add(c);
        }
        return myContacts;
    }


    private void setupContactsRecyclerView(ArrayList<Contact> contactsList) {
        RecyclerView contactsRecyclerView = findViewById(R.id.contacts_rv);
        contactsAdapter = new ContactsAdapter(contactsList);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();
    }


    private boolean checkPermissions() {
        String permission = Manifest.permission.READ_CONTACTS;
        int permissionState = ActivityCompat.checkSelfPermission(this, permission);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(StepOnePickContact.this,
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}
                    , REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void showSnackbar(final int mainTextStringId, final int actionStringId,
                             View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Toast.makeText(this, "User interaction was cancelled.", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                openContactsToPick();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}
