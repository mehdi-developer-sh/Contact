package com.sky.contact.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sky.contact.database.dao.ContactDao;

@Database(entities = {ContactEntity.class}, version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppDatabase.db";
    private static AppDatabase instance;
    private static final Object LOCK = new Object();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        return instance;
    }

    public abstract ContactDao getContactDao();
}
