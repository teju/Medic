package com.moguls.medic.model.getAppointment;

public class Result {
    private String AppointmentRef;
    private String ConsultationFee;
    private String Prescriptions;
    private String ID;
    private Doctor Doctor;
    private Patient Patient;
    private String AppointmentOn;
    private String Remarks;
    private String IsCancelled;

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    private String Distance;

    public String getAppointmentRef() {
        return AppointmentRef;
    }

    public void setAppointmentRef(String appointmentRef) {
        AppointmentRef = appointmentRef;
    }

    public String getConsultationFee() {
        return ConsultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        ConsultationFee = consultationFee;
    }

    public String getPrescriptions() {
        return Prescriptions;
    }

    public void setPrescriptions(String prescriptions) {
        Prescriptions = prescriptions;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Doctor getDoctor() {
        return Doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.Doctor = doctor;
    }

    public Patient getPatient() {
        return Patient;
    }

    public void setPatient(Patient patient) {
        this.Patient = patient;
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

    private String Status;


}
