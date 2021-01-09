package com.moguls.medic.model.doctorProfileDetails;

import java.util.ArrayList;
import java.util.List;

public class IDProof {

    String No;
    String Year;
    Registration Registration;
    Registration PhotoIdentity;
    List<Registration> Degrees = new ArrayList();
    List<Specializations> Specializations = new ArrayList();
    List<Specializations> Qualifications = new ArrayList();

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

    public List<com.moguls.medic.model.doctorProfileDetails.Specializations> getSpecializations() {
        return Specializations;
    }

    public void setSpecializations(List<com.moguls.medic.model.doctorProfileDetails.Specializations> specializations) {
        Specializations = specializations;
    }

    public List<com.moguls.medic.model.doctorProfileDetails.Specializations> getQualifications() {
        return Qualifications;
    }

    public void setQualifications(List<com.moguls.medic.model.doctorProfileDetails.Specializations> qualifications) {
        Qualifications = qualifications;
    }

    public List<com.moguls.medic.model.doctorProfileDetails.Specializations> getRegistartionCouncils() {
        return RegistartionCouncils;
    }

    public void setRegistartionCouncils(List<com.moguls.medic.model.doctorProfileDetails.Specializations> registartionCouncils) {
        RegistartionCouncils = registartionCouncils;
    }

    List<Specializations> RegistartionCouncils = new ArrayList();
}
