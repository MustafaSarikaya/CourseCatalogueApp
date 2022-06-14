package com.example.coursecatalogueapp.modules;

public class Instructor extends User {
    public Instructor(String name, String email, String uid) {
        super(name, "Instructor", email, uid);
    }

    @Override
    public String toString() {
        return "UID: " + getUid()
                + "\nName: " + getName()
                + "\nEmail: " + getEmailAddress()
                + "\nRole: " + getRole();
    }
}
