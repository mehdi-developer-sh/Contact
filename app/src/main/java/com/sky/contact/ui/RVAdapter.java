package com.sky.contact.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.alphabemodule.AlphabetView;
import com.sky.contact.Act_ContactDetail;
import com.sky.contact.R;
import com.sky.contact.database.ContactEntity;
import com.sky.contact.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private final List<ContactEntity> contacts;
    private final Context context;
    private boolean isSelectingMode;
    private final RecyclerView recyclerView;
    private int selectedCount = 0;

    public RVAdapter(RecyclerView recyclerView, Context context, List<ContactEntity> contacts) {
        if (!(context instanceof OnSelectingModeListener)) {
            throw new IllegalStateException("context must be instance of OnSelectingModeListener");
        }
        this.context = context;
        this.contacts = contacts;
        this.recyclerView = recyclerView;
        onSelectingModeListener = (OnSelectingModeListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactEntity contact = contacts.get(position);

        holder.alphabetView.setClickable(false);

        holder.alphabetView.setSourceText(contact.getName());
        holder.tvName.setText(contact.getFullName());

        holder.alphabetView.setChecked(contact.isSelected());

        holder.itemView.setOnClickListener(v -> {
            if (isSelectingMode) {
                setSelected(holder);
            } else {
                Intent intent = new Intent(context, Act_ContactDetail.class);
                intent.putExtra(Constants.KEY_CONTACT_ID, contacts.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (!isSelectingMode) {
                isSelectingMode = true;
                onSelectingModeListener.onStartSelectingMode();
                setSelected(holder);
                return true;
            }
            setSelected(holder);
            return true;
        });
    }

    private void setSelected(ViewHolder holder) {
        ContactEntity c = contacts.get(holder.getAdapterPosition());
        holder.alphabetView.toggle();
        c.setSelected(holder.alphabetView.isChecked());

        if (c.isSelected()) {
            selectedCount += 1;
        } else {
            selectedCount -= 1;
        }

        onSelectingModeListener.onSelectedChanged(selectedCount);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public List<ContactEntity> getSelectedItems() {
        List<ContactEntity> selected = new ArrayList<>();
        for (ContactEntity contact :
                contacts) {
            if (contact.isSelected()) {
                selected.add(contact);
            }
        }
        return selected;
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

    public void destroySelectingMode() {
        isSelectingMode = false;

        for (ContactEntity contact : contacts) {
            if (contact.isSelected()) {
                contact.setSelected(false);
            }
        }

        selectedCount = 0;

        notifyItemChangedInScreen();
    }

    private void notifyItemChangedInScreen() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            notifyItemRangeChanged(
                    layoutManager.findFirstVisibleItemPosition(),
                    layoutManager.findLastVisibleItemPosition() + 1
            );
        } else {
            notifyItemChanged(0, getItemCount());
        }
    }

    public void setSelectedAll(boolean selected) {
        if (selected) {
            if (selectedCount == getItemCount())
                return;
        } else if (selectedCount == 0) {
            return;
        }
        for (ContactEntity c : contacts) {
            c.setSelected(selected);
        }

        selectedCount = selected ? getItemCount() : 0;

        onSelectingModeListener.onSelectedChanged(selectedCount);
        notifyItemChangedInScreen();
    }

    public interface OnSelectingModeListener {
        void onStartSelectingMode();

        void onEndSelectingMode();

        void onSelectedChanged(int selectedCount);
    }

    private final OnSelectingModeListener onSelectingModeListener;
}
