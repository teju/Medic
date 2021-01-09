package com.moguls.medic.ui.fragments.me;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moguls.medic.R;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.model.doctorProfileDetails.Result;
import com.moguls.medic.model.doctorProfileDetails.Specializations;
import com.moguls.medic.ui.settings.BaseFragment;

import java.util.List;


public class DoctorMedicalTabFragment extends BaseFragment implements View.OnClickListener {

    private Result profileInit;
    private TextView specialization,education;
    private EditText reg_council,reg_year,registration_no;

    public void setProfileInit(Result profileInit) {
        this.profileInit = profileInit;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_medical_tab, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reg_year = (EditText)v.findViewById(R.id.reg_year);
        registration_no = (EditText)v.findViewById(R.id.registration_no);
        reg_council = (EditText)v.findViewById(R.id.reg_council);
        reg_year = (EditText)v.findViewById(R.id.reg_year);
        specialization = (TextView)v.findViewById(R.id.specialization);
        education = (TextView)v.findViewById(R.id.education);
        specialization.setOnClickListener(this);
        education.setOnClickListener(this);
        setData();
    }
    public void setData() {
        reg_year.setText(profileInit.getMedical().getYear());
        reg_council.setText(profileInit.getMedical().getCouncil().getName());
        registration_no.setText(profileInit.getMedical().getNo());
        specialization.setText(profileInit.getPersonnel().getSpecializations().get(0).getName());
        education.setText(profileInit.getPersonnel().getQualifications().get(0).getName());
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.specialization :
                showPopUpMenu(profileInit.getPersonnel().getSpecializations(),specialization);
                break;
            case R.id.education :
                showPopUpMenu(profileInit.getPersonnel().getQualifications());
                break;
        }

    }
    public void showPopUpMenu(List<Specializations> arrayList, TextView specialization) {
        PopupMenu popup = new PopupMenu(getActivity(), specialization);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.relation_popup, popup.getMenu());
        for(Specializations s : arrayList) {
            popup.getMenu().add(Menu.NONE, 1, Menu.NONE, s.getName());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                specialization.setText(item.getTitle());
                return true;
            }
        });

        popup.show();//showing popup menu
    }
    public void showPopUpMenu(List<Specializations> arrayList) {
        PopupMenu popup = new PopupMenu(getActivity(), education);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.relation_popup, popup.getMenu());
        for(Specializations s : arrayList) {
            popup.getMenu().add(Menu.NONE, 1, Menu.NONE, s.getName());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                education.setText(item.getTitle());
                return true;
            }
        });

        popup.show();//showing popup menu
    }

}