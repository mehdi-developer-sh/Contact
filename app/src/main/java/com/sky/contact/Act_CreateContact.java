package com.sky.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Act_CreateContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_contact);
        findViews();
    }

    private void findViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Create ContactEntity");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int ID = item.getItemId();
        if (ID == android.R.id.home || ID == R.id.action_cancel) {
            onBackPressed();
        } else if (ID == R.id.action_save) {
            saveAndReturn();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndReturn() {
        finish();
    }
}