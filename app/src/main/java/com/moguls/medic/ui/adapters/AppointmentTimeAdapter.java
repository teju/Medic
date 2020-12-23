package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.moguls.medic.R;
import com.moguls.medic.model.appointmentSlots.Sessions;
import com.moguls.medic.model.appointmentSlots.Slots;

import java.util.ArrayList;
import java.util.List;


public class AppointmentTimeAdapter extends RecyclerView.Adapter<AppointmentTimeAdapter.MyViewHolder>  {

    private List<Slots> timeList = new ArrayList<>();

    private OnItemClickListner listener;
    private Context context;
    int selectedPos = -1;

    public interface OnItemClickListner {
        void OnItemClick(String time,int Pos,int parent_id);
    }

    public AppointmentTimeAdapter(Context context, List<Slots> timeList, OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public  AppointmentTimeAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentTimeAdapter.MyViewHolder holder, int position) {

        System.out.println("setParentID "+timeList.get(position).getID()
                +" "+selectedPos);

        holder.time.setTag(position);
        holder.time.setText(""+timeList.get(position).getTime());

        try {
            if (timeList.get(position).getID() == selectedPos) {
                holder.time.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
                holder.time.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.time.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.light_gray)));
                holder.time.setTextColor(context.getResources().getColor(R.color.black));
            }
        } catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView time;
        public MyViewHolder(View view) {
            super(view);
            time = (TextView)view.findViewById(R.id.time);
            time.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = (int)v.getTag();
            listener.OnItemClick(timeList.get(pos).getTime(),timeList.get(pos).getID(),timeList.get(pos).getParentID());
            selectedPos = timeList.get(pos).getID();
            notifyDataSetChanged();
        }

    }
}
