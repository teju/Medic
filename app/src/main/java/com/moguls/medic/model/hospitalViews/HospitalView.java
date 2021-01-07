package com.moguls.medic.model.hospitalViews;

import com.moguls.medic.model.myChat.File;

import java.util.ArrayList;
import java.util.List;

public class HospitalView {
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

    public List<com.moguls.medic.model.hospitalViews.Result> getResult() {
        return Result;
    }

    public void setResult(List<com.moguls.medic.model.hospitalViews.Result> result) {
        Result = result;
    }

    List<Result> Result = new ArrayList<>();


}
