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

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.adapters.DoctorHospitalsAdapter;
import com.moguls.medic.ui.fragments.appointment.PatientBookAppointmentFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetHospitalsViewModel;

import java.util.ArrayList;


public class DoctorHospitalListFragment extends BaseFragment implements View.OnClickListener , DoctorHospitalsAdapter.OnItemClickListner {

    private TextView header_title;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private DoctorHospitalsAdapter myHospitalsAdapter;
    private Handler handler;
    private LoadingCompound ld;
    private GetHospitalsViewModel hospitalsViewModel;

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
        ld = (LoadingCompound)v.findViewById(R.id.ld);

        header_title.setText("My Hospitals");

        setGetHospitalAPIObserver();
        hospitalsViewModel.loadData();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myHospitalsAdapter = new DoctorHospitalsAdapter(getActivity(),hospitalsViewModel.hospitalView.getResult(),this);
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
                PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                home().setFragment(patientBookAppointmentFragment);
            }
        },500);
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

}