package com.example.swapn.bookmyclass.models;

import java.util.ArrayList;

/**
 * Created by swapn on 11/20/2016.
 */

public class User {
    private String uid;
    private String name;
    private String gender;
    private String email;
    private boolean account_setuped;
    private String prof_pic;
    private ArrayList<User_Conversation> conversations;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isAccount_setuped() {
        return account_setuped;
    }

    public void setAccount_setuped(boolean account_setuped) {
        this.account_setuped = account_setuped;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProf_pic() {
        return prof_pic;
    }

    public void setProf_pic(String prof_pic) {
        this.prof_pic = prof_pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean insert_user() {
        //new FirebaseDb().getmDatabase().getReference("message");
        // mDatabase.child("users").
        return true;
    }
}
