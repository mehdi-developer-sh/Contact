package com.sky.contact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.contact.database.ContactEntity;
import com.sky.contact.ui.RVAdapter;
import com.sky.contact.viewmodel.VM_Main;

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;

public class MainActivity extends AppCompatActivity implements RVAdapter.OnSelectingModeListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView tvNotingFound;
    private RVAdapter adapter;
    private VM_Main mViewModel;
    private TextView tvSelectedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        initRecyclerView();

        initViewModel();
    }

    private void initViewModel() {
        final Observer<List<ContactEntity>> observer = contactEntities -> {
            if (adapter == null) {
                adapter = new RVAdapter(recyclerView, this);
                recyclerView.setAdapter(adapter);
            }
            adapter.submitList(contactEntities);
        };

        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(VM_Main.class);

        mViewModel.mLiveContacts.observe(this, observer);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInDownAnimator());
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        tvNotingFound = findViewById(R.id.tvNotingFound);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int Id = item.getItemId();
        if (Id == R.id.action_add_sample) {
            addSampleData();
        } else if (Id == R.id.action_delete_all) {
            deleteAllContacts();
        } else if (Id == R.id.action_get_count) {
            Toast.makeText(this, adapter.getItemCount() + "", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllContacts() {
        mViewModel.deleteAll();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }

    public void addContactBtnClick(View view) {
        startActivity(new Intent(this, Act_CreateContact.class));
    }

    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int Id = item.getItemId();
            switch (Id) {
                case R.id.action_cancel: {
                    mode.finish();
                    break;
                }
                case R.id.action_deselect_all: {
                    adapter.setSelectedAll(false);
                    break;
                }
                case R.id.action_delete: {
                    mViewModel.deleteAll(adapter.getSelectedItems());
                    recyclerView.postDelayed(mode::finish, 100);
                    break;
                }
                case R.id.action_select_all: {
                    adapter.setSelectedAll(true);
                    break;
                }
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.destroySelectingMode();
        }
    };
    private View actionView;

    @Override
    public void onStartSelectingMode() {
        ActionMode actionMode = toolbar.startActionMode(callback);

        if (actionView == null) {
            actionView = LayoutInflater.from(this).inflate(R.layout.action_mode_layout, toolbar, false);
        }
        actionMode.setCustomView(actionView);

        tvSelectedCount = actionView.findViewById(R.id.tvCount);
    }


    @Override
    public void onSelectedChanged(int selectedCount) {
        final String text = selectedCount + " " + getString(R.string.selected);
        tvSelectedCount.setText(text);
    }
}