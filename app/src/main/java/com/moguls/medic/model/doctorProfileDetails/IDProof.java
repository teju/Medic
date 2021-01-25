package com.moguls.medic.model.doctorProfileDetails;

import java.util.ArrayList;
import java.util.List;

public class IDProof {

    String No;
    String Year;
    Registration Registration;
    Registration PhotoIdentity;
    List<Registration> Degrees = new ArrayList();


    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public com.moguls.medic.model.doctorProfileDetails.Registration getRegistration() {
        return Registration;
    }

    public void setRegistration(com.moguls.medic.model.doctorProfileDetails.Registration registration) {
        Registration = registration;
    }

    public com.moguls.medic.model.doctorProfileDetails.Registration getPhotoIdentity() {
        return PhotoIdentity;
    }

    public void setPhotoIdentity(com.moguls.medic.model.doctorProfileDetails.Registration photoIdentity) {
        PhotoIdentity = photoIdentity;
    }

    public List<com.moguls.medic.model.doctorProfileDetails.Registration> getDegrees() {
        return Degrees;
    }

    public void setDegrees(List<com.moguls.medic.model.doctorProfileDetails.Registration> degrees) {
        Degrees = degrees;
    }




}
