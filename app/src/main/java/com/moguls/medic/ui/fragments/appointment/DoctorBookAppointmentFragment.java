package com.moguls.medic.ui.fragments.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.ui.adapters.AppointmentDateAdapter;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.adapters.DoctorBookApptHospitalListAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.model.appointmentSlots.Sessions;

import java.util.ArrayList;
import java.util.Date;


public class DoctorBookAppointmentFragment extends BaseFragment implements View.OnClickListener {

    private AppointmentDateAdapter appointmentDateAdapter;
    private RecyclerView recyclerView_date;
    private TextView header_title;
    private Button book_now;
    private RecyclerView recyclerView;
    private DoctorBookApptHospitalListAdapter doctorBookApptHospitalListAdapter;
    private TextView today_date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        book_now = (Button)v.findViewById(R.id.book_now);
        recyclerView_date = (RecyclerView)v.findViewById(R.id.recyclerView_date);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        today_date = (TextView)v.findViewById(R.id.today_date);
        header_title.setText("Select a time slot");
        initAdapter();
        setBackButtonToolbarStyleOne(v);
        initAdapter();
        initDateAdapter();
        today_date.setText(Helper.dateFormat("dd MMM yyyy",new Date()));

    }
    private void initDateAdapter() {
        recyclerView_date.setHasFixedSize(true);
        recyclerView_date.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        appointmentDateAdapter = new AppointmentDateAdapter(getActivity(),new AppointmentDateAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(String date,int pos) {

            }
        });

        recyclerView_date.setAdapter(appointmentDateAdapter);
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        doctorBookApptHospitalListAdapter = new DoctorBookApptHospitalListAdapter(getActivity(),
                new ArrayList<Sessions>(), new DoctorBookApptHospitalListAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void OnCancelClick(int position) {
                showNotifyDialog("Are you sure you want to cancel the appointment?",
                        "", "OK",
                        "Cancel", (NotifyListener)(new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if(which == NotifyDialogFragment.BUTTON_POSITIVE) {

                                }
                            }
                        }));

            }

            @Override
            public void OnChatClicked(int position) {
                home().setFragment(new ChatFragment());
            }
        }, new DoctorBookApptHospitalListAdapter.OnTimeClickListner() {
            @Override
            public void OnItemClick(String time) {

            }
        });
        recyclerView.setAdapter(doctorBookApptHospitalListAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.book_now :
                home().setFragment(new PatientConfirmAppointmentFragment());
                break;
        }
    }
}