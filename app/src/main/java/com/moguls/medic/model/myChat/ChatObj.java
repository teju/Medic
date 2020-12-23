package com.moguls.medic.model.myChat;

public class ChatObj {
    String FromUserID;

    String UnreadCount;
    String Message;
    String SendOn;
    String DeletedOn;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String messageID) {
        MessageID = messageID;
    }

    String MessageID;


    public String getFromUserID() {
        return FromUserID;
    }

    public void setFromUserID(String fromUserID) {
        FromUserID = fromUserID;
    }



    public String getUnreadCount() {
        return UnreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        UnreadCount = unreadCount;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSendOn() {
        return SendOn;
    }

    public void setSendOn(String sendOn) {
        SendOn = sendOn;
    }

    public String getDeletedOn() {
        return DeletedOn;
    }

    public void setDeletedOn(String deletedOn) {
        DeletedOn = deletedOn;
    }

    public com.moguls.medic.model.myChat.File getFile() {
        return File;
    }

    public void setFile(com.moguls.medic.model.myChat.File file) {
        File = file;
    }

    File File;
}
