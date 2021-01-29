package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.model.doctorPatients.Result;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorPatientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int SECTION_VIEW = 1;
    public static final int VIEW_TYPE_LOADING = 2;
    public  final Context context;

    public void setmItemList(List<PatientList> mItemList) {
        this.mItemList = mItemList;
    }

    public List<PatientList> mItemList;

    private OnItemClickListner listner;
    public interface OnItemClickListner {
        void OnItemClick(int position);
        void OnChatClicked(int position);
    }
    public DoctorPatientListAdapter(Context context, OnItemClickListner listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        } else if(viewType == SECTION_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_title, parent, false);
            return new SectionHeaderViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof SectionHeaderViewHolder) {
            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) viewHolder;
            PatientList sectionItem = mItemList.get(position);
            sectionHeaderViewHolder.headerTitleTextview.setText(sectionItem.getName());
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
        try {
            if( mItemList.get(position) == null) {
                return VIEW_TYPE_LOADING;
            }
            else if(mItemList.get(position).getType() == VIEW_TYPE_ITEM) {
                return VIEW_TYPE_ITEM;
            } else  {
                return SECTION_VIEW;
            }
        } catch (Exception e){
            return VIEW_TYPE_LOADING;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ph_call,chat;
        CircleImageView patient_img;
        TextView remarks,age,blood_group,height,weight,date,mobile_number,name;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ph_call = itemView.findViewById(R.id.ph_call);
            chat = itemView.findViewById(R.id.chat);
            age = itemView.findViewById(R.id.age);
            remarks = itemView.findViewById(R.id.remarks);
            blood_group = itemView.findViewById(R.id.blood_group);
            height = itemView.findViewById(R.id.height);
            weight = itemView.findViewById(R.id.weight);
            date = itemView.findViewById(R.id.date);
            mobile_number = itemView.findViewById(R.id.mobile_number);
            name = itemView.findViewById(R.id.name);
            patient_img = itemView.findViewById(R.id.patient_img);
        }
    }
    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitleTextview;

        public SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = (TextView) itemView.findViewById(R.id.headerTitleTextview);
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
        viewHolder.ph_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = null;
                Intent call;
                if(mItemList.get(position).getResult().getMobileNo() != null) {
                    number = Uri.parse("tel:" + mItemList.get(position).getResult().getMobileNo());
                }
                if(number != null) {
                    call = new Intent(Intent.ACTION_DIAL,number);

                } else {
                    call = new Intent(Intent.ACTION_DIAL);

                }
                context.startActivity(call);
            }
        });
        viewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listner.OnChatClicked(position);
            }
        });
        viewHolder.name.setText(mItemList.get(position).getResult().getName());
        viewHolder.mobile_number.setText(mItemList.get(position).getResult().getMobileNo());
        viewHolder.date.setText(mItemList.get(position).getResult().getAppointmentOn());
        viewHolder.age.setText(mItemList.get(position).getResult().getAge());
        viewHolder.blood_group.setText(mItemList.get(position).getResult().getBloodGroup());
        if(mItemList.get(position).getResult().getHeight() != null) {
            viewHolder.height.setText(mItemList.get(position).getResult().getHeight() + "ft");
        }
        if(mItemList.get(position).getResult().getWeight() != null) {
            viewHolder.weight.setText(mItemList.get(position).getResult().getWeight() + " Kgs");
        }
        viewHolder.remarks.setText(mItemList.get(position).getResult().getAppointmentRemark());
        //viewHolder.patient_img.setImageResource(R.drawable.hospital_img);
        if(mItemList.get(position).getResult().getPhotoUrl() != null) {
            Helper.loadImage(context, mItemList.get(position).getResult().getPhotoUrl(), R.drawable.doctor_profile_pic_default, viewHolder.patient_img);
        } else {
            viewHolder.patient_img.setImageResource(R.drawable.doctor_profile_pic_default);
        }


    }

}