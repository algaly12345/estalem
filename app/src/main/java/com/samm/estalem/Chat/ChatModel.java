package com.samm.estalem.Chat;

public class ChatModel {
    public int id;
    public String message;
    public String userPhone;
    public String otherUserPhone;
    public String dateTimeNow;
    public String messageType;


    public ChatModel(String message, String userPhone, String otherUserPhone, String dateTimeNow,String MessageType) {
        this.message = message;
        this.userPhone = userPhone;
        this.otherUserPhone = otherUserPhone;
        this.dateTimeNow = dateTimeNow;
        this.messageType = MessageType;
    }
}
