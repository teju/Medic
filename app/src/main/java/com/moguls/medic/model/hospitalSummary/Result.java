package com.moguls.medic.model.hospitalSummary;

import java.util.ArrayList;
import java.util.List;

public class Result {
    String Today;


    public ArrayList<com.moguls.medic.model.hospitalSummary.Sessions> getSessions() {
        return Sessions;
    }

    public void setSessions(ArrayList<com.moguls.medic.model.hospitalSummary.Sessions> sessions) {
        Sessions = sessions;
    }

    ArrayList<Sessions> Sessions = new ArrayList<>();
    String PhotoUrl;
    String Address;
    String Name;

    public String getToday() {
        return Today;
    }

    public void setToday(String today) {
        Today = today;
    }


    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;



}
