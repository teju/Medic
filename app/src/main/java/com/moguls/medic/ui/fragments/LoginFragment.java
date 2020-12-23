package com.moguls.medic.ui.fragments;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.callback.OtpListener;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.dialog.OtpDialogFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.PostSendOtpViewModel;
import com.moguls.medic.webservices.PostVerifyOtpViewModel;

import java.util.ArrayList;
import java.util.List;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private Button btnlogin;
    private TextView register;
    private EditText mobile_number;
    private PostVerifyOtpViewModel postVerifyOtpViewModel;
    private PostSendOtpViewModel postSendOtpViewModel;
    private LoadingCompound ld;
    private boolean isResend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSendOtpAPIObserver();
        setVerifyOtpAPIObserver();

        ld = (LoadingCompound)v.findViewById(R.id.ld);
        btnlogin = (Button)v.findViewById(R.id.btnlogin);
        mobile_number = (EditText)v.findViewById(R.id.mobile_number);
        register = (TextView)v.findViewById(R.id.register);
        btnlogin.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register:
                home().setFragment(new RegisterFragment());
                break;
            case R.id.btnlogin:
                isResend = false;
                if(Helper.isValidMobile(mobile_number.getText().toString())) {
                    postSendOtpViewModel.loadData(mobile_number.getText().toString());
                } else {
                    mobile_number.setError("Invalid mobile no.");
                }
                break;
        }
    }
    public void setSendOtpAPIObserver() {
        postSendOtpViewModel = ViewModelProviders.of(this).get(PostSendOtpViewModel.class);
        postSendOtpViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
            @Override
            public void onChanged(BaseViewModel.ErrorMessageModel errorMessageModel) {
                showNotifyDialog(errorMessageModel.title,
                        errorMessageModel.message, "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {

                            }
                        }));
            }
        });
        postSendOtpViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postSendOtpViewModel.isNetworkAvailable.observe(this,obsNoInternet);
        postSendOtpViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(!isResend) {
                    showOtpVerifyDialog(new OtpListener() {
                        @Override
                        public void onButtonClicked(String otp, int which) {
                            if (which == NotifyDialogFragment.BUTTON_POSITIVE) {
                                postVerifyOtpViewModel.loadData(mobile_number.getText().toString(), otp);
                            } else if (which == OtpDialogFragment.BUTTON_RESENDOTP) {
                                isResend = true;
                                if (Helper.isValidMobile(mobile_number.getText().toString())) {
                                    postSendOtpViewModel.loadData(mobile_number.getText().toString());
                                } else {
                                    mobile_number.setError("Invalid mobile no.");
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    public void setVerifyOtpAPIObserver() {
        postVerifyOtpViewModel = ViewModelProviders.of(this).get(PostVerifyOtpViewModel.class);
        postVerifyOtpViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
            @Override
            public void onChanged(BaseViewModel.ErrorMessageModel errorMessageModel) {
                showNotifyDialog(errorMessageModel.title,
                        errorMessageModel.message, "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {

                            }
                        }));
            }
        });
        postVerifyOtpViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postVerifyOtpViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postVerifyOtpViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                SharedPreference.setBoolean(getActivity(),SharedPreference.isLoggedIn,true);
                //SharedPreference.setBoolean(getActivity(),SharedPreference.isDOCTOR,Boolean.valueOf(postVerifyOtpViewModel.verifyOtp.getResult().getIsDoctor()));
                SharedPreference.setBoolean(getActivity(),SharedPreference.isDOCTOR,true);
                SharedPreference.setString(getActivity(),SharedPreference.AccountID,postVerifyOtpViewModel.verifyOtp.getResult().getID());
                SharedPreference.setString(getActivity(), BaseKeys.Authorization,postVerifyOtpViewModel.verifyOtp.getResult().getToken());
                home().setFragment(new MainTabFragment());
            }
        });
    }

}