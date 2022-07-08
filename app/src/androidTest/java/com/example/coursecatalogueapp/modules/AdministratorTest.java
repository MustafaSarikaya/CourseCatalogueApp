package com.example.coursecatalogueapp.modules;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import org.junit.rules.Timeout;
import org.junit.Rule;



public class AdministratorTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);
    //private static int count = 0;


    @Test
    public void testConstructor(){
        Administrator admin = new Administrator("A3467");
        assertEquals("A3467", admin.getUid());
        assertEquals("admin", admin.getName());
        assertEquals("admin", admin.getRole());
        assertEquals("admin@gmail.com", admin.getEmailAddress());
        //count++;

    }

//    @Test
//    public void testAddCourse(){
//        Administrator admin = new Administrator("A3467");
//        assertEquals(0, admin.getCourseList().size());
//        Course c= new Course("SEG2105", "Introduction To Software Engineering", "SEG21052022SS");
//        admin.addCourse(c);
//        assertEquals(1, admin.getCourseList().size());
//        //count++;
//    }
//
//    @Test
//    public void testDeleteCourse(){
//        Administrator admin = new Administrator("A3467");
//        Course c= new Course("SEG2105", "Introduction To Software Engineering", "SEG21052022SS");
//        admin.addCourse(c);
//        assertEquals(1, admin.getCourseList().size());
//        admin.deleteCourse("SEG2105");
//        assertEquals(0, admin.getCourseList().size());
//        //count++;
//    }
//
//
//    @Test
//    public void testSetCourseCode(){
//        Administrator admin = new Administrator("A3467");
//        Course c= new Course("SEG2105", "Introduction To Digital Systems", "ITI11002022SS");
//        admin.addCourse(c);
//        admin.setcourseCode(c, "ITI1101");
//        assertEquals("ITI1100", admin.getCourseList().getFirst().getCourseCode());
//        //count++;
//    }
//
//
//    @Test
//    public void testSetCourseName(){
//        Administrator admin = new Administrator("A3467");
//        Course c= new Course("SEG2105", "Introduction To Digital Systems", "SEG21052022SS");
//        admin.addCourse(c);
//        admin.setCourseName(c, "Introduction To Software Engineering");
//        assertEquals("Introduction To Software Engineering", admin.getCourseList().getFirst().getCourseName());
//        //count++;
//    }
//
//
//    @Test
//    public void testSearchCourse(){
//        Administrator admin = new Administrator("A3467");
//        Course c1 = new Course("SEG2105", "Introduction To Software Engineering", "SEG21052022SS");
//        Course c2 = new Course("ITI1100", "Introduction To Digital Systems", "ITI11002022SS");
//        Course c3 = new Course("ITI1120", "Introduction To Computing I", "ITI11202022SS");
//        Course c4 = new Course("ITI1121", "Introduction To Computing II", "ITI11212022SS");
//        Course c5 = new Course("CEG2136", "Computer Architecture I", "CEG21362022SS");
//        admin.addCourse(c1);
//        admin.addCourse(c2);
//        admin.addCourse(c3);
//        admin.addCourse(c4);
//        admin.addCourse(c5);
//
//        Course searchResult1 = admin.searchCourseByName("Introduction To Digital Systems");
//        assertEquals(c1, searchResult1);
//        Course searchResult2 = admin.searchCourseByCode("ITI1100");
//        assertEquals(c2, searchResult2);
//
//        //count++;
//
//    }


//    public static void main(String[] args){
//        TestUtils.runClass(AdministratorTest.class);
//    }



}