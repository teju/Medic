package com.moguls.medic.ui.fragments.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.ui.fragments.consultation.ConsultationFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.doctor.DoctorPatientListFragment;
import com.moguls.medic.ui.fragments.doctor.DoctorProfileHospitalListFragment;
import com.moguls.medic.ui.fragments.me.DoctorProfileTabbedFragment;


public class DoctorMeFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout update;
    private LinearLayout ll_consulation_fee,ll_patients,ll_hospitals,llprofile,ll_logout;

    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor_me, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackButtonToolbarStyleOne(v);
        llprofile = (LinearLayout) v.findViewById(R.id.llprofile);
        ll_patients = (LinearLayout) v.findViewById(R.id.ll_patients);
        ll_hospitals = (LinearLayout) v.findViewById(R.id.ll_hospitals);
        ll_logout = (LinearLayout) v.findViewById(R.id.ll_logout);
        ll_consulation_fee = (LinearLayout) v.findViewById(R.id.ll_consulation_fee);
        llprofile.setOnClickListener(this);
        ll_patients.setOnClickListener(this);
        ll_consulation_fee.setOnClickListener(this);
        ll_hospitals.setOnClickListener(this);
        ll_logout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llprofile :
                DoctorProfileTabbedFragment doctorProfileTabbedFragment = new DoctorProfileTabbedFragment();
                home().setFragment(doctorProfileTabbedFragment);
                break;
            case R.id.ll_patients :
                home().setFragment(new DoctorPatientListFragment());
                break;
            case R.id.ll_hospitals :
                    home().setFragment(new DoctorProfileHospitalListFragment());
                    break;
            case R.id.ll_consulation_fee :
                    home().setFragment(new ConsultationFragment());
                    break;
            case R.id.ll_logout :
                showNotifyDialog("",
                        "Are you sure you want to logout ?", "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {
                                home().clearFragment();
                                SharedPreference.setBoolean(getActivity(),SharedPreference.isLoggedIn,false);
                                home().setFragment(new LoginFragment());
                            }
                        }));

                break;

        }
    }
    @Override
    public void onBackTriggered() {
        super.onBackTriggered();
        bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }
}