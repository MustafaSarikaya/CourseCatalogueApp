package com.example.coursecatalogueapp.modules;

public abstract class User {

    private String name, role, emailAddress, uid;

    public User(String name, String role, String emailAddress, String uid){
        this.name = name;
        this.role = role;
        this.emailAddress = emailAddress;
        this.uid = uid;
    }

    public String toString(){
        String result="";
        result+=getFullName()+"\n"
                +"Email Address: "+getEmailAddress()+"\n";
        return result;
    }

    public String getFullName(){
        return "Name: "+ name +" "+ role;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }
}
