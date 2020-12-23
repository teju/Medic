package com.moguls.medic.model.appointmentSlots;

import java.util.ArrayList;
import java.util.List;

public class Result {

    String Today;
    String Date;

    public String getToday() {
        return Today;
    }

    public void setToday(String today) {
        Today = today;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public List<Sessions> getSessions() {
        return Sessions;
    }

    public void setSessions(List<Sessions> sessions) {
        this.Sessions = sessions;
    }

    private List<Sessions> Sessions = new ArrayList<>();
    private List<Slots> slots = new ArrayList<>();

}
