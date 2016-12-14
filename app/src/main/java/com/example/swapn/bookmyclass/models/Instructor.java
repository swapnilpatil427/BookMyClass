package com.example.swapn.bookmyclass.models;

import java.util.List;

/**
 * Created by swapn on 12/9/2016.
 */

public class Instructor
{
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPortfoliourl() {
        return portfoliourl;
    }

    public void setPortfoliourl(String portfoliourl) {
        this.portfoliourl = portfoliourl;
    }

    public List<String> getDeptid() {
        return deptid;
    }

    public void setDeptid(List<String> deptid) {
        this.deptid = deptid;
    }

    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    private String portfoliourl;
    private List<String> deptid;
    private List<String> course;

}
