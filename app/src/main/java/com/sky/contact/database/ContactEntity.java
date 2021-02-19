package com.sky.contact.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "tbl_contacts")
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String family;
    private String phone;
    private String email;
    private int color;

    @Ignore
    private boolean isSelected;

    public ContactEntity() {
    }

    @Ignore
    public ContactEntity(String name, String family, String phone, String email) {
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.email = email;
    }

    @Ignore
    public ContactEntity(int id, String name, String family, String phone, String email) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.email = email;
    }

    @Ignore
    public ContactEntity(String name, String family, String phone, String email, int color) {
        this.name = name;
        this.family = family;
        this.phone = phone;
        this.email = email;
        this.color = color;
    }

    @Ignore
    public boolean isSelected() {
        return isSelected;
    }

    @Ignore
    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return name + " " + family;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return "ContactEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", mobileNumber='" + phone + '\'' +
                ", emailAddress='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactEntity that = (ContactEntity) o;
        return id == that.id &&
                color == that.color &&
                isSelected == that.isSelected &&
                Objects.equals(name, that.name) &&
                Objects.equals(family, that.family) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, family, phone, email, color, isSelected);
    }
}
