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
import com.moguls.medic.model.hospitalSummary.Sessions;

import java.util.ArrayList;

public class DoctorHospitalScheduledTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ArrayList<Sessions> mItemList = new ArrayList<>();
    public Context context;

    private OnItemClickListner listener;



    public interface OnItemClickListner {
        void OnItemClick(int position);
    }
    public DoctorHospitalScheduledTimeAdapter(Context context, ArrayList<Sessions> itemList, OnItemClickListner listener) {
        this.listener = listener;
        this.context = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_scheduledlist_time, parent, false);
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
        /*if(mItemList.size() > 3) {
            return  3;
        }*/
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRoot;
        TextView day,time;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.llRoot);
            day = itemView.findViewById(R.id.day);
            time = itemView.findViewById(R.id.time);
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
        viewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
        viewHolder.day.setText(mItemList.get(position).getDay());
        viewHolder.time.setText(mItemList.get(position).getTime());
    }
}