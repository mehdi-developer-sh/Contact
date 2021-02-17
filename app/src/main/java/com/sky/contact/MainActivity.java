package com.sky.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.contact.database.ContactEntity;
import com.sky.contact.ui.RVAdapter;
import com.sky.contact.viewmodel.VM_Main;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView tvNotingFound;
    private RVAdapter adapter;
    private final List<ContactEntity> contactList = new ArrayList<>();

    private VM_Main mViewModel;

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
            contactList.clear();
            contactList.addAll(contactEntities);

            if (adapter == null) {
                adapter = new RVAdapter(this, contactList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        };

        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(VM_Main.class);

        mViewModel.mLiveContacts.observe(this, observer);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
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
        if (Id == R.id.action_add_sample){
            addSampleData();
        }else if (Id==R.id.action_delete_all){
            deleteAllContacts();
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
}