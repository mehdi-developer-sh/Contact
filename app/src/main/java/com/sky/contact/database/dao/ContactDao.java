package com.sky.contact.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sky.contact.database.ContactEntity;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContactEntity contactEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContactEntity> contactEntity);

    @Delete
    void delete(ContactEntity contactEntity);

    @Query("DELETE FROM tbl_contacts")
    void deleteAll();

    @Query("SELECT * FROM tbl_contacts")
    LiveData<List<ContactEntity>> getAllContacts();

    @Query("SELECT * FROM tbl_contacts WHERE id=:id")
    ContactEntity findById(int id);

    @Query("SELECT COUNT(*) FROM tbl_contacts")
    int getCount();

    @Delete
    void deleteAll(List<ContactEntity> selectedItems);
}