package com.moguls.medic.ui.fragments.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorEditTimeSlotAdapter;
import com.moguls.medic.model.PatientList;

import java.util.ArrayList;


public class DoctorAddDoctorFragment extends BaseFragment {

    private boolean isLoading = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    private TextView header_title,update;
    private RecyclerView recyclerView;
    private DoctorEditTimeSlotAdapter doctorEditTimeSlotAdapter;
    ArrayList<String> weekdays = new ArrayList<>();

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
        header_title = (TextView)v.findViewById(R.id.header_title);
        update = (TextView)v.findViewById(R.id.update);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        update.setVisibility(View.VISIBLE);
        setBackButtonToolbarStyleOne(v);
        header_title.setText("Add Hospitals/Clinincs");
        update.setText("Save");
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

}