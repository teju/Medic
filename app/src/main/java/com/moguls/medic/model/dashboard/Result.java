package com.moguls.medic.model.dashboard;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private String NoOfDoctors;
    private String NoOfAppointments;
    private String NoOfUnreadMessages;

    public String getNoOfDoctors() {
        return NoOfDoctors;
    }

    public void setNoOfDoctors(String noOfDoctors) {
        NoOfDoctors = noOfDoctors;
    }

    public String getNoOfAppointments() {
        return NoOfAppointments;
    }

    public void setNoOfAppointments(String noOfAppointments) {
        NoOfAppointments = noOfAppointments;
    }

    public String getNoOfUnreadMessages() {
        return NoOfUnreadMessages;
    }

    public void setNoOfUnreadMessages(String noOfUnreadMessages) {
        NoOfUnreadMessages = noOfUnreadMessages;
    }

    public List<com.moguls.medic.model.dashboard.Appointments> getAppointments() {
        return Appointments;
    }

    public void setAppointments(List<com.moguls.medic.model.dashboard.Appointments> appointments) {
        Appointments = appointments;
    }

    private List<Appointments> Appointments = new ArrayList<>();

}
