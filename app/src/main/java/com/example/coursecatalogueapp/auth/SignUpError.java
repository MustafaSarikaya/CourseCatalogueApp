package com.example.coursecatalogueapp.auth;

public enum SignUpError {
    None {
        public String toString() {
            return "";
        }
    },
    FieldsEmpty {
        public String toString() {
            return "One or more required fields are empty. ";
        }
    },
    InvalidName {
        public String toString() {
            return "Invalid name inputted. ";
        }
    },
    InvalidEmail {
        public String toString() {
            return "Email is invalid.";
        }
    },
    PasswordTooShort {
        public String toString() {
            return "Password is too short.";
        }
    },
    PasswordsNoMatch {
        public String toString() {
            return "Passwords do not match.";
        }
    }
}