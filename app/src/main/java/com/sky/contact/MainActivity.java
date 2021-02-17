package com.sky.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.contact.database.AppDatabase;
import com.sky.contact.database.ContactEntity;
import com.sky.contact.ui.RVAdapter;
import com.sky.contact.utility.SampleData;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView tvNotingFound;
    private RVAdapter adapter;
    private final List<ContactEntity> contactList = SampleData.getContacts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new RVAdapter(this, contactList);
        recyclerView.setAdapter(adapter);
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

    public void addContactBtnClick(View view) {
        startActivity(new Intent(this, Act_CreateContact.class));
    }
}