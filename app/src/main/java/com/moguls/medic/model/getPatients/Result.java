package com.moguls.medic.model.getPatients;

public class Result {

    String IsMale;
    Relation relation;
    String PhotoUrl;
    String Name;

    public String getIsMale() {
        return IsMale;
    }

    public void setIsMale(String isMale) {
        IsMale = isMale;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;

}
