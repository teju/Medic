package com.moguls.medic.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.moguls.medic.ui.fragments.tabs.HomeFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.PostSendOtpViewModel;
import com.moguls.medic.webservices.PostRegisterViewModel;
import com.moguls.medic.webservices.PostVerifyOtpViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private TextView login;
    private Button btnregister,dob;
    final Calendar myCalendar = Calendar.getInstance();
    private CheckBox terms_condition;
    private EditText mobile_number,full_name;
    private PostRegisterViewModel postRegisterViewModel;
    private PostVerifyOtpViewModel postVerifyOtpViewModel;
    private PostSendOtpViewModel postSendOtpViewModel;
    private LoadingCompound ld;
    boolean IsMale = true;
    private RadioButton female,male;
    private boolean isResend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_register, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRegisterAPIObserver();
        setSendOtpAPIObserver();
        setVerifyOtpAPIObserver();
        login = (TextView)v.findViewById(R.id.login);
        btnregister = (Button)v.findViewById(R.id.btnregister);
        dob = (Button)v.findViewById(R.id.dob);
        female = (RadioButton)v.findViewById(R.id.female);
        male = (RadioButton)v.findViewById(R.id.male);
        full_name = (EditText)v.findViewById(R.id.full_name);
        mobile_number = (EditText)v.findViewById(R.id.mobile_number);
        terms_condition = (CheckBox)v.findViewById(R.id.terms_condition);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        login.setOnClickListener(this);
        dob.setOnClickListener(this);
        btnregister.setOnClickListener(this);
        terms_condition.setOnClickListener(this);
        female.setOnClickListener(this);
        male.setOnClickListener(this);
    }

    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login :
                home().onBackPressed();
                break;
            case R.id.btnregister:
                isResend = false;
                if(validate()) {
                    postRegisterViewModel.loadData(full_name.getText().toString(),
                            mobile_number.getText().toString(),dob.getText().toString(),IsMale);
                }
                break;
            case R.id.dob:
                new DatePickerDialog(getActivity(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.terms_condition:
                break;
            case R.id.male :
                female.setChecked(false);
                IsMale = true;
                break;
            case R.id.female :
                IsMale = false;
                male.setChecked(false);
                break;
        }
    }

    public Boolean validate() {
        if(Helper.isEmpty(full_name.getText().toString())) {
            full_name.setError("Invalid name no.");
            return false;
        } else if(!Helper.isValidMobile(mobile_number.getText().toString().trim())) {
            mobile_number.setError("Invalid mobile no.");
            return false;
        } else if(Helper.isEmpty(dob.getText().toString())) {
            dob.setError("Invalid dob no.");
            return false;
        }  else if(!terms_condition.isChecked()) {
            showNotifyDialog("Please Accept Terms & Conditions to proceed",
                    "", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        } else {
            return true;
        }
    }

    public void setRegisterAPIObserver() {
        postRegisterViewModel = ViewModelProviders.of(this).get(PostRegisterViewModel.class);
        postRegisterViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postRegisterViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postRegisterViewModel.isNetworkAvailable.observe(this,obsNoInternet);
        postRegisterViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                postSendOtpViewModel.loadData(mobile_number.getText().toString());
            }
        });
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
                SharedPreference.setString(getActivity(),SharedPreference.AccountID,postVerifyOtpViewModel.verifyOtp.getResult().getID());
                SharedPreference.setString(getActivity(), BaseKeys.Authorization,postVerifyOtpViewModel.verifyOtp.getResult().getToken());
                home().setFragment(new MainTabFragment());
            }
        });
    }
}