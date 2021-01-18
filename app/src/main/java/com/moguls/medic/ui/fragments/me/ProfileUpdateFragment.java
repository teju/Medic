package com.moguls.medic.ui.fragments.me;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.callback.OtpListener;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.model.getProfile.Result;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetGetRelationsViewModel;
import com.moguls.medic.webservices.GetProfilePatientViewModel;
import com.moguls.medic.webservices.PostDeletePatientViewModel;
import com.moguls.medic.webservices.PostUpdateProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.moguls.medic.etc.Helper.loadImage;


public class ProfileUpdateFragment extends BaseFragment implements View.OnClickListener {
    private static final int PHOTO_IMAGE = 1002;
    private RecyclerView recyclerView;
    private LinearLayout ll_mobile_number,ll_email,ll_gender,ll_location,ll_status,ll_add,profile_details,ll_relation;
    private Button btn_delete;
    private EditText edt_two,edt_one;
    private TextView update;
    private TextView header_title;
    private TextView dob;
    final Calendar myCalendar = Calendar.getInstance();
    private TextView gender,marital_status,blood_group,add_details,add_more,relation;
    private EditText phone_number;
    private EditText edt_email_id,edtWeight,edt_height_ft,location,edt_height_in;
    private ImageView profile_pic;
    private PostUpdateProfileViewModel postUpdateProfileViewModel;
    private GetGetRelationsViewModel getRelationsViewModel;
    private GetProfilePatientViewModel getProfilePatientViewModel;
    private PostDeletePatientViewModel deletePatientViewModel;
    private Uri cameraOutputUri;

    private LoadingCompound ld;
    private String real_Path = "";

    public void setIsprofile(boolean isprofile) {
        this.isprofile = isprofile;
    }

    boolean isprofile = true;

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String patient_id = "";

    boolean isAdd = false;
    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_update, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setObservers();

        ll_mobile_number = (LinearLayout)v.findViewById(R.id.ll_mobile_number);
        ll_email = (LinearLayout)v.findViewById(R.id.ll_email);
        ll_gender = (LinearLayout)v.findViewById(R.id.ll_gender);
        ll_location = (LinearLayout)v.findViewById(R.id.ll_location);
        ll_status = (LinearLayout)v.findViewById(R.id.ll_status);
        profile_details = (LinearLayout)v.findViewById(R.id.profile_details);
        ll_relation = (LinearLayout)v.findViewById(R.id.ll_relation);
        ll_add = (LinearLayout)v.findViewById(R.id.ll_add);
        btn_delete = (Button)v.findViewById(R.id.btn_delete);
        edt_two = (EditText)v.findViewById(R.id.edt_two);
        edt_one = (EditText)v.findViewById(R.id.edt_one);
        phone_number = (EditText)v.findViewById(R.id.phone_number);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        update = (TextView)v.findViewById(R.id.update);
        relation = (TextView)v.findViewById(R.id.relation);
        dob = (TextView) v.findViewById(R.id.dob);
        gender = (TextView) v.findViewById(R.id.gender);
        marital_status = (TextView) v.findViewById(R.id.marital_status);
        blood_group = (TextView) v.findViewById(R.id.blood_group);
        add_details = (TextView) v.findViewById(R.id.add_details);
        add_more = (TextView) v.findViewById(R.id.add_more);
        header_title = (TextView)v.findViewById(R.id.header_title);
        edt_email_id = (EditText)v.findViewById(R.id.edt_email_id);
        location = (EditText)v.findViewById(R.id.location);
        edtWeight = (EditText)v.findViewById(R.id.edtWeight);
        edt_height_ft = (EditText)v.findViewById(R.id.edt_height_ft);
        edt_height_in = (EditText)v.findViewById(R.id.edt_height_in);
        profile_pic = (ImageView)v.findViewById(R.id.profile_pic);
        update.setVisibility(View.VISIBLE);
        update.setOnClickListener(this);
        ll_relation.setOnClickListener(this);
        profile_pic.setOnClickListener(this);

        setListners();
        initContact();
        getRelationsViewModel.loadData();

    }
    public void updateData() {
        Result getprofileRes = getProfilePatientViewModel.getProfile.getResult();
        edt_one.setText(getprofileRes.getFirstName());
        header_title.setText(getprofileRes.getFirstName());
        if(isprofile) {
            phone_number.setText(getprofileRes.getMobileNo());
            edt_two.setText(getprofileRes.getLastName());
        } else {
            edt_two.setText(getprofileRes.getMobileNo());
        }
        if(getprofileRes.getEmailID() != null) {
            edt_email_id.setText(getprofileRes.getEmailID());
        }
        if(getprofileRes.getDateOfBirth() != null) {
            dob.setText(getprofileRes.getDateOfBirth());
        }
        if(getprofileRes.getLocation() != null) {
            location.setText(getprofileRes.getLocation());
        }
        if(getprofileRes.getRelationID() != null) {
            for (com.moguls.medic.model.getrelations.Result result: getRelationsViewModel.getRelations.getResult()) {
                if(getprofileRes.getRelationID().equalsIgnoreCase(result.getID())) {
                    relation.setText(result.getName());

                }
            }
        }
        if(getprofileRes.getIsMale() != null) {
            if(getprofileRes.getIsMale() == "false") {
                gender.setText("Female");
            } else {
                gender.setText("Male");
            }
        }
        if(getprofileRes.getBloodGroup() != null) {
            blood_group.setText(getprofileRes.getBloodGroup());
        }
        if(getprofileRes.getHeight() != null) {
            String[] height = getprofileRes.getHeight().split("\\.");
            edt_height_ft.setText(height[0]);
            if(height.length > 1) {
                edt_height_in.setText(height[1]);
            }
        }
        if(getprofileRes.getWeight() != null) {
            edtWeight.setText(getprofileRes.getWeight());
        }
        loadImage(getActivity(),getprofileRes.getPhotoUrl(),R.drawable.doctor_profile_pic_default,profile_pic);

    }

    public void setListners() {
        setBackButtonToolbarStyleOne(v);
        dob.setOnClickListener(this);
        gender.setOnClickListener(this);
        marital_status.setOnClickListener(this);
        blood_group.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        add_details.setOnClickListener(this);
        add_more.setOnClickListener(this);
        phone_number.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    public void setObservers() {
        setDeleteProfileAPIObserver();
        setUpdateProfileAPIObserver();
        setGetRelationsAPIObserver();
        setProfileAPIObserver();
    }

    public void initContact() {
        if(!isprofile || isAdd) {
            ll_mobile_number.setVisibility(View.GONE);
            ll_location.setVisibility(View.GONE);
            ll_status.setVisibility(View.GONE);
            ll_add.setVisibility(View.GONE);
            ll_relation.setVisibility(View.VISIBLE);
            if(!isAdd){
                btn_delete.setVisibility(View.VISIBLE);
            }
            edt_one.setHint("Name");
            edt_two.setHint("Mobile Number");
            //edt_two.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.account_box_multiple_outline, 0);
        }
        if(isAdd) {
            edt_email_id.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            profile_pic.setImageDrawable(getActivity().getDrawable(R.drawable.account_circle));
            header_title.setText("Add New");
            update.setText("Add");
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_pic:
                pickImage();
                break;
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
            case R.id.marital_status :
                showPopUpMenu(getActivity(), marital_status, R.menu.marital_popup, new PopUpListener() {
                    @Override
                    public void onButtonClicked(String selected) {
                        marital_status.setText(selected);
                    }
                });
                break;
            case R.id.blood_group :
                showPopUpMenu(getActivity(), blood_group, R.menu.bloodgroup_popup, new PopUpListener() {
                    @Override
                    public void onButtonClicked(String selected) {
                        blood_group.setText(selected);
                    }
                });
                break;
            case R.id.ll_relation :
                showPopUpMenu(getActivity(), relation, R.menu.relation_popup,getRelationsViewModel.getRelations.getResult(), new PopUpListener() {
                    @Override
                    public void onButtonClicked(String selected) {
                        relation.setText(selected);
                    }
                });
                break;
            case R.id.add_details:
                ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();
                profileUpdateFragment.setAdd(true);
                home().setFragment(profileUpdateFragment);
                break;
            case R.id.add_more:
                profileUpdateFragment = new ProfileUpdateFragment();
                profileUpdateFragment.setAdd(true);
                home().setFragment(profileUpdateFragment);
                break;
            case R.id.btn_delete:
                showNotifyDialog("Are you sure you want to delete this contact?",
                        "", "OK",
                        "Cancel", (NotifyListener)(new NotifyListener() {
                            public void onButtonClicked(int which) {
                                if(which == NotifyDialogFragment.BUTTON_POSITIVE) {
                                    deletePatientViewModel.loadData(patient_id);
                                }
                            }
                    }));
                break;
            case R.id.phone_number :

                break;
            case R.id.update :
                if(validate()) {
                    String mobileNumber = edt_two.getText().toString();
                    boolean isMale;
                    if(isprofile) {
                        mobileNumber = phone_number.getText().toString();
                    }
                    if(gender.getText().toString().equalsIgnoreCase("male")) {
                        isMale = true;
                    } else {
                        isMale = false;
                    }
                    String ID = "";
                    if(getProfilePatientViewModel.getProfile != null) {
                        ID = getProfilePatientViewModel.getProfile.getResult().getID();
                    }
                    String LastName = "";
                    if(isprofile) {
                        LastName = edt_two.getText().toString();
                    }
                    postUpdateProfileViewModel.loadData(ID,
                            edt_one.getText().toString(),mobileNumber,dob.getText().toString(),
                            blood_group.getText().toString(),edtWeight.getText().toString(),getHeightInCMS(),
                            edt_email_id.getText().toString(),"",
                            getRelationID(),marital_status.getText().toString(),location.getText().toString(),real_Path,isMale,LastName);
                }
        }
    }
    public String getHeightInCMS() {
        String centimeter = edt_height_ft.getText().toString()+"."+edt_height_in.getText().toString();
        return centimeter;
    }
    public String getRelationID() {
        for (com.moguls.medic.model.getrelations.Result result: getRelationsViewModel.getRelations.getResult()) {
            if(relation.getText().toString().equalsIgnoreCase(result.getName())) {
                return result.getID();
            }
        }
        return "";
    }
    public void setUpdateProfileAPIObserver() {
        postUpdateProfileViewModel = ViewModelProviders.of(this).get(PostUpdateProfileViewModel.class);
        postUpdateProfileViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postUpdateProfileViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postUpdateProfileViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postUpdateProfileViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postUpdateProfileViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
               home().proceedDoOnBackPressed();

            }
        });
    }

    public void setGetRelationsAPIObserver() {
        getRelationsViewModel = ViewModelProviders.of(this).get(GetGetRelationsViewModel.class);
        getRelationsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getRelationsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getRelationsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getRelationsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getRelationsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(!patient_id.isEmpty()) {
                    getProfilePatientViewModel.loadData(patient_id);
                }

            }
        });
    }
    public void setProfileAPIObserver() {
        getProfilePatientViewModel = ViewModelProviders.of(this).get(GetProfilePatientViewModel.class);
        getProfilePatientViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getProfilePatientViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getProfilePatientViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getProfilePatientViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getProfilePatientViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateData();

            }
        });
    }
    public void setDeleteProfileAPIObserver() {
        deletePatientViewModel = ViewModelProviders.of(this).get(PostDeletePatientViewModel.class);
        deletePatientViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        deletePatientViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        deletePatientViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        deletePatientViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        deletePatientViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                showNotifyDialog("",
                        "Successfully deleted", "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {
                                home().proceedDoOnBackPressed();
                            }
                        }));

            }
        });
    }

    public boolean validate() {
        if(edt_one.getText().toString().isEmpty()) {
            edt_one.setError("Invalid Name.");
            edt_one.requestFocus();
            return false;
        }else if(isprofile && edt_two.getText().toString().isEmpty()) {
            edt_two.setError("Invalid LastName.");
            edt_two.requestFocus();
            return false;
        }
        else if(isprofile && !Helper.isValidMobile(phone_number.getText().toString())) {
            phone_number.setError("Invalid mobile no.");
            phone_number.requestFocus();
            return false;
        }  else if(!Helper.isValidEmail(edt_email_id.getText().toString())) {
            edt_email_id.setError("Invalid Email ID");
            edt_email_id.requestFocus();
            return false;
        } else if(dob.getText().toString().isEmpty()) {
            dob.setError("Invalid Date of birth");
            dob.requestFocus();
            return false;
        } else if(edt_height_ft.getText().toString().isEmpty()) {
            edt_height_ft.setError("Invalid Height feet");
            edt_height_ft.requestFocus();
            return false;
        } else if(edt_height_in.getText().toString().isEmpty()) {
            edt_height_in.setError("Invalid Height inches");
            edt_height_in.requestFocus();
            return false;
        } else if(edtWeight.getText().toString().isEmpty()) {
            edtWeight.setError("Invalid Weight");
            edtWeight.requestFocus();
            return false;
        }  else if(!isprofile && relation.getText().toString().isEmpty()) {
            relation.setError("Invalid Relationship");
            relation.requestFocus();
            return false;
        } else if(!isprofile && !Helper.isValidMobile(edt_two.getText().toString())) {
            edt_two.setError("Invalid mobile no.");
            edt_two.requestFocus();
            return false;
        } else {
            dob.setError(null);
            relation.setError(null);
            return true;
        }
    }
}