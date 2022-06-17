package com.example.coursecatalogueapp.auth;

public enum LoginError {
    None {
        public String toString() {
            return "";
        }
    },
    InvalidAdminLogin {
        public String toString() {
            return "Invalid admin login. ";
        }
    },
    FieldsEmpty {
        public String toString() {
            return "One or more required fields are empty. ";
        }
    },
    EmailInvalid {
        public String toString() {
            return "Email is invalid.";
        }
    },
    PasswordTooShort {
        public String toString() {
            return "Password is too short.";
        }
    },
}