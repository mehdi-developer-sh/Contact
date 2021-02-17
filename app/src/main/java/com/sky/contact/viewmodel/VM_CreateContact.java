package com.sky.contact.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sky.contact.database.AppRepository;
import com.sky.contact.database.ContactEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VM_CreateContact extends AndroidViewModel {
    public MutableLiveData<ContactEntity> mContactLiveData = new MutableLiveData<>();
    private final AppRepository mRepository;

    public VM_CreateContact(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public boolean saveContact(String name, String family, String phone, String email) {
        if (TextUtils.isEmpty(name.trim())) {
            return false;
        }

        if (TextUtils.isEmpty(phone.trim())) {
            return false;
        }

        if (!TextUtils.isEmpty(email.trim()) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }

        ContactEntity contact = mContactLiveData.getValue();
        if (contact == null) {
            contact = new ContactEntity();
        }

        contact.setName(name);
        contact.setFamily(family);
        contact.setPhone(phone);
        contact.setEmail(email);

        mRepository.saveContact(contact);
        return true;
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadContact(int id) {
        executor.execute(() -> mContactLiveData.postValue(mRepository.findById(id)));
    }
}
