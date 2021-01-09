package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moguls.medic.R;
import com.moguls.medic.model.hospitalViews.Result;

import java.util.ArrayList;
import java.util.List;

public class HospitalSpinnerAdapter extends BaseAdapter {
    private final OnItemClickListner onItemClickListner;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    List<Result> results = new ArrayList<>();
    Context context;
    int selPos = 0;
    public interface OnItemClickListner {
        void OnItemClick(int position);
    }
    public HospitalSpinnerAdapter(Context applicationContext,OnItemClickListner onItemClickListner) {
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
        if(i == selPos) {
            radio_button.setChecked(true);
        } else {
            radio_button.setChecked(false);
        }
        radio_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selPos = (int)buttonView.getTag();
                notifyDataSetChanged();
                onItemClickListner.OnItemClick(selPos);
            }
        });
        radio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                onItemClickListner.OnItemClick(selPos);
            }
        });
        hospital_name.setText(results.get(i).getName());
        hospital_address.setText(results.get(i).getAddress());
        return view;
    }
}
