package com.example.coursecatalogueapp.modules;

import static org.junit.Assert.*;

import org.junit.Test;

public class InstructorTest {

    @Test
    public void testToString() {
        Instructor person = new Instructor("John Doe", "johndoe@noname.com", "S00000001");
        String s = "UID: " + "S00000001"
                + "\nName: " + "John Doe"
                + "\nEmail: " + "johndoe@noname.com"
                + "\nRole: " + "Instructor";

        assertEquals(s, person.toString());
    }

//    public static void main(String[] args){
//        TestUtils.runClass(AdministratorTest.class);
//    }
}