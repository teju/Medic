package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.patientDoctorProfile.Hospitals;

import java.util.List;

public class DoctorProfileHospitalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final Context context;

    public List<Hospitals> mItemList;

    private OnItemClickListner listener;
    public interface OnItemClickListner {
        void OnItemClick(int position);
    }
    public DoctorProfileHospitalListAdapter(Context context, List<Hospitals> itemList, OnItemClickListner listener) {
        this.listener = listener;

        mItemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_profile_hospital_list, parent, false);
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

        LinearLayout llRoot;
        TextView doctor_name,Address,fees;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            llRoot = itemView.findViewById(R.id.llRoot);
            doctor_name = itemView.findViewById(R.id.doctor_name);
            Address = itemView.findViewById(R.id.Address);
            fees = itemView.findViewById(R.id.fees);
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

        Hospitals item = mItemList.get(position);
        viewHolder.llRoot.setTag(position);
        viewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick((int)v.getTag());
            }
        });
        viewHolder.Address.setText(mItemList.get(position).getAddress());
        viewHolder.doctor_name.setText(mItemList.get(position).getName());
        viewHolder.fees.setText(mItemList.get(position).getFee()+"/-");
    }


}