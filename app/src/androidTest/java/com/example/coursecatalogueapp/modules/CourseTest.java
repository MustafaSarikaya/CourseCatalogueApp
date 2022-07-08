package com.example.coursecatalogueapp.modules;

import static org.junit.Assert.*;

import org.junit.Test;

public class CourseTest {

    @Test
    public void testGeneral() {
        Course c= new Course("SEG2105", "Introduction To Software Engineering", "SEG21052022SS");
        assertEquals("SEG2105", c.getCourseCode());
        assertEquals("Introduction To Software Engineering", c.getCourseName());
        assertEquals("SEG21052022SS", c.getId());
    }

}