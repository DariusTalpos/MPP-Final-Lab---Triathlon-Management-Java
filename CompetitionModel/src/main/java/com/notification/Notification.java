package com.notification;

public class Notification {
    private NotificationType type;
    private String user, sender, messageText;

    public Notification() {
    }

    public Notification(NotificationType type) {

        this.type = type;
    }

    public Notification(NotificationType type, String user) {
        this.type = type;
        this.user = user;
    }

    public Notification(NotificationType type, String sender, String messageText) {
        this.type = type;
        this.sender=sender;
        this.messageText=messageText;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", user='" + user + '\'' +
                ", sender='" + sender + '\'' +
                ", messageText='" + messageText + '\'' +
                '}';
    }
}
