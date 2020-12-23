package com.moguls.medic.ui.fragments.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moguls.medic.R;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.ui.adapters.HospitalSpinnerAdapter;
import com.moguls.medic.ui.settings.BaseFragment;


public class AddConsultationFragment extends BaseFragment implements View.OnClickListener {

    private TextView header_title,tv_visitType,hospital_name;
    private RelativeLayout rl_visit_type,rl_doctor_list;
    private Spinner sp_visit_type,sp_hospital;
    String[] visit_type = { "Physical Visit","Audio Consultation"};
    private Button create_plan;

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
        header_title = (TextView)v.findViewById(R.id.header_title);
        tv_visitType = (TextView)v.findViewById(R.id.tv_visitType);
        hospital_name = (TextView)v.findViewById(R.id.hospital_name);
        rl_visit_type = (RelativeLayout)v.findViewById(R.id.rl_visit_type);
        rl_doctor_list = (RelativeLayout)v.findViewById(R.id.rl_doctor_list);
        create_plan = (Button)v.findViewById(R.id.create_plan);
        sp_visit_type = (Spinner)v.findViewById(R.id.sp_visit_type);
        sp_hospital = (Spinner)v.findViewById(R.id.sp_hospital);
        header_title.setText("Add consultation Plan");
        rl_visit_type.setOnClickListener(this);
        rl_doctor_list.setOnClickListener(this);
        create_plan.setOnClickListener(this);
        setBackButtonToolbarStyleOne(v);
        visitTypeAdapter();
        hospitalAdapter();
    }

    public void visitTypeAdapter() {
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,visit_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_visit_type.setAdapter(aa);
    }

    public void hospitalAdapter() {
        HospitalSpinnerAdapter aa = new HospitalSpinnerAdapter(getActivity(), new HospitalSpinnerAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                hospital_name.setText("Fortis Hospital");
                hospital_name.setTextColor(getActivity().getResources().getColor(R.color.black));
                hideSpinnerDropDown(sp_hospital);

            }
        });
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
                home().proceedDoOnBackPressed();
                break;
        }
    }
}