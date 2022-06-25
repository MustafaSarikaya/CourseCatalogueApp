package com.example.coursecatalogueapp.modules;

public class Test {
    public static void main(String[] args) {
        TestUtils.runClass(Administrator.class);
        TestUtils.runClass(StudentTest.class);

        TestUtils.runClass(CourseTest.class);
        TestUtils.runClass(InstructorTest.class);
    }
}
