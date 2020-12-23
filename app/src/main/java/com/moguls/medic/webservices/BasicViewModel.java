package com.moguls.medic.webservices;

import android.app.Application;

import androidx.annotation.NonNull;

import com.moguls.medic.etc.Constants;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.Response;
import com.moguls.medic.webservices.settings.HTTPAsyncTask;

import org.json.JSONObject;

public class BasicViewModel extends BaseViewModel {


    private final Application apl;
    private HTTPAsyncTask genericHttpAsyncTask;

    public BasicViewModel(@NonNull Application application) {
        super(application);
        apl = application;
    }

    public void loadData() {
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

//                    if (json != null) {
//                        try {
//                            gson = GsonBuilder().create()
//                            obj = gson.fromJson(response!!.content.toString(), ProfileWall::class.java)
//                            if (obj!!.status.equals(Keys.STATUS_CODE)) {
//                                trigger.postValue(NEXT_STEP)
//                            }else{
//                                errorMessage.value = createErrorMessageObject(response)
//
//                            }
//                        } catch ( Exception e) {
//                            showUnknowResponseErrorMessage()
//                        }
//                    }

                }
            }

        });

        genericHttpAsyncTask.method = Constants.POST;
        //genericHttpAsyncTask.setUrl(APIs.getUserActivities);

        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();

    }

}
