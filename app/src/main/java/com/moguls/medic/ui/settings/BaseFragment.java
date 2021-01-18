package com.moguls.medic.ui.settings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.moguls.medic.activity.MainActivity;
import com.moguls.medic.R;
import com.moguls.medic.callback.EditSlotsListener;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.callback.OtpListener;
import com.moguls.medic.callback.PermissionListener;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.etc.APIs;
import com.moguls.medic.etc.Constants;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.model.BaseParams;
import com.moguls.medic.ui.dialog.EditDialogFragment;
import com.moguls.medic.ui.fragments.LoginFragment;
import com.moguls.medic.ui.dialog.ConfirmBookingDialogFragment;
import com.moguls.medic.ui.dialog.ApptActionsDialogFragment;
import com.moguls.medic.ui.dialog.NotifyDialogFragment;
import com.moguls.medic.ui.dialog.OtpDialogFragment;
import com.moguls.medic.model.BookingData;
import com.moguls.medic.model.getrelations.Result;

import java.lang.reflect.Method;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;


public class BaseFragment extends GenericFragment {

    List<String> permissionsThatNeedTobeCheck = new ArrayList<>();
    public PermissionListener permissionListener;
    public View  v = null;
    public BaseParams baseParams = new BaseParams();
    public void onBackTriggered() {
        home().proceedDoOnBackPressed();
    }

    public MainActivity home() {
        return (MainActivity)getActivity();
    }

    @NonNull
    public Observer obsNoInternet = (Observer)(new Observer() {
        // $FF: synthetic method
        // $FF: bridge method
        public void onChanged(Object var1) {
            this.onChanged((Boolean)var1);
        }

        public final void onChanged(Boolean isHaveInternet) {
            try {
                if (!isHaveInternet) {
                    if (BaseFragment.this.getActivity() == null) {
                        return;
                    }
                    BaseFragment.this.showNotifyDialog(getActivity().getString(R.string.no_internet),
                            getString(R.string.no_connection), "OK",
                            "", (NotifyListener)(new NotifyListener() {
                        public void onButtonClicked(int which) {
                        }
                    }));
                }
            } catch (Exception var3) {
            }

        }
    });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> permissions = new ArrayList<String>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkPermissions(permissions, permissionListener);
    }

    public void checkPermissions(List<String> permissionsThatNeedTobeCheck, PermissionListener permissionListener) {
        this.permissionsThatNeedTobeCheck = permissionsThatNeedTobeCheck;
        this.permissionListener = permissionListener;
        ArrayList<String> permissionsNeeded =new  ArrayList<String>();
        List<String> permissionsList = permissionsThatNeedTobeCheck;
        try {
            for (String s : permissionsThatNeedTobeCheck) {
                if (s.equals(Manifest.permission.CAMERA)) {
                    if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                        permissionsNeeded.add("Camera");
                    else if (s.equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
                            permissionsNeeded.add("ACCESS COARSE LOCATION");
                    } else if (s.equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                            permissionsNeeded.add("ACCESS FINE LOCATION");
                    }
                }
            }
        } catch (Exception e){

        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        permissionsList.toArray(new String[permissionsList.size()]),
                        Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            ActivityCompat.requestPermissions(
                    getActivity(), permissionsList.toArray(new String[permissionsList.size()]),
                    Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        } else {
            permissionListener.onPermissionAlreadyGranted();
        }
    }

    public Boolean addPermission(List<String> permissionsList,String permission)  {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission))
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {

                Boolean isAllGranted = false;
                int index = 0;
                for (String permission : permissionsThatNeedTobeCheck) {
                    if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                            isAllGranted = false;
                            break;
                        } else {
                            isAllGranted = true;
                        }
                    } else if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                            isAllGranted = false;
                            break;
                        } else {
                            isAllGranted = true;
                        }
                    }

                }
                if (isAllGranted) {
                    permissionListener.onCheckPermission(permissions[index], true);
                } else {
                    permissionListener.onCheckPermission(permissions[index], false);
                }
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public void setBackButtonToolbarStyleOne(View v) {
        try {
            RelativeLayout llBack = v.findViewById(R.id.llBack);

                    llBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            home().onBackPressed();
                        }
                    });
        } catch (Exception e) {
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
            f.show(getActivity().getSupportFragmentManager(), NotifyDialogFragment.TAG);
        }
    }
    public void showOtpVerifyDialog(OtpListener n) {
        OtpDialogFragment f = new OtpDialogFragment();
        f.listener = n;
        f.setCancelable(false);
        f.show(getActivity().getSupportFragmentManager(), NotifyDialogFragment.TAG);

    }
    public void showEDitSlotsDialog(EditSlotsListener n) {
        EditDialogFragment f = new EditDialogFragment();
        f.listener = n;
        f.setCancelable(false);
        f.show(getActivity().getSupportFragmentManager(), EditDialogFragment.TAG);

    }

    public void showBookingConfirmyDialog(String refNo, BookingData bookingData, NotifyListener n) {
        ConfirmBookingDialogFragment f = new ConfirmBookingDialogFragment();
        f.listener = n;
        f.bookingData = bookingData;
        f.refNo = refNo;
        f.setCancelable(false);
        f.show(getActivity().getSupportFragmentManager(), ConfirmBookingDialogFragment.TAG);

    }
    public void showDialogDoctor(NotifyListener n) {
        ApptActionsDialogFragment f = new ApptActionsDialogFragment();
        f.listener = n;
        f.setCancelable(true);
        f.show(getActivity().getSupportFragmentManager(), ConfirmBookingDialogFragment.TAG);

    }

    public void showPopUpMenu(Context context, View button, int menu, PopUpListener popUpListener) {
        PopupMenu popup = new PopupMenu(context, button);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                popUpListener.onButtonClicked(item.getTitle().toString());
                return true;
            }
        });

        popup.show();//showing popup menu
    }
    public void showPopUpMenu(Context context, View button, int menu, List<Result> arrayList, PopUpListener popUpListener) {
        PopupMenu popup = new PopupMenu(context, button);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(menu, popup.getMenu());
        for(Result s : arrayList) {
            popup.getMenu().add(Menu.NONE, 1, Menu.NONE, s.getName());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                popUpListener.onButtonClicked(item.getTitle().toString());
                return true;
            }
        });

        popup.show();//showing popup menu
    }
    public void logOut(Context context) {
        try {
            home().clearFragment();
            SharedPreference.setBoolean(context, SharedPreference.isLoggedIn, false);
            home().setFragment(new LoginFragment());
        } catch (Exception e){
            e.printStackTrace();
        }
        showNotifyDialog("",
               getString(R.string.conn_timeout), "OK",
                "", (NotifyListener) (new NotifyListener() {
                    public void onButtonClicked(int which) {

                    }
                }));

    }
    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}