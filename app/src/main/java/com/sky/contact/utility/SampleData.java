package com.sky.contact.utility;

import com.sky.contact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();

        contacts.add(new Contact(0, "Mehdi", "Khosravi",
                "09172185392", "mehdidev78@gmail.com"));
        contacts.add(new Contact(1, "Ali", "Mohammadi",
                "09172185375", "mina@gmail.com"));
        contacts.add(new Contact(2, "Reza", "Rasooli",
                "09172185784", "maryam@gmail.com"));

        return contacts;
    }
}
