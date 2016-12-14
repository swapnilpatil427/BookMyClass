package com.example.swapn.bookmyclass.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swapn on 11/30/2016.
 */

public class Course {
    private String courseid;
    private String coursename;
    private String greensheetUrl;
    private String  professor;
    private String schedule;
    private List<String> textbooks;
    private HashMap<String,String> studygroups;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public List<String> getDept() {
        return dept;
    }

    public void setDept(List<String> dept) {
        this.dept = dept;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private List<String> dept;
    private String summary;

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getGreensheetUrl() {
        return greensheetUrl;
    }

    public void setGreensheetUrl(String greensheetUrl) {
        this.greensheetUrl = greensheetUrl;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getScedule() {
        return schedule;
    }

    public void setScedule(String schedule) {
        this.schedule = schedule;
    }

    public List<String> getTextbooks() {
        return textbooks;
    }

    public void setTextbooks(List<String> textbooks) {
        this.textbooks = textbooks;
    }

    public HashMap<String,String> getStudygroups() {
        return studygroups;
    }

    public void setStudygroups(HashMap<String,String> studygroups) {
        this.studygroups = studygroups;
    }
}
