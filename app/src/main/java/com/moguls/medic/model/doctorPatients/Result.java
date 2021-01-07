package com.moguls.medic.model.doctorPatients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.ui.adapters.DoctorPatientListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result implements Comparable<Result>{

    @SerializedName("PhotoUrl")
    @Expose
    private String PhotoUrl;
    @SerializedName("MobileNo")
    @Expose
    private String MobileNo;
    @SerializedName("AppointmentID")
    @Expose
    private String AppointmentID;
    @SerializedName("AppointmentOn")
    @Expose
    private String AppointmentOn;
    @SerializedName("AppointmentRemark")
    @Expose
    private String AppointmentRemark;
    @SerializedName("Age")
    @Expose
    private String Age;
    @SerializedName("BloodGroup")
    @Expose
    private String BloodGroup;
    @SerializedName("Height")
    @Expose
    private String Height;
    @SerializedName("Weight")
    @Expose
    private String Weight;
    @SerializedName("Name")
    @Expose
    private String Name;

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

    public String getAppointmentID() {
        return AppointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        AppointmentID = appointmentID;
    }

    public String getAppointmentOn() {
        return AppointmentOn;
    }

    public void setAppointmentOn(String appointmentOn) {
        AppointmentOn = appointmentOn;
    }

    public String getAppointmentRemark() {
        return AppointmentRemark;
    }

    public void setAppointmentRemark(String appointmentRemark) {
        AppointmentRemark = appointmentRemark;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
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

    @SerializedName("ID")
    @Expose
    private String ID;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type = DoctorPatientListAdapter.VIEW_TYPE_ITEM;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    private String header;

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public int compareTo(Result o) {
        return 1;
    }
}
