package com.moguls.medic.ui.fragments.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorHospitalScheduledTimeAdapter;
import com.moguls.medic.model.PatientList;

import java.util.ArrayList;


public class DoctorViewProfileFragment extends BaseFragment {

    private boolean isLoading = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    private TextView header_title,update;
    private RecyclerView recyclerView;
    private DoctorHospitalScheduledTimeAdapter doctorHospitalScheduledTimeAdapter;

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
        setBackButtonToolbarStyleOne(v);
        header_title.setText("Fortis Hospital");
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        iniAdapter();

    }
    private void iniAdapter() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        doctorHospitalScheduledTimeAdapter = new DoctorHospitalScheduledTimeAdapter(getActivity(), new ArrayList<>(), new DoctorHospitalScheduledTimeAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }
        });

        recyclerView.setAdapter(doctorHospitalScheduledTimeAdapter);
    }

}