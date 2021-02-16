package com.sky.contact.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.alphabemodule.AlphabetView;
import com.sky.contact.Act_ContactDetail;
import com.sky.contact.R;
import com.sky.contact.model.Contact;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private final List<Contact> contacts;
    private final Context context;

    public RVAdapter(Context context, List<Contact> contacts) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.alphabetView.setSourceText(contact.getName());
        holder.tvName.setText(contact.getFullName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Act_ContactDetail.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AlphabetView alphabetView;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alphabetView = itemView.findViewById(R.id.alphabetView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
