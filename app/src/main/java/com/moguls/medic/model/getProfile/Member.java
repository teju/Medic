package com.moguls.medic.model.getProfile;

import com.moguls.medic.model.getPatients.Relation;

import java.util.ArrayList;
import java.util.List;

public class Member {


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;

}
