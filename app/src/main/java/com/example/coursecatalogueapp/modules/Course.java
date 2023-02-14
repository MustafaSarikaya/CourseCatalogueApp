package com.example.coursecatalogueapp.modules;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseCode;
    private String courseName;
    private String courseInstructor;
    private String id;
    private String courseDescription;
    private String courseCapacity;
    private int  courseCap;
    private Student  courseStudents;
    private String lecture1Day;
    private String lecture1Time;
    private String lecture2Day;
    private String lecture2Time;
    public Course() {
        id = "";
        courseName = "";
        courseCode = "";
        courseInstructor= "";

    }

    public Course(String courseCode, String courseName, String id){
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public void setCourseCode(String courseCode){
        this.courseCode=courseCode;
    }

    public String getCourseName(){
        return courseName;
    }

    public  void setCourseInstructor(String courseProf) {this.courseInstructor = courseProf;}
    public  String getCourseInstructor(){return courseInstructor;}

    public void setCourseName(String courseName){
        this.courseName=courseName;
    }


    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseCapacity() {
        return courseCapacity;
    }
    public Integer getCourseCap(){return courseCap;}
    public Student getCourseStudents(){return courseStudents;}

    public void setCourseCapacity(String courseCapacity) {
        this.courseCapacity = courseCapacity;
    }

    public String getLecture1Day() {
        return lecture1Day;
    }

    public void setLecture1Day(String lecture1Day) {
        this.lecture1Day = lecture1Day;
    }

    public String getLecture1Time() {
        return lecture1Time;
    }

    public void setLecture1Time(String lecture1Time) {
        this.lecture1Time = lecture1Time;
    }

    public String getLecture2Day() {
        return lecture2Day;
    }

    public void setLecture2Day(String lecture2Day) {
        this.lecture2Day = lecture2Day;
    }

    public String getLecture2Time() {
        return lecture2Time;
    }

    public void setLecture2Time(String lecture2Time) {
        this.lecture2Time = lecture2Time;
    }



    public void setCourseStudents(Student courseStudents) {
        this.courseStudents = courseStudents;
    }
}
