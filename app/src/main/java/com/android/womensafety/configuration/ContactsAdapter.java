package com.android.womensafety.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.womensafety.R;
import com.android.womensafety.model.Contact;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactsAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.setContactData(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView name, phone;
        private ImageButton delete;

        public ContactViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.person_name_textview);
            phone = itemView.findViewById(R.id.person_phone_textview);
            delete = itemView.findViewById(R.id.delete_btn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("CONTACTS", Context.MODE_PRIVATE);
                    HashMap<String, String> savedContacts = (HashMap<String, String>) sharedPreferences.getAll();

                    String thisPhone = contacts.get(getAdapterPosition()).getPhone();
                    for (String phone : savedContacts.keySet()) {
                        if (phone.equals(thisPhone)) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(phone);
                            editor.apply();
                            contacts.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }

        public void setContactData(Contact contact) {
            name.setText(contact.getName());
            phone.setText(contact.getPhone());
        }
    }
}
