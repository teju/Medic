package com.moguls.medic.model.appointmentSlots;


public class GetAppointmentSlots {
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

    public Result getResult() {
        return Result;
    }

    public void setResult(Result result) {
        this.Result = result;
    }

    private Result Result ;

}
