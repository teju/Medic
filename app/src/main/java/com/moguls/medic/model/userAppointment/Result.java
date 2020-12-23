package com.moguls.medic.model.userAppointment;

import com.moguls.medic.model.getAppointment.Doctor;
import com.moguls.medic.model.getAppointment.Patient;

public class Result {
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    Doctor Doctor;
    Patient Patient;
    String AppointmentOn;
    String Remarks;
    String IsCancelled;
    String Status;


}
