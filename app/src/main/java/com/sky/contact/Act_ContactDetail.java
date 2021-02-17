package com.sky.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.sky.alphabemodule.AlphabetView;
import com.sky.contact.database.ContactEntity;
import com.sky.contact.utility.Constants;
import com.sky.contact.viewmodel.VM_ContactDetail;

public class Act_ContactDetail extends AppCompatActivity {
    private static final int REQUEST_CODE_EDIT = 10;
    private static final int REQUEST_CODE_CALL_PHONE = 11;
    private AlphabetView alphabetView;
    private TextView tvFullName;
    private TextView tvPhone;
    private TextView tvEmail;

    private VM_ContactDetail mViewModel;
    private ContactEntity contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contact_detail);

        findViews();

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(VM_ContactDetail.class);

        mViewModel.mLiveContact.observe(this, contactEntity -> {
            this.contact = contactEntity;
            alphabetView.setSourceText(contactEntity.getName());
            alphabetView.invalidate();
            tvFullName.setText(contactEntity.getFullName());
            tvPhone.setText(contactEntity.getPhone());
            tvEmail.setText(contactEntity.getEmail());
        });

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt(Constants.KEY_CONTACT_ID);
        mViewModel.loadContact(id);
    }

    private void findViews() {
        alphabetView = findViewById(R.id.alphabetView);
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.contact_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int Id = item.getItemId();

        if (Id == android.R.id.home) {
            onBackPressed();
        } else if (Id == R.id.action_delete) {
            deleteAndReturn();
        } else if (Id == R.id.action_edit) {
            edit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void edit() {
        ContactEntity entity = mViewModel.mLiveContact.getValue();
        if (entity != null) {
            Intent intent = new Intent(this, Act_CreateContact.class);
            intent.putExtra(Constants.KEY_CONTACT_ID, entity.getId());
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            mViewModel.reloadContact();
        }
    }

    private void deleteAndReturn() {
        mViewModel.delete();
        finish();
    }

    public void call(View view) {
        int granted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (granted == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
        } else {
            call();
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getPhone()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CALL_PHONE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call();
        }
    }

    public void textMessage(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.getPhone(), null)));
    }
}