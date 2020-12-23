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
import com.moguls.medic.model.userAppointment.UserAppointments;
import com.moguls.medic.webservices.settings.HTTPAsyncTask;
import com.moguls.medic.webservices.settings.SingleLiveEvent;

import org.json.JSONObject;

public class PostAppointmentListViewModel extends BaseViewModel {


    private final Application apl;
    private HTTPAsyncTask genericHttpAsyncTask;
    SingleLiveEvent trigger = new SingleLiveEvent<Integer>();
    public UserAppointments userAppointments = null;
    int page_number = 1;
    int pageSize = 100;
    public PostAppointmentListViewModel(@NonNull Application application) {
        super(application);
        apl = application;
    }
    public SingleLiveEvent<Integer> getTrigger()  {
        return trigger;
    }

    public void loadData(String isPast) {
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
                            userAppointments = gson.fromJson(response.getContent().toString(), UserAppointments.class);
                            if (userAppointments.getStatus().equals(BaseKeys.STATUS_CODE)) {
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

        genericHttpAsyncTask.method = Constants.GET;
        genericHttpAsyncTask.setUrl(APIs.getuserappointments+"/"+isPast+"/"+page_number+"/"+pageSize);
        Helper.applyHeader(apl,genericHttpAsyncTask);
        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();

    }
    int NEXT_STEP = 1;

}
