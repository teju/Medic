package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.hospitalViews.Result;

import java.util.List;

public class DoctorHospitalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Result> mItemList;
    public Context context;

    private OnItemClickListner listener;
    public interface OnItemClickListner {
        void OnItemClick(int position);
    }
    public DoctorHospitalsAdapter(Context context, List<Result> itemList, OnItemClickListner listener) {
        this.listener = listener;
        this.context = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_hospital_list, parent, false);
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

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView recyclerView;
        LinearLayout llRoot;
        TextView appointments,sppointment_cnt,hospital_name;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.llRoot);
            appointments = itemView.findViewById(R.id.appointments);
            sppointment_cnt = itemView.findViewById(R.id.sppointment_cnt);
            hospital_name = itemView.findViewById(R.id.hospital_name);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Result item = mItemList.get(position);
        viewHolder.llRoot.setTag(position);
        viewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick((int)v.getTag());
            }
        });
        viewHolder.recyclerView.setHasFixedSize(true);
        viewHolder.recyclerView.setLayoutManager(new GridLayoutManager(context,5));
        viewHolder.hospital_name.setText(mItemList.get(position).getName());
        String str = String.join(",", mItemList.get(position).getSessions());

        viewHolder.appointments.setText(str);
        viewHolder.sppointment_cnt.setText(mItemList.get(position).getAppointmentCount()+" appointments");
        DoctorBookedPatientAdapter appointmentAdapter = new DoctorBookedPatientAdapter(context,
                new DoctorBookedPatientAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }
        });
        viewHolder.recyclerView.setAdapter(appointmentAdapter);
    }



}