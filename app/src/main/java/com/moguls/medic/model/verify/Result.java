package com.moguls.medic.model.verify;

public class Result {
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;
    String IsDoctor;

    public String getIsDoctor() {
        return IsDoctor;
    }

    public void setIsDoctor(String isDoctor) {
        IsDoctor = isDoctor;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    String Token;


}
