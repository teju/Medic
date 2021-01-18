package com.moguls.medic.model.doctorAppointments;

public class Slots {


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    int ID;

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    int parentID;
}
