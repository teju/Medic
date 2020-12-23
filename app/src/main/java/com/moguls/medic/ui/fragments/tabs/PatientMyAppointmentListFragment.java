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
import com.google.android.material.tabs.TabLayout;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.adapters.PatientAppointmentAdapter;
import com.moguls.medic.ui.adapters.PastAppointmentAdapter;
import com.moguls.medic.ui.fragments.appointment.PatientAppointmentDetailFragment;
import com.moguls.medic.ui.fragments.appointment.PatientBookAppointmentFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.dialog.ApptActionsDialogFragment;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetPatientsViewModel;
import com.moguls.medic.webservices.PostAppointmentListViewModel;
import com.moguls.medic.webservices.PostCancelAppointmentViewModel;

import java.util.ArrayList;


public class PatientMyAppointmentListFragment extends BaseFragment implements
        PatientAppointmentAdapter.OnItemClickListner,
        TabLayout.OnTabSelectedListener ,PastAppointmentAdapter.OnClickListner, View.OnClickListener {

    private PatientAppointmentAdapter patientAppointmentAdapter;
    private PastAppointmentAdapter pastappointmentAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private TextView header_title,no_patients;
    private TabLayout tabLayout;
    final Handler handler = new Handler();
    private LoadingCompound ld;

    public PostAppointmentListViewModel appointmentListViewModel;
    private PostCancelAppointmentViewModel postCancelAppointmentViewModel;
    private int selectedTabPos = 0;

    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetAppointmentsAPIObserver();
        setCancelApptAPIObserver();
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        no_patients = (TextView)v.findViewById(R.id.no_patients);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("UPCOMING"));
        tabLayout.addTab(tabLayout.newTab().setText("PAST"));

        setBackButtonToolbarStyleOne(v);
        header_title.setText("My Appointments");
        tabLayout.addOnTabSelectedListener(this);
        appointmentListViewModel.loadData("false");

    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(selectedTabPos == 0) {
            patientAppointmentAdapter = new PatientAppointmentAdapter(appointmentListViewModel.userAppointments.getResult(),this,getActivity());
            recyclerView.setAdapter(patientAppointmentAdapter);
        } else {
            pastappointmentAdapter = new PastAppointmentAdapter(appointmentListViewModel.userAppointments.getResult(),this,getActivity());
            recyclerView.setAdapter(pastappointmentAdapter);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            appointmentListViewModel.loadData("false");
        }
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
        patientAppointmentAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                patientAppointmentAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                patientAppointmentAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }

    @Override
    public void OnItemClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PatientAppointmentDetailFragment patientAppointmentDetailFragment = new PatientAppointmentDetailFragment();
                patientAppointmentDetailFragment.setAppointmentID (appointmentListViewModel.userAppointments.getResult().get(position).getID());
                home().setFragment(patientAppointmentDetailFragment);

            }
        },500);

    }

    @Override
    public void OnChatClicked(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setToUserID(appointmentListViewModel.userAppointments.getResult().get(position).getDoctor().getID());
                chatFragment.setName(appointmentListViewModel.userAppointments.getResult().get(position).getDoctor().getName());
                home().setFragment(chatFragment);

            }
        },500);
    }

    @Override
    public void OnSettingsClicked(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogDoctor(new NotifyListener() {
                    @Override
                    public void onButtonClicked(int which) {
                        if(which == ApptActionsDialogFragment.BUTTON_CANCEL) {
                            showNotifyDialog("Are you sure you want to cancel the appointment?",
                                    "", "OK",
                                    "Cancel", (NotifyListener)(new NotifyListener() {
                                        public void onButtonClicked(int which) {
                                            if(which == NotifyDialogFragment.BUTTON_POSITIVE) {
                                                try {
                                                    if (appointmentListViewModel.userAppointments.getResult().size() != 0) {
                                                        postCancelAppointmentViewModel.loadData(appointmentListViewModel.userAppointments.getResult().get(position).getID(), "Appointment Cancel");
                                                    }

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

                                        }
                                    }));

                        }
                        if(which == ApptActionsDialogFragment.BUTTON_REBOOK) {
                            PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                            if(appointmentListViewModel.userAppointments.getResult().size() != 0) {
                                patientBookAppointmentFragment.setDoctor_id((appointmentListViewModel.userAppointments.getResult().get(position).getDoctor().getID()));
                                patientBookAppointmentFragment.appointmentID = appointmentListViewModel.userAppointments.getResult().get(position).getID();
                            }
                            home().setFragment(patientBookAppointmentFragment);
                        }
                        if(which == ApptActionsDialogFragment.BUTTON_VIEWDETAILS) {
                            PatientAppointmentDetailFragment patientAppointmentDetailFragment = new PatientAppointmentDetailFragment();
                            patientAppointmentDetailFragment.setAppointmentID (appointmentListViewModel.userAppointments.getResult().get(position).getID());
                            home().setFragment(patientAppointmentDetailFragment);
                        }
                    }
                });

            }
        },500);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selectedTabPos = tab.getPosition();
        if(tab.getPosition() == 0) {
            appointmentListViewModel.loadData("false");
        } else {
            appointmentListViewModel.loadData("true");
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        selectedTabPos = tab.getPosition();
        if(tab.getPosition() == 0) {
            appointmentListViewModel.loadData("false");
        } else {
            appointmentListViewModel.loadData("true");
        }
    }


    @Override
    public void OnReBookClicked(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PatientBookAppointmentFragment patientBookAppointmentFragment = new PatientBookAppointmentFragment();
                if(appointmentListViewModel.userAppointments.getResult().size() != 0) {
                    patientBookAppointmentFragment.setDoctor_id((appointmentListViewModel.userAppointments.getResult().get(position).getDoctor().getID()));
                    patientBookAppointmentFragment.appointmentID = appointmentListViewModel.userAppointments.getResult().get(position).getID();
                }
                home().setFragment(patientBookAppointmentFragment);
            }
        },500);

    }

    @Override
    public void onClick(View v) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()) {

                }
            }
        },500);
    }

    public void setGetAppointmentsAPIObserver() {
        appointmentListViewModel = ViewModelProviders.of(this).get(PostAppointmentListViewModel.class);
        appointmentListViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        appointmentListViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        appointmentListViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        appointmentListViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        appointmentListViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
                initScrollListener();
                if( appointmentListViewModel.userAppointments.getResult().size() != 0) {
                    no_patients.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    no_patients.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
    }
    public void setCancelApptAPIObserver() {
        postCancelAppointmentViewModel = ViewModelProviders.of(this).get(PostCancelAppointmentViewModel.class);
        postCancelAppointmentViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postCancelAppointmentViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postCancelAppointmentViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postCancelAppointmentViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postCancelAppointmentViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appointmentListViewModel.loadData("false");
            }
        });
    }
    @Override
    public void onBackTriggered() {
        super.onBackTriggered();
        bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }
}