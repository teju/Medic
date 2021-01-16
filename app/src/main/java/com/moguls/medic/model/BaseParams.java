package com.moguls.medic.model;

import com.moguls.medic.model.doctorProfileDetails.IDProof;
import com.moguls.medic.model.doctorProfileDetails.Medical;
import com.moguls.medic.model.doctorProfileDetails.Personnel;

public class BaseParams {
    Personnel personnel;
    Medical medical;

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public Medical getMedical() {
        return medical;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    public IDProof getIdProof() {
        return idProof;
    }

    public void setIdProof(IDProof idProof) {
        this.idProof = idProof;
    }

    IDProof idProof;
}
