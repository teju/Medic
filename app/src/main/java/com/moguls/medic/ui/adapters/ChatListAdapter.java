package com.moguls.medic.ui.adapters;


import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.myChat.Result;

import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final Context context;

    public List<Result> mItemList;

    private OnClickListner listener;
    public interface OnClickListner {
        void OnClick(int position);
    }
    public ChatListAdapter(List<Result> itemList, OnClickListner listener, Context context) {
        mItemList = itemList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false);
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

        RelativeLayout llroot;
        TextView name,latest_msg,date,count;
        ImageView logo;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            llroot = itemView.findViewById(R.id.llroot);
            name = itemView.findViewById(R.id.name);
            latest_msg = itemView.findViewById(R.id.latest_msg);
            date = itemView.findViewById(R.id.date);
            count = itemView.findViewById(R.id.count);
            logo = itemView.findViewById(R.id.logo);
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
        viewHolder.llroot.setTag(position);
        viewHolder.llroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick((int)v.getTag());
            }
        });
        Date date = Helper.convertStringToDate("dd MMM yyyy hh:mm",mItemList.get(position).getChat().getSendOn());
        boolean isToday = DateUtils.isToday(date.getTime());

        viewHolder.name.setText(mItemList.get(position).getName());
        viewHolder.latest_msg.setText(mItemList.get(position).getChat().getMessage());
        if(isToday) {
            Date d = Helper.convertStringToDate("dd MMM yyyy hh:mm",mItemList.get(position).getChat().getSendOn());
            viewHolder.date.setText(Helper.dateFormat("hh:mm a",d));
        } else {
            viewHolder.date.setText(mItemList.get(position).getChat().getSendOn());
        }
        if(mItemList.get(position).getUnreadCount() != null &&
                Integer.parseInt(mItemList.get(position).getUnreadCount()) != 0) {
            viewHolder.count.setText(mItemList.get(position).getUnreadCount());
            viewHolder.count.setVisibility(View.VISIBLE);
        } else {
            viewHolder.count.setVisibility(View.GONE);
        }
        if(mItemList.get(position).getPhotoUrl() != null) {
            Helper.loadImage(context,mItemList.get(position).getPhotoUrl(),R.drawable.doctor_profile_pic_default,viewHolder.logo);
        } else {
            viewHolder.logo.setImageResource(R.drawable.doctor_profile_pic_default);
        }
    }
}