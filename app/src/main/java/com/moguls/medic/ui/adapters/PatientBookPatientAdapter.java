package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PatientBookPatientAdapter extends RecyclerView.Adapter<PatientBookPatientAdapter.MyViewHolder>  {

    private List<String> timeList;
    Context context;
    private OnItemClickListner listener;
    int selectedPos = -1;

    public interface OnItemClickListner {
        void OnItemClick(int position);
    }

    public PatientBookPatientAdapter(Context context, OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
    }

    @NonNull
    @Override
    public  PatientBookPatientAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_patient, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == selectedPos) {
            holder.profile_pic.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.profile_pic.setBorderColor(context.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile_pic;
        public MyViewHolder(View view) {
            super(view);
            profile_pic = (CircleImageView) view.findViewById(R.id.profile_pic);
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
