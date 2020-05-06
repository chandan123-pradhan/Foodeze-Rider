package com.foodeze.rider.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */
public class ChatMessage {

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public ChatMessage(String receiver_id,String sender_id,String sender_name,String text )
    {
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.text = text;

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        timestamp = formattedDate;
    }

    public ChatMessage(){

    }

    private String text;
    private String sender_name;
    private String timestamp;
    private String sender_id,receiver_id;

   /* public String getSenderID() {
        return sender_id;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }



    public ChatMessage(String messageText, String messageUser,String senderID,String receiverID) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.senderID = senderID;
        this.receiverID = receiverID;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }*/
}
