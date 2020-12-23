package com.moguls.medic.model.dashboard;

import com.moguls.medic.model.getAppointment.Doctor;
import com.moguls.medic.model.getAppointment.Patient;

public class Appointments {
    private String ID;
    private Doctor Doctor;
    private Patient Patient;
    private String AppointmentOn;
    private String Remarks;



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public com.moguls.medic.model.getAppointment.Doctor getDoctor() {
        return Doctor;
    }

    public void setDoctor(com.moguls.medic.model.getAppointment.Doctor doctor) {
        Doctor = doctor;
    }

    public com.moguls.medic.model.getAppointment.Patient getPatient() {
        return Patient;
    }

    public void setPatient(com.moguls.medic.model.getAppointment.Patient patient) {
        Patient = patient;
    }

    public String getAppointmentOn() {
        return AppointmentOn;
    }

    public void setAppointmentOn(String appointmentOn) {
        AppointmentOn = appointmentOn;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getIsCancelled() {
        return IsCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        IsCancelled = isCancelled;
    }

    private String IsCancelled;

}
