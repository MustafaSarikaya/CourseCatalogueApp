package com.example.coursecatalogueapp.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



import org.junit.Test;

import java.time.DayOfWeek;
import java.util.List;


public class Valid_Tests {

    @Test
    // Testing whether the student is already enrolled while attempting to enroll
    public void testEnrollment(){
        Course c1 = new Course();
        Student s1 = new Student("Selim", "s@gmail.com", "30020");

        assertTrue(Validator.Enrollment(c1,s1));
    }
    @Test
    public void testEnrollment_false(){
        Course c1 = new Course();
        Student s1 = new Student("Selim", "s@gmail.com", "30020");

        assertFalse(Validator.Enrollment1(c1,s1));
    }

    @Test
    // Testing whether there is a time conflict
    public void testConflictTime_Time() {
        assertTrue(Validator.Conflict("14:00-15:00", "14:30-16:00"));
        assertTrue(Validator.Conflict("14:00-15:00", "13:30-16:00"));
    }

    @Test
    // Testing whether the courses are at the same time including day
    public void testConflictTime(){
        String[] Day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Course c1 = new Course();
        Course c2 = new Course();
        c1.setLecture1Day(Day[0]);    c2.setLecture1Day(Day[1]);
        c1.setLecture1Time("16:30");
        c1.setLecture2Time("16:30");

        assertTrue(Validator.validTimeDay(c1,c2));

    }

    @Test
    // Testing whether there is a capacity issue while enrolling in a course
    public void Valid_Capacity_true(){
        Course c = new Course();
        c.setCourseCapacity("10");

        assertTrue(Validator.validCapacity(c));
    }
    @Test
    public void Valid_Capacity_False(){
        Course c = new Course();
        c.setCourseCapacity("0");

        assertFalse(Validator.validCapacity(c));
    }
}