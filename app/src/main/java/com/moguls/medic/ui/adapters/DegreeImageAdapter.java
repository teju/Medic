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
import com.moguls.medic.model.chat.Result;
import com.moguls.medic.model.doctorProfileDetails.Registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DegreeImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final Context context;

    public List<Registration> mItemList = new ArrayList<>();

    private OnClickListner listener;
    public interface OnClickListner {
        void OnClick(int position);
    }
    public DegreeImageAdapter(Context context, OnClickListner listener) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_degree, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((ItemViewHolder) viewHolder, position);


    }

    @Override
    public int getItemCount() {
        return  mItemList.size();
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView degree_proof;
        RelativeLayout root;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            degree_proof = itemView.findViewById(R.id.degree_proof);
            root = itemView.findViewById(R.id.root);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }



    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        try {
            Helper.loadImage(context, mItemList.get(position).getDocumentUrl(), R.drawable.camera_plus, viewHolder.degree_proof);
        } catch (Exception e){
            e.printStackTrace();
        }
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(position);
            }
        });
    }


}