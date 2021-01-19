package com.moguls.medic.webservices;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.moguls.medic.etc.APIs;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Constants;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.GenericResponse;
import com.moguls.medic.model.Response;
import com.moguls.medic.model.doctorProfileDetails.IDProof;
import com.moguls.medic.model.doctorProfileDetails.Medical;
import com.moguls.medic.model.doctorProfileDetails.Personnel;
import com.moguls.medic.webservices.settings.HTTPAsyncTask;
import com.moguls.medic.webservices.settings.SingleLiveEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class PostSaveDoctorViewModel extends BaseViewModel {


    private final Application apl;
    private HTTPAsyncTask genericHttpAsyncTask;
    SingleLiveEvent trigger = new SingleLiveEvent<Integer>();
    public GenericResponse genericResponse = null;

    public PostSaveDoctorViewModel(@NonNull Application application) {
        super(application);
        apl = application;
    }
    public SingleLiveEvent<Integer> getTrigger()  {
        return trigger;
    }

    public void loadData(Personnel personnel, Medical Medical, IDProof IDProof) {
        genericHttpAsyncTask = new Helper.GenericHttpAsyncTask(new Helper.GenericHttpAsyncTask.TaskListener() {


            @Override
            public void onPreExecute() {
                isLoading.postValue(true);
            }
            @Override
            public void onPostExecute(Response response) {
                {
                    isLoading.postValue(false);

                    if (!Helper.isNetworkAvailable(apl)) {
                        isNetworkAvailable.postValue(false);
                        return;
                    }

                    JSONObject json = checkResponse(response, apl);
                    if (response.statusCode == 401) {
                        isOauthExpired.postValue(true);
                        return;
                    }
                    if (json != null) {
                        try {
                            Gson gson = new GsonBuilder().create();
                            genericResponse = gson.fromJson(response.getContent().toString(), GenericResponse.class);
                            if (genericResponse.getStatus().equals(BaseKeys.STATUS_CODE)) {
                                trigger.postValue(NEXT_STEP);
                            } else{
                                errorMessage.postValue(createErrorMessageObject(response));
                            }
                        } catch ( Exception e) {
                            e.printStackTrace();
                            showUnknowResponseErrorMessage();
                        }
                    }

                }
            }
        });
        Gson gson = new GsonBuilder().create();

        String medicalcounciljson = gson.toJson(Medical.getCouncil());
        String medicalspl = gson.toJson(IDProof.getSpecializations());
        String medicalQualifications = gson.toJson(IDProof.getQualifications());

        genericHttpAsyncTask.method = Constants.POST;
        genericHttpAsyncTask.setUrl(APIs.savedoctor);
        Helper.applyHeader(apl,genericHttpAsyncTask);
        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setFileParams(BaseKeys.PersonnelPhotoData,personnel.getPhotoUrl(),"multipart/form-data; boundar");
        genericHttpAsyncTask.setFileParams(BaseKeys.IDProofRegistrationData,IDProof.getRegistration().getDocumentUrl(),"multipart/form-data; boundar");
        genericHttpAsyncTask.setFileParams(BaseKeys.IDProofPhotoIdentityData,IDProof.getPhotoIdentity().getDocumentUrl(),"multipart/form-data; boundar");
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelFirstName,personnel.getFirstName());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelLastName,personnel.getLastName());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelMobileNo,personnel.getMobileNo());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelEmailID,personnel.getEmailID());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelIsMale,String.valueOf(personnel.getIsMale()));
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelDateOfBirth,String.valueOf(personnel.getDateOfBirth()));
        genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalPracticingFrom,personnel.getPracticingFrom());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelLocation,personnel.getLocation());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PersonnelEmergencyContactNo,personnel.getEmergencyContactNo());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalStatement,personnel.getStatement());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalNo,Medical.getNo());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalYear,Medical.getYear());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.IDProofRegistrationID,IDProof.getRegistration().getID());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.IDProofPhotoIdentityID,IDProof.getPhotoIdentity().getID());
        try {
            genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalCouncil,new JSONObject(medicalcounciljson).toString());
            genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalSpecializations,new JSONObject(medicalspl).toString());
            genericHttpAsyncTask.settxtFileParams(BaseKeys.MedicalQualifications,new JSONObject(medicalQualifications).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();

    }
    int NEXT_STEP = 1;

}
