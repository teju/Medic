package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.doctors.Result;

import java.util.List;

public class PatientDoctorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Result> mItemList;
    public Context context;

    private OnItemClickListner listener;
    public interface OnItemClickListner {
        void OnItemClick(int position);
        void OnApptClick(int position);
        void OnChatClick(int position);
    }
    public PatientDoctorListAdapter(Context context, List<Result> itemList, OnItemClickListner listener) {
        this.listener = listener;
        this.context = context;
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
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
        return VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRoot;
        ImageView chat,call,btnappt,logo;
        TextView name,specialization,distance,experience;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            llRoot = itemView.findViewById(R.id.llRoot);
            btnappt = itemView.findViewById(R.id.btnappt);
            chat = itemView.findViewById(R.id.chat);
            call = itemView.findViewById(R.id.call);
            logo = itemView.findViewById(R.id.logo);
            name = itemView.findViewById(R.id.name);
            specialization = itemView.findViewById(R.id.specialization);
            distance = itemView.findViewById(R.id.distance);
            experience = itemView.findViewById(R.id.experience);
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
        viewHolder.btnappt.setTag(position);
        try {
            //Glide.with(context).load(url).into(viewHolder.logo);
            viewHolder.name.setText(item.getName());
            viewHolder.specialization.setText(item.getSpecialization());
            if(item.getDistance() != null){
                viewHolder.distance.setText(item.getDistance()+" km away");
            } else {
                viewHolder.distance.setText("Unknown");
            }
            viewHolder.experience.setText("Experience "+item.getExperience()+"years");
        } catch (Exception e){

        }
//        viewHolder.tvItem.setText(item);
        viewHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick((int)v.getTag());
            }
        });
        viewHolder.btnappt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnApptClick((int)v.getTag());
            }
        });
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:"+item.getMobileNo());
                Intent call = new Intent(Intent.ACTION_DIAL,number);
                context.startActivity(call);
            }
        });
        viewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnChatClick(position);

            }
        });
        Helper.loadImage(context, item.getPhotoUrl(),
                R.drawable.doctor_profile_pic_default,viewHolder.logo);
    }


}