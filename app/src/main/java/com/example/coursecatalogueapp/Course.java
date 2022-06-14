package com.example.coursecatalogueapp;

public class Course {

    private String courseName;
    private String courseCode;
    private String courseDescription;

    public Course() {
    }

    public Course(String courseName, String id){
        this.courseName = courseName;
        this.courseCode = id;
        this.courseDescription = "";
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
