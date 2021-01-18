package com.moguls.medic.model;

import com.moguls.medic.model.doctors.Result;
import com.moguls.medic.model.getAppointment.Doctor;

public class BookingData {
    String refID;
    String time;
    String ID;
    String date;
    String photo;
    String HospitalID;
    private String fees;
    private String doctorName;

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    private String PhotoUrl;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecializaion() {
        return specializaion;
    }

    public void setSpecializaion(String specializaion) {
        this.specializaion = specializaion;
    }

    private String specializaion;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }


    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

}
