package com.moguls.medic.ui.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.BookingData;

import java.util.Date;

import static com.moguls.medic.etc.Helper.loadImage;

public class ConfirmBookingDialogFragment extends DialogFragment {
    public static String TAG = "NotifyDialogFragment";
    public BookingData bookingData;
    public String refNo;
    private View v;
    public NotifyListener listener;
    long diffdays = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_confirm_booking, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnok = (Button) v.findViewById(R.id.btnok);
        TextView ref_id = (TextView) v.findViewById(R.id.ref_id);
        LinearLayout llRefID = (LinearLayout) v.findViewById(R.id.llRefID);
        ImageView logo = (ImageView) v.findViewById(R.id.logo);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView app_time = (TextView) v.findViewById(R.id.app_time);
        TextView doctor_name = (TextView) v.findViewById(R.id.doctor_name);
        TextView doctor_spl = (TextView) v.findViewById(R.id.doctor_spl);
        TextView fees = (TextView) v.findViewById(R.id.fees);
        TextView diff_days = (TextView) v.findViewById(R.id.diff_days);
        try {
            ref_id.setText(bookingData.getRefID());
            date.setText(bookingData.getDate());
            app_time.setText(bookingData.getTime());
            doctor_name.setText(bookingData.getDoctorName());
            doctor_spl.setText(bookingData.getSpecializaion());
            fees.setText("Rs "+bookingData.getFees()+" per/session");
            diffdays = Helper.findDifferenceBetweenDates(new Date(),Helper.convertStringToDate("dd MMM yyyy",bookingData.getDate()));
            if(diffdays != 0) {
                diff_days.setText("in "+diffdays+" days");
            }
            if(refNo == null) {
                llRefID.setVisibility(View.GONE);
            } else {
                llRefID.setVisibility(View.VISIBLE);
                ref_id.setText(refNo);
            }

        } catch (Exception e){

        }
        loadImage(getActivity(),bookingData.getPhotoUrl()
                ,R.drawable.doctor_profile_pic_default,logo);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked(1);
                dismiss();
            }
        });

    }
}
