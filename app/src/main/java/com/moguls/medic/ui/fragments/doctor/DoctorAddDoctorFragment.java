package com.moguls.medic.ui.fragments.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.model.consultations.Result;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorEditTimeSlotAdapter;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetHospitalSymmaryViewModel;
import com.moguls.medic.webservices.GetHospitalViewModel;
import com.moguls.medic.webservices.GetHospitalsViewModel;

import java.util.ArrayList;


public class DoctorAddDoctorFragment extends BaseFragment {

    public boolean isEdit = false;
    boolean isAdd = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    private TextView header_title,update,hospital_name,hospital_address,distance,duration;
    private RecyclerView recyclerView;
    private DoctorEditTimeSlotAdapter doctorEditTimeSlotAdapter;
    ArrayList<String> weekdays = new ArrayList<>();
    private LoadingCompound ld;
    private GetHospitalViewModel gtHospitalsViewModel;
    String hospitalID = "";
    private LinearLayout viewView,editView;
    private EditText edt_one,edt_two;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor_add_doctor, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetHospitalAPIObserver();
        viewView = (LinearLayout)v.findViewById(R.id.viewView);
        editView = (LinearLayout)v.findViewById(R.id.editView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        edt_one = (EditText)v.findViewById(R.id.edt_one);
        edt_two = (EditText)v.findViewById(R.id.edt_two);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        hospital_name = (TextView)v.findViewById(R.id.hospital_name);
        hospital_address = (TextView)v.findViewById(R.id.hospital_address);
        distance = (TextView)v.findViewById(R.id.distance);
        duration = (TextView)v.findViewById(R.id.duration);
        update = (TextView)v.findViewById(R.id.update);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        update.setVisibility(View.VISIBLE);
        setBackButtonToolbarStyleOne(v);
        if(isAdd) {
            header_title.setText("Add Hospitals/Clinincs");
            editView.setVisibility(View.VISIBLE);
            viewView.setVisibility(View.GONE);
            iniAdapter();
        } else if(isEdit) {
            editView.setVisibility(View.VISIBLE);
            viewView.setVisibility(View.GONE);
        } else {
            update.setVisibility(View.GONE);
            editView.setVisibility(View.GONE);
            viewView.setVisibility(View.VISIBLE);
        }
        update.setText("Save");
        if(!hospitalID.isEmpty()) {
            gtHospitalsViewModel.loadData(hospitalID);
        }
    }
    public void setData() {
        Result result = gtHospitalsViewModel.hospital.getResult();
        hospital_name.setText(result.getName());
        edt_one.setText(result.getName());
        header_title.setText(result.getName());
        hospital_address.setText(result.getAddress());
        edt_two.setText(result.getAddress());
        duration.setText(result.getTimeslot()+" mins");
        iniAdapter();
    }
    private void iniAdapter() {
        initAdapterData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorEditTimeSlotAdapter = new DoctorEditTimeSlotAdapter(getActivity(), weekdays, new DoctorEditTimeSlotAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
            }
        });
        doctorEditTimeSlotAdapter.isAdd =isAdd;
        recyclerView.setAdapter(doctorEditTimeSlotAdapter);
    }

    public void initAdapterData() {
        weekdays.add("Sun");
        weekdays.add("Mon");
        weekdays.add("Tue");
        weekdays.add("Wed");
        weekdays.add("Thur");
        weekdays.add("Fri");
        weekdays.add("Sat");
    }
    public void setGetHospitalAPIObserver() {
        gtHospitalsViewModel = ViewModelProviders.of(this).get(GetHospitalViewModel.class);
        gtHospitalsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        gtHospitalsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        gtHospitalsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        gtHospitalsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        gtHospitalsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setData();
            }
        });
    }


}