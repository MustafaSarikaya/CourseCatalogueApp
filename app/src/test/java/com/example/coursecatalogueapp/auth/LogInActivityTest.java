package com.example.coursecatalogueapp.auth;

import static org.junit.Assert.*;

import org.junit.Test;

public class LogInActivityTest {

    @Test
    public void testInputValidation(){

        //Email is empty
        LogInActivity bla = new LogInActivity("", "123456");
        assertEquals(bla.validateInputForTesting(), LoginError.FieldsEmpty);

        //Email is invalid
        bla = new LogInActivity("blalalala", "123456");
        assertEquals(bla.validateInputForTesting(), LoginError.EmailInvalid);

        //password is invalid
        bla = new LogInActivity("blalalala@bla.com", "12345");
        assertEquals(bla.validateInputForTesting(), LoginError.PasswordTooShort);

        //Everything is correct
        bla = new LogInActivity("blalalala@bla.com", "123456");
        assertEquals(bla.validateInputForTesting(), LoginError.None);

    }

//    @Test
//    public void testFailedAuthentication(){
//
//    }

}