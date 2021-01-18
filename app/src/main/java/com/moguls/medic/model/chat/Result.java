package com.moguls.medic.model.chat;

import com.moguls.medic.model.myChat.File;

public class Result {
    String IsSendByMe;
    String Message;

    public com.moguls.medic.model.myChat.File getFile() {
        return File;
    }

    public void setFile(com.moguls.medic.model.myChat.File file) {
        File = file;
    }

    File File;
    String SendOn;
    String ReadOn;
    String DeletedOn;

    public String getIsSendByMe() {
        return IsSendByMe;
    }

    public void setIsSendByMe(String isSendByMe) {
        IsSendByMe = isSendByMe;
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

    public String getReadOn() {
        return ReadOn;
    }

    public void setReadOn(String readOn) {
        ReadOn = readOn;
    }

    public String getDeletedOn() {
        return DeletedOn;
    }

    public void setDeletedOn(String deletedOn) {
        DeletedOn = deletedOn;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;



}
