package com.moguls.medic.model.consultations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConsultationPlans {
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


    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("Message")
    @Expose
    private String Message;


    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    @SerializedName("Result")
    @Expose
    List<Result> result = new ArrayList<>();

}
