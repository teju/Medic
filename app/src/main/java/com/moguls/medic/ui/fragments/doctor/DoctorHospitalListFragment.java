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
import com.moguls.medic.ui.adapters.DoctorHospitalsAdapter;
import com.moguls.medic.ui.fragments.appointment.DoctorBookAppointmentFragment;
import com.moguls.medic.ui.settings.BaseFragment;

import java.util.ArrayList;


public class DoctorHospitalListFragment extends BaseFragment implements View.OnClickListener , DoctorHospitalsAdapter.OnItemClickListner {

    private TextView header_title;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private DoctorHospitalsAdapter myHospitalsAdapter;
    private Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_hospital, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackButtonToolbarStyleOne(v);
        header_title = (TextView)v.findViewById(R.id.header_title);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        header_title.setText("My Hospitals");
        populateData();
        initAdapter();
        initScrollListener();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myHospitalsAdapter = new DoctorHospitalsAdapter(getActivity(),rowsArrayList,this);
        recyclerView.setAdapter(myHospitalsAdapter);
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
            rowsArrayList.add("Item " + i);
            i++;
        }
    }

    private void loadMore() {
        rowsArrayList.add(null);
        myHospitalsAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                myHospitalsAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                myHospitalsAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

        }
    }

    @Override
    public void OnItemClick(int position) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                home().setFragment(new DoctorBookAppointmentFragment());
            }
        },500);
    }

}