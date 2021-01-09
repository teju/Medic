package com.moguls.medic.model.consultations;

public class Result {

    String Hospital;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;
    String Address;
    String HospitalID;
    String Timeslot;
    String Fee;
    String AdvanceBookingDays;

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getTimeslot() {
        return Timeslot;
    }

    public void setTimeslot(String timeslot) {
        Timeslot = timeslot;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }

    public String getAdvanceBookingDays() {
        return AdvanceBookingDays;
    }

    public void setAdvanceBookingDays(String advanceBookingDays) {
        AdvanceBookingDays = advanceBookingDays;
    }

    public com.moguls.medic.model.consultations.ConsultationType getConsultationType() {
        return ConsultationType;
    }

    public void setConsultationType(com.moguls.medic.model.consultations.ConsultationType consultationType) {
        ConsultationType = consultationType;
    }

    ConsultationType ConsultationType;


}
