package com.moguls.medic.ui.fragments.doctor;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorHospitalsClinicsAdapter;
import com.moguls.medic.ui.adapters.DoctorPatientListAdapter;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetHospitalsViewModel;

import java.util.ArrayList;


public class DoctorProfileHospitalListFragment extends BaseFragment implements View.OnClickListener {

    private DoctorHospitalsClinicsAdapter doctorPatientListAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    private TextView header_title;
    private FloatingActionButton floating_action_button;
    private GetHospitalsViewModel hospitalsViewModel;
    private LoadingCompound ld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_doctor_hospital_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetHospitalAPIObserver();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        ld = (LoadingCompound)v.findViewById(R.id.ld);

        floating_action_button = (FloatingActionButton)v.findViewById(R.id.floating_action_button);
        setBackButtonToolbarStyleOne(v);
        header_title.setText("Hospitals/Clinincs");
        floating_action_button.setOnClickListener(this);
        hospitalsViewModel.loadData();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorPatientListAdapter = new DoctorHospitalsClinicsAdapter(getActivity(), hospitalsViewModel.hospitalView.getResult(),
                new DoctorHospitalsClinicsAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                DoctorViewProfileHospitalFragment doctorViewProfileHospitalFragment = new DoctorViewProfileHospitalFragment();
                doctorViewProfileHospitalFragment.hospitalID = hospitalsViewModel.hospitalView.getResult().get(position).getID();
                home().setFragment(doctorViewProfileHospitalFragment);
            }
        });
        recyclerView.setAdapter(doctorPatientListAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }
    private void populateData() {
        int i = 0;
        while (i < 10) {
            PatientList patientList = new PatientList();
            patientList.setName("Item " + i);
            if(i == 0 || i == 3 || i == 9) {
                patientList.setType(DoctorPatientListAdapter.SECTION_VIEW);
            } else {
                patientList.setType(DoctorPatientListAdapter.VIEW_TYPE_ITEM);
            }
            rowsArrayList.add(patientList);
            i++;
        }
    }

    private void loadMore() {
        rowsArrayList.add(null);
        doctorPatientListAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                doctorPatientListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    PatientList patientList = new PatientList();
                    patientList.setName("Item " + currentSize);
                    if(currentSize == 0 || currentSize == 3 || currentSize == 9) {
                        patientList.setType(DoctorPatientListAdapter.SECTION_VIEW);
                    } else {
                        patientList.setType(DoctorPatientListAdapter.VIEW_TYPE_ITEM);
                    }
                    rowsArrayList.add(patientList);
                    currentSize++;
                }

                doctorPatientListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }
    public void setGetHospitalAPIObserver() {
        hospitalsViewModel = ViewModelProviders.of(this).get(GetHospitalsViewModel.class);
        hospitalsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        hospitalsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        hospitalsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        hospitalsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        hospitalsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_button :
                DoctorAddDoctorFragment doctorAddDoctorFragment = new DoctorAddDoctorFragment();
                doctorAddDoctorFragment.isAdd = true;
                home().setFragment(doctorAddDoctorFragment);
                break;
        }
    }
}