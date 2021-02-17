package com.sky.contact;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sky.contact.database.ContactEntity;
import com.sky.contact.utility.Constants;
import com.sky.contact.viewmodel.VM_CreateContact;

public class Act_CreateContact extends AppCompatActivity {
    private EditText etxtName;
    private EditText etxtFamily;
    private EditText etxtPhone;
    private EditText etxtEmail;
    private VM_CreateContact mViewModel;
    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_contact);

        findViews();

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(VM_CreateContact.class);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            isEditing = true;
            int id = bundle.getInt(Constants.KEY_CONTACT_ID);

            final Observer<ContactEntity> observer = contactEntity -> {
                etxtName.setText(contactEntity.getName());
                etxtFamily.setText(contactEntity.getFamily());
                etxtPhone.setText(contactEntity.getPhone());
                etxtEmail.setText(contactEntity.getEmail());
            };
            mViewModel.mContactLiveData.observe(this, observer);
            mViewModel.loadContact(id);
            setTitle(R.string.edit_contact);
        } else {
            setTitle(R.string.create_contact);
        }
    }

    private void findViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etxtName = findViewById(R.id.etxtName);
        etxtFamily = findViewById(R.id.etxtFamily);
        etxtPhone = findViewById(R.id.etxtPhone);
        etxtEmail = findViewById(R.id.etxtEmail);
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
        boolean saved = mViewModel.saveContact(etxtName.getText().toString(),
                etxtFamily.getText().toString(),
                etxtPhone.getText().toString(),
                etxtEmail.getText().toString());
        if (saved) {
            if (isEditing) {
                setResult(RESULT_OK);
            }
            finish();
        }
    }
}