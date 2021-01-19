package com.moguls.medic.ui.fragments.me;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.moguls.medic.R;
import com.moguls.medic.callback.DoctorSaveListener;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.etc.Helper;
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

    private static final int PHOTO_IMAGE = 101;
    private TextView dob;
    final Calendar myCalendar = Calendar.getInstance();
    private TextView gender;
    private Result profileInit;
    private EditText phone_number;
    private EditText edt_email_id;
    private TextView edt_exp_yrs;
    private EditText emergency_contact;
    private EditText location;
    private EditText edt_one;
    private EditText edt_two;
    private Button btn_save;
    DoctorSaveListener doctorSaveListener;
    boolean isDOB = false;
    private Uri cameraOutputUri;
    private String real_Path = "";
    private ImageView profile_pic;

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
        profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
        edt_email_id = (EditText) v.findViewById(R.id.edt_email_id);
        edt_exp_yrs = (TextView) v.findViewById(R.id.edt_exp_yrs);
        edt_one = (EditText) v.findViewById(R.id.edt_one);
        edt_two = (EditText) v.findViewById(R.id.edt_two);
        emergency_contact = (EditText) v.findViewById(R.id.emergency_contact);
        location = (EditText) v.findViewById(R.id.location);
        gender = (TextView) v.findViewById(R.id.gender);
        btn_save = (Button) v.findViewById(R.id.btn_save);
        gender.setOnClickListener(this);
        dob.setOnClickListener(this);
        edt_exp_yrs.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        profile_pic.setOnClickListener(this);
        setData();
    }
    public void pickImage() {
        cameraOutputUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Intent intent = Helper.getPickIntent(cameraOutputUri,getActivity());
        startActivityForResult(intent, PHOTO_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageuri = null;
        if(data != null) {
            imageuri = data.getData();// Get intent
        } else {
            imageuri = cameraOutputUri;
        }
        real_Path = Helper.getRealPathFromUri(getActivity(), imageuri);
        profile_pic.setImageURI(imageuri);
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
        if(isDOB) {
            dob.setText(sdf.format(myCalendar.getTime()));
        } else {
            edt_exp_yrs.setText(sdf.format(myCalendar.getTime()));
        }
    }
    public boolean validate() {
        if(edt_one.getText().toString().isEmpty()) {
            edt_one.setError("Enter your First Name");
            edt_one.requestFocus();
            return false;
        } else if(edt_two.getText().toString().isEmpty()) {
            edt_two.setError("Enter your Last Name");
            edt_two.requestFocus();
            return false;
        } else if(!Helper.isValidMobile(phone_number.getText().toString())) {
            phone_number.setError("Enter Valid Phone Number");
            phone_number.requestFocus();
            return false;
        } else if(!Helper.isValidEmail(edt_email_id.getText().toString())) {
            edt_email_id.setError("Enter Valid EmailID");
            edt_email_id.requestFocus();
            return false;
        } else if(edt_exp_yrs.getText().toString().isEmpty()) {
            edt_exp_yrs.setError("Enter Your Experience");
            edt_exp_yrs.requestFocus();
            return false;
        } else if(emergency_contact.getText().toString().isEmpty()) {
            emergency_contact.setError("Enter Your Emergency Contact");
            emergency_contact.requestFocus();
            return false;
        } else if(location.getText().toString().isEmpty()) {
            location.setError("Enter Your Location");
            location.requestFocus();
            return false;
        } else if(gender.getText().toString().isEmpty()) {
            gender.setError("Select your gender");
            gender.requestFocus();
            return false;
        } else if(dob.getText().toString().isEmpty()) {
            dob.setError("Select your DOB");
            dob.requestFocus();
            return false;
        } else if(dob.getText().toString().isEmpty()) {
            dob.setError("Select your DOB");
            dob.requestFocus();
            return false;
        }  else if(real_Path.isEmpty()) {
            showNotifyDialog("","Select your profile image",
                    "OK","",new NotifyListener() {

                @Override
                public void onButtonClicked(int which) {

                }
            });
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dob :
                new DatePickerDialog(getActivity(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                isDOB = true;
                break;
            case R.id.edt_exp_yrs :
                new DatePickerDialog(getActivity(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                isDOB = false;
                break;
            case R.id.gender :
                showPopUpMenu(getActivity(), gender, R.menu.gender_popup, new PopUpListener() {
                    @Override
                    public void onButtonClicked(String selected) {
                        gender.setText(selected);
                    }
                });
                break;
            case R.id.profile_pic:
                pickImage();
                break;
            case R.id.btn_save:
                if(validate()) {
                    Personnel personnel = profileInit.getPersonnel();
                    personnel.setFirstName(edt_one.getText().toString());
                    personnel.setLastName(edt_two.getText().toString());
                    personnel.setMobileNo(phone_number.getText().toString());
                    personnel.setEmailID(edt_email_id.getText().toString());
                    personnel.setPracticingFrom(edt_exp_yrs.getText().toString());
                    personnel.setEmergencyContactNo(emergency_contact.getText().toString());
                    personnel.setLocation(location.getText().toString());
                    personnel.setDateOfBirth(dob.getText().toString());
                    if(gender.getText().toString().equalsIgnoreCase("male")) {
                        personnel.setIsMale(true);
                    } else {
                        personnel.setIsMale(false);
                    }
                    personnel.setPhotoUrl(real_Path);
                    baseParams.setPersonnel(personnel);
                    doctorSaveListener.onButtonClicked();
                }
                break;
        }
    }

}