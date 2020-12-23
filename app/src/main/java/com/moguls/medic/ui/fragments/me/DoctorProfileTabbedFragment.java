package com.moguls.medic.ui.fragments.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.moguls.medic.R;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.ViewPagerAdapter;


public class DoctorProfileTabbedFragment extends BaseFragment implements View.OnClickListener{

    private TabLayout tabLayout;
    private TextView header_title;
    private ViewPager viewPager;

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
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        header_title = (TextView)v.findViewById(R.id.header_title);
        header_title.setText("Your Profile");
        addTabs();
    }

    private void addTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new DoctorProfileUpdateFragment(), "Personal");
        adapter.addFrag(new DoctorMedicalTabFragment(), "Medical");
        adapter.addFrag(new DoctorIDProofTabFragment(), "ID Proof");
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
}