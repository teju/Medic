package com.moguls.medic.webservices;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.moguls.medic.etc.APIs;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Constants;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.Response;
import com.moguls.medic.model.consultations.ConsultationPlans;
import com.moguls.medic.webservices.settings.HTTPAsyncTask;
import com.moguls.medic.webservices.settings.SingleLiveEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class PostConsultationsAddViewModel extends BaseViewModel {

    private final Application apl;
    private HTTPAsyncTask genericHttpAsyncTask;
    SingleLiveEvent trigger = new SingleLiveEvent<Integer>();
    public ConsultationPlans getcConsultationPlans = null;

    public PostConsultationsAddViewModel(@NonNull Application application) {
        super(application);
        apl = application;
    }

    public SingleLiveEvent<Integer> getTrigger()  {
        return trigger;
    }

    public void loadData(String hospitalID,String timeslot,String Fee,String advanceBookingDays,String Name) {
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
                    if (response.statusCode == 401) {
                        isOauthExpired.postValue(true);
                        return;
                    }
                    JSONObject json = checkResponse(response, apl);
                    if (json != null) {
                        try {
                            Gson gson = new GsonBuilder().create();
                            getcConsultationPlans = gson.fromJson(response.getContent().toString(), ConsultationPlans.class);
                            if (getcConsultationPlans.getStatus().equals(BaseKeys.STATUS_CODE)) {
                                trigger.postValue(NEXT_STEP);
                            } else{
                                errorMessage.postValue(createErrorMessageObject(response));
                            }
                        } catch ( Exception e) {
                            showUnknowResponseErrorMessage();
                        }
                    }
                }
            }
        });

        genericHttpAsyncTask.method = Constants.POST;
        genericHttpAsyncTask.setUrl(APIs.saveconsultationplan);
        Helper.applyHeader(apl,genericHttpAsyncTask);
        genericHttpAsyncTask.setPostParams(BaseKeys.HospitalID,hospitalID);
        genericHttpAsyncTask.setPostParams(BaseKeys.Timeslot,timeslot);
        genericHttpAsyncTask.setPostParams(BaseKeys.Fee,Fee);
        genericHttpAsyncTask.setPostParams(BaseKeys.AdvanceBookingDays,advanceBookingDays);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Name",Name);
            jsonObject.put("ID",Name.substring(0,1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        genericHttpAsyncTask.setPostParams(BaseKeys.ConsultationType,jsonObject);

        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();
    }
    int NEXT_STEP = 1;

}
