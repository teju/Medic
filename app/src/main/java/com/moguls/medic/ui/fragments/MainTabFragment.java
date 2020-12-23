package com.moguls.medic.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moguls.medic.R;

import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.tabs.PatientMyAppointmentListFragment;
import com.moguls.medic.ui.fragments.tabs.ChatListFragment;
import com.moguls.medic.ui.fragments.tabs.DoctorsListFragment;
import com.moguls.medic.ui.fragments.tabs.DoctorMeFragment;
import com.moguls.medic.ui.fragments.tabs.HomeFragment;
import com.moguls.medic.ui.fragments.tabs.PatientMeFragment;

public class MainTabFragment extends BaseFragment {

    public BottomNavigationView bottomNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_tab_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    public void initUI() {
        bottomNavigation = (BottomNavigationView)v.findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setBottomNavigation(bottomNavigation);
        home().setOrShowExistingFragmentByTag(
                R.id.mainLayoutFragment, "FIRST_TAB",
                "MAIN_TAB", homeFragment, Helper.listFragmentsMainTab());
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, homeFragment,
                                    "MAIN_TAB","FIRST_TAB");
                            return true;
                        case R.id.navigation_chats:
                            ChatListFragment chatListFragment = new ChatListFragment();
                            chatListFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, chatListFragment,
                                    "MAIN_TAB","SECOND_TAB");
                            return true;
                        case R.id.navigation_appts:
                            PatientMyAppointmentListFragment patientMyAppointmentListFragment = new PatientMyAppointmentListFragment();
                            patientMyAppointmentListFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, patientMyAppointmentListFragment,
                                        "MAIN_TAB", "THIRD_TAB");
                            return true;
                        case R.id.navigation_doctors:
                            DoctorsListFragment doctorsListFragment = new DoctorsListFragment();
                            doctorsListFragment.setBottomNavigation(bottomNavigation);
                            home().setFragmentInFragment(
                                    R.id.mainLayoutFragment, doctorsListFragment,
                                    "MAIN_TAB", "FOURTH_TAB");
                            return true;
                        case R.id.navigation_profile:
                            if(SharedPreference.getBoolean(getActivity(),SharedPreference.isDOCTOR)) {
                                DoctorMeFragment doctorMeFragment = new DoctorMeFragment();
                                doctorMeFragment.setBottomNavigation(bottomNavigation);
                                home().setFragmentInFragment(
                                        R.id.mainLayoutFragment, doctorMeFragment,
                                        "MAIN_TAB","FOURTH_TAB");
                            } else {
                                PatientMeFragment patientMeFragment = new PatientMeFragment();
                                patientMeFragment.setBottomNavigation(bottomNavigation);
                                home().setFragmentInFragment(
                                        R.id.mainLayoutFragment, patientMeFragment,
                                        "MAIN_TAB","FOURTH_TAB");
                            }

                            return true;
                    }
                    return false;
                }
            };
    @Override
    public void onBackTriggered() {
        //home().resetAndExit();
    }
}