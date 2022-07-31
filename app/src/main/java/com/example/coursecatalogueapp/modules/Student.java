package com.example.coursecatalogueapp.modules;

import java.util.LinkedList;

public class Student extends User{

    //private LinkedList<String> courses;

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
