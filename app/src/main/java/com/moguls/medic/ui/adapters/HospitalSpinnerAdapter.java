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

public class HospitalSpinnerAdapter extends BaseAdapter {
    private final OnItemClickListner onItemClickListner;
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
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflter = (LayoutInflater.from(context));

        view = inflter.inflate(R.layout.hospital_spinner_items, null);
        RadioButton radio_button = (RadioButton)view.findViewById(R.id.radio_button);
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
        return view;
    }
}
