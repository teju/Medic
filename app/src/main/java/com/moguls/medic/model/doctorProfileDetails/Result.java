package com.moguls.medic.model.doctorProfileDetails;

import java.util.ArrayList;
import java.util.List;

public class Result {

    public com.moguls.medic.model.doctorProfileDetails.Personnel getPersonnel() {
        return Personnel;
    }

    public void setPersonnel(com.moguls.medic.model.doctorProfileDetails.Personnel personnel) {
        Personnel = personnel;
    }

    Personnel Personnel;
    Medical Medical;

    public com.moguls.medic.model.doctorProfileDetails.Medical getMedical() {
        return Medical;
    }

    public void setMedical(com.moguls.medic.model.doctorProfileDetails.Medical medical) {
        Medical = medical;
    }

    public com.moguls.medic.model.doctorProfileDetails.IDProof getIDProof() {
        return IDProof;
    }

    public void setIDProof(com.moguls.medic.model.doctorProfileDetails.IDProof IDProof) {
        this.IDProof = IDProof;
    }

    IDProof IDProof;

    List<Specializations> Specializations = new ArrayList();
    List<Specializations> Qualifications = new ArrayList();
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
    List<Specializations> RegistartionCouncils = new ArrayList();
    public List<com.moguls.medic.model.doctorProfileDetails.Specializations> getRegistartionCouncils() {
        return RegistartionCouncils;
    }

    public void setRegistartionCouncils(List<com.moguls.medic.model.doctorProfileDetails.Specializations> registartionCouncils) {
        RegistartionCouncils = registartionCouncils;
    }
}
