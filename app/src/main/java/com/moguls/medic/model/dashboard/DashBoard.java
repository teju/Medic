package com.moguls.medic.model.dashboard;

public class DashBoard {

    private String Status;
    private String Message;

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

    public com.moguls.medic.model.dashboard.Result getResult() {
        return Result;
    }

    public void setResult(com.moguls.medic.model.dashboard.Result result) {
        Result = result;
    }

    private Result Result;


}
