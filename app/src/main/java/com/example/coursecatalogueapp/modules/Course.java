package com.example.coursecatalogueapp.modules;

public class Course {

    private String courseCode, courseName, courseInfo, courseInstructor, id;

    public Course() {
        id = "";
        courseName = "";
        courseCode = "";
        courseInfo = "";
        courseInstructor="";
    }

    public Course(String courseCode, String courseName, String id,String courseInstructor){
        this.courseCode=courseCode;
        this.courseName=courseName;
        this.courseInstructor = courseInstructor;
        this.id = id;
        courseInfo = "";
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

    public  void setCourseInstructor(String courseProf) {
        this.courseInstructor = courseProf;
        // courseProf= new CourseProf(courseProf);}
    }
    public  String getCourseInstructor(){
        return courseInstructor;}

    public void setCourseName(String courseName){
        this.courseName=courseName;
    }

    public String getCourseInfo(){
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) { this.courseInfo=courseInfo;}
}
