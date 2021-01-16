package com.moguls.medic.ui.fragments.me;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moguls.medic.R;
import com.moguls.medic.callback.DoctorSaveListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.doctorProfileDetails.Personnel;
import com.moguls.medic.model.doctorProfileDetails.Result;
import com.moguls.medic.ui.settings.BaseFragment;


public class DoctorIDProofTabFragment extends BaseFragment implements View.OnClickListener {

    private ImageView reg_proof,degree_proof,id_proof;
    private Uri cameraOutputUri;
    int PICK_ID_PHOTO_PHOTO = 10010;
    int PICK_REG_PHOTO_PHOTO = 10011;
    int PICK_DEGREE_PHOTO_PHOTO = 10012;
    private Result profileInit;
    private EditText statement;
    DoctorSaveListener doctorSaveListener;
    private Button btnSave;

    public void setProfileInit(Result profileInit) {
        this.profileInit = profileInit;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_id_proof, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reg_proof = (ImageView)v.findViewById(R.id.reg_proof);
        degree_proof = (ImageView)v.findViewById(R.id.degree_proof);
        id_proof = (ImageView)v.findViewById(R.id.id_proof);
        statement = (EditText)v.findViewById(R.id.statement);
        btnSave = (Button)v.findViewById(R.id.btnSave);
        statement.setText(profileInit.getPersonnel().getStatement());
        reg_proof.setOnClickListener(this);
        degree_proof.setOnClickListener(this);
        id_proof.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void pickImage(int reqCode) {
        cameraOutputUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
         Intent intent = Helper.getPickIntent(cameraOutputUri,getActivity());
        startActivityForResult(intent, reqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageuri = null;
        if(data != null) {
            imageuri = data.getData();// Get intent
        } else {
            imageuri = cameraOutputUri;
        }
        //String real_Path = Helper.getRealPathFromUri(getActivity(), imageuri);

        if(requestCode == PICK_DEGREE_PHOTO_PHOTO) {
            degree_proof.setImageURI(imageuri);
        } else if(requestCode == PICK_ID_PHOTO_PHOTO) {
            id_proof.setImageURI(imageuri);
        } else if(requestCode == PICK_REG_PHOTO_PHOTO) {
            reg_proof.setImageURI(imageuri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_proof:
                pickImage(PICK_ID_PHOTO_PHOTO);
                break;
            case R.id.reg_proof:
                pickImage(PICK_REG_PHOTO_PHOTO);
                break;
            case R.id.degree_proof:
                pickImage(PICK_DEGREE_PHOTO_PHOTO);
                break;
            case R.id.btnSave:
                Personnel personnel = baseParams.getPersonnel();
                if(!statement.getText().toString().isEmpty()) {
                    personnel.setStatement(statement.getText().toString());
                }
                doctorSaveListener.onButtonClicked();
                break;
        }
    }
}