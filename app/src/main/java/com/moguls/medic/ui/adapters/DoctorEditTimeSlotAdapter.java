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

import java.util.ArrayList;
import java.util.List;

public class DoctorEditTimeSlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<String> mItemList;
    public Context context;
    public boolean isAdd;

    private OnItemClickListner listener;

    public interface OnItemClickListner {
        void OnItemClick(int position);
    }
    public DoctorEditTimeSlotAdapter(Context context, List<String> itemList, OnItemClickListner listener) {
        this.listener = listener;
        this.context = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_time_slot, parent, false);
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
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRoot,llNight,llevening,llmorning;
        TextView weekday_txt,evening_shift_timing,morning_shift_timing,night_shift_timing;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.llRoot);
            weekday_txt = itemView.findViewById(R.id.weekday_txt);
            llNight = itemView.findViewById(R.id.llNight);
            llevening = itemView.findViewById(R.id.llevening);
            llmorning = itemView.findViewById(R.id.llmorning);
            evening_shift_timing = itemView.findViewById(R.id.evening_shift_timing);
            morning_shift_timing = itemView.findViewById(R.id.morning_shift_timing);
            night_shift_timing = itemView.findViewById(R.id.night_shift_timing);
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
        viewHolder.weekday_txt.setText(mItemList.get(position));
        viewHolder.llmorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.llevening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        viewHolder.llNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(isAdd) {
            viewHolder.morning_shift_timing.setText("+ Add shift");
            viewHolder.morning_shift_timing.setTextColor(context.getResources().getColor(R.color.orange));

            viewHolder.evening_shift_timing.setText("+ Add shift");
            viewHolder.evening_shift_timing.setTextColor(context.getResources().getColor(R.color.orange));

            viewHolder.night_shift_timing.setText("+ Add shift");
            viewHolder.night_shift_timing.setTextColor(context.getResources().getColor(R.color.orange));
        }
    }
}