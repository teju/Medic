package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.PatientList;

import java.util.List;

public class DoctorPatientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int SECTION_VIEW = 1;
    public static final int VIEW_TYPE_LOADING = 2;
    public  final Context context;

    public List<PatientList> mItemList;

    private OnItemClickListner listner;
    public interface OnItemClickListner {
        void OnItemClick(int position);
        void OnChatClicked(int position);
    }
    public DoctorPatientListAdapter(Context context, List<PatientList> itemList,OnItemClickListner listner) {
        mItemList = itemList;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
            return new ItemViewHolder(view);
        } else if(viewType == SECTION_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_title, parent, false);
            return new SectionHeaderViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof SectionHeaderViewHolder) {
            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) viewHolder;
            PatientList sectionItem = mItemList.get(position);
           // sectionHeaderViewHolder.headerTitleTextview.setText(sectionItem.getName());
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
            if(mItemList.get(position).getType() == VIEW_TYPE_ITEM) {
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

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ph_call = itemView.findViewById(R.id.ph_call);
            chat = itemView.findViewById(R.id.chat);
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
                Intent call = new Intent(Intent.ACTION_DIAL);
                context.startActivity(call);
            }
        });
        viewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listner.OnChatClicked(position);
            }
        });
    }
}