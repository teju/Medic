package com.moguls.medic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.model.getPatients.Result;
import com.moguls.medic.model.getProfile.Member;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.moguls.medic.etc.Helper.loadImage;


public class PatientBookingAdapter extends RecyclerView.Adapter<PatientBookingAdapter.MyViewHolder>  {

    private List<Member> result = new ArrayList<>();
    Context context;
    private OnItemClickListner listener;
    int selectedPos = -1;

    public interface OnItemClickListner {
        void OnItemClick(int position);
    }

    public PatientBookingAdapter(Context context, List<Member> result, OnItemClickListner onItemClickListner) {
        this.listener = onItemClickListner;
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public  PatientBookingAdapter.MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_patient, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == selectedPos) {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.profile_pic.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.dark_gray));
            holder.profile_pic.setBorderColor(context.getResources().getColor(R.color.transparent));
        }
        holder.llRoot.setTag(position);
        holder.name.setText(result.get(position).getName());
        holder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = (int)v.getTag();
                notifyDataSetChanged();
                listener.OnItemClick((int)v.getTag());
            }
        });
        loadImage(context, result.get(position).getPhotoUrl(),
                R.drawable.doctor_profile_pic_default,holder.profile_pic);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final LinearLayout llRoot;
        TextView name;
        CircleImageView profile_pic;
        public MyViewHolder(View view) {
            super(view);
            profile_pic = (CircleImageView) view.findViewById(R.id.profile_pic);
            name = (TextView) view.findViewById(R.id.name);
            llRoot = (LinearLayout) view.findViewById(R.id.llRoot);

        }

        @Override
        public void onClick(View v) {


        }
    }
}
