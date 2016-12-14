package com.example.swapn.bookmyclass.models;

/**
 * Created by swapn on 12/9/2016.
 */

public class User_Conversation {
    public User_Conversation(String user_id, String conversation_id) {
        this.user_id = user_id;
        this.conversation_id = conversation_id;
    }

    public User_Conversation() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    private String user_id;
    private String conversation_id;
}
