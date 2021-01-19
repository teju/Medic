package com.moguls.medic.model.doctorProfileDetails;

public class Registration {

    public String getDocumentUrl() {
        return DocumentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        DocumentUrl = documentUrl;
    }

    String DocumentUrl;

    public String getVerifierComment() {
        return VerifierComment;
    }

    public void setVerifierComment(String verifierComment) {
        VerifierComment = verifierComment;
    }

    String VerifierComment;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;
}
