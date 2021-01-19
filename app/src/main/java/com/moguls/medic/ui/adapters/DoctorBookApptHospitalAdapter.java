package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.doctorAppointments.Sessions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorBookApptHospitalAdapter extends RecyclerView.Adapter<DoctorBookApptHospitalAdapter.MyViewHolder>  {

    private final List<Sessions> expandableListTitle;
    public boolean isDoctor = false;
    Context context;
    private OnItemClickListner listener;
    int selectedPos = -1;
    int timeselectedPos = -1;
    private OnTimeClickListner onTimeClickListner;
    private AppointmentTimeAdapter appointmentAdapter;

    public interface OnItemClickListner {
        void OnItemClick(int position);
    }

    public interface OnTimeClickListner {
        void OnItemClick(String time);
    }
    private OnClickListner onClickListner;
    public interface OnClickListner {
        void OnItemClick(String position);
        void OnCancelClick(String position);
        void OnChatClicked(String position, String name,String image);
    }
    public DoctorBookApptHospitalAdapter(Context context, List<com.moguls.medic.model.doctorAppointments.Sessions> expandableListTitle,
                                         OnItemClickListner onItemClickListner,
                                         OnTimeClickListner onTimeClickListner, OnClickListner onClickListner) {
        this.listener = onItemClickListner;
        this.onClickListner = onClickListner;
        this.onTimeClickListner = onTimeClickListner;
        this.expandableListTitle = expandableListTitle;
        this.context = context;
    }

    @NonNull
    @Override
    public  DoctorBookApptHospitalAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_hospital_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sessions sessions = expandableListTitle.get(position);

        holder.rlRoot.setTag(position);
        holder.hospital_name.setText(sessions.getHospitalName());
        holder.timing.setText(sessions.getSessionName());
        if(selectedPos == position) {
            holder.rlRoot.setBackgroundColor(context.getResources().getColor(R.color.light_theme_color));
        } else {
            holder.rlRoot.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.recyclerView.setHasFixedSize(true);

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DoctorBookPatientDetailsAdapter appointmentAdapter =
                new DoctorBookPatientDetailsAdapter(sessions.getSlots(),
                        context, new DoctorBookPatientDetailsAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(String position) {
                onClickListner.OnItemClick(position);

            }

            @Override
            public void OnCancelClick(String position) {
                onClickListner.OnCancelClick(position);
            }

            @Override
            public void OnChatClicked(String ID,String  name,String image) {
                onClickListner.OnChatClicked(ID,name,image);
            }
        });

        holder.recyclerView.setAdapter(appointmentAdapter);

        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = (int)v.getTag();
                notifyDataSetChanged();
                listener.OnItemClick(selectedPos);
                timeselectedPos = -1;
                if(appointmentAdapter != null) {
                    appointmentAdapter.selectedPos = -1;
                    appointmentAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return expandableListTitle.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RelativeLayout rlRoot;
        TextView hospital_name,timing;
        RecyclerView recyclerView;
        CircleImageView profile_pic;
        public MyViewHolder(View view) {
            super(view);
            rlRoot =  view.findViewById(R.id.rlRoot);
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
