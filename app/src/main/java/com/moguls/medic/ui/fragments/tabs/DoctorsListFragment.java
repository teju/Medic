package com.moguls.medic.ui.fragments.tabs;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.fragments.appointment.PatientBookAppointmentFragment;
import com.moguls.medic.ui.adapters.PatientDoctorListAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.ui.fragments.doctor.PatientDoctorProfileFragment;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.model.doctors.Result;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetDoctorsViewModel;

import java.util.ArrayList;
import java.util.List;


public class DoctorsListFragment extends BaseFragment implements PatientDoctorListAdapter.OnItemClickListner {

    private PatientDoctorListAdapter patientDoctorListAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    List<Result> rowsArrayList = new ArrayList<>();
    private TextView header_title,no_list;
    final Handler handler = new Handler();
    public GetDoctorsViewModel getDoctorsViewModel;
    private LoadingCompound ld;

    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctors_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObservers();

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        no_list = (TextView)v.findViewById(R.id.no_list);
        ld = (LoadingCompound)v.findViewById(R.id.ld);

       // initScrollListener();
        setBackButtonToolbarStyleOne(v);
        header_title.setText("My Doctors");
        getDoctorsViewModel.loadData();
    }

    public void setObservers() {
        setGetDoctorsAPIObserver();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        patientDoctorListAdapter = new PatientDoctorListAdapter(getActivity(),rowsArrayList,this);
        recyclerView.setAdapter(patientDoctorListAdapter);
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


    private void loadMore() {
        rowsArrayList.add(null);
        patientDoctorListAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                patientDoctorListAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                   // rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                patientDoctorListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }


    @Override
    public void OnItemClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PatientDoctorProfileFragment patientDoctorProfileFragment = new PatientDoctorProfileFragment();
                patientDoctorProfileFragment.setIsPRofileView(true);
                patientDoctorProfileFragment.setDoctorID(getDoctorsViewModel.getDoctors.getResult().get(position).getID());
                home().setFragment(patientDoctorProfileFragment);
            }
        },500);
    }

    @Override
    public void OnApptClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                patientBookAppointmentFragment.setDoctor_id(getDoctorsViewModel.getDoctors.getResult().get(position).getID());
                home().setFragment(patientBookAppointmentFragment);
            }
        },500);

    }

    @Override
    public void OnChatClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setPhoto(getDoctorsViewModel.getDoctors.getResult().get(position).getPhotoUrl());
                chatFragment.setToUserID(getDoctorsViewModel.getDoctors.getResult().get(position).getID());
                chatFragment.setName(getDoctorsViewModel.getDoctors.getResult().get(position).getName());
                home().setFragment(chatFragment);
            }
        },500);

    }
    public void setGetDoctorsAPIObserver() {
        getDoctorsViewModel = ViewModelProviders.of(this).get(GetDoctorsViewModel.class);
        getDoctorsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getDoctorsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getDoctorsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getDoctorsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });

        getDoctorsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getDoctorsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
               if(getDoctorsViewModel.getDoctors.getResult().size() == 0) {
                   recyclerView.setVisibility(View.GONE);
                   no_list.setVisibility(View.VISIBLE);
               } else {
                   recyclerView.setVisibility(View.VISIBLE);
                   no_list.setVisibility(View.GONE);
                   rowsArrayList = getDoctorsViewModel.getDoctors.getResult();
                   initAdapter();
               }
            }
        });
    }
    @Override
    public void onBackTriggered() {
        super.onBackTriggered();
        bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }
}