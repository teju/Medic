package com.moguls.medic.ui.adapters;


import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.chat.Result;

import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Result> mItemList;

    private OnClickListner listener;
    public interface OnClickListner {
        void OnClick(int position);
    }
    public ChatAdapter(List<com.moguls.medic.model.chat.Result> itemList, OnClickListner listener) {
        mItemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbubble, parent, false);
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
        return  VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name,sender_message_text,my_msgs_message_text,sender_msgs_date,my_msg_date;
        LinearLayout sender_msgs,my_msgs;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            my_msgs_message_text = itemView.findViewById(R.id.my_msgs_message_text);
            sender_message_text = itemView.findViewById(R.id.sender_message_text);
            sender_msgs = itemView.findViewById(R.id.sender_msgs);
            my_msgs = itemView.findViewById(R.id.my_msgs);
            sender_msgs_date = itemView.findViewById(R.id.sender_msgs_date);
            my_msg_date = itemView.findViewById(R.id.my_msg_date);

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
        Date date = Helper.convertStringToDate("dd MMM yyyy hh:mm",mItemList.get(position).getSendOn());
        boolean isToday = DateUtils.isToday(date.getTime());
        String strDate = mItemList.get(position).getSendOn();
        if(isToday) {
            Date d = Helper.convertStringToDate("dd MMM yyyy hh:mm",mItemList.get(position).getSendOn());
            strDate = Helper.dateFormat("hh:mm a",d);
        }
        if(mItemList.get(position).getIsSendByMe() == "false") {
            viewHolder.sender_msgs.setVisibility(View.VISIBLE);
            viewHolder.my_msgs.setVisibility(View.GONE);
            viewHolder.sender_message_text.setText(mItemList.get(position).getMessage());
            viewHolder.sender_msgs_date.setText(strDate);
        } else {
            viewHolder.sender_msgs.setVisibility(View.GONE);
            viewHolder.my_msgs.setVisibility(View.VISIBLE);
            viewHolder.my_msgs_message_text.setText(mItemList.get(position).getMessage());
            viewHolder.my_msg_date.setText(strDate);
        }

    }


}