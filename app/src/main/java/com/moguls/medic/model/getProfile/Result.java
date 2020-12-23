package com.moguls.medic.model.getProfile;

import com.moguls.medic.model.getPatients.Relation;

import java.util.ArrayList;
import java.util.List;

public class Result {

    String IsMale;
    Relation relation;
    String PhotoUrl;
    String Name;
    String DateOfBirth;
    String MobileNo;
    String BloodGroup;
    String Weight;
    String Height;
    String EmailID;
    String EmergencyContactNo;
    String RelationID;
    String IsMarried;
    String Age;

    public String getCompletedPercentage() {
        return CompletedPercentage;
    }

    public void setCompletedPercentage(String completedPercentage) {
        CompletedPercentage = completedPercentage;
    }

    String CompletedPercentage;

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public List<Member> getMembers() {
        return Members;
    }

    public void setMembers(List<Member> members) {
        Members = members;
    }

    List<Member> Members = new ArrayList();

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getEmergencyContactNo() {
        return EmergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        EmergencyContactNo = emergencyContactNo;
    }

    public String getRelationID() {
        return RelationID;
    }

    public void setRelationID(String relationID) {
        RelationID = relationID;
    }

    public String getIsMarried() {
        return IsMarried;
    }

    public void setIsMarried(String isMarried) {
        IsMarried = isMarried;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    String Location;

    public String getIsMale() {
        return IsMale;
    }

    public void setIsMale(String isMale) {
        IsMale = isMale;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;
    String FirstName;

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

    String LastName;

}
