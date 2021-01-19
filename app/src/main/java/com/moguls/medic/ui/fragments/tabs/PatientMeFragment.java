package com.moguls.medic.ui.fragments.tabs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.moguls.medic.etc.APIs;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.adapters.PatientBookingAdapter;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.me.ProfileUpdateFragment;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetProfileViewModel;

import static com.moguls.medic.etc.Helper.loadImage;


public class PatientMeFragment extends BaseFragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private RelativeLayout update;
    private LinearLayout ll_doctor,ll_appointment,ll_logout;
    private ImageView btnadd;
    final Handler handler = new Handler();
    private ImageView settings;
    private LoadingCompound ld;
    private GetProfileViewModel getProfileViewModel;
    private TextView no_patients,name,experience,location,blood_group,height,weight,profile_completed;
    private ProgressBar progress;
    private ImageView logo;

    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_patient_me, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObservers();
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_patient);
        update = (RelativeLayout) v.findViewById(R.id.update);
        btnadd = (ImageView) v.findViewById(R.id.btnadd);
        settings = (ImageView) v.findViewById(R.id.settings);
        logo = (ImageView) v.findViewById(R.id.logo);
        ll_appointment = (LinearLayout) v.findViewById(R.id.ll_appointment);
        ll_logout = (LinearLayout) v.findViewById(R.id.ll_logout);
        ll_doctor = (LinearLayout) v.findViewById(R.id.ll_doctor);
        no_patients = (TextView) v.findViewById(R.id.no_patients);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        name = (TextView) v.findViewById(R.id.name);
        experience = (TextView) v.findViewById(R.id.experience);
        location = (TextView) v.findViewById(R.id.location);
        weight = (TextView) v.findViewById(R.id.weight);
        height = (TextView) v.findViewById(R.id.height);
        profile_completed = (TextView) v.findViewById(R.id.profile_completed);
        blood_group = (TextView) v.findViewById(R.id.blood_group);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        setListners();
        getProfileViewModel.loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            getProfileViewModel.loadData();
        }
    }

    public void setListners() {
        setBackButtonToolbarStyleOne(v);
        update.setOnClickListener(this);
        ll_doctor.setOnClickListener(this);
        btnadd.setOnClickListener(this);
        ll_appointment.setOnClickListener(this);
        settings.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setObservers() {
        setProfileAPIObserver();

    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        PatientBookingAdapter bookingpatientadapter = new PatientBookingAdapter(getActivity(), getProfileViewModel.getProfile.getResult().getMembers(), new PatientBookingAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();
                profileUpdateFragment.setIsprofile(false);
                profileUpdateFragment.setPatient_id(getProfileViewModel.getProfile.getResult().getMembers().get(position).getID());
                home().setFragment(profileUpdateFragment);
            }
        });

        recyclerView.setAdapter(bookingpatientadapter);
    }

    @Override
    public void onClick(View v) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()){
                    case R.id.update :
                        ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();
                        profileUpdateFragment.setIsprofile(true);
                        profileUpdateFragment.setPatient_id(getProfileViewModel.getProfile.getResult().getID());
                        home().setFragment(profileUpdateFragment);

                        break;
                    case R.id.settings :

                        break;
                    case R.id.ll_appointment :
                        PatientMyAppointmentListFragment patientMyAppointmentListFragment = new PatientMyAppointmentListFragment();
                        patientMyAppointmentListFragment.setBottomNavigation(bottomNavigation);
                        home().setFragmentInFragment(
                                R.id.mainLayoutFragment, patientMyAppointmentListFragment,
                                "MAIN_TAB","THIRD_TAB");
                        bottomNavigation.getMenu().findItem(R.id.navigation_appts).setChecked(true);
                        break;
                    case R.id.ll_doctor :
                        DoctorsListFragment doctorsListFragment = new DoctorsListFragment();
                        doctorsListFragment.setBottomNavigation(bottomNavigation);
                        home().setFragmentInFragment(
                                R.id.mainLayoutFragment, doctorsListFragment,
                                "MAIN_TAB","FOURTH_TAB");
                        bottomNavigation.getMenu().findItem(R.id.navigation_doctors).setChecked(true);
                        break;
                    case R.id.btnadd :
                        profileUpdateFragment = new ProfileUpdateFragment();
                        profileUpdateFragment.setAdd(true);
                        profileUpdateFragment.setIsprofile(false);
                        home().setFragment(profileUpdateFragment);
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
        },500);
    }
    public void setProfileAPIObserver() {
        getProfileViewModel = ViewModelProviders.of(this).get(GetProfileViewModel.class);
        getProfileViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getProfileViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getProfileViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getProfileViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getProfileViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
                initData();
                if( getProfileViewModel.getProfile.getResult().getMembers().size() != 0) {
                    no_patients.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    no_patients.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
    }

    private void initData() {
        name.setText(getProfileViewModel.getProfile.getResult().getName());
        experience.setText("("+getProfileViewModel.getProfile.getResult().getAge()+") yrs");
        blood_group.setText(getProfileViewModel.getProfile.getResult().getBloodGroup());
        if (getProfileViewModel.getProfile.getResult().getHeight() != null) {
            String[] heightArr = getProfileViewModel.getProfile.getResult().getHeight().split("\\.");
            height.setText(heightArr[0]+"'");
            if(heightArr.length > 1) {
                height.append(heightArr[1]+"\"");
            }
        }
        if(getProfileViewModel.getProfile.getResult().getWeight() != null) {
            double weight_val = Double.parseDouble(getProfileViewModel.getProfile.getResult().getWeight());
            weight.setText((int) weight_val + " Kgs");
        }
        if(getProfileViewModel.getProfile.getResult().getLocation() != null) {
            location.setText(getProfileViewModel.getProfile.getResult().getLocation());
        }
        loadImage(getActivity(), getProfileViewModel.getProfile.getResult().getPhotoUrl(),
                R.drawable.doctor_profile_pic_default,logo);
        progress.setProgress(Integer.valueOf(getProfileViewModel.getProfile.getResult().getCompletedPercentage()));
        profile_completed.setText("Completed Profile "+getProfileViewModel.getProfile.getResult().getCompletedPercentage()+"%");
    }
    @Override
    public void onBackTriggered() {
        super.onBackTriggered();
        bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }
}