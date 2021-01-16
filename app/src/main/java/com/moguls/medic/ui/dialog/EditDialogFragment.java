package com.moguls.medic.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.moguls.medic.R;
import com.moguls.medic.callback.EditSlotsListener;
import com.moguls.medic.callback.OtpListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.SpannString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class EditDialogFragment extends DialogFragment {
    public static String TAG = "NotifyDialogFragment";
    public  static  int BUTTON_POSITIVE = 1;
    public  static int BUTTON_NEGATIVE = 0;
    public  static int BUTTON_RESENDOTP = 2;
    public EditSlotsListener listener;
    private View v;
    private OtpTextView otpTextView;
    private TextView resendOtp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.edit_slots_dialog, container, false);
        return v;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.windowAnimations = R.style.DialogAnimation;
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setBackgroundDrawableResource(R.color.transparent);
        TextView cancel=(TextView) v.findViewById(R.id.cancel);
        TextView done=(TextView) v.findViewById(R.id.done);
        TimePicker fromtime=(TimePicker)v.findViewById(R.id.fromtime);
        TimePicker to_time=(TimePicker)v.findViewById(R.id.to_time);
        fromtime.setIs24HourView(true);
        to_time.setIs24HourView(true);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDAte=String.format("%02d:%02d",fromtime.getCurrentHour(),fromtime.getMinute());
                String endDAte=String.format("%02d:%02d",to_time.getCurrentHour(),to_time.getMinute());
                listener.onButtonClicked(startDAte,endDAte);
                dismiss();
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }
}
