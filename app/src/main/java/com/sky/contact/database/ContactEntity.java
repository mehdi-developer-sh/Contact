package com.sky.contact.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_contacts")
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String family;
    private String mobileNumber;
    private String emailAddress;

    public ContactEntity() {
    }

    @Ignore
    public ContactEntity(String name, String family, String mobileNumber, String emailAddress) {
        this.name = name;
        this.family = family;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
    }

    @Ignore
    public ContactEntity(int id, String name, String family, String mobileNumber, String emailAddress) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        return name + " " + family;
    }

    @NonNull
    @Override
    public String toString() {
        return "ContactEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
