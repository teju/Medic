package com.moguls.medic.model.hospital;

import com.moguls.medic.model.consultations.Result;

public class Hospital {
    String Status;
    String Message;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


    public com.moguls.medic.model.consultations.Result getResult() {
        return Result;
    }

    public void setResult(com.moguls.medic.model.consultations.Result result) {
        Result = result;
    }

    com.moguls.medic.model.consultations.Result Result;


}
