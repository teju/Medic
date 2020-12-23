package com.moguls.medic.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moguls.medic.R;
import com.moguls.medic.callback.OtpListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.SpannString;

import java.util.ArrayList;
import java.util.List;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpDialogFragment extends DialogFragment {
    public static String TAG = "NotifyDialogFragment";
    public  static  int BUTTON_POSITIVE = 1;
    public  static int BUTTON_NEGATIVE = 0;
    public  static int BUTTON_RESENDOTP = 2;
    public OtpListener listener;
    private View v;
    private OtpTextView otpTextView;
    private TextView resendOtp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_otp_verify, container, false);
        return v;
    }


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

        Button btnsubmit = (Button) v.findViewById(R.id.btnsubmit);
        Button btncancel = (Button) v.findViewById(R.id.btncancel);
        resendOtp = (TextView) v.findViewById(R.id.resendOtp);
        OtpTextView otpTextView = (OtpTextView)v.findViewById(R.id.otp_view);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
            }
        });
        if(btncancel.getText() != "") {
            btncancel.setVisibility(View.VISIBLE);
        } else {
            btncancel.setVisibility(View.GONE);
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onButtonClicked(otpTextView.getOTP(),BUTTON_POSITIVE);
                }
                dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClicked(otpTextView.getOTP(),BUTTON_RESENDOTP);

            }
        });
        spanStringArr();
    }

    public void spanStringArr(){
        List<SpannString> spannStrings = new ArrayList<>();
        SpannString spannString = new SpannString();
        spannString.setString("Didn't receive OTP? ");
        spannString.setColour(getResources().getColor(R.color.dark_gray));
        spannStrings.add(spannString);
        spannString = new SpannString();
        spannString.setString("Resend OTP");
        spannString.setColour(getResources().getColor(R.color.colorAccent));
        spannStrings.add(spannString);
        resendOtp.setText(Helper.spanString(spannStrings), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);


    }
}
