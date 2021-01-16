package com.moguls.medic.ui.fragments.doctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.model.hospitalSummary.Result;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorHospitalScheduledTimeAdapter;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetHospitalSymmaryViewModel;
import com.moguls.medic.webservices.GetLeaveHospitalViewModel;
import com.moguls.medic.webservices.PostSaveHospitalViewModel;

import java.util.ArrayList;


public class DoctorViewProfileHospitalFragment extends BaseFragment implements View.OnClickListener {

    private boolean isLoading = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    private TextView header_title,update,hospital_status,hospital_address,distance,editSlots,viewSlots;
    private RecyclerView recyclerView;
    private DoctorHospitalScheduledTimeAdapter doctorHospitalScheduledTimeAdapter;
    private GetHospitalSymmaryViewModel getHospitalSymmaryViewModel;
    private GetLeaveHospitalViewModel getLeaveHospitalViewModel;
    private LoadingCompound ld;
    String hospitalID;
    private Button leave_hospital;
    private RelativeLayout goTogoogle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_doctor_profile, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header_title = (TextView)v.findViewById(R.id.header_title);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        hospital_status = (TextView)v.findViewById(R.id.hospital_status);
        goTogoogle = (RelativeLayout)v.findViewById(R.id.goTogoogle);
        editSlots = (TextView)v.findViewById(R.id.editSlots);
        viewSlots = (TextView)v.findViewById(R.id.viewSlots);
        hospital_address = (TextView)v.findViewById(R.id.hospital_address);
        leave_hospital = (Button)v.findViewById(R.id.leave_hospital);
        distance = (TextView)v.findViewById(R.id.distance);
        setBackButtonToolbarStyleOne(v);
        setGetHospitalAPIObserver();
        setGetLeaveHospitalViewModelHospitalAPIObserver();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        editSlots.setOnClickListener(this);
        viewSlots.setOnClickListener(this);
        leave_hospital.setOnClickListener(this);
        goTogoogle.setOnClickListener(this);
        getHospitalSymmaryViewModel.loadData(hospitalID);


    }

    private void setData() {
        Result result = getHospitalSymmaryViewModel.hospitalSummary.getResult();
        header_title.setText(result.getName());
        hospital_status.setText(result.getToday());
        hospital_address.setText(result.getAddress());
        //distance.setText(result.());
        iniAdapter();
    }
    private void iniAdapter() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        doctorHospitalScheduledTimeAdapter = new DoctorHospitalScheduledTimeAdapter(getActivity(),
                getHospitalSymmaryViewModel.hospitalSummary.getResult().getSessions(), new DoctorHospitalScheduledTimeAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }
        });
        recyclerView.setAdapter(doctorHospitalScheduledTimeAdapter);
    }

    public void setGetHospitalAPIObserver() {
        getHospitalSymmaryViewModel = ViewModelProviders.of(this).get(GetHospitalSymmaryViewModel.class);
        getHospitalSymmaryViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getHospitalSymmaryViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        getHospitalSymmaryViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getHospitalSymmaryViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getHospitalSymmaryViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setData();
            }
        });
    }

    public void setGetLeaveHospitalViewModelHospitalAPIObserver() {
        getLeaveHospitalViewModel = ViewModelProviders.of(this).get(GetLeaveHospitalViewModel.class);
        getLeaveHospitalViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getLeaveHospitalViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        getLeaveHospitalViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getLeaveHospitalViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getLeaveHospitalViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                showNotifyDialog("",
                        getLeaveHospitalViewModel.genericResponse.getMessage(), "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {

                            }
                        }));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editSlots :
                DoctorAddDoctorFragment doctorAddDoctorFragment = new DoctorAddDoctorFragment();
                doctorAddDoctorFragment.isEdit = true;
                doctorAddDoctorFragment.hospitalID = hospitalID;
                home().setFragment(doctorAddDoctorFragment);
                break;
            case R.id.viewSlots :
                doctorAddDoctorFragment = new DoctorAddDoctorFragment();
                doctorAddDoctorFragment.isEdit = false;
                doctorAddDoctorFragment.hospitalID = hospitalID;
                home().setFragment(doctorAddDoctorFragment);
                break;
            case R.id.leave_hospital:
                showNotifyDialog("",
                        "Are you sure you want to delete ?", "OK",
                        "Cancel", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if(which == NotifyDialogFragment.BUTTON_POSITIVE) {
                                    getLeaveHospitalViewModel.loadData(hospitalID);
                                }
                            }
                        }));

                break;
            case R.id.goTogoogle:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+getHospitalSymmaryViewModel.hospitalSummary.getResult().getAddress()));
                startActivity(intent);
                break;

        }
    }
}