package com.moguls.medic.ui.fragments.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.model.consultations.Result;
import com.moguls.medic.ui.adapters.ConsultationsPlanAdapter;
import com.moguls.medic.ui.adapters.PatientDoctorListAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GeConsultationsViewModel;
import com.moguls.medic.webservices.GetDoctorProfileSummaryViewModel;


public class ConsultationFragment extends BaseFragment implements View.OnClickListener {

    private FloatingActionButton floating_action_button;
    private TextView header_title,duration,fee,hospital_name,hospital_address,consultationType;
    private GeConsultationsViewModel gConsultationsViewModel;
    private LoadingCompound ld;
    private RecyclerView recyclerView;
    private ConsultationsPlanAdapter consultationsPlanAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_consultation_plan, container, false);
        return v;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            gConsultationsViewModel.loadData();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setConsultationAPIObserver();
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        header_title = (TextView)v.findViewById(R.id.header_title);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        floating_action_button = (FloatingActionButton)v.findViewById(R.id.floating_action_button);
        floating_action_button.setOnClickListener(this);
        header_title.setText("Consultation Plan");
        setBackButtonToolbarStyleOne(v);
        gConsultationsViewModel.loadData();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        consultationsPlanAdapter = new ConsultationsPlanAdapter(getActivity(), new ConsultationsPlanAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int pos) {

            }
        });
        consultationsPlanAdapter.setConsultationPlans(gConsultationsViewModel.getcConsultationPlans.getResult());
        recyclerView.setAdapter(consultationsPlanAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_button :
                home().setFragment(new AddConsultationFragment());
                break;
        }
    }
    public void setConsultationAPIObserver() {
        gConsultationsViewModel = ViewModelProviders.of(this).get(GeConsultationsViewModel.class);
        gConsultationsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        gConsultationsViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        gConsultationsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        gConsultationsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        gConsultationsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
            }
        });
    }



}