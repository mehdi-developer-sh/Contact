package com.sky.contact.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sky.contact.database.AppRepository;
import com.sky.contact.database.ContactEntity;

import java.util.List;

public class VM_Main extends AndroidViewModel {
    public LiveData<List<ContactEntity>> mLiveContacts;
    private final AppRepository mRepository;

    public VM_Main(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mLiveContacts = mRepository.getAllContacts();
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteAll(List<ContactEntity> selectedItems) {
        mRepository.deleteAll(selectedItems);
    }
}