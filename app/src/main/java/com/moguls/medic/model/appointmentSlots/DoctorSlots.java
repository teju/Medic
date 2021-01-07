package com.moguls.medic.model.appointmentSlots;

import com.moguls.medic.model.doctorPatients.Result;
import com.moguls.medic.model.getAppointment.Patient;

public class DoctorSlots {

    String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public com.moguls.medic.model.getAppointment.Patient getPatient() {
        return Patient;
    }

    public void setPatient(com.moguls.medic.model.getAppointment.Patient patient) {
        Patient = patient;
    }

    Patient Patient;

    public String getAppointmentOn() {
        return AppointmentOn;
    }

    public void setAppointmentOn(String appointmentOn) {
        AppointmentOn = appointmentOn;
    }

    private String AppointmentOn;

}
