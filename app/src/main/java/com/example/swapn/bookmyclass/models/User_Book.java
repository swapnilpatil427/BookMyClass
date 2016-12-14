package com.example.swapn.bookmyclass.models;

import java.util.Date;

/**
 * Created by swapn on 11/30/2016.
 */

public class User_Book {
    private String user_id;
    private String book_id;
    private Date selling_date;
    private boolean sold;
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    private String place;
    private String email;
    private String contact;

    public Date getSold_date() {
        return sold_date;
    }

    public void setSold_date(Date sold_date) {
        this.sold_date = sold_date;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public Date getSelling_date() {
        return selling_date;
    }

    public void setSelling_date(Date selling_date) {
        this.selling_date = selling_date;
    }

    private Date sold_date;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String location;
}
