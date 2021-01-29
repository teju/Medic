package com.moguls.medic.ui.fragments.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.model.consultations.ConsultationPlans;
import com.moguls.medic.model.consultations.Result;
import com.moguls.medic.ui.adapters.HospitalSpinnerAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GeConsultationsViewModel;
import com.moguls.medic.webservices.GetHospitalsViewModel;
import com.moguls.medic.webservices.PostConsultationsAddViewModel;


public class AddConsultationFragment extends BaseFragment implements View.OnClickListener {

    public Result consultationsobj;
    private TextView header_title,tv_visitType,hospital_name;
    private RelativeLayout rl_visit_type,rl_doctor_list;
    private Spinner sp_visit_type,sp_hospital;
    String[] visit_type = {"Physical Visit"};
    private Button create_plan;
    private PostConsultationsAddViewModel postConsultationsAddViewModel;
    private GetHospitalsViewModel hospitalsViewModel;
    private LoadingCompound ld;
    String hospitalID = "";
    private EditText consultationFee,slots_duration;
    private EditText advance_booking_days;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_consultation_plan, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        header_title = (TextView)v.findViewById(R.id.header_title);
        consultationFee = (EditText) v.findViewById(R.id.consultationFee);
        slots_duration = (EditText) v.findViewById(R.id.slots_duration);
        advance_booking_days = (EditText) v.findViewById(R.id.advance_booking_days);
        tv_visitType = (TextView)v.findViewById(R.id.tv_visitType);
        hospital_name = (TextView)v.findViewById(R.id.hospital_name);
        rl_visit_type = (RelativeLayout)v.findViewById(R.id.rl_visit_type);
        rl_doctor_list = (RelativeLayout)v.findViewById(R.id.rl_doctor_list);
        create_plan = (Button)v.findViewById(R.id.create_plan);
        sp_visit_type = (Spinner)v.findViewById(R.id.sp_visit_type);
        sp_hospital = (Spinner)v.findViewById(R.id.sp_hospital);
        header_title.setText("Edit consultation Plan");
        rl_visit_type.setOnClickListener(this);
        rl_doctor_list.setOnClickListener(this);
        create_plan.setOnClickListener(this);
        setConsultationAddAPIObserver();
        setGetHospitalAPIObserver();
        setBackButtonToolbarStyleOne(v);
        visitTypeAdapter();
        hospitalsViewModel.loadData();

    }

    public void setData() {
        consultationFee.setText("Rs "+consultationsobj.getFee());
        slots_duration.setText(consultationsobj.getTimeslot()+" mins");
        advance_booking_days.setText(consultationsobj.getAdvanceBookingDays()+" days");
        for (int i= 0;i<visit_type.length;i++) {
            if(visit_type[i].equalsIgnoreCase(consultationsobj.getConsultationType().getName())) {
                sp_visit_type.setSelection(i);
            }
        }
        for(int i=0;i<hospitalsViewModel.hospitalView.getResult().size();i++) {
            if(hospitalsViewModel.hospitalView.getResult().get(i).getID().equalsIgnoreCase(consultationsobj.getHospitalID())) {
                hospitalID = hospitalsViewModel.hospitalView.getResult().get(i).getID();
                hospital_name.setText(hospitalsViewModel.hospitalView.getResult().get(i).getName());
                hospital_name.setTextColor(getActivity().getResources().getColor(R.color.black));
            }
        }
    }

    public void visitTypeAdapter() {
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,visit_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_visit_type.setAdapter(aa);
        sp_visit_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void hospitalAdapter() {
        HospitalSpinnerAdapter aa = new HospitalSpinnerAdapter(getActivity(), new HospitalSpinnerAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                hospitalID = hospitalsViewModel.hospitalView.getResult().get(position).getID();
                hospital_name.setText(hospitalsViewModel.hospitalView.getResult().get(position).getName());
                hospital_name.setTextColor(getActivity().getResources().getColor(R.color.black));
                hideSpinnerDropDown(sp_hospital);
            }
        });
        aa.setResults(hospitalsViewModel.hospitalView.getResult());
        sp_hospital.setAdapter(aa);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_doctor_list:
                sp_hospital.performClick();
                break;
            case R.id.create_plan :
                if(validate()) {
                    postConsultationsAddViewModel.loadData(hospitalID,
                            slots_duration.getText().toString().replaceAll("mins","").trim(),
                            consultationFee.getText().toString().replaceAll("Rs","").trim(),
                            advance_booking_days.getText().toString().replaceAll("days","").trim(),
                            sp_visit_type.getSelectedItem().toString());
                }
                break;
        }
    }

    public boolean validate() {
        if(hospitalID.isEmpty()) {
            showNotifyDialog("",
                    "Select Hospital", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));
            return false;
        } else if( sp_visit_type.getSelectedItem().toString().isEmpty()) {
            showNotifyDialog("",
                    "Select Consultation Type", "OK",
                    "", (NotifyListener) (new NotifyListener() {
                        public void onButtonClicked(int which) {

                        }
                    }));

            return false;
        } else if(consultationFee.getText().toString().isEmpty()) {
            consultationFee.setError("Fees cannot be blank");
            consultationFee.setFocusable(true);
            return false;
        } else if(slots_duration.getText().toString().isEmpty()) {
            slots_duration.setError("Slot duration cannot be blank");
            slots_duration.setFocusable(true);
            return false;
        } else {
            return true;
        }
    }

    public void setConsultationAddAPIObserver() {
        postConsultationsAddViewModel = ViewModelProviders.of(this).get(PostConsultationsAddViewModel.class);
        postConsultationsAddViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postConsultationsAddViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postConsultationsAddViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postConsultationsAddViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postConsultationsAddViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                home().proceedDoOnBackPressed();
            }
        });
    }
    public void setGetHospitalAPIObserver() {
        hospitalsViewModel = ViewModelProviders.of(this).get(GetHospitalsViewModel.class);
        hospitalsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        hospitalsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());

            }
        });
        hospitalsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        hospitalsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        hospitalsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                hospitalAdapter();
                if(consultationsobj != null) {
                    setData();
                }
            }
        });
    }

}