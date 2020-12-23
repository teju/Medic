package com.moguls.medic.ui.fragments.doctor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.PostAddDoctorViewModel;


public class PatientAddDoctorFragment extends BaseFragment implements View.OnClickListener  {

    private CodeScanner mCodeScanner;
    public PostAddDoctorViewModel postAddDoctorViewModel;
    private LoadingCompound ld;
    private static final int RC_PERMISSION = 10;
    private boolean mPermissionGranted;
    String doctor_id = "";
    private CodeScannerView scannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_doctor, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObservers();
        setBackButtonToolbarStyleOne(v);
        TextView header_title = (TextView) v.findViewById(R.id.header_title);
        header_title.setText("Scan QrCode");
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        scannerView = v.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        initCamera();
        mCodeScanner.startPreview();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden) {
            mCodeScanner.releaseResources();
            mCodeScanner.stopPreview();
        } else {
            mCodeScanner.startPreview();
        }
    }

    public void initCamera() {

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (result.getRawBytes() != null) {
                                String[] rawBytes = result.getText().split(",");
                                if (rawBytes.length != 0) {
                                    String[] splitIds = rawBytes[rawBytes.length - 1].split("-");
                                    if(splitIds.length != 0) {
                                        doctor_id = splitIds[0];
                                        if(splitIds.length > 1) {
                                            postAddDoctorViewModel.loadData(splitIds[0], splitIds[1]);
                                        } else {
                                            postAddDoctorViewModel.loadData(splitIds[0], "");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e){

                        }
                    }
                });
            }
        });
        mCodeScanner.setErrorCallback(error -> getActivity().runOnUiThread(
                () -> Toast.makeText(getActivity(), getString(R.string.scanner_error, error), Toast.LENGTH_LONG).show()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }
    public void setObservers() {
        setPostAddDoctorAPIObserver();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

        }
    }

    public void setPostAddDoctorAPIObserver() {
        postAddDoctorViewModel = ViewModelProviders.of(this).get(PostAddDoctorViewModel.class);
        postAddDoctorViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
            @Override
            public void onChanged(BaseViewModel.ErrorMessageModel errorMessageModel) {
                showNotifyDialog(errorMessageModel.title,
                        errorMessageModel.message, "OK",
                        "", (NotifyListener) (new NotifyListener() {
                            public void onButtonClicked(int which) {

                            }
                        }));
            }
        });
        postAddDoctorViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postAddDoctorViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postAddDoctorViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postAddDoctorViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                PatientDoctorProfileFragment patientDoctorProfileFragment = new PatientDoctorProfileFragment();
                patientDoctorProfileFragment.setIsPRofileView(false);
                patientDoctorProfileFragment.setDoctorID(doctor_id);
                home().setFragment(patientDoctorProfileFragment);
            }
        });
    }

}