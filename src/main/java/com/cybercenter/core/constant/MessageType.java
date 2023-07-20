package com.cybercenter.core.constant;

public enum MessageType {
    EMAIL(1 , "email"),
    SMS(2, "sms");

    private final int id;
    private final String title;

    MessageType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
