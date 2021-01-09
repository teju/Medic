package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.hospitalViews.Result;

import java.util.List;

public class DoctorHospitalsClinicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Result> mItemList;
    public Context context;

    private OnItemClickListner listener;
    public interface OnItemClickListner {
        void OnItemClick(int position);
    }
    public DoctorHospitalsClinicsAdapter(Context context, List<Result> itemList, OnItemClickListner listener) {
        this.listener = listener;
        this.context = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital_clinics, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRoot;
        ImageView icon;
        TextView hospital_name,hospital_address,slots,verifies;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.llRoot);
            hospital_name = itemView.findViewById(R.id.hospital_name);
            hospital_address = itemView.findViewById(R.id.hospital_address);
            slots = itemView.findViewById(R.id.slots);
            verifies = itemView.findViewById(R.id.verifies);
            icon = itemView.findViewById(R.id.icon);

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
        viewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
        viewHolder.hospital_name.setText(mItemList.get(position).getName());
        viewHolder.hospital_address.setText(mItemList.get(position).getAddress());
        String str = String.join(",", mItemList.get(position).getSessions());
        viewHolder.slots.setText(str);
        if(mItemList.get(position).getIsVerified()) {
            viewHolder.verifies.setText("Verified");
            viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.check_circle));
        } else {
            viewHolder.icon.setImageDrawable(context.getDrawable(R.drawable.close_circle));
            viewHolder.verifies.setText("UnVerified");
        }
    }



}