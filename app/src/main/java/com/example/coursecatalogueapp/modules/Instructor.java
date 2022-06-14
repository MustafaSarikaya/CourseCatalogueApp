package com.example.coursecatalogueapp.modules;
import java.util.LinkedList;

public class Instructor extends User  {
    protected LinkedList<Course> courseList;

    public Instructor(String firstName, String lastName, String emailAddress, String passWord) {
        super(firstName, lastName, emailAddress, passWord);
        courseList = new LinkedList<Course>();
    }

    public LinkedList<Course> getCourseList(){
        return courseList;
    }
    public void addCourse(Course c) {
        courseList.add(c);
    }
    public void removeCourse(Course c) {
        courseList.remove(c);
    }
}