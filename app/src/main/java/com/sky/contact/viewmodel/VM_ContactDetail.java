package com.sky.contact.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sky.contact.database.AppRepository;
import com.sky.contact.database.ContactEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VM_ContactDetail extends AndroidViewModel {
    public MutableLiveData<ContactEntity> mLiveContact = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AppRepository mRepository;

    public VM_ContactDetail(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application);
    }

    public void loadContact(int id) {
        executor.execute(() -> mLiveContact.postValue(mRepository.findById(id)));
    }

    public void reloadContact() {
        ContactEntity c = mLiveContact.getValue();
        if (c != null) {
            loadContact(c.getId());
        }
    }

    public void delete() {
        mRepository.delete(mLiveContact.getValue());
    }
}
