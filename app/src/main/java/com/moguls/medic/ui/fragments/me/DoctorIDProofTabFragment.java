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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.DoctorSaveListener;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.model.doctorProfileDetails.IDProof;
import com.moguls.medic.model.doctorProfileDetails.Medical;
import com.moguls.medic.model.doctorProfileDetails.Personnel;
import com.moguls.medic.model.doctorProfileDetails.Registration;
import com.moguls.medic.model.doctorProfileDetails.Result;
import com.moguls.medic.ui.adapters.DegreeImageAdapter;
import com.moguls.medic.ui.settings.BaseFragment;

import java.util.List;


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
    private String degree_real_Path = "",id_real_Path ="",reg_real_Path = "";
    private RecyclerView recyclerView;
    private Registration registrations;
    private int selImageposition;
    private DegreeImageAdapter degreeImageAdapter;

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
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        statement.setText(profileInit.getMedical().getStatement());
        reg_proof.setOnClickListener(this);
        //degree_proof.setOnClickListener(this);
        id_proof.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        setData();

    }
    public void setData() {
        IDProof idProof = profileInit.getIDProof();
        Helper.loadImage(getActivity(),idProof.getPhotoIdentity().getDocumentUrl(),R.drawable.camera_plus,id_proof);
        Helper.loadImage(getActivity(),idProof.getRegistration().getDocumentUrl(),R.drawable.camera_plus,reg_proof);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        degreeImageAdapter = new DegreeImageAdapter(getActivity(), new DegreeImageAdapter.OnClickListner() {
            @Override
            public void OnClick(int position) {
                pickImage(PICK_DEGREE_PHOTO_PHOTO);
                selImageposition = position;


            }
        });
        degreeImageAdapter.mItemList =  profileInit.getIDProof().getDegrees();
        recyclerView.setAdapter(degreeImageAdapter);
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
        if(requestCode != 0) {
            Uri imageuri = null;
            if (data != null) {
                imageuri = data.getData();// Get intent
            } else {
                imageuri = cameraOutputUri;
            }
            if (requestCode == PICK_DEGREE_PHOTO_PHOTO) {
                degree_real_Path = Helper.getRealPathFromUri(getActivity(), imageuri);
            } else if (requestCode == PICK_ID_PHOTO_PHOTO) {
                id_real_Path = Helper.getRealPathFromUri(getActivity(), imageuri);
                id_proof.setImageURI(imageuri);
            } else if (requestCode == PICK_REG_PHOTO_PHOTO) {
                reg_real_Path = Helper.getRealPathFromUri(getActivity(), imageuri);
                reg_proof.setImageURI(imageuri);
            }
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
                Medical personnel = profileInit.getMedical();
                if(!statement.getText().toString().isEmpty()) {
                    personnel.setStatement(statement.getText().toString());
                }
                IDProof idProof = profileInit.getIDProof();
                if(!reg_real_Path.isEmpty()) {
                    idProof.getRegistration().setDocumentUrl(reg_real_Path);
                } else if(!id_real_Path.isEmpty()) {
                    idProof.getPhotoIdentity().setDocumentUrl(id_real_Path);
                }else if(!degree_real_Path.isEmpty()) {
                    idProof.getDegrees().get(selImageposition).setDocumentUrl(degree_real_Path);
                }
                baseParams.setIdProof(idProof);
                baseParams.setMedical(personnel);
                doctorSaveListener.onButtonClicked();
                break;
        }
    }
}