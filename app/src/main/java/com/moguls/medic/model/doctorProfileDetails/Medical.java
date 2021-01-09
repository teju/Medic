package com.moguls.medic.model.doctorProfileDetails;

public class Medical {

    String No;
    String Year;

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

    public Specializations getCouncil() {
        return Council;
    }

    public void setCouncil(Specializations council) {
        Council = council;
    }

    Specializations Council;
}
