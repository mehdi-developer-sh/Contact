package com.sky.contact.utility;

import com.sky.contact.database.ContactEntity;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<ContactEntity> getContacts() {
        List<ContactEntity> contactEntities = new ArrayList<>();

        contactEntities.add(new ContactEntity( "Mehdi", "Khosravi",
                "09172185392", "mehdidev78@gmail.com"));
        contactEntities.add(new ContactEntity( "Ali", "Mohammadi",
                "09172185375", "mina@gmail.com"));
        contactEntities.add(new ContactEntity( "Reza", "Rasooli",
                "09172185784", "maryam@gmail.com"));

        return contactEntities;
    }
}
