package com.moguls.medic.model;

import com.moguls.medic.model.doctorProfileDetails.IDProof;
import com.moguls.medic.model.doctorProfileDetails.Medical;
import com.moguls.medic.model.doctorProfileDetails.Personnel;
import com.moguls.medic.model.consultations.Result;
import com.moguls.medic.model.hospital.Hospital;

public class BaseParams {
    Personnel personnel;
    Medical medical;
    IDProof idProof;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    Result result;
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

}
