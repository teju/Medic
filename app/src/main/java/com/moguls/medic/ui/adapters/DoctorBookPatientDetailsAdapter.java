package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;

import java.util.List;


public class DoctorBookPatientDetailsAdapter extends RecyclerView.Adapter<DoctorBookPatientDetailsAdapter.MyViewHolder>  {

    private List<String> timeList;
    Context context;
    private OnItemClickListner listener;
    int selectedPos = -1;

    public interface OnItemClickListner {
        void OnItemClick(int position);
        void OnCancelClick(int position);
        void OnChatClicked(int position);

    }

    public DoctorBookPatientDetailsAdapter(Context context, OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
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
                listener.OnCancelClick(position);
            }
        });
        holder.ph_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                context.startActivity(call);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnChatClicked(position);
            }
        });
        holder.llsubroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = position;
                notifyDataSetChanged();
            }
        });

        if(position == selectedPos) {
            holder.llsubroot.setBackgroundColor(context.getResources().getColor(R.color.light_theme_color));
        } else {
            holder.llsubroot.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView cancel,chat,ph_call;
        LinearLayout llsubroot;
        public MyViewHolder(View view) {
            super(view);
            cancel = view.findViewById(R.id.cancel);
            chat = view.findViewById(R.id.chat);
            ph_call = view.findViewById(R.id.ph_call);
            llsubroot = view.findViewById(R.id.llsubroot);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(getAdapterPosition());
            selectedPos = getAdapterPosition();
            notifyDataSetChanged();
        }
    }
}
