package com.moguls.medic.model.doctorAppointments;

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

    public List<com.moguls.medic.model.doctorAppointments.Sessions> getSessions() {
        return Sessions;
    }

    public void setSessions(List<com.moguls.medic.model.doctorAppointments.Sessions> sessions) {
        this.Sessions = sessions;
    }

    private List<com.moguls.medic.model.doctorAppointments.Sessions> Sessions = new ArrayList<>();

}
