package com.example.coursecatalogueapp;

public class Course {

    private String courseName;
    private String courseId;
    private String courseDescription;

    public Course(String n, String id){
        courseName = n;
        courseId = id;
        courseDescription = "";
    }


    public Course(String n, String id, String d){
        courseName = n;
        courseId = id;
        courseDescription = d;
    }


}
