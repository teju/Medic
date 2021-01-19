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

public class PostUpdateProfileViewModel extends BaseViewModel {


    private final Application apl;
    private HTTPAsyncTask genericHttpAsyncTask;
    SingleLiveEvent trigger = new SingleLiveEvent<Integer>();


    public GenericResponse genericResponse = null;

    public PostUpdateProfileViewModel(@NonNull Application application) {
        super(application);
        apl = application;
    }
    public SingleLiveEvent<Integer> getTrigger()  {
        return trigger;
    }

    public void loadData(String ID,
                         String Name,
                         String MobileNo,
                         String DateOfBirth,
                         String BloodGroup,
                         String Weight,
                         String Height,
                         String EmailID,
                         String EmergencyContactNo,
                         String RelationID,
                         String IsMarried,
                         String Location,
                         String PhotoData,
                         Boolean IsMale,String LastName) {
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
        genericHttpAsyncTask.setUrl(APIs.getUpdatePatientProfile);
        Helper.applyHeader(apl,genericHttpAsyncTask);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.ID,ID);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.FirstName,Name);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.LastName,LastName);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.MOBILENO,MobileNo);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.DATEOFBIRTH,DateOfBirth);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.IsMale,IsMale.toString());
        genericHttpAsyncTask.settxtFileParams(BaseKeys.BloodGroup,BloodGroup);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.Weight,Weight);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.Height,Height);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.EmailID,EmailID);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.EmergencyContactNo,EmergencyContactNo);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.RelationID,RelationID);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.IsMarried,IsMarried);
        genericHttpAsyncTask.settxtFileParams(BaseKeys.Location,Location);
        genericHttpAsyncTask.setFileParams(BaseKeys.PhotoData,PhotoData,"multipart/form-data; boundar");
        genericHttpAsyncTask.context = apl.getApplicationContext();
        genericHttpAsyncTask.setCache(false);
        genericHttpAsyncTask.execute();

    }
    int NEXT_STEP = 1;

}
