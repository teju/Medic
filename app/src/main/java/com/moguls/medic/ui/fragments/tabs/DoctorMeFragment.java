package com.moguls.medic.ui.fragments.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.model.doctorProfileSummary.Result;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.ui.fragments.consultation.ConsultationFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.doctor.DoctorPatientListFragment;
import com.moguls.medic.ui.fragments.doctor.DoctorProfileHospitalListFragment;
import com.moguls.medic.ui.fragments.me.DoctorProfileTabbedFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetDoctorProfileSummaryViewModel;
import com.moguls.medic.webservices.GetProfileViewModel;


public class DoctorMeFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout update;
    private LinearLayout ll_consulation_fee,ll_patients,ll_hospitals,llprofile,ll_logout;
    private GetDoctorProfileSummaryViewModel getDoctorProfileSummaryViewModel;
    private LoadingCompound ld;
    private TextView name,age,specialization,prodile_complete,education,gender,experience;
    private ProgressBar progress;

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
        progress = (ProgressBar) v.findViewById(R.id.progress);
        name = (TextView) v.findViewById(R.id.name);
        age = (TextView) v.findViewById(R.id.age);
        specialization = (TextView) v.findViewById(R.id.specialization);
        prodile_complete = (TextView) v.findViewById(R.id.prodile_complete);
        education = (TextView) v.findViewById(R.id.education);
        gender = (TextView) v.findViewById(R.id.gender);
        experience = (TextView) v.findViewById(R.id.experience);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        llprofile.setOnClickListener(this);
        ll_patients.setOnClickListener(this);
        ll_consulation_fee.setOnClickListener(this);
        ll_hospitals.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        setProfileAPIObserver();
        getDoctorProfileSummaryViewModel.loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            getDoctorProfileSummaryViewModel.loadData();
        }
    }

    public void setData() {
        Result result = getDoctorProfileSummaryViewModel.getDoctors.getResult();
        age.setText("("+result.getAge()+" yrs)");
        name.setText(result.getName());
        specialization.setText(result.getSpecializations());
        education.setText(result.getQualifications());
        experience.setText(result.getExperience()+" yrs");
        if(result.getIsMale()) {
            gender.setText("Male");
        } else {
            gender.setText("FeMale");
        }
        progress.setProgress(result.getCompletedPercentage());
        prodile_complete.setText("Completed Profile "+result.getCompletedPercentage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llprofile :
                if( getDoctorProfileSummaryViewModel.getDoctors != null) {
                    DoctorProfileTabbedFragment doctorProfileTabbedFragment = new DoctorProfileTabbedFragment();
                    doctorProfileTabbedFragment.percentageVal = getDoctorProfileSummaryViewModel.getDoctors.getResult().getCompletedPercentage();
                    home().setFragment(doctorProfileTabbedFragment);
                }
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
                        "Cancel", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if(which == NotifyDialogFragment.BUTTON_POSITIVE) {
                                    home().clearFragment();
                                    SharedPreference.setBoolean(getActivity(), SharedPreference.isLoggedIn, false);
                                    home().setFragment(new LoginFragment());
                                }
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

    public void setProfileAPIObserver() {
        getDoctorProfileSummaryViewModel = ViewModelProviders.of(this).get(GetDoctorProfileSummaryViewModel.class);
        getDoctorProfileSummaryViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getDoctorProfileSummaryViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getDoctorProfileSummaryViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getDoctorProfileSummaryViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getDoctorProfileSummaryViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setData();
            }
        });
    }
}