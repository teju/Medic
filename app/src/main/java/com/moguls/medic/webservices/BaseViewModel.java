package com.moguls.medic.webservices;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.moguls.medic.R;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Constants;
import com.moguls.medic.webservices.settings.SingleLiveEvent;
import com.moguls.medic.model.Response;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
    public SingleLiveEvent isLoading = new SingleLiveEvent<Boolean>();
    public SingleLiveEvent isOauthExpired = new SingleLiveEvent<Boolean>();
    public SingleLiveEvent isNetworkAvailable = new SingleLiveEvent<Boolean>();
    public SingleLiveEvent isMaintenance = new SingleLiveEvent<String>();
    public SingleLiveEvent errorMessage = new SingleLiveEvent<ErrorMessageModel>();



    public class ErrorMessageModel {
        public Boolean isShouldDisplayDialog = false;
        public String title = "";
        public String message = "";
    }

    public JSONObject checkResponse(
            Response response,
            Context context) {

        ErrorMessageModel errorMessageModel = new ErrorMessageModel();

        if (response != null) {
            JSONObject json = response.content;

            if (response.statusCode == Constants.STATUS_NOT_FOUND) {
                return json;
            }

            if (response.statusCode == Constants.STATUS_BAD_REQUEST) {
                try {
                    if(json != null){
                        return json;
                    }
                } catch (Exception e) {

                    showUnknowResponseErrorMessage(String.valueOf(response.statusCode));
                }
                return null;
            }

            if (response.statusCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                isNetworkAvailable.postValue(false);
                return null;
            }

            if (response.statusCode == Constants.STATUS_SUCCESS) {
                return json;
            } else if (response.statusCode == Constants.STATUS_NO_CONNECTION) {
                errorMessageModel.message = context.getString(R.string.conn_fail);
                errorMessage.postValue(errorMessageModel);
            } else {

                try {
                    if (response.statusCode == 403) {
                        isOauthExpired.postValue(true);
                        return null;
                    }else{
                        try {
                            String statusCode = json.getString(BaseKeys.STATUS_CODE);
                            showUnknowResponseErrorMessage(String.valueOf(statusCode));
                        } catch ( Exception e) {
                            showUnknowResponseErrorMessage(String.valueOf(response.statusCode));
                        }
                    }
                } catch ( Exception e) {
                    showUnknowResponseErrorMessage();
                }
            }
        } else {

            showUnknowResponseErrorMessage();
        }

        return null;
    }




    protected ErrorMessageModel createErrorMessageObject( Boolean displayDialog, String title,  String message) {
        ErrorMessageModel errorMessageModel = new ErrorMessageModel();
        errorMessageModel.isShouldDisplayDialog = displayDialog;
        errorMessageModel.title = title;
        errorMessageModel.message = message;
        return errorMessageModel;
    }
    public void showUnknowResponseErrorMessage() {
         errorMessage.postValue(createErrorMessageObject(
          false,
            getApplication().getString(R.string.unknown_response),
            getApplication().getString(R.string.network_error)
        ));
    }

    public void  showUnknowResponseErrorMessage(String errorStatusCode) {
        errorMessage.postValue(createErrorMessageObject(
            false,
            "",
            String.format("%s (%s)", getApplication().getString(R.string.unknown_response), errorStatusCode)
        ));
    }
    protected ErrorMessageModel createErrorMessageObject( Response response)  {
        try {
            ErrorMessageModel errorMessageModel = new ErrorMessageModel();
            errorMessageModel.isShouldDisplayDialog = true;
            errorMessageModel.title = "";
            errorMessageModel.message = response.content.getString(BaseKeys.MESSAGE);
            return errorMessageModel;
        } catch (Exception e) {
            ErrorMessageModel errorMessageModel = new ErrorMessageModel();
            errorMessageModel.isShouldDisplayDialog = false;
            errorMessageModel.title = "";
            errorMessageModel.message = "";
            return errorMessageModel;
        }
    }

    SingleLiveEvent<Boolean> isOauthExpiredSeamlessLogin = null;


}
