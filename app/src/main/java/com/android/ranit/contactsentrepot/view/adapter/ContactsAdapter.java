package com.android.ranit.contactsentrepot.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ranit.contactsentrepot.R;
import com.android.ranit.contactsentrepot.data.ContactResponse;

import java.util.List;

/**
 * Created by: Ranit Raj Ganguly on 17/04/21
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<ContactResponse> contactsList;

    // Constructor
    public ContactsAdapter(List<ContactResponse> list) {
        this.contactsList = list;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        ContactResponse currentItem = contactsList.get(position);

        holder.getContactNameTextView().setText(currentItem.getName());
        holder.getContactNumberTextView().setText(currentItem.getPhoneNumberList()
                .get(0).getNumber());
    }

    @Override
    public int getItemCount() {
        return Math.max(contactsList.size(), 0);
    }

    // Static Inner Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contactNameTextView;
        private TextView contactNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactNameTextView = itemView.findViewById(R.id.contact_name);
            contactNumberTextView = itemView.findViewById(R.id.contact_number);
        }

        public TextView getContactNameTextView() {
            return contactNameTextView;
        }

        public TextView getContactNumberTextView() {
            return contactNumberTextView;
        }
    }
}
