package com.moguls.medic.model.getAppointment;

import java.util.ArrayList;
import java.util.List;

public class GetAppointmentDashBoard {
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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    private String Message;
    private List<Result> result = new ArrayList<>();

}
