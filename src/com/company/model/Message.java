package com.company.model;

public class Message {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String type;
    String userId;
    String text;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;
    public Message(){}

    public Message(String address, String userId, String type, String text) {
        this.type = type;
        this.userId = userId;
        this.text = text;
        this.address = address;
    }

    public Message(String message){
        try {
            String [] parts = message.split(";");
            setAddress(parts[0]);
            setType(parts[2]);
            setUserId(parts[1]);
            setText(parts[3]);
        }catch (Exception e){

        }
    }

    public String getMessage(){
        return getAddress()+";"+getUserId()+";"+getType()+";"+getText();
    }

    public static Message createPublicMessage(String type, String text){
       return new Message("PUBLIC","Admin",type,text);
    }

    public static Message createPrivateMessage(String type, String text){
        return new Message("PRIVATE","Admin",type,text);
    }
}

