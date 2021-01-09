package com.moguls.medic.ui.fragments.me;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.model.doctorProfileDetails.Personnel;
import com.moguls.medic.model.doctorProfileDetails.Result;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GeDoctorProdileDetailsViewModel;
import com.moguls.medic.webservices.GetProfilePatientViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DoctorProfileUpdateFragment extends BaseFragment implements View.OnClickListener {

    private TextView dob;
    final Calendar myCalendar = Calendar.getInstance();
    private TextView gender;
    private Result profileInit;
    private EditText phone_number,edt_email_id,edt_exp_yrs,emergency_contact,location,edt_one,edt_two;

    public void setProfileInit(Result profileInit) {
        this.profileInit = profileInit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor_profile_update, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dob = (TextView) v.findViewById(R.id.dob);
        phone_number = (EditText) v.findViewById(R.id.phone_number);
        edt_email_id = (EditText) v.findViewById(R.id.edt_email_id);
        edt_exp_yrs = (EditText) v.findViewById(R.id.edt_exp_yrs);
        edt_one = (EditText) v.findViewById(R.id.edt_one);
        edt_two = (EditText) v.findViewById(R.id.edt_two);
        emergency_contact = (EditText) v.findViewById(R.id.emergency_contact);
        location = (EditText) v.findViewById(R.id.location);
        gender = (TextView) v.findViewById(R.id.gender);
        gender.setOnClickListener(this);
        dob.setOnClickListener(this);
        setData();
    }

    public void setData(){
        Personnel personnel = profileInit.getPersonnel();
        if(personnel.getMobileNo() != null) {
            phone_number.setText(personnel.getMobileNo());
        }
        if(personnel.getEmailID() != null) {
            edt_email_id.setText(personnel.getEmailID());
        }
        if(personnel.getPracticingFrom() != null) {
            edt_exp_yrs.setText(personnel.getPracticingFrom());
        }
        if(personnel.getFirstName() != null) {
            edt_one.setText(personnel.getFirstName());
        }
        if(personnel.getLastName() != null) {
            edt_two.setText(personnel.getLastName());
        }
        if(personnel.getEmergencyContactNo() != null) {
            emergency_contact.setText(personnel.getEmergencyContactNo());
        }
        if(personnel.getLocation() != null) {
            location.setText(personnel.getLocation());
        }
        if(personnel.getIsMale()) {
            gender.setText("Male");
        } else {
            gender.setText("Female");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dob :
                new DatePickerDialog(getActivity(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.gender :
                showPopUpMenu(getActivity(), gender, R.menu.gender_popup, new PopUpListener() {
                    @Override
                    public void onButtonClicked(String selected) {
                        gender.setText(selected);
                    }
                });
                break;
        }
    }



}