package com.moguls.medic.ui.fragments.appointment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.model.getAppointment.Result;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetAppointmentViewModel;
import com.moguls.medic.webservices.PostCancelAppointmentViewModel;

import java.util.Date;

import static com.moguls.medic.etc.Helper.loadImage;


public class PatientAppointmentDetailFragment extends BaseFragment implements View.OnClickListener {

    private Button btnlogin;
    private TextView register;
    private LoadingCompound ld;
    private GetAppointmentViewModel getAppointment;
    private PostCancelAppointmentViewModel postCancelAppointmentViewModel;
    private String id = "";
    private TextView doctor_name,specialization,date,days_remaining,hospital_name,address,
            patient_name,appointmentid,fee,distance;
    private LinearLayout llDist;
    private Button cancel;
    private ImageView logo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetAppointmentDetailAPIObserver();
        setCancelApptAPIObserver();

        TextView header_title = (TextView) v.findViewById(R.id.header_title);
        llDist = (LinearLayout) v.findViewById(R.id.llDist);
        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        specialization = (TextView) v.findViewById(R.id.specialization);
        date = (TextView) v.findViewById(R.id.date);
        days_remaining = (TextView) v.findViewById(R.id.days_remaining);
        hospital_name = (TextView) v.findViewById(R.id.hospital_name);
        address = (TextView) v.findViewById(R.id.address);
        patient_name = (TextView) v.findViewById(R.id.patient_name);
        distance = (TextView) v.findViewById(R.id.distance);
        fee = (TextView) v.findViewById(R.id.fee);
        appointmentid = (TextView) v.findViewById(R.id.appointmentid);
        header_title.setText("Appointment Details");
        Button reschedule = (Button) v.findViewById(R.id.reschedule);
        ImageView chat = (ImageView) v.findViewById(R.id.chat);
        ImageView call = (ImageView) v.findViewById(R.id.call);
        logo = (ImageView) v.findViewById(R.id.logo);
        cancel = (Button) v.findViewById(R.id.cancel);
        ld = (LoadingCompound)v.findViewById(R.id.ld);

        reschedule.setOnClickListener(this);
        cancel.setOnClickListener(this);
        chat.setOnClickListener(this);
        llDist.setOnClickListener(this);
        call.setOnClickListener(this);
        setBackButtonToolbarStyleOne(v);
        if(!id.isEmpty()) {
            getAppointment.loadData(id);
        }
    }

    public void updateData() {
        Result result = getAppointment.getAppointment.getResult();
        if(result != null) {
            if(result.getDoctor() != null) {
                doctor_name.setText(result.getDoctor().getName());
                specialization.setText(result.getDoctor().getSpecialization());
                hospital_name.setText(result.getDoctor().getHospital());
                address.setText(result.getDoctor().getHospitalAddress());
            }
            if(result.getPatient() != null) {
                patient_name.setText(result.getPatient().getName());
            }
            if(result.getIsCancelled().equalsIgnoreCase("true")) {
                cancel.setVisibility(View.GONE);
            } else {
                cancel.setVisibility(View.VISIBLE);
            }
            appointmentid.setText(result.getAppointmentRef());
            distance.setText("Direction -"+result.getDoctor().getDistance()+"km");
            fee.setText("Rs " + result.getConsultationFee() + " per/session");
            date.setText(result.getAppointmentOn());
            long diffdays = Helper.findDifferenceBetweenDates(new Date(), Helper.convertStringToDate("dd MMM yyyy", result.getAppointmentOn()));
            if (diffdays != 0) {
                days_remaining.setText("in " + diffdays + " daya");
            }
            loadImage(getActivity(),result.getDoctor().getPhotoUrl()
                    ,R.drawable.doctor_profile_pic_default,logo);

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.reschedule :
                PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                if(getAppointment.getAppointment != null) {
                    patientBookAppointmentFragment.setDoctor_id(getAppointment.getAppointment.getResult().getDoctor().getID());
                    patientBookAppointmentFragment.appointmentID = getAppointment.getAppointment.getResult().getID();

                }
                home().setFragment(patientBookAppointmentFragment);
                break;
            case R.id.chat :
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setToUserID(getAppointment.getAppointment.getResult().getDoctor().getID());
                chatFragment.setName(getAppointment.getAppointment.getResult().getDoctor().getName());
                home().setFragment(chatFragment);
                break;
            case R.id.call :
                Uri number = null;
                Intent call;
                if(getAppointment.getAppointment.getResult().getDoctor().getMobileNo() != null) {
                    number = Uri.parse("tel:" + getAppointment.getAppointment.getResult().getDoctor().getMobileNo());
                }
                if(number != null) {
                    call = new Intent(Intent.ACTION_DIAL,number);

                } else {
                    call = new Intent(Intent.ACTION_DIAL);

                }
                startActivity(call);

                break;
            case R.id.cancel :
                showNotifyDialog("Are you sure you want to cancel the appointment?",
                        "", "OK",
                        "Cancel", (NotifyListener)(new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if(which == NotifyDialogFragment.BUTTON_POSITIVE) {
                                    try {
                                        if (getAppointment.getAppointment.getResult() != null) {
                                            postCancelAppointmentViewModel.loadData(getAppointment.getAppointment.getResult().getID(), "Appointment Cancel");
                                        }

                                    }catch (Exception e){

                                    }
                                }

                            }
                        }));

                break;
            case R.id.llDist :
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+getAppointment.getAppointment.getResult().getDoctor().getHospitalAddress()));
                startActivity(intent);
                break;
        }
    }

    public void setGetAppointmentDetailAPIObserver() {
        getAppointment = ViewModelProviders.of(this).get(GetAppointmentViewModel.class);
        getAppointment.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getAppointment.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getAppointment.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getAppointment.isNetworkAvailable.observe(this, obsNoInternet);
        getAppointment.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateData();
            }
        });
    }
    public void setCancelApptAPIObserver() {
        postCancelAppointmentViewModel = ViewModelProviders.of(this).get(PostCancelAppointmentViewModel.class);
        postCancelAppointmentViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postCancelAppointmentViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postCancelAppointmentViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postCancelAppointmentViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postCancelAppointmentViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                home().proceedDoOnBackPressed();
            }
        });
    }


    public void setAppointmentID(String id) {
        this.id = id;
    }
}