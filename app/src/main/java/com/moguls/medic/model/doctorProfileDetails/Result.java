package com.moguls.medic.model.doctorProfileDetails;

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

}
