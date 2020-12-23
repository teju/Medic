package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.appointmentSlots.Sessions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorBookApptHospitalListAdapter extends RecyclerView.Adapter<DoctorBookApptHospitalListAdapter.MyViewHolder>  {

    private final List<Sessions> sessionsList;
    private List<String> timeList;
    Context context;
    private OnItemClickListner listener;
    int selectedPos = -1;
    private OnTimeClickListner onTimeClickListner;
    public interface OnItemClickListner {
        void OnItemClick(int position);
        void OnCancelClick(int position);
        void OnChatClicked(int position);
    }

    public interface OnTimeClickListner {
        void OnItemClick(String time);

    }
    public DoctorBookApptHospitalListAdapter(Context context, List<Sessions> sessionsList,
                                             OnItemClickListner onItemClickListner, OnTimeClickListner onTimeClickListner) {
        this.listener = onItemClickListner;
        this.onTimeClickListner = onTimeClickListner;
        this.sessionsList = sessionsList;
        this.context = context;
    }

    @NonNull
    @Override
    public  DoctorBookApptHospitalListAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_booking_hospital_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Sessions sessions = sessionsList.get(position);

        holder.rlRoot.setTag(position);
        holder.hospital_name.setText("Fortis Hospital");
        holder.timing.setText("9.00 AM - 12.00 PM");
        if(selectedPos == position) {
            holder.rlRoot.setBackgroundColor(context.getResources().getColor(R.color.light_theme_color));
        } else {
            holder.rlRoot.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = (int)v.getTag();
                notifyDataSetChanged();
                listener.OnItemClick(selectedPos);
            }
        });
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DoctorBookPatientDetailsAdapter appointmentAdapter = new DoctorBookPatientDetailsAdapter(context, new DoctorBookPatientDetailsAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void OnCancelClick(int position) {
                listener.OnCancelClick(position);
            }

            @Override
            public void OnChatClicked(int position) {
                listener.OnChatClicked(position);
            }
        });

        holder.recyclerView.setAdapter(appointmentAdapter);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RelativeLayout rlRoot;
        TextView hospital_name,timing;
        RecyclerView recyclerView;
        CircleImageView profile_pic;
        LinearLayout llRoot;
        public MyViewHolder(View view) {
            super(view);
            rlRoot =  view.findViewById(R.id.rlRoot);
            llRoot =  view.findViewById(R.id.llRoot);
            hospital_name = view.findViewById(R.id.hospital_name);
            timing = view.findViewById(R.id.timing);
            recyclerView =  view.findViewById(R.id.recyclerView);
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
