package com.example.swapn.bookmyclass.models;

import java.util.Date;

/**
 * Created by swapn on 12/8/2016.
 */

public class Message {
    public String getSender() {
        return sender;
    }

    public Message() {
    }

    public Message(String sender, Date date, String messageText) {
        this.sender = sender;
        this.date = date;
        this.messageText = messageText;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    private String sender;
    private Date date;
    private String messageText;
}
