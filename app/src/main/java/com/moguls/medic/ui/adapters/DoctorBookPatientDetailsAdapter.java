package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.doctorAppointments.DoctorSlots;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DoctorBookPatientDetailsAdapter extends RecyclerView.Adapter<DoctorBookPatientDetailsAdapter.MyViewHolder>  {
    private List<DoctorSlots> slots = new ArrayList<>();
    Context context;
    private OnItemClickListner listener;
    int selectedPos = -1;

    public interface OnItemClickListner {
        void OnItemClick(String position);
        void OnCancelClick(String ID);
        void OnChatClicked(String ID,String name);

    }

    public DoctorBookPatientDetailsAdapter(List<DoctorSlots> slots,
                                           Context context, OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
        this.slots = slots;
    }

    @NonNull
    @Override
    public  MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_patients__details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnCancelClick(slots.get(position).getID());
            }
        });
        holder.ph_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = null;
                Intent call;
                if(slots.get(position).getPatient().getMobileNo() != null) {
                    number = Uri.parse("tel:" + slots.get(position).getPatient().getMobileNo());
                }
                if(number != null) {
                    call = new Intent(Intent.ACTION_DIAL,number);

                } else {
                    call = new Intent(Intent.ACTION_DIAL);

                }
                context.startActivity(call);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnChatClicked(slots.get(position).getID(),slots.get(position).getPatient().getName());
            }
        });
        holder.llsubroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = position;
                notifyDataSetChanged();
                listener.OnItemClick(slots.get(position).getID());

            }
        });

       /* if(position == selectedPos) {
            holder.llsubroot.setBackgroundColor(context.getResources().getColor(R.color.light_theme_color));
        } else {
            holder.llsubroot.setBackgroundColor(context.getResources().getColor(R.color.white));
        }*/
        holder.name.setText(slots.get(position).getPatient().getName());
        Date d = Helper.convertStringToDate("dd MMM yyyy hh:mm",slots.get(position).getAppointmentOn());
        holder.time.setText(Helper.dateFormat("hh:mm",d));
        holder.am_pm.setText(Helper.dateFormat("a",d));
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cancel,chat,ph_call;
        LinearLayout llsubroot;
        TextView am_pm,time,name;
        public MyViewHolder(View view) {
            super(view);
            cancel = view.findViewById(R.id.cancel);
            chat = view.findViewById(R.id.chat);
            ph_call = view.findViewById(R.id.ph_call);
            am_pm = view.findViewById(R.id.am_pm);
            time = view.findViewById(R.id.time);
            name = view.findViewById(R.id.name);
            llsubroot = view.findViewById(R.id.llsubroot);

        }

    }
}
