package com.moguls.medic.model.getAppointment;

public class Patient {
    private String Name;

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

    private String ID;
    private String MobileNo;

    public String getMobileNo() {
        return MobileNo;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    private String PhotoUrl;
}
