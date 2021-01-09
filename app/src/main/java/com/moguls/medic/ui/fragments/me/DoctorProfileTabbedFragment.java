package com.moguls.medic.ui.fragments.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.ViewPagerAdapter;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GeDoctorProdileDetailsViewModel;


public class DoctorProfileTabbedFragment extends BaseFragment implements View.OnClickListener{

    private TabLayout tabLayout;
    private TextView header_title,percentage;
    private ViewPager viewPager;
    private LoadingCompound ld;
    private GeDoctorProdileDetailsViewModel getDoctorProdileDetailsViewModel;
    public int percentageVal;
    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor_profile_viewpager, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackButtonToolbarStyleOne(v);
        setProfileAPIObserver();

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        header_title = (TextView)v.findViewById(R.id.header_title);
        percentage = (TextView)v.findViewById(R.id.percentage);
        header_title.setText("Your Profile");
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        getDoctorProdileDetailsViewModel.loadData();
        percentage.setText(percentageVal+"% Completed");


    }

    private void addTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        DoctorProfileUpdateFragment doctorProfileUpdateFragment = new DoctorProfileUpdateFragment();
        DoctorMedicalTabFragment doctorMedicalTabFragment = new DoctorMedicalTabFragment();
        DoctorIDProofTabFragment doctorIDProofTabFragment = new DoctorIDProofTabFragment();
        doctorProfileUpdateFragment.setProfileInit(getDoctorProdileDetailsViewModel.getdDoctorsProfileDetails.getResult());
        doctorMedicalTabFragment.setProfileInit(getDoctorProdileDetailsViewModel.getdDoctorsProfileDetails.getResult());
        doctorIDProofTabFragment.setProfileInit(getDoctorProdileDetailsViewModel.getdDoctorsProfileDetails.getResult());
        adapter.addFrag(doctorProfileUpdateFragment, "Personal");
        adapter.addFrag(doctorMedicalTabFragment, "Medical");
        adapter.addFrag(doctorIDProofTabFragment, "ID Proof");
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
    public void setProfileAPIObserver() {
        getDoctorProdileDetailsViewModel = ViewModelProviders.of(this).get(GeDoctorProdileDetailsViewModel.class);
        getDoctorProdileDetailsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getDoctorProdileDetailsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getDoctorProdileDetailsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getDoctorProdileDetailsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getDoctorProdileDetailsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                addTabs();
            }
        });
    }

}