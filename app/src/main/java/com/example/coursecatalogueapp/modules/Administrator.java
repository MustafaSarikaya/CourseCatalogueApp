package com.example.coursecatalogueapp.modules;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Administrator extends User {

    protected LinkedList<Course> courseList;//may be protected data

    public Administrator(String uid) {
        super("admin", "admin", "admin@gmail.com", uid);
    }



    public void addCourse(Course course) {
        courseList.add(course);
    }

    public void deleteCourse(String courseCode) {
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getCourseCode() == courseCode) {
                iter.remove();
            }
        }
    }

    public void setcourseCode(Course course, String courseCode){course.setCourseCode(courseCode);}

    public void setCourseName(Course course, String courseName){course.setCourseName(courseName);}

    public Course searchCourseByCode(String courseCode){
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getCourseCode() == courseCode) {
                return iter.next();
            }
        }
        throw new NoSuchElementException("cannot find this course!");
    }

    public Course searchCourseByName(String courseName){
        for (ListIterator<Course> iter = courseList.listIterator(); iter.hasNext(); ) {
            if (iter.next().getCourseName() == courseName) {
                return iter.next();
            }
        }
        throw new NoSuchElementException("cannot find this course!");
    }

    public LinkedList<Course> getCourseList(){
        return courseList;
    }
}
