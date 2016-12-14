package com.example.swapn.bookmyclass.models;

import java.util.List;

/**
 * Created by swapn on 11/30/2016.
 */

public class Book {
    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public List<String> getCourse_ids() {
        return course_ids;
    }

    public void setCourse_ids(List<String> course_ids) {
        this.course_ids = course_ids;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String book_id;
    private String book_name;
    private List<String> course_ids;
    private double price;
    private String author;
    private String book_image;
    private String summary;
}
