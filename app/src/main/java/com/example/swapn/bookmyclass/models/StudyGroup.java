package com.example.swapn.bookmyclass.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by swapn on 12/9/2016.
 */

public class StudyGroup {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }

    private String id;
    private String name;
    private String description;
    private HashMap<String, String> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
