package com.sky.contact;


import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sky.contact.database.AppDatabase;
import com.sky.contact.database.dao.ContactDao;
import com.sky.contact.utility.SampleData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final String TAG = "DatabaseTest";
    private AppDatabase db;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), AppDatabase.class)
                .build();
        Log.i(TAG, "createDb");
    }

    @Test
    public void insertData() {
        ContactDao dao = db.getContactDao();
        dao.insertAll(SampleData.getContacts());
        Log.i(TAG, "insertData: ");
        Log.i(TAG, "Number of rows : " + dao.getCount());

        Assert.assertEquals(SampleData.getContacts().size(), dao.getCount());
    }

    @After
    public void closeDb() {
        db.close();
    }
}