package com.moguls.medic.webservices;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moguls.medic.etc.APIs;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Constants;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.GenericResponse;
import com.moguls.medic.model.Response;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.settings.HTTPAsyncTask;
import com.moguls.medic.webservices.settings.SingleLiveEvent;

import org.json.JSONObject;

public class PostCancelAppointmentViewModel extends BaseViewModel {


    private final Application apl;
    private HTTPAsyncTask genericHttpAsyncTask;
    SingleLiveEvent trigger = new SingleLiveEvent<Integer>();
    public GenericResponse genericResponse = null;

    public PostCancelAppointmentViewModel(@NonNull Application application) {
        super(application);
        apl = application;
    }
    public SingleLiveEvent<Integer> getTrigger()  {
        return trigger;
    }

    public void loadData(String ID,String Remarks) {
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
                            showUnknowResponseErrorMessage();
                        }
                    }

                }
            }
        });

        genericHttpAsyncTask.method = Constants.POST;
        genericHttpAsyncTask.setUrl(APIs.cancelappointment);
        Helper.applyHeader(apl,genericHttpAsyncTask);
        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setPostParams(BaseKeys.ID,ID);
        genericHttpAsyncTask.setPostParams(BaseKeys.Remarks,Remarks);
        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();

    }
    int NEXT_STEP = 1;

}
