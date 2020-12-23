package com.moguls.medic.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;

public class ApptActionsDialogFragment extends DialogFragment {
    public static String TAG = "DoctorDialogFragment";
    public NotifyListener listener;
    private View v;
    public static int BUTTON_CANCEL = 1;
    public static int BUTTON_REBOOK = 2;
    public static int BUTTON_VIEWDETAILS = 3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.appt_actions_dialog, container, false);
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
         window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        window.setBackgroundDrawableResource(R.color.transparent);
        window.setBackgroundDrawable(new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 0));
        RelativeLayout cancel_appt = (RelativeLayout)v.findViewById(R.id.cancel_appt);
        RelativeLayout re_schedule = (RelativeLayout)v.findViewById(R.id.re_schedule);
        RelativeLayout view_details = (RelativeLayout)v.findViewById(R.id.view_details);
        cancel_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onButtonClicked(BUTTON_CANCEL);
            }
        });
        re_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onButtonClicked(BUTTON_REBOOK);

            }
        });
        view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onButtonClicked(BUTTON_VIEWDETAILS);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

}
