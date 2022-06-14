package com.example.coursecatalogueapp.modules;

public class Course {

    private String courseCode, courseName, courseInfo, id;

    public Course() {}

    public Course(String courseCode, String courseName, String id){
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.id = id;
    }

//    public Course(String courseCode, String courseName, String courseInfo){
//        this.courseCode=courseCode;
//        this.courseName=courseName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//        this.courseInfo=courseInfo;
//    }

    public String getCourseCode(){
        return courseCode;
    }

    public void setCourseCode(String courseCode){
        this.courseCode=courseCode;
    }

    public String getCourseName(){
        return courseName;
    }

    public void setCourseName(String courseName){
        this.courseName=courseName;
    }

    public String getCourseInfo(){
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) { this.courseInfo=courseInfo;}
}
