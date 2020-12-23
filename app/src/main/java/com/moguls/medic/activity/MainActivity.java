package com.moguls.medic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.MainTabFragment;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.fragments.LoginFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import static com.moguls.medic.etc.SharedPreference.isLoggedIn;

public class MainActivity extends AppCompatActivity {

    int MAIN_FLOW_INDEX = 0;
    String MAIN_FLOW_TAG = "MainFlowFragment";
    private Fragment currentFragment;
    private ImageView logo_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.activity_main);
        getcurrentFragment();
        logo_icon = (ImageView)findViewById(R.id.logo_icon);
        new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               logo_icon.setVisibility(View.GONE);
               triggerMainProcess();
           }
       },2 * 2000);
        addFCMBroadcastReceiver();
    }

    public void addFCMBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseKeys.ACTION_NEW_MESSAGE_RECEIVED);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.FCMBroadcastReceiver, filter);

    }
    private BroadcastReceiver FCMBroadcastReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, final Intent intent) {

        }
    };

    public void triggerMainProcess() {
        if (SharedPreference.getBoolean(this, isLoggedIn)){
            setFragment(new MainTabFragment());
        } else {
            setFragment(new LoginFragment());
        }
    }

    public void clearFragment() {
        getSupportFragmentManager().popBackStack(MAIN_FLOW_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        for (int i =MAIN_FLOW_INDEX; i >=0 ;i--) {
            try {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(MAIN_FLOW_TAG + i);
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
            } catch (Exception e) {
            }
        }
        getSupportFragmentManager().popBackStack("MAIN_TAB", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        MAIN_FLOW_INDEX = 0;
    }

    public void   getcurrentFragment() {
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.layoutFragment);
    }

    public void setFragment(Fragment fragment) {
        try {
            FragmentTransaction f = getSupportFragmentManager().beginTransaction();
            List<Fragment> list = getSupportFragmentManager().getFragments();
            for(Fragment frag : list){
                if(frag.isVisible()){
                    f.hide(frag);
                }
            }

            MAIN_FLOW_INDEX = MAIN_FLOW_INDEX + 1;
            f.add(R.id.layoutFragment, fragment, MAIN_FLOW_TAG + MAIN_FLOW_INDEX).addToBackStack(
                    MAIN_FLOW_TAG
            ).commitAllowingStateLoss();
            Helper.hideKeyboard(this);
        } catch (Exception e) {
            System.out.println("setFragmentException "+e.toString());
            e.printStackTrace();
        }


    }

    public void resetAndGoToFragment( Fragment frag) {
        clearFragment();
        setFragment(frag);
    }
    public void resetAndExit() {
        showNotifyDialog("Are you sure you want to exit ?",
                "", "OK",
                "Cancel", (NotifyListener) (new NotifyListener() {
                    public void onButtonClicked(int which) {
                        if(which == NotifyDialogFragment.BUTTON_POSITIVE) {
                            clearFragment();
                            finish();
                        }
                    }
                }));

    }

    public void backToMainScreen(){
        try {
            clearFragment();
        } catch (Exception e){
            e.printStackTrace();
        }
        setFragment(new MainTabFragment());
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        List<Fragment> list = getSupportFragmentManager().getFragments();
        Boolean foundVisible = false;
        for (int i =0;i< list.size() ;i++){
            if(list.get(i).isVisible()){
                if(list.get(i) instanceof BaseFragment) {
                    foundVisible = true;
                    ((BaseFragment)(list.get(i))).onBackTriggered();
                }
            }
        }
        if(!foundVisible)
            proceedDoOnBackPressed();
    }

    public void proceedDoOnBackPressed(){
        Helper.hideKeyboard(this);
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        List<Fragment> list  = getSupportFragmentManager().getFragments();
        try {
            for (Fragment frag : list) {
                if (frag.getTag().contentEquals(MAIN_FLOW_TAG + (MAIN_FLOW_INDEX - 1))) {
                    f.show(frag);
                }
            }
        }catch (Exception e){

        }

        if (getSupportFragmentManager().getBackStackEntryCount() <= 1
                || (currentFragment instanceof LoginFragment) || (currentFragment instanceof MainTabFragment)) {
            finish();
        } else {
            super.onBackPressed();
        }

        MAIN_FLOW_INDEX = MAIN_FLOW_INDEX - 1;
    }
    public void setOrShowExistingFragmentByTag(
            int layoutId ,
            String fragTag,
            String backstackTag,
            Fragment newFrag,
            ArrayList<String> listFragmentTagThatNeedToHide) {

        Boolean foundExistingFragment = false;

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            for (int i=0;i< getSupportFragmentManager().getFragments().size();i++) {

                try {
                    Fragment f = getSupportFragmentManager().getFragments().get(i);

                    for (String tag : listFragmentTagThatNeedToHide ) {
                        try {
                            if (tag.toString().toLowerCase().equals(tag.toLowerCase())) {
                                transaction.hide(f);
                            }
                        } catch ( Exception e) {

                        }

                    }

                } catch (Exception e) {
                }

            }

            try {
                transaction.show(fragment).commit();
            } catch (Exception e) {
                try {
                    transaction.show(fragment).commitAllowingStateLoss();
                } catch (Exception e1) { }

            }

            foundExistingFragment = true;

        }

        if (foundExistingFragment) {
            Fragment _fragment = getSupportFragmentManager().findFragmentByTag(fragTag);
            if(_fragment != null)
                getSupportFragmentManager().beginTransaction().remove(_fragment).commit();
        }
        setFragmentInFragment(layoutId, newFrag, fragTag, backstackTag);


    }
    public void setFragmentInFragment(int fragmentLayout, Fragment frag, String tag, String backstackTag) {
        try {
            getSupportFragmentManager().beginTransaction().replace(fragmentLayout, frag, tag).addToBackStack(backstackTag)
                    .commit();
            Helper.hideKeyboard(this);
        } catch (Exception e) {
            try {
                getSupportFragmentManager().beginTransaction().replace(fragmentLayout, frag, tag).addToBackStack(backstackTag)
                        .commitAllowingStateLoss();
                Helper.hideKeyboard(this);
            } catch (Exception e1) {
            }
        }
    }
    public void showNotifyDialog(String tittle, String messsage, String button_positive, String button_negative, NotifyListener n) {
        NotifyDialogFragment f = new NotifyDialogFragment();
        f.listener = n;

        f.notify_tittle = tittle;
        f.notify_messsage = messsage;
        f.button_positive = button_positive;
        f.button_negative = button_negative;
        f.setCancelable(false);
        if(!Helper.isEmpty(tittle) || !Helper.isEmpty(messsage)) {
            f.show(getSupportFragmentManager(), NotifyDialogFragment.TAG);
        }
    }

}