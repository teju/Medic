package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moguls.medic.R;
import com.moguls.medic.model.doctorProfileDetails.Specializations;
import com.moguls.medic.model.hospitalViews.Result;

import java.util.ArrayList;
import java.util.List;

public class SpecializationSpinnerAdapter extends BaseAdapter {
    private final OnItemClickListner onItemClickListner;

    public List<Specializations> getResults() {
        return results;
    }

    public void setResults(List<Specializations> results) {
        this.results = results;
    }

    List<Specializations> results = new ArrayList<>();
    Context context;
    public interface OnItemClickListner {
        void OnItemClick(int position, boolean isRemove);
    }
    public SpecializationSpinnerAdapter(Context applicationContext, OnItemClickListner onItemClickListner) {
        this.context = applicationContext;
        this.onItemClickListner = onItemClickListner;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int i) {
        return results.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflter = (LayoutInflater.from(context));

        view = inflter.inflate(R.layout.hospital_spinner_items, null);
        RadioButton radio_button = (RadioButton)view.findViewById(R.id.radio_button);
        TextView hospital_address = (TextView)view.findViewById(R.id.hospital_address);
        TextView hospital_name = (TextView)view.findViewById(R.id.hospital_name);
        radio_button.setTag(i);
        if(results.get(i).isSelected()) {
            radio_button.setChecked(true);
        } else {
            radio_button.setChecked(false);
        }
        radio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int)v.getTag();
                if(results.get(i).isSelected()) {
                   radio_button.setChecked(false);
                    results.get(i).setSelected(false);
                    notifyDataSetInvalidated();
                    onItemClickListner.OnItemClick(i,true);
                } else {
                    radio_button.setChecked(true);
                    results.get(i).setSelected(true);
                    notifyDataSetInvalidated();
                    onItemClickListner.OnItemClick(i,false);
                }
            }
        });
        hospital_name.setText(results.get(i).getName());
        hospital_address.setVisibility(View.GONE);
        return view;
    }
}
