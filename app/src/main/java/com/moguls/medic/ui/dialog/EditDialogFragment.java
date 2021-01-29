package com.moguls.medic.ui.dialog;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.moguls.medic.R;
import com.moguls.medic.callback.EditSlotsListener;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;

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
    public int from = 8;
    public int to = 12;
    public int range = 4;
    private NumberPicker mFromHourPicker;
    private NumberPicker mToHourPicker;
    private NumberPicker mFromMinutePicker;
    private NumberPicker mToMinutePicker;


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
        TimePicker fromtime = (TimePicker)v.findViewById(R.id.fromtime);
        TimePicker to_time = (TimePicker)v.findViewById(R.id.to_time);

        setFromTimePickerInterval(fromtime,from ,to,range - 1);
        setToTimePickerInterval(to_time,from,to,range);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked(null, null,false);
                dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDAte = String.format("%s:%s", mFromHourPicker.getDisplayedValues()[mFromHourPicker.getValue()],
                        mFromMinutePicker.getDisplayedValues()[mFromMinutePicker.getValue()]);
                String endDAte = String.format("%s:%s", mToHourPicker.getDisplayedValues()[mToHourPicker.getValue()],
                        mToMinutePicker.getDisplayedValues()[mToMinutePicker.getValue()]);
                boolean validate = validate(String.format("%s.%s", mFromHourPicker.getDisplayedValues()[mFromHourPicker.getValue()],
                        mFromMinutePicker.getDisplayedValues()[mFromMinutePicker.getValue()]),String.format("%s.%s", mToHourPicker.getDisplayedValues()[mToHourPicker.getValue()],
                        mToMinutePicker.getDisplayedValues()[mToMinutePicker.getValue()]));
                listener.onButtonClicked(startDAte, endDAte,validate);
                dismiss();
            }
        });

        mToMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int toMinValue =  Integer.parseInt(mToHourPicker.getDisplayedValues()[mToHourPicker.getValue()]);
                int compareValue = newVal;
                if(toMinValue == to - 1 && compareValue > 0) {
                    mToHourPicker.setValue(mToHourPicker.getValue() - 1);
                } else if(toMinValue == from && compareValue < 30){
                    mToHourPicker.setValue(mToHourPicker.getValue() + 1);
                    mToMinutePicker.setValue(0);
                }
            }
        });

        mToHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int toMinValue =  Integer.parseInt(mToHourPicker.getDisplayedValues()[mToHourPicker.getValue()]);
                int compareValue = newVal;
                if(toMinValue == to - 1 && compareValue > 0) {
                    mToMinutePicker.setValue(0);
                } else if(toMinValue == from && compareValue < 30) {
                    mToMinutePicker.setValue(30);
                }
            }
        });

    }

    public boolean validate(String fromVal, String toVal) {
        double tempFrom = Double.parseDouble(fromVal);
        double tempTo = Double.parseDouble(toVal);
        if(tempFrom >= tempTo) {
            return true;
        }
        return false;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }
    @SuppressLint("NewApi")
    private void setFromTimePickerInterval(TimePicker timePicker,int from,int to,int range) {
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");

            Field fieldHr = classForid.getField("hour");
            mFromHourPicker = (NumberPicker) timePicker.findViewById(fieldHr.getInt(null));

            //set hours from 9am to 7pm (opening hours)
            mFromHourPicker.setMinValue(0);
            mFromHourPicker.setMaxValue(range);
            mFromHourPicker.setValue(0);
            ArrayList<String> mDisplayedValuesHr = new ArrayList<String>();

            for (int i = from; i < to; i++) {
                mDisplayedValuesHr.add(String.format("%02d", i));
            }

            mFromHourPicker.setDisplayedValues(mDisplayedValuesHr.toArray(new String[0]));

            Field fieldMin = classForid.getField("minute");
            mFromMinutePicker = (NumberPicker) timePicker.findViewById(fieldMin.getInt(null));

            //set minutes in 15 mins interval
            mFromMinutePicker.setMinValue(0);
            mFromMinutePicker.setMaxValue(59);
            mFromMinutePicker.setValue(0);
            ArrayList<String> mDisplayedValuesMin = new ArrayList<String>();

            for (int i = 0; i < 60; i += 1) {
                mDisplayedValuesMin.add(String.format("%02d", i));
            }

            mFromMinutePicker.setDisplayedValues(mDisplayedValuesMin.toArray(new String[0]));
            mFromMinutePicker.setWrapSelectorWheel(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    private void setToTimePickerInterval(TimePicker timePicker,int from,int to,int range) {
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");

            Field fieldHr = classForid.getField("hour");
            mToHourPicker = (NumberPicker) timePicker.findViewById(fieldHr.getInt(null));

            //set hours from 9am to 7pm (opening hours)
            mToHourPicker.setMinValue(0);
            mToHourPicker.setMaxValue(range);
            mToHourPicker.setValue(0);
            ArrayList<String> mDisplayedValuesHr = new ArrayList<String>();
            for (int i = from; i < to; i++) {
                mDisplayedValuesHr.add(String.format("%02d", i));
            }

            mToHourPicker.setDisplayedValues(mDisplayedValuesHr.toArray(new String[0]));

            Field fieldMin = classForid.getField("minute");
            mToMinutePicker = (NumberPicker) timePicker.findViewById(fieldMin.getInt(null));

            //set minutes in 15 mins interval
            mToMinutePicker.setMinValue(0);
            mToMinutePicker.setMaxValue(59);
            mToMinutePicker.setValue(30);
            ArrayList<String> mDisplayedValuesMin = new ArrayList<String>();

            for (int i = 0; i < 60; i += 1) {
                mDisplayedValuesMin.add(String.format("%02d", i));
            }

            mToMinutePicker.setDisplayedValues(mDisplayedValuesMin.toArray(new String[0]));
            mToMinutePicker.setWrapSelectorWheel(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
