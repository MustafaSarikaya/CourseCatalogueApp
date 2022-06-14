package com.example.coursecatalogueapp.modules;

public class Student extends User{

    public Student(String name, String email, String uid) {
        super(name, "Student", email, uid);
    }

    @Override
    public String toString() {
        return "UID: " + getUid()
                + "\nName: " + getName()
                + "\nEmail: " + getEmailAddress()
                + "\nRole: " + getRole();
    }
}
