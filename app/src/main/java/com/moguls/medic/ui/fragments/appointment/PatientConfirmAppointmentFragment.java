package com.moguls.medic.ui.fragments.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.moguls.medic.model.patientDoctorProfile.Hospitals;
import com.moguls.medic.ui.adapters.PatientBookingAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.me.ProfileUpdateFragment;
import com.moguls.medic.model.BookingData;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetPatientDoctorProfileViewModel;
import com.moguls.medic.webservices.GetPatientsViewModel;
import com.moguls.medic.webservices.PostConfirmAppointmentViewModel;

import java.security.spec.ECField;
import java.util.Date;

import static com.moguls.medic.etc.Helper.loadImage;


public class PatientConfirmAppointmentFragment extends BaseFragment implements View.OnClickListener {


    private TextView header_title,doctor_name,specialization,appointmentOn,hospital_name,address,no_patients;
    private RecyclerView recyclerView;
    private Button btn_confirm;
    private ImageView add_patient,doctor_profile_pic;
    public PostConfirmAppointmentViewModel postConfirmAppointmentViewModel;
    private LoadingCompound ld;
    private BookingData bookingData;
    private GetPatientsViewModel getPatientsViewModel;
    private EditText remarks;
    int selectedPatientPos = 0;
    String appointmentID = "";

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    String doctorID = "";
    private GetPatientDoctorProfileViewModel getPatientDoctorProfileViewModel;

    public void setBookingData(BookingData bookingData) {
        this.bookingData = bookingData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_confirm_appointment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObservers();

        header_title = (TextView) v.findViewById(R.id.header_title);
        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        hospital_name = (TextView) v.findViewById(R.id.hospital_name);
        address = (TextView) v.findViewById(R.id.address);
        appointmentOn = (TextView) v.findViewById(R.id.appointmentOn);
        specialization = (TextView) v.findViewById(R.id.specialization);
        no_patients = (TextView) v.findViewById(R.id.no_patients);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        add_patient = (ImageView) v.findViewById(R.id.add_patient);
        doctor_profile_pic = (ImageView) v.findViewById(R.id.doctor_profile_pic);
        TextView update = (TextView) v.findViewById(R.id.update);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_patient);
        btn_confirm = (Button) v.findViewById(R.id.btn_confirm);
        remarks = (EditText) v.findViewById(R.id.remarks);
        header_title.setText("Book Appointment");
        setBackButtonToolbarStyleOne(v);
        btn_confirm.setOnClickListener(this);
        add_patient.setOnClickListener(this);
        update.setOnClickListener(this);
        getPatientDoctorProfileViewModel.loadData(doctorID);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            getPatientsViewModel.loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPatientsViewModel.loadData();
    }

    public void initData() {
        doctor_name.setText(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getName());
        specialization.setText(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getSpecializations());
        setHospitalDEtails();
        appointmentOn.setText(bookingData.getDate()+" "+bookingData.getTime());
        try {
            long diffdays = Helper.findDifferenceBetweenDates(new Date(), Helper.convertStringToDate("dd MMM yyyy", bookingData.getDate()));
            if (diffdays != 0) {
                appointmentOn.append(" in " + diffdays + " days");
            }
        } catch (Exception e) {

        }
        loadImage(getActivity(),getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getPhotoUrl()
                ,R.drawable.doctor_profile_pic_default,doctor_profile_pic);

        bookingData.setDoctorName(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getName());
        bookingData.setSpecializaion(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getSpecializations());
        bookingData.setPhotoUrl(getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getPhotoUrl());
    }

    public void setHospitalDEtails() {
        for(Hospitals hospital : getPatientDoctorProfileViewModel.getDoctorsProfile.getResult().getHospitals()) {
            if(hospital.getID().equals(bookingData.getHospitalID())) {
                hospital_name.setText(hospital.getName());
                address.setText(hospital.getAddress());
            }
        }
    }
    public void setObservers() {
        setPostConfirmAppointmentAPIObserver();
        setGetPatientsAPIObserver();
        setGetDoctorsProfileAPIObserver();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        PatientBookingAdapter bookingpatientadapter = new PatientBookingAdapter(getActivity(), getPatientsViewModel.getPatients.getResult(), new PatientBookingAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                selectedPatientPos = position;
            }
        });
        recyclerView.setAdapter(bookingpatientadapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if(validate()) {
                    postConfirmAppointmentViewModel.loadData(bookingData.getID(),bookingData.getHospitalID(),
                            bookingData.getDate()+" "+bookingData.getTime(),remarks.getText().toString(),
                            getPatientsViewModel.getPatients.getResult().get(selectedPatientPos).getID());
                }
                break;
            case R.id.update:
                home().setFragment(new PatientBookAppointmentFragment());
                break;
            case R.id.add_patient:
                ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();
                profileUpdateFragment.setAdd(true);
                profileUpdateFragment.setIsprofile(false);
                home().setFragment(profileUpdateFragment);
                break;
        }
    }
    public Boolean validate() {
        if(selectedPatientPos  == -1) {
            showNotifyDialog("",
                    "Please select a patient", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        } else if(remarks.getText().toString().isEmpty()) {
            showNotifyDialog("",
                    "Please enter remarks", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        } else {
            return true;
        }
    }

    public void setPostConfirmAppointmentAPIObserver() {
        postConfirmAppointmentViewModel = ViewModelProviders.of(this).get(PostConfirmAppointmentViewModel.class);
        postConfirmAppointmentViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postConfirmAppointmentViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postConfirmAppointmentViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postConfirmAppointmentViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postConfirmAppointmentViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                showBookingConfirmyDialog(postConfirmAppointmentViewModel.saveAppointment.getResult().getRefNo(),bookingData,new NotifyListener() {
                    @Override
                    public void onButtonClicked(int which) {
                        home().backToMainScreen();
                    }
                });

            }
        });
    }

    public void setGetPatientsAPIObserver() {
        getPatientsViewModel = ViewModelProviders.of(this).get(GetPatientsViewModel.class);
        getPatientsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getPatientsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getPatientsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getPatientsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getPatientsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
                if( getPatientsViewModel.getPatients.getResult().size() != 0) {
                    no_patients.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    no_patients.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
    }
    public void setGetDoctorsProfileAPIObserver() {
        getPatientDoctorProfileViewModel = ViewModelProviders.of(this).get(GetPatientDoctorProfileViewModel.class);
        getPatientDoctorProfileViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getPatientDoctorProfileViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getPatientDoctorProfileViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getPatientDoctorProfileViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });

        getPatientDoctorProfileViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getPatientDoctorProfileViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initData();
            }
        });
    }

}