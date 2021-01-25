package com.moguls.medic.ui.fragments.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.model.appointmentSlots.Sessions;
import com.moguls.medic.model.appointmentSlots.Slots;
import com.moguls.medic.ui.adapters.AppointmentDateAdapter;
import com.moguls.medic.ui.adapters.DoctorBookApptHospitalAdapter;
import com.moguls.medic.ui.adapters.PatientBookApptHospitalAdapter;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.ui.fragments.me.ProfileUpdateFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.model.BookingData;
import com.moguls.medic.model.appointmentSlots.Result;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.PostGetAppointmentsSlotsViewModel;
import com.moguls.medic.webservices.PostGetDoctorAppointmentsSlotsViewModel;
import com.moguls.medic.webservices.PostSaveAppointmentViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PatientBookAppointmentFragment extends BaseFragment implements View.OnClickListener {

    private AppointmentDateAdapter appointmentDateAdapter;
    private RecyclerView recyclerView_date;
    private TextView header_title,today_date,no_appointments;
    private Button book_now;
    public String appointmentID = "";
    public PostGetAppointmentsSlotsViewModel postGetAppointmentsSlotsViewModel;
    public PostGetDoctorAppointmentsSlotsViewModel postGetDoctorAppointmentsSlotsViewModel;
    public PostSaveAppointmentViewModel postSaveAppointmentViewModel;
    private LoadingCompound ld;
    private RecyclerView recyclerView;
    private int selectedPos = -1;
    private String selectedDate = "";
    private String selectedTime = "";

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    String doctor_id = "";

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
        setObservers();
        book_now = (Button)v.findViewById(R.id.book_now);
        recyclerView_date = (RecyclerView)v.findViewById(R.id.recyclerView_date);
        header_title = (TextView)v.findViewById(R.id.header_title);
        today_date = (TextView)v.findViewById(R.id.today_date);
        no_appointments = (TextView)v.findViewById(R.id.no_appointments);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        header_title.setText("Select a time slot");

        book_now.setOnClickListener(this);
        setBackButtonToolbarStyleOne(v);
        initData();
        initDateAdapter();
        if(doctor_id.isEmpty()) {
            book_now.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        initData();
    }

    public void initData() {
        today_date.setText(Helper.dateFormat("dd MMM yyyy",new Date()));
        selectedDate = Helper.dateFormat("dd-MMM-yyyy",new Date());
        if(doctor_id.isEmpty()) {
            postGetDoctorAppointmentsSlotsViewModel.loadData(doctor_id, selectedDate);
        } else {
            postGetAppointmentsSlotsViewModel.loadData(doctor_id, selectedDate);
        }

    }

    public void setObservers() {
        setGetAppointmentSlotsAPIObserver();
        setPostSaveAppointmentsAPIObserver();
        setGetDoctorAppointmentSlotsAPIObserver();
    }
    private void initDateAdapter() {
        recyclerView_date.setHasFixedSize(true);
        recyclerView_date.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        appointmentDateAdapter = new AppointmentDateAdapter(getActivity(),new AppointmentDateAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(String date,int pos) {
                selectedDate = date;
                if(!doctor_id.isEmpty()) {
                    postGetAppointmentsSlotsViewModel.loadData(doctor_id, selectedDate);
                } else {
                    postGetDoctorAppointmentsSlotsViewModel.loadData(doctor_id, selectedDate);

                }

                today_date.setText(date);
            }
        });

        recyclerView_date.setAdapter(appointmentDateAdapter);
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(postGetAppointmentsSlotsViewModel.getAppointmentSlots != null) {
            patientAdapter();
        } else {
            doctorAdapter();
        }
    }

    public void doctorAdapter() {
        DoctorBookApptHospitalAdapter doctorBookAppointmentFragment = new DoctorBookApptHospitalAdapter(getActivity(),
                postGetDoctorAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions(),
                new DoctorBookApptHospitalAdapter.OnItemClickListner() {
                    @Override
                    public void OnItemClick(int position) {
                        selectedPos = position;
                    }
                }, new DoctorBookApptHospitalAdapter.OnTimeClickListner() {
            @Override
            public void OnItemClick(String time) {
                selectedTime = time;
            }
        }, new DoctorBookApptHospitalAdapter.OnClickListner() {
            @Override
            public void OnItemClick(String position) {
                ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();
                profileUpdateFragment.setIsprofile(false);
                profileUpdateFragment.setPatient_id(position);
                // home().setFragment(profileUpdateFragment);
            }

            @Override
            public void OnCancelClick(String position) {
                showNotifyDialog("Are you sure you want to cancel the appointment?",
                        "", "OK",
                        "Cancel", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if (which == NotifyDialogFragment.BUTTON_POSITIVE) {

                                }
                            }
                        }));
            }

            @Override
            public void OnChatClicked(String position, String name,String image) {
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setPhoto(image);
                chatFragment.setToUserID(position);
                chatFragment.setName(name);
                home().setFragment(chatFragment);
            }
        });
        recyclerView.setAdapter(doctorBookAppointmentFragment);

    }
    public void patientAdapter() {
        int ID = 1;
        for (int i = 0; i < postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().size(); i++) {
            List<Slots> slotsArr = new ArrayList<>();
            Sessions sessions = postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().get(i);
            for (String s : sessions.getSlots()) {
                Slots slots = new Slots();
                slots.setTime(s);
                slots.setParentID(i);
                slots.setID(ID);
                ID = ID + 1;
                slotsArr.add(slots);
            }
            sessions.setSlotsArr(slotsArr);
        }


        PatientBookApptHospitalAdapter patientBookApptHospitalAdapter = new PatientBookApptHospitalAdapter(getActivity(),
                postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions(),
                new PatientBookApptHospitalAdapter.OnItemClickListner() {
                    @Override
                    public void OnItemClick(int position) {
                        selectedPos = position;
                    }
                }, new PatientBookApptHospitalAdapter.OnTimeClickListner() {
            @Override
            public void OnItemClick(String time) {
                selectedTime = time;
            }
        }, new PatientBookApptHospitalAdapter.OnClickListner() {
            @Override
            public void OnItemClick(String position) {
                ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();
                profileUpdateFragment.setIsprofile(false);
                profileUpdateFragment.setPatient_id(position);
                // home().setFragment(profileUpdateFragment);
            }

            @Override
            public void OnCancelClick(String position) {
                showNotifyDialog("Are you sure you want to cancel the appointment?",
                        "", "OK",
                        "Cancel", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if (which == NotifyDialogFragment.BUTTON_POSITIVE) {

                                }
                            }
                        }));
            }

            @Override
            public void OnChatClicked(String position, String name,String image) {
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setPhoto(image);
                chatFragment.setToUserID(position);
                chatFragment.setName(name);
                home().setFragment(chatFragment);
            }
        });

        recyclerView.setAdapter(patientBookApptHospitalAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.book_now :
                if(validate()) {
                    if (postGetAppointmentsSlotsViewModel.getAppointmentSlots != null) {
                        String date = selectedDate;
                        if (Helper.convertStringToDate("dd-MMM-yyyy", selectedDate) != null) {
                            date = Helper.dateFormat("dd MMM yyyy", Helper.convertStringToDate("dd-MMM-yyyy", selectedDate));
                        }
                        Result app = postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult();
                        if (app.getSessions() != null) {
                            postSaveAppointmentViewModel.loadData(appointmentID,
                                    app.getSessions().get(selectedPos).getHospitalID(), date + " " + selectedTime);
                        }
                    }
                }
                break;
        }
    }

    public Boolean validate() {
        if(selectedDate.isEmpty()) {
            showNotifyDialog("",
                    "Please select Appointment Date", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        }
        else if(selectedPos == -1 ) {
            showNotifyDialog("",
                    "Please select Appointment Slot", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        } else if(selectedTime.isEmpty()) {
            showNotifyDialog("",
                    "Please select time from the Slot", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        } else {
            return true;
        }

    }

    public void setGetAppointmentSlotsAPIObserver() {
        postGetAppointmentsSlotsViewModel = ViewModelProviders.of(this).get(PostGetAppointmentsSlotsViewModel.class);
        postGetAppointmentsSlotsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postGetAppointmentsSlotsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postGetAppointmentsSlotsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postGetAppointmentsSlotsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postGetAppointmentsSlotsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult() != null) {
                    initAdapter();
                }
                if(postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().size() == 0){
                    no_appointments.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    no_appointments.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void setGetDoctorAppointmentSlotsAPIObserver() {
        postGetDoctorAppointmentsSlotsViewModel = ViewModelProviders.of(this).get(PostGetDoctorAppointmentsSlotsViewModel.class);
        postGetDoctorAppointmentsSlotsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postGetDoctorAppointmentsSlotsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postGetDoctorAppointmentsSlotsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postGetDoctorAppointmentsSlotsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postGetDoctorAppointmentsSlotsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(postGetDoctorAppointmentsSlotsViewModel.getAppointmentSlots.getResult() != null) {
                    initAdapter();
                }
                if(postGetDoctorAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().size() == 0){
                    no_appointments.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    no_appointments.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setPostSaveAppointmentsAPIObserver() {
        postSaveAppointmentViewModel = ViewModelProviders.of(this).get(PostSaveAppointmentViewModel.class);
        postSaveAppointmentViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postSaveAppointmentViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postSaveAppointmentViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postSaveAppointmentViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postSaveAppointmentViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String date = selectedDate;
                if(Helper.convertStringToDate("dd-MMM-yyyy",selectedDate) != null) {
                    date = Helper.dateFormat("dd MMM yyyy",Helper.convertStringToDate("dd-MMM-yyyy",selectedDate));
                }

                PatientConfirmAppointmentFragment patientConfirmAppointmentFragment = new PatientConfirmAppointmentFragment();
                BookingData bookingData = new BookingData();
                bookingData.setDate(date);
                bookingData.setTime(selectedTime);
                bookingData.setRefID(postSaveAppointmentViewModel.saveAppointment.getResult().getRefNo());
                if(postGetAppointmentsSlotsViewModel.getAppointmentSlots != null) {
                    bookingData.setHospitalID(postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().get(selectedPos).getHospitalID());
                    bookingData.setFees(postGetAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().get(selectedPos).getFee());
                } else {
                    bookingData.setHospitalID(postGetDoctorAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().get(selectedPos).getHospitalID());
                    bookingData.setFees(postGetDoctorAppointmentsSlotsViewModel.getAppointmentSlots.getResult().getSessions().get(selectedPos).getFee());
                }


                bookingData.setID(postSaveAppointmentViewModel.saveAppointment.getResult().getID());
                patientConfirmAppointmentFragment.setBookingData(bookingData);
                patientConfirmAppointmentFragment.setDoctorID(doctor_id);
                home().setFragment(patientConfirmAppointmentFragment);
            }
        });
    }



}