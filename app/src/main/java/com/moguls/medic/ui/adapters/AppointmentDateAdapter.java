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

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AppointmentDateAdapter extends RecyclerView.Adapter<AppointmentDateAdapter.MyViewHolder>  {

    private List<String> timeList;
    Context context;
    private OnItemClickListner listener;
    public int selectedPos = -1;
    public interface OnItemClickListner {
        void OnItemClick(String date,int pos);
    }

    public AppointmentDateAdapter(Context context,OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
    }

    @NonNull
    @Override
    public  AppointmentDateAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time.setTag(position);
        if(position == selectedPos) {
            holder.time.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
            holder.weekday_txt.setTextColor(context.getResources().getColor(R.color.white));
            holder.day_txt.setTextColor(context.getResources().getColor(R.color.white));
        } else if(position == 0) {
            holder.time.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.light_gray)));
            holder.weekday_txt.setTextColor(context.getResources().getColor(R.color.grey));
            holder.day_txt.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.time.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.transparent)));
            holder.weekday_txt.setTextColor(context.getResources().getColor(R.color.grey));
            holder.day_txt.setTextColor(context.getResources().getColor(R.color.black));
        }
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, position);
        dt = c.getTime();
        holder.day_txt.setText(Helper.dateFormat("dd",dt));
        holder.weekday_txt.setText(Helper.dateFormat("E",dt));
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dt = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                c.add(Calendar.DATE, position);
                dt = c.getTime();
                listener.OnItemClick(Helper.dateFormat("dd-MMM-yyyy",dt),position);
                selectedPos = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView weekday_txt,day_txt;
        LinearLayout time;
        public MyViewHolder(View view) {
            super(view);
            time = (LinearLayout) view.findViewById(R.id.time);
            weekday_txt = (TextView) view.findViewById(R.id.weekday_txt);
            day_txt = (TextView) view.findViewById(R.id.day_txt);
        }

        @Override
        public void onClick(View v) {
            selectedPos = getAdapterPosition();
            notifyDataSetChanged();
        }
    }
}
