package com.moguls.medic.ui.fragments.consultation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moguls.medic.R;
import com.moguls.medic.ui.settings.BaseFragment;


public class ConsultationFragment extends BaseFragment implements View.OnClickListener {

    private FloatingActionButton floating_action_button;
    private TextView header_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_consultation_plan, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header_title = (TextView)v.findViewById(R.id.header_title);
        floating_action_button = (FloatingActionButton)v.findViewById(R.id.floating_action_button);
        floating_action_button.setOnClickListener(this);
        header_title.setText("Consultation Plan");
        setBackButtonToolbarStyleOne(v);

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
}