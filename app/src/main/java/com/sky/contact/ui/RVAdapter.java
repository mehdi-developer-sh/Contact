package com.sky.contact.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
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
    private final AsyncListDiffer<ContactEntity> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private boolean isSelectingMode;
    private final RecyclerView recyclerView;
    private int selectedCount = 0;
    private final Context context;

    public RVAdapter(RecyclerView recyclerView, Context context) {
        if (!(context instanceof OnSelectingModeListener)) {
            throw new IllegalStateException("context must be instance of OnSelectingModeListener");
        }
        this.context = context;
        this.recyclerView = recyclerView;
        onSelectingModeListener = (OnSelectingModeListener) context;
    }

    public void submitList(List<ContactEntity> list) {
        mDiffer.submitList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactEntity contact = mDiffer.getCurrentList().get(position);
        holder.bindTo(contact);
    }

    private void setSelected(ViewHolder holder) {
        ContactEntity c = mDiffer.getCurrentList().get(holder.getAdapterPosition());
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
        return mDiffer.getCurrentList().size();
    }

    public List<ContactEntity> getSelectedItems() {
        List<ContactEntity> selected = new ArrayList<>();
        List<ContactEntity> currentList = mDiffer.getCurrentList();

        for (ContactEntity contact : currentList) {
            if (contact.isSelected()) {
                selected.add(contact);
            }
        }
        return selected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AlphabetView alphabetView;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alphabetView = itemView.findViewById(R.id.alphabetView);
            tvName = itemView.findViewById(R.id.tvName);
        }

        public void bindTo(ContactEntity contact) {
            alphabetView.setClickable(false);
            alphabetView.setSourceText(contact.getName());

            tvName.setText(contact.getFullName());
            alphabetView.setBackgroundColor(contact.getColor());

            alphabetView.setChecked(contact.isSelected());
            alphabetView.invalidate();

            itemView.setOnClickListener(v -> {
                if (isSelectingMode) {
                    setSelected(this);
                } else {
                    Intent intent = new Intent(context, Act_ContactDetail.class);
                    intent.putExtra(Constants.KEY_CONTACT_ID, mDiffer.getCurrentList().get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (!isSelectingMode) {
                    isSelectingMode = true;
                    onSelectingModeListener.onStartSelectingMode();
                    setSelected(this);
                    return true;
                }
                setSelected(this);
                return true;
            });
        }
    }

    public void destroySelectingMode() {
        isSelectingMode = false;
        List<ContactEntity> currentList = new ArrayList<>(mDiffer.getCurrentList());
        for (ContactEntity contact : currentList) {
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

        List<ContactEntity> currentList = mDiffer.getCurrentList();

        for (ContactEntity c : currentList) {
            c.setSelected(selected);
        }
        selectedCount = selected ? getItemCount() : 0;

        onSelectingModeListener.onSelectedChanged(selectedCount);

        submitList(currentList);
        notifyItemChangedInScreen();
    }

    public interface OnSelectingModeListener {
        void onStartSelectingMode();

        void onSelectedChanged(int selectedCount);
    }

    private final OnSelectingModeListener onSelectingModeListener;


    public static final DiffUtil.ItemCallback<ContactEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<ContactEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ContactEntity oldItem, @NonNull ContactEntity newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.isSelected() == newItem.isSelected();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContactEntity oldItem, @NonNull ContactEntity newItem) {
            return oldItem.equals(newItem);
        }
    };
}