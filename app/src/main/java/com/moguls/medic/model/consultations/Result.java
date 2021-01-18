package com.moguls.medic.model.consultations;

import java.util.ArrayList;
import java.util.List;

public class Result {

    String Hospital ="";
    String Name = "";
    String Address ="";
    String HospitalID="";
    String Timeslot="";
    String Fee="";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    String PhotoUrl;
    Double Latitude = 0.0;

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    Double Longitude = 0.0;

    public String getConsultationFee() {
        return ConsultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        ConsultationFee = consultationFee;
    }

    String ConsultationFee = "";
    String AdvanceBookingDays = "";
    List<Sessions> Sessions = new ArrayList<>();
    ConsultationType ConsultationType ;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


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



    public List<com.moguls.medic.model.consultations.Sessions> getSessions() {
        return Sessions;
    }

    public void setSessions(List<com.moguls.medic.model.consultations.Sessions> sessions) {
        Sessions = sessions;
    }



}
