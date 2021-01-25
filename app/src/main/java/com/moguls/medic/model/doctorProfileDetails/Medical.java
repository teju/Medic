package com.moguls.medic.model.doctorProfileDetails;

import java.util.ArrayList;
import java.util.List;

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

    public String getPracticingFrom() {
        return PracticingFrom;
    }

    public void setPracticingFrom(String practicingFrom) {
        PracticingFrom = practicingFrom;
    }
    List<Specializations> Specializations = new ArrayList<>();
    List<Specializations> Qualifications = new ArrayList<>();
    public List<com.moguls.medic.model.doctorProfileDetails.Specializations> getQualifications() {
        return Qualifications;
    }

    public void setQualifications(List<com.moguls.medic.model.doctorProfileDetails.Specializations> qualifications) {
        Qualifications = qualifications;
    }
    String PracticingFrom;

    public String getStatement() {
        return Statement;
    }

    public void setStatement(String statement) {
        Statement = statement;
    }

    String Statement;
    public List<com.moguls.medic.model.doctorProfileDetails.Specializations> getSpecializations() {
        return Specializations;
    }

    public void setSpecializations(List<com.moguls.medic.model.doctorProfileDetails.Specializations> specializations) {
        Specializations = specializations;
    }
}
