package com.sky.contact.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sky.contact.utility.SampleData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository instance;
    private final AppDatabase mDb;
    private static final Object LOCK = new Object();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
    }

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new AppRepository(context);
            }
        }
        return instance;
    }

    public LiveData<List<ContactEntity>> getAllContacts() {
        return mDb.getContactDao().getAllContacts();
    }

    public void addSampleData() {
        executor.execute(() -> mDb.getContactDao().insertAll(SampleData.getContacts()));
    }

    public void deleteAll() {
        executor.execute(() -> mDb.getContactDao().deleteAll());
    }

    public void saveContact(ContactEntity contact) {
        executor.execute(() -> mDb.getContactDao().insert(contact));
    }

    public ContactEntity findById(int id) {
        return mDb.getContactDao().findById(id);
    }

    public void delete(ContactEntity value) {
        executor.execute(() -> mDb.getContactDao().delete(value));
    }

    public void deleteAll(List<ContactEntity> selectedItems) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.getContactDao().deleteAll(selectedItems);
            }
        });
    }
}
