package it.uniupo.sportapp.models;

import java.util.Calendar;

/**
 * Created by dgavio on 15/11/17.
 */

public class ChatMessage {

    private String messageText;
    private String messageUserKey;
    private String messageUserName;
    private String messageUserImage;
    private long messageTime;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, String messageUserKey, String messageUserName, String messageUserImage) {
        this.messageText = messageText;
        this.messageUserKey = messageUserKey;
        this.messageUserName = messageUserName;
        this.messageUserImage = messageUserImage;
        this.messageTime = Calendar.getInstance().getTimeInMillis();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUserKey() {
        return messageUserKey;
    }

    public void setMessageUserKey(String messageUser) {
        this.messageUserKey = messageUser;
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

    public String getMessageUserName() {
        return messageUserName;
    }

    public void setMessageUserName(String messageUserName) {
        this.messageUserName = messageUserName;
    }
}
