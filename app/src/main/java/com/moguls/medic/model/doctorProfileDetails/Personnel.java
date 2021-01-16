package com.moguls.medic.model.doctorProfileDetails;

import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Personnel implements Serializable {


    String PhotoUrl;
    String FirstName;
    String LastName;
    String MobileNo;
    String EmailID;
    boolean IsMale;
    String DateOfBirth;
    String PracticingFrom;
    String Location;
    String EmergencyContactNo;
    String Statement;
    String IsVerified;
    List<Specializations> Specializations = new ArrayList<>();

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public boolean getIsMale() {
        return IsMale;
    }

    public void setIsMale(boolean isMale) {
        IsMale = isMale;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getPracticingFrom() {
        return PracticingFrom;
    }

    public void setPracticingFrom(String practicingFrom) {
        PracticingFrom = practicingFrom;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEmergencyContactNo() {
        return EmergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        EmergencyContactNo = emergencyContactNo;
    }

    public String getStatement() {
        return Statement;
    }

    public void setStatement(String statement) {
        Statement = statement;
    }

    public String getIsVerified() {
        return IsVerified;
    }

    public void setIsVerified(String isVerified) {
        IsVerified = isVerified;
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

    List<Specializations> Qualifications = new ArrayList<>();

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Personnel.class);
    }
}
