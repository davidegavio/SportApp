package it.uniupo.sportapp.models;

import java.util.Calendar;

/**
 * Created by dgavio on 15/11/17.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageUserImage;
    private long messageTime;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, String messageUser, String messageUserImage) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = Calendar.getInstance().getTimeInMillis();
        this.messageUserImage = messageUserImage;
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
    }

    public String getMessageUserImage() {
        return messageUserImage;
    }

    public void setMessageUserImage(String messageUserImage) {
        this.messageUserImage = messageUserImage;
    }
}
