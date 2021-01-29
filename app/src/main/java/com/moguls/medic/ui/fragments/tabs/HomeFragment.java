package com.moguls.medic.ui.fragments.tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.appointment.PatientAppointmentDetailFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.ui.fragments.doctor.PatientAddDoctorFragment;
import com.moguls.medic.ui.fragments.doctor.DoctorHospitalListFragment;
import com.moguls.medic.ui.fragments.doctor.DoctorPatientListFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetDashBoardViewModel;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    int MAIN_FLOW_INDEX = 0;
    String MAIN_FLOW_TAG = "MainFlowFragment";
    private Fragment currentFragment;
    private RelativeLayout ll_two;
    private RelativeLayout ll_three;
    private RelativeLayout ll_one;
    private LinearLayout upcoming_appt;
    private TextView tv_desc_one,tv_title_one,tv_title_two,tv_desc_two,msgs_cnt,doctor_name,
            specialization,address, date_time,patient_name,remarks;
    private ImageView img_doctor_white;
    private ImageView icn_one,icn_two,book_appointment_banner,logo;
    private GetDashBoardViewModel getDashBoardViewModel;
    private LoadingCompound ld;

    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        getDashBoardViewModel.loadData();
    }

    public void initUI() {
        setGetDashboardAPIObserver();
        ll_two = (RelativeLayout) v.findViewById(R.id.ll_two);
        upcoming_appt = (LinearLayout) v.findViewById(R.id.upcoming_appt);
        ll_three = (RelativeLayout) v.findViewById(R.id.ll_three);
        ll_one = (RelativeLayout) v.findViewById(R.id.ll_one);
        tv_desc_one = (TextView) v.findViewById(R.id.tv_desc_one);
        tv_title_two = (TextView) v.findViewById(R.id.tv_title_two);
        tv_title_one = (TextView) v.findViewById(R.id.tv_title_one);
        tv_desc_two = (TextView) v.findViewById(R.id.tv_desc_two);
        msgs_cnt = (TextView) v.findViewById(R.id.msgs_cnt);
        date_time = (TextView) v.findViewById(R.id.date_time);
        patient_name = (TextView) v.findViewById(R.id.patient_name);
        remarks = (TextView) v.findViewById(R.id.remarks);
        doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        specialization = (TextView) v.findViewById(R.id.specialization);
        address = (TextView) v.findViewById(R.id.address);
        icn_one = (ImageView) v.findViewById(R.id.icn_one);
        icn_two = (ImageView) v.findViewById(R.id.icn_two);
        logo = (ImageView) v.findViewById(R.id.logo);
        book_appointment_banner = (ImageView) v.findViewById(R.id.book_appointment_banner);
        ld = (LoadingCompound) v.findViewById(R.id.ld);

        FloatingActionButton scan_doctor = (FloatingActionButton) v.findViewById(R.id.floating_action_button);
        ImageView chat = (ImageView) v.findViewById(R.id.chat);
        ImageView call = (ImageView) v.findViewById(R.id.call);
        ll_two.setOnClickListener(this);
        ll_three.setOnClickListener(this);
        ll_one.setOnClickListener(this);
        scan_doctor.bringToFront();
        scan_doctor.setOnClickListener(this);
        chat.setOnClickListener(this);
        call.setOnClickListener(this);
        upcoming_appt.setOnClickListener(this);
        if (SharedPreference.getBoolean(getActivity(), SharedPreference.isDOCTOR)) {
            scan_doctor.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            getDashBoardViewModel.loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateData() {
        try {
            doctor_name.setText(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getName());
            specialization.setText(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getSpecialization()
                    + "|" + getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getHospital());
            address.setText(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getHospitalAddress());
            remarks.setText("Experience " + getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getExperience() + " years");
        } catch (Exception e){

        }
        date_time.setText(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getAppointmentOn());
        patient_name.setText(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getPatient().getName());
        Helper.loadImage(getActivity(),
                getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getPatient().getPhotoUrl(),
                R.drawable.doctor_profile_pic_default,logo);
    }

    public void initDoctorView() {
        ll_three.setVisibility(View.GONE);
        tv_title_one.setText("My Patients");
        tv_title_two.setText("My Hospitals");
        tv_desc_one.setText(getDashBoardViewModel.dashBoard.getResult().getNoOfPatients()+" Patients");
        tv_desc_two.setText(getDashBoardViewModel.dashBoard.getResult().getNoOfHospitals()+" Hospitals");
        icn_one.setImageDrawable(getActivity().getDrawable(R.drawable.account_group));
        icn_two.setImageDrawable(getActivity().getDrawable(R.drawable.domain));
    }

    @Override
    public void onBackTriggered() {
       home().resetAndExit();
    }

    @Override
    public void onClick(View v) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                switch (v.getId()){
                    case R.id.ll_one :
                        if(SharedPreference.getBoolean(getActivity(),SharedPreference.isDOCTOR)) {
                            DoctorPatientListFragment doctorsListFragment = new DoctorPatientListFragment();
                            doctorsListFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, doctorsListFragment,
                                    "MAIN_TAB","FOURTH_TAB");
                            bottomNavigation.getMenu().findItem(R.id.navigation_doctors).setChecked(true);
                        } else {
                            DoctorsListFragment doctorsListFragment = new DoctorsListFragment();
                            doctorsListFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, doctorsListFragment,
                                    "MAIN_TAB","FOURTH_TAB");
                            bottomNavigation.getMenu().findItem(R.id.navigation_doctors).setChecked(true);
                        }
                        break;
                    case R.id.ll_two :
                        if(SharedPreference.getBoolean(getActivity(),SharedPreference.isDOCTOR)) {
                            home().setFragment(new DoctorHospitalListFragment());
                        } else {
                            PatientMyAppointmentListFragment patientMyAppointmentListFragment = new PatientMyAppointmentListFragment();
                            patientMyAppointmentListFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, patientMyAppointmentListFragment,
                                    "MAIN_TAB", "THIRD_TAB");
                            bottomNavigation.getMenu().findItem(R.id.navigation_appts).setChecked(true);
                        }
                        break;
                    case R.id.ll_three :
                        ChatListFragment chatListFragment = new ChatListFragment();
                        chatListFragment.setBottomNavigation(bottomNavigation);
                        home().setFragmentInFragment(
                                R.id.mainLayoutFragment, chatListFragment,
                                "MAIN_TAB","SECOND_TAB");
                        bottomNavigation.getMenu().findItem(R.id.navigation_chats).setChecked(true);
                        break;
                    case R.id.floating_action_button :
                        home().setFragment(new PatientAddDoctorFragment());
                        break;
                    case R.id.chat :
                        ChatFragment chatFragment = new ChatFragment();
                        chatFragment.setPhoto(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getPhotoUrl());
                        chatFragment.setToUserID(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getID());
                        chatFragment.setName(getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getName());
                        home().setFragment(chatFragment);
                        break;
                    case R.id.call :
                        Uri number = Uri.parse("tel:"+getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getDoctor().getMobileNo());
                        if(number != null) {
                            Intent call = new Intent(Intent.ACTION_DIAL,number);
                            getActivity().startActivity(call);
                        } else {
                            Intent call = new Intent(Intent.ACTION_DIAL);
                            getActivity().startActivity(call);
                        }

                        break;
                    case R.id.upcoming_appt :
                        PatientAppointmentDetailFragment patientAppointmentDetailFragment = new PatientAppointmentDetailFragment();
                        patientAppointmentDetailFragment.setAppointmentID (getDashBoardViewModel.dashBoard.getResult().getAppointments().get(0).getID());
                        home().setFragment(patientAppointmentDetailFragment);
                        break;
                }

            }
        }, 100);
    }

    public void setGetDashboardAPIObserver() {
        getDashBoardViewModel = ViewModelProviders.of(this).get(GetDashBoardViewModel.class);
        getDashBoardViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getDashBoardViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        getDashBoardViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getDashBoardViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getDashBoardViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(!SharedPreference.getBoolean(getActivity(),SharedPreference.isDOCTOR)) {
                    tv_desc_one.setText(getDashBoardViewModel.dashBoard.getResult().getNoOfDoctors() + " Doctors");
                    tv_desc_two.setText(getDashBoardViewModel.dashBoard.getResult().getNoOfAppointments() + " Future Appointments");
                    msgs_cnt.setText(getDashBoardViewModel.dashBoard.getResult().getNoOfUnreadMessages() + " Unread Messages");
                } else {
                    initDoctorView();
                }

                if(getDashBoardViewModel.dashBoard.getResult().getAppointments().size() == 0) {
                    upcoming_appt.setVisibility(View.GONE);
                    book_appointment_banner.setVisibility(View.VISIBLE);
                } else {
                    upcoming_appt.setVisibility(View.VISIBLE);
                    book_appointment_banner.setVisibility(View.GONE);
                    updateData();
                }
            }
        });
    }
}