package com.example.coursecatalogueapp.modules;

public abstract class User {

    private String firstName, lastName, emailAddress, passWord;

    public User(String firstName,String lastName, String emailAddress, String passWord){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.passWord = passWord;
    }

    public String toString(){
        String result="";
        result+=getFullName()+"\n"
                +"Email Address: "+getEmailAddress()+"\n";
        return result;
    }

    public String getFullName(){
        return "Name: "+firstName+" "+lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public String getPassWord(){
        return passWord;
    }

    public void setPassWord(String passWord){
        this.passWord = passWord;
    }
}
