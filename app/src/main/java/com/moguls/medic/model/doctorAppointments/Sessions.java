package com.moguls.medic.model.doctorAppointments;

import java.util.ArrayList;
import java.util.List;

public class Sessions {
    private String HospitalID;
    private String HospitalName;
    private String SessionName;
    private String Fee;


    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getSessionName() {
        return SessionName;
    }

    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }

    public List<DoctorSlots> getSlots() {
        return Slots;
    }

    public void setSlots(List<DoctorSlots> slots) {
        this.Slots = slots;
    }

    private List<DoctorSlots> Slots = new ArrayList<>();

    public List<com.moguls.medic.model.doctorAppointments.Slots> getSlotsArr() {
        return SlotsArr;
    }

    public void setSlotsArr(List<com.moguls.medic.model.doctorAppointments.Slots> slotsArr) {
        SlotsArr = slotsArr;
    }

    private List<com.moguls.medic.model.doctorAppointments.Slots> SlotsArr = new ArrayList<>();


}
