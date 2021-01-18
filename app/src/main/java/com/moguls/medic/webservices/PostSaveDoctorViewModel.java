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

        String personaljson = gson.toJson(personnel);
        String Medicaljson = gson.toJson(Medical);
        String IDProofjson = gson.toJson(IDProof);

        genericHttpAsyncTask.method = Constants.POST;
        genericHttpAsyncTask.setUrl(APIs.savedoctor);
        Helper.applyHeader(apl,genericHttpAsyncTask);
        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setFileParams(BaseKeys.PhotoData,personnel.getPhotoUrl(),"multipart/form-data; boundar");
        genericHttpAsyncTask.settxtFileParams(BaseKeys.FirstName,personnel.getFirstName());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.LastName,personnel.getLastName());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.MobileNo,personnel.getMobileNo());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.EmailID,personnel.getEmailID());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.IsMale,String.valueOf(personnel.getIsMale()));
        genericHttpAsyncTask.settxtFileParams(BaseKeys.DateOfBirth,String.valueOf(personnel.getDateOfBirth()));
        genericHttpAsyncTask.settxtFileParams(BaseKeys.PracticingFrom,personnel.getPracticingFrom());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.Location,personnel.getLocation());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.EmergencyContactNo,personnel.getEmergencyContactNo());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.Statement,personnel.getStatement());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.IsVerified,personnel.getIsVerified());
//        try {
//            genericHttpAsyncTask.setFileParams(BaseKeys.Medical,new JSONObject(Medicaljson).toString(),"multipart/form-data; boundar");
//            //genericHttpAsyncTask.settxtFileParams(BaseKeys.Personnel,new JSONObject(personaljson).toString());
//            genericHttpAsyncTask.setFileParams(BaseKeys.IDProof,new JSONObject(IDProofjson).toString(),"multipart/form-data; boundar");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();

    }
    int NEXT_STEP = 1;

}