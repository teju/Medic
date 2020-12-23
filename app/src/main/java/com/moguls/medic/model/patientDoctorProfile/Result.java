package com.moguls.medic.model.patientDoctorProfile;

import java.util.ArrayList;
import java.util.List;

public class Result {


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getSpecializations() {
        return Specializations;
    }

    public void setSpecializations(String specializations) {
        Specializations = specializations;
    }

    public String getQualifications() {
        return Qualifications;
    }

    public void setQualifications(String qualifications) {
        Qualifications = qualifications;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getStatement() {
        return Statement;
    }

    public void setStatement(String statement) {
        Statement = statement;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<com.moguls.medic.model.patientDoctorProfile.Hospitals> getHospitals() {
        return Hospitals;
    }

    public void setHospitals(List<com.moguls.medic.model.patientDoctorProfile.Hospitals> hospitals) {
        Hospitals = hospitals;
    }

    String ID;
    String PhotoUrl;
    String MobileNo;
    String Specializations;
    String Qualifications;
    String Experience;
    String Statement;
    String Name;
    List<Hospitals> Hospitals = new ArrayList<>();


}
