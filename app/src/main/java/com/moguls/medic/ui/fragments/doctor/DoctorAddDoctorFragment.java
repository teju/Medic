package com.moguls.medic.ui.fragments.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.EditSlotsListener;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.model.consultations.Result;
import com.moguls.medic.model.consultations.Sessions;
import com.moguls.medic.model.consultations.StartEndTime;
import com.moguls.medic.model.hospital.Hospital;
import com.moguls.medic.ui.fragments.consultation.AddConsultationFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorEditTimeSlotAdapter;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetHospitalSymmaryViewModel;
import com.moguls.medic.webservices.GetHospitalViewModel;
import com.moguls.medic.webservices.GetHospitalsViewModel;
import com.moguls.medic.webservices.PostSaveHospitalViewModel;

import java.util.ArrayList;
import java.util.List;


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
    private EditText edt_one,edt_two,consultationFee,slotsDuration,bookingDays;
    private PostSaveHospitalViewModel postSaveHospitalViewModel;
    private RelativeLayout createSlots;
    private LinearLayout lladd;
    List<Sessions> Sessions = new ArrayList<>();

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
        setGetSaveHospitalViewModelHospitalAPIObserver();

        viewView = (LinearLayout)v.findViewById(R.id.viewView);
        lladd = (LinearLayout)v.findViewById(R.id.lladd);
        createSlots = (RelativeLayout)v.findViewById(R.id.createSlots);
        editView = (LinearLayout)v.findViewById(R.id.editView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        edt_one = (EditText)v.findViewById(R.id.edt_one);
        consultationFee = (EditText)v.findViewById(R.id.consultationFee);
        slotsDuration = (EditText)v.findViewById(R.id.slotsDuration);
        edt_two = (EditText)v.findViewById(R.id.edt_two);
        bookingDays = (EditText)v.findViewById(R.id.bookingDays);
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
            lladd.setVisibility(View.GONE);
            iniAdapter();
        } else if(isEdit) {
            editView.setVisibility(View.VISIBLE);
            viewView.setVisibility(View.GONE);
            lladd.setVisibility(View.GONE);
        } else {
            update.setVisibility(View.GONE);
            editView.setVisibility(View.GONE);
            viewView.setVisibility(View.VISIBLE);
            lladd.setVisibility(View.GONE);
        }
        update.setText("Save");
        if(!hospitalID.isEmpty()) {
            gtHospitalsViewModel.loadData(hospitalID);
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    makeParams();
                }
            }
        });
    }
    public boolean validate() {
        if(edt_one.getText().toString().isEmpty()) {
            edt_one.setError("Enter Name");
            edt_one.requestFocus();
            return false;
        } else if(edt_two.getText().toString().isEmpty()) {
            edt_two.setError("Enter Address");
            edt_two.requestFocus();
            return false;
        } /*else if(isAdd && consultationFee.getText().toString().isEmpty()) {
            consultationFee.setError("Enter consultation Fee");
            consultationFee.requestFocus();
            return false;
        } else if(isAdd && slotsDuration.getText().toString().isEmpty()) {
            slotsDuration.setError("Enter Duration");
            slotsDuration.requestFocus();
            return false;
        } else if(isAdd && bookingDays.getText().toString().isEmpty()) {
            bookingDays.setError("Enter Advance Booking Days");
            bookingDays.requestFocus();
            return false;
        } */else {
            return true;
        }
    }

    public void makeParams() {
        Result result = new Result();
        if(!isAdd) {
            result = gtHospitalsViewModel.hospital.getResult();
        }
        result.setName(edt_one.getText().toString());
        result.setAddress(edt_two.getText().toString());
        result.setTimeslot(slotsDuration.getText().toString());
        result.setConsultationFee(consultationFee.getText().toString());
        result.setAdvanceBookingDays(bookingDays.getText().toString());
        result.setAdvanceBookingDays(bookingDays.getText().toString());
        result.setSessions(Sessions);
        postSaveHospitalViewModel.loadData(result);
    }

    public void setData() {
        if(gtHospitalsViewModel.hospital != null) {
            Result result = gtHospitalsViewModel.hospital.getResult();
            hospital_name.setText(result.getName());
            edt_one.setText(result.getName());
            header_title.setText(result.getName());
            hospital_address.setText(result.getAddress());
            edt_two.setText(result.getAddress());
            duration.setText(result.getTimeslot() + " mins");
        }

    }
    private void iniAdapter() {
        if(gtHospitalsViewModel.hospital != null) {
            Sessions =  gtHospitalsViewModel.hospital.getResult().getSessions();
        } else {
            Sessions.clear();
            Sessions  session = new Sessions();
            session.setDay("Sun");
            Sessions.add(session);
            session = new Sessions();
            session.setDay("Mon");
            Sessions.add(session);
            session = new Sessions();
            session.setDay("Tue");
            Sessions.add(session);
            session = new Sessions();
            session.setDay("Wed");
            Sessions.add(session);
            session = new Sessions();
            session.setDay("Thu");
            Sessions.add(session);
            session = new Sessions();
            session.setDay("Fri");
            Sessions.add(session);
            session = new Sessions();
            session.setDay("Sat");
            Sessions.add(session);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<com.moguls.medic.model.consultations.Sessions> finalSessions = Sessions;
        doctorEditTimeSlotAdapter = new DoctorEditTimeSlotAdapter(getActivity(),Sessions,
                new DoctorEditTimeSlotAdapter.OnItemClickListner() {
                @Override
                public void OnItemClick(int position, int type) {
                    showEDitSlotsDialog(new EditSlotsListener() {
                        @Override
                        public void onButtonClicked(String startDate,String endDate) {
                            if(type == DoctorEditTimeSlotAdapter.MORNING) {
                                try {
                                    finalSessions.get(position).getMorning().setEnd(endDate);
                                    finalSessions.get(position).getMorning().setStart(startDate);
                                } catch (Exception e) {
                                    StartEndTime startEndTime = new StartEndTime();
                                    startEndTime.setStart(startDate);
                                    startEndTime.setEnd(endDate);
                                    finalSessions.get(position).setMorning(startEndTime);
                                }
                            } else if(type == DoctorEditTimeSlotAdapter.AFTERNOON) {
                                try {
                                    finalSessions.get(position).getAfterNoon().setEnd(endDate);
                                    finalSessions.get(position).getAfterNoon().setStart(startDate);
                                } catch (Exception e) {
                                    StartEndTime startEndTime = new StartEndTime();
                                    startEndTime.setStart(startDate);
                                    startEndTime.setEnd(endDate);
                                    finalSessions.get(position).setAfterNoon(startEndTime);
                                }
                            } if(type == DoctorEditTimeSlotAdapter.EVENING) {
                                try {
                                    finalSessions.get(position).getEvening().setEnd(endDate);
                                    finalSessions.get(position).getEvening().setStart(startDate);
                                } catch (Exception e) {
                                    StartEndTime startEndTime = new StartEndTime();
                                    startEndTime.setStart(startDate);
                                    startEndTime.setEnd(endDate);
                                    finalSessions.get(position).setEvening(startEndTime);
                                };
                            }
                            doctorEditTimeSlotAdapter.mItemList = finalSessions;
                            doctorEditTimeSlotAdapter.notifyDataSetChanged();
                        }
                    });
                }
        });
        doctorEditTimeSlotAdapter.isAdd =isAdd;
        recyclerView.setAdapter(doctorEditTimeSlotAdapter);
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
                iniAdapter();
            }
        });
    }
    public void setGetSaveHospitalViewModelHospitalAPIObserver() {
        postSaveHospitalViewModel = ViewModelProviders.of(this).get(PostSaveHospitalViewModel.class);
        postSaveHospitalViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postSaveHospitalViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        postSaveHospitalViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postSaveHospitalViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postSaveHospitalViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                showNotifyDialog("",
                        postSaveHospitalViewModel.genericResponse.getMessage(), "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {

                            }
                        }));
            }
        });
    }


}