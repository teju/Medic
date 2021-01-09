package com.moguls.medic.model.hospitalViews;

import java.util.ArrayList;
import java.util.List;

public class Result {
    List<String> Sessions = new ArrayList<>();
    String Name;
    String ID;
    String Address;
    String PhotoUrl;

    public String getAppointmentCount() {
        return AppointmentCount;
    }

    public void setAppointmentCount(String appointmentCount) {
        AppointmentCount = appointmentCount;
    }

    String AppointmentCount;

    public List<String> getSessions() {
        return Sessions;
    }

    public void setSessions(List<String> sessions) {
        Sessions = sessions;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public boolean getIsVerified() {
        return IsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        IsVerified = isVerified;
    }

    boolean IsVerified;


}
