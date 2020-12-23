package com.moguls.medic.model.getPatients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.moguls.medic.model.getProfile.Member;

import java.util.ArrayList;
import java.util.List;

public class GetPatients {
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

    public List<Member> getResult() {
        return result;
    }

    public void setResult(List<Member> result) {
        this.result = result;
    }

    @SerializedName("Result")
    @Expose
    private List<Member> result = new ArrayList<>();

}
