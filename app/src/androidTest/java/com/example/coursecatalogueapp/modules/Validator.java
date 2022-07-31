package com.example.coursecatalogueapp.modules;


public class Validator {



    public static boolean Enrollment(Course c, Student s){

        for(int i = 0; i < c.getCourseCap();i++){
            if(c.getCourseStudents() == s){
            return false;
            }
        }
        return true;

    }

    public static boolean Enrollment1(Course c, Student s){
       c.setCourseStudents(s);
       if(c.getCourseStudents() == (s)){
            return false;
        }
        return true;
    }


    public static int toTime(String timeStr) {
        String[] spliced = timeStr.split(":");
        int hour = Integer.parseInt(spliced[0]);
        int min = Integer.parseInt(spliced[1]);
        return hour * 60 + min;
    }

    public static boolean Conflict(String time1, String time2) {
        String[] spliced1 = time1.split("-");
        String[] spliced2 = time2.split("-");

        Integer start1 = toTime(spliced1[0]);
        Integer end1 = toTime(spliced1[1]);
        Integer start2 = toTime(spliced2[0]);
        Integer end2 = toTime(spliced2[1]);

        if (start1.compareTo(start2) >= 0 && start1.compareTo(end2) < 0) {
            return true;
        }

        if (end1.compareTo(start2) > 0 && end1.compareTo(end2) <= 0) {
            return true;
        }

        if (start2.compareTo(start1) >= 0 && start2.compareTo(end1) < 0) {
            return true;
        }

        return end2.compareTo(start1) > 0 && end2.compareTo(end1) <= 0;
    }

    public static boolean validTimeDay(Course c1, Course c2)  {

        if (!c1.getLecture1Day().equals(c2.getLecture1Day())) {
            return true;
        }
        else if (c1.getLecture1Time() != c2.getLecture1Time() && c1.getLecture2Time() != c2.getLecture2Time()) {
            return false;
        }
        else {
            return true;
        }
    }


    public static boolean validCapacity(Course c) {

        if(c.getCourseCapacity() != "0" ){
            return true;
        }
    return false;

    }
}