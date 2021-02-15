package com.sky.contact.utility;

import com.sky.contact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            contacts.add(new Contact(i, "Mehdi", "Khosravi" + i,
                    "09172185392", "mehdidev78@gmail.com"));
            contacts.add(new Contact(i, "Mina", "Mohammadi" + i,
                    "09172185375", "mina@gmail.com"));
            contacts.add(new Contact(i, "Maryam", "Rasooli" + i,
                    "09172185784", "maryam@gmail.com"));
        }
        return contacts;
    }
}
