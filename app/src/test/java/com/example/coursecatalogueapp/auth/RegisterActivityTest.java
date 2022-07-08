package com.example.coursecatalogueapp.auth;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegisterActivityTest {

    @Test
    public void testInfoValidation(){

        //Email validation testing
        RegisterActivity bla = new RegisterActivity("John Doe", "blablablabla", "lalalala");
        assertEquals(bla.validateInputForTesting(), SignUpError.InvalidEmail);

        //Username validation testing
        bla = new RegisterActivity("   ", "blablablabla@bla.com", "lalalala");
        assertEquals(bla.validateInputForTesting(), SignUpError.InvalidName);
        bla = new RegisterActivity("", "blablablabla@bla.com", "lalalala");
        assertEquals(bla.validateInputForTesting(), SignUpError.FieldsEmpty);

        //Password validation testing
        bla = new RegisterActivity("John Doe", "blablablabla@bla.com", "lala");
        assertEquals(bla.validateInputForTesting(), SignUpError.PasswordTooShort);

        //No problem testing
        bla = new RegisterActivity("John Doe", "blablablabla@bla.com", "lalalala");
        assertEquals(bla.validateInputForTesting(), SignUpError.None);
    }

}