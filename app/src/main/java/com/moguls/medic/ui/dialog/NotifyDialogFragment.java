package com.moguls.medic.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.Helper;

public class NotifyDialogFragment extends DialogFragment {
    public static String TAG = "NotifyDialogFragment";
    public static int BUTTON_POSITIVE = 1;
    public static int BUTTON_NEGATIVE = 0;
    public String notify_tittle = "";
    public String notify_messsage  = "";
    public String button_positive  = "";
    public String button_negative = "";
    public NotifyListener listener;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_notify, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setBackgroundDrawable(new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20));

        TextView vw_title = (TextView) v.findViewById(R.id.vw_text);
        TextView vw_text = (TextView) v.findViewById(R.id.vw_title);
        Button btn_positive = (Button) v.findViewById(R.id.btn_positive);
        Button btn_negative = (Button) v.findViewById(R.id.btn_negative);
        if(Helper.isEmpty(notify_tittle)){
            vw_title.setVisibility(View.GONE);
        }else{
            vw_title.setVisibility(View.VISIBLE);
            vw_title.setText(notify_tittle);
        }
        if(!Helper.isEmpty(notify_messsage)){
            vw_text.setVisibility(View.VISIBLE);
            vw_text.setText(notify_messsage);
        }

        btn_positive.setText(button_positive) ;
        btn_negative.setText(button_negative);
        if(btn_negative.getText() != "") {
            btn_negative.setVisibility(View.VISIBLE);
        } else {
            btn_negative.setVisibility(View.GONE);
        }
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onButtonClicked(BUTTON_POSITIVE);
                    dismiss();
                }
            }
        });
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onButtonClicked(BUTTON_NEGATIVE);
                    dismiss();
                }
            }
        });
    }
}
