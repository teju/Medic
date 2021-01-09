package com.moguls.medic.model.hospitalSummary;

import java.util.ArrayList;
import java.util.List;

public class HospitalSummary {
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


    public com.moguls.medic.model.hospitalSummary.Result getResult() {
        return Result;
    }

    public void setResult(com.moguls.medic.model.hospitalSummary.Result result) {
        Result = result;
    }

    Result Result;


}
