package com.example.coursecatalogueapp.modules;

import java.util.LinkedList;

public class Student extends User {
    protected LinkedList<Course> enrollmentList;

    public Student(String firstName, String lastName, String emailAddress, String passWord) {
        super(firstName, lastName, emailAddress, passWord);
        enrollmentList = new LinkedList<Course>();
    }

    public LinkedList<Course> getEnrollmentList(){
        return enrollmentList;
    }
    public void addEnrollmentCourse(Course c) {
        enrollmentList.add(c);
    }
    public void removeEnrollmentCourse(Course c) {
        enrollmentList.remove(c);
    }
}