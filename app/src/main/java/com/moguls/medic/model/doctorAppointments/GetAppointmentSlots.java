package com.moguls.medic.model.doctorAppointments;


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

    public com.moguls.medic.model.doctorAppointments.Result getResult() {
        return Result;
    }

    public void setResult(com.moguls.medic.model.doctorAppointments.Result result) {
        this.Result = result;
    }

    private com.moguls.medic.model.doctorAppointments.Result Result ;

}
