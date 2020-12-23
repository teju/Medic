package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.userAppointment.Result;

import java.util.List;

public class PastAppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final Context context;

    public List<Result> mItemList;

    private OnClickListner listener;
    public interface OnClickListner {
        void OnReBookClicked(int position);
        void OnItemClick(int position);
    }
    public PastAppointmentAdapter(List<Result> itemList, OnClickListner listener, Context context) {
        mItemList = itemList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_appointment, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRoot;
        Button rebook;
        TextView doctor_name,specialization,address,status,date,patient_name;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            llRoot = itemView.findViewById(R.id.llRoot);
            rebook = itemView.findViewById(R.id.rebook);
            doctor_name = itemView.findViewById(R.id.doctor_name);
            specialization = itemView.findViewById(R.id.specialization);
            address = itemView.findViewById(R.id.address);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            patient_name = itemView.findViewById(R.id.patient_name);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Result item = mItemList.get(position);
        viewHolder.llRoot.setTag(position);
        viewHolder.rebook.setTag(position);
        viewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick((int)v.getTag());
            }
        });
        viewHolder.rebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnReBookClicked((int)v.getTag());

            }
        });
        viewHolder.doctor_name.setText(mItemList.get(position).getDoctor().getName());
        viewHolder.specialization.setText(mItemList.get(position).getDoctor().getSpecialization());
        viewHolder.address.setText(mItemList.get(position).getDoctor().getHospitalAddress());
        viewHolder.date.setText(mItemList.get(position).getAppointmentOn());
        viewHolder.patient_name.setText(mItemList.get(position).getPatient().getName());
        viewHolder.status.setText(mItemList.get(position).getStatus());
    }


}