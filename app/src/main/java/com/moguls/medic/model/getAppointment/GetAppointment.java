package com.moguls.medic.model.getAppointment;

public class GetAppointment {
    private String Status;

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


    private String Message;

    public com.moguls.medic.model.getAppointment.Result getResult() {
        return Result;
    }

    public void setResult(com.moguls.medic.model.getAppointment.Result result) {
        Result = result;
    }

    private Result Result ;

}
