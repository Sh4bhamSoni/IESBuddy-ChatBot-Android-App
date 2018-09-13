package com.ssgmail.shubhammsoni.iesbuddy;

public class Message_Model {

    String message, imageUrl = null;
    boolean sender;


    public Message_Model(String message, boolean sender, String imageUrl) {
        this.message = message;
        this.sender = sender;
        this.imageUrl = imageUrl;
    }

    public Message_Model(String message, boolean sender) {
        this.message = message;
        this.sender = sender;
    }


    public String getMessage() {
        return message;
    }

    public boolean isSender() {
        return sender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSender(boolean sender) {
        this.sender = sender;
    }
}
