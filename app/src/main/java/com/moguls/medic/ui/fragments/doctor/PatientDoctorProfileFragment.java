package com.moguls.medic.ui.fragments.doctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.adapters.DoctorProfileHospitalListAdapter;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.ui.fragments.appointment.PatientBookAppointmentFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetDoctorsViewModel;
import com.moguls.medic.webservices.GetPatientDoctorProfileViewModel;

import java.util.ArrayList;


public class PatientDoctorProfileFragment extends BaseFragment implements View.OnClickListener, DoctorProfileHospitalListAdapter.OnItemClickListner  {
    private TextView header_title;
    private RecyclerView recyclerView;
    private DoctorProfileHospitalListAdapter doctorprofileHospitalListAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private boolean isLoading = false;
    private ImageView btnappt,chat,call;
    final Handler handler = new Handler();
    private boolean isPRofileView = false;
    private Button confirm,cancel;
    private ScrollView rlRoot;
    private LoadingCompound ld;
    private TextView doctor_name,specialization,experience,description;

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    private String doctorID = "";
    private GetPatientDoctorProfileViewModel getPatientDoctorProfileViewModel;

    public void setIsPRofileView(boolean isPRofileView) {
        this.isPRofileView = isPRofileView;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetDoctorsProfileAPIObserver();
        header_title = (TextView)v.findViewById(R.id.header_title);
        ImageView arrow_left = (ImageView) v.findViewById(R.id.arrow_left);
        arrow_left.setOnClickListener(this);
        initUI();
    }
    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorprofileHospitalListAdapter = new DoctorProfileHospitalListAdapter(getActivity(),getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getHospitals(),this);
        recyclerView.setAdapter(doctorprofileHospitalListAdapter);
    }

    public void initUI(){
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        doctor_name = (TextView)v.findViewById(R.id.doctor_name);
        specialization = (TextView)v.findViewById(R.id.specialization);
        description = (TextView)v.findViewById(R.id.description);
        experience = (TextView)v.findViewById(R.id.experience);
        btnappt = (ImageView)v.findViewById(R.id.btnappt);
        chat = (ImageView)v.findViewById(R.id.chat);
        call = (ImageView)v.findViewById(R.id.call);
        confirm = (Button)v.findViewById(R.id.confirm);
        cancel = (Button)v.findViewById(R.id.cancel);
        rlRoot = (ScrollView)v.findViewById(R.id.rlRoot);
        ld = (LoadingCompound)v.findViewById(R.id.ld);

        setBackButtonToolbarStyleOne(v);
        call.setOnClickListener(this);
        btnappt.setOnClickListener(this);
        chat.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        if(isPRofileView) {
            confirm.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 150);
            rlRoot.setLayoutParams(params);
            confirm.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
        getPatientDoctorProfileViewModel.loadData(doctorID);
    }

    private void populateData() {
        int i = 0;
        while (i < 3) {
            rowsArrayList.add("Item " + i);
            i++;
        }
    }

    private void loadMore() {
        rowsArrayList.add(null);
        doctorprofileHospitalListAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                doctorprofileHospitalListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                doctorprofileHospitalListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }

    @Override
    public void onClick(View v) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()){
                    case R.id.chat:
                        ChatFragment chatFragment = new ChatFragment();
                        chatFragment.setPhoto(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getPhotoUrl());
                        chatFragment.setToUserID(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getID());
                        chatFragment.setName(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getName());
                        home().setFragment(chatFragment);
                        break;
                    case R.id.btnappt:
                        PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                        patientBookAppointmentFragment.setDoctor_id(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getID());
                        home().setFragment(patientBookAppointmentFragment);
                        break;
                    case R.id.call:
                        Uri number = null;
                        Intent call;
                        if(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getMobileNo()!= null) {
                            number = Uri.parse("tel:" + getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getMobileNo());
                        }
                        if(number != null) {
                            call = new Intent(Intent.ACTION_DIAL,number);

                        } else {
                            call = new Intent(Intent.ACTION_DIAL);

                        }
                        startActivity(call);
                        break;
                    case R.id.arrow_left:
                        home().onBackPressed();
                        break;
                    case R.id.confirm:
                        home().backToMainScreen();
                        break;
                    case R.id.cancel:
                        home().onBackPressed();
                        break;
                }

            }
        },500);
    }

    @Override
    public void OnItemClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                patientBookAppointmentFragment.setDoctor_id(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getID());
                home().setFragment(patientBookAppointmentFragment);
            }
        },500);
    }
    public void setGetDoctorsProfileAPIObserver() {
        getPatientDoctorProfileViewModel = ViewModelProviders.of(this).get(GetPatientDoctorProfileViewModel.class);
        getPatientDoctorProfileViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getPatientDoctorProfileViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getPatientDoctorProfileViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getPatientDoctorProfileViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
               logOut(getActivity());
            }
        });

        getPatientDoctorProfileViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getPatientDoctorProfileViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateData();
            }
        });
    }

    private void updateData() {
        try {
            doctor_name.setText(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getName());
            experience.setText(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getExperience() + " Years");
            description.setText(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getStatement());
            specialization.setText(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getQualifications() + ", "
                    + getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getSpecializations());
        } catch (Exception e){

        }
        initAdapter();
    }

}