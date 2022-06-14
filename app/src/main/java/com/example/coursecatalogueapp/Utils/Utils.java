package com.example.coursecatalogueapp.Utils;

import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Utils {

    /**
     * Checks if string is of valid email format
     * @param email the email to verify
     * @return boolean true if valid, false if invalid.
     */
    public static boolean isEmailValid(String email) {
        return androidx.core.util.PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Checks if string is of valid name format
     * @param name the name to verify
     * @return whether the name is a valid format
     */
    public static boolean isNameValid(String name) {
        return name.matches("^[a-zA-Z\\s]*$");
    }

    /**
     * Shows snackbar with given message. The snackbar has a close button, which does nothing.
     * @param message the message to show.
     * @param view the view to show the snackbar on.
     */
    public static void showSnackbar(String message, View view) {
        //Create snackbar
        Snackbar snackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT);
        //Add close button that does nothing
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
        //Shows the snackbar
        snackbar.show();
    }
}
