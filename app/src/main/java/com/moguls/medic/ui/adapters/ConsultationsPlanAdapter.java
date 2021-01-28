package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.consultations.ConsultationPlans;
import com.moguls.medic.model.consultations.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ConsultationsPlanAdapter extends RecyclerView.Adapter<ConsultationsPlanAdapter.MyViewHolder>  {

    public void setConsultationPlans(List<Result> consultationPlans) {
        this.consultationPlans = consultationPlans;
    }

    List<Result> consultationPlans = new ArrayList<>();
    Context context;
    private OnItemClickListner listener;
    public interface OnItemClickListner {
        void OnItemClick(int pos);
    }

    public ConsultationsPlanAdapter(Context context, OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
    }

    @NonNull
    @Override
    public  ConsultationsPlanAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation_plan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Result result = consultationPlans.get(position);
        holder.duration.setText("Slots duration "+result.getTimeslot()+" mins");
        holder.fee.setText(result.getFee()+" Rs");
        holder.hospital_name.setText(result.getHospital());
        holder.hospital_address.setText(result.getAddress());
        holder.consultationType.setText(result.getConsultationType().getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return consultationPlans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView header_title,duration,fee,hospital_name,hospital_address,consultationType;
        LinearLayout time;
        public MyViewHolder(View v) {
            super(v);
            time = (LinearLayout) v.findViewById(R.id.time);
            duration = (TextView)v.findViewById(R.id.duration);
            fee = (TextView)v.findViewById(R.id.fee);
            consultationType = (TextView)v.findViewById(R.id.consultationType);
            hospital_name = (TextView)v.findViewById(R.id.hospital_name);
            hospital_address = (TextView)v.findViewById(R.id.hospital_address);
        }


    }
}
