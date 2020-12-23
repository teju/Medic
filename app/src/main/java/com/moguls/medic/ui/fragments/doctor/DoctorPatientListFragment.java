package com.moguls.medic.ui.fragments.doctor;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorPatientListAdapter;
import com.moguls.medic.model.PatientList;

import java.util.ArrayList;


public class DoctorPatientListFragment extends BaseFragment {

    private DoctorPatientListAdapter doctorPatientListAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    private TextView header_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_patient_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        initAdapter();
        populateData();
        initScrollListener();
        setBackButtonToolbarStyleOne(v);
        header_title.setText("My Patients");
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorPatientListAdapter = new DoctorPatientListAdapter(getActivity(), rowsArrayList, new DoctorPatientListAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void OnChatClicked(int position) {
                    home().setFragment(new ChatFragment());
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
}