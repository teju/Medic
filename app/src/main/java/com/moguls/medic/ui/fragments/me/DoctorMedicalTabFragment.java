package com.moguls.medic.ui.fragments.me;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moguls.medic.R;
import com.moguls.medic.callback.DoctorSaveListener;
import com.moguls.medic.callback.PopUpListener;
import com.moguls.medic.model.doctorProfileDetails.Medical;
import com.moguls.medic.model.doctorProfileDetails.Result;
import com.moguls.medic.model.doctorProfileDetails.Specializations;
import com.moguls.medic.ui.adapters.HospitalSpinnerAdapter;
import com.moguls.medic.ui.adapters.SpecializationSpinnerAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DoctorMedicalTabFragment extends BaseFragment implements View.OnClickListener {

    private Result profileInit;
    private TextView specialization,education;
    private EditText reg_council;
    private TextView reg_year;
    private TextView edt_exp_yrs;

    private EditText registration_no;
    private Button save;
    final Calendar myCalendar = Calendar.getInstance();
    DoctorSaveListener doctorSaveListener;
    int choosenYear = 2021;
    private Spinner sp_specialization,sp_qualification;
    private RelativeLayout rl_specialization,rl_qualification;
    private EditText statement;

    public void setProfileInit(Result profileInit) {
        this.profileInit = profileInit;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_medical_tab, container, false);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reg_year = (TextView)v.findViewById(R.id.reg_year);
        registration_no = (EditText)v.findViewById(R.id.registration_no);
        reg_council = (EditText)v.findViewById(R.id.reg_council);
        specialization = (TextView)v.findViewById(R.id.specialization);
        edt_exp_yrs = (TextView) v.findViewById(R.id.edt_exp_yrs);
        sp_specialization = (Spinner) v.findViewById(R.id.sp_specialization);
        sp_qualification = (Spinner) v.findViewById(R.id.sp_qualification);
        rl_specialization = (RelativeLayout) v.findViewById(R.id.rl_specialization);
        rl_qualification = (RelativeLayout) v.findViewById(R.id.rl_qualification);
        statement = (EditText)v.findViewById(R.id.statement);

        education = (TextView)v.findViewById(R.id.education);
        save = (Button)v.findViewById(R.id.save);
        education.setOnClickListener(this);
        save.setOnClickListener(this);
        reg_year.setOnClickListener(this);
        edt_exp_yrs.setOnClickListener(this);
        rl_specialization.setOnClickListener(this);
        rl_qualification.setOnClickListener(this);

        setData();
        initSpzAdapterAdapter();
        initQualificationAdapterAdapter();
    }

    public void containsSpeciaization() {
        try {
            if (profileInit.getMedical().getSpecializations() != null) {
                for (Specializations specializations : profileInit.getSpecializations()) {
                    for (Specializations specializations1 : profileInit.getMedical().getSpecializations())
                    if (specializations1.getName().equals(specializations.getName())) {
                        specializations.setSelected(true);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void containsQualifications() {
        try {
            if (profileInit.getMedical().getQualifications() != null) {
                for (Specializations specializations : profileInit.getQualifications()) {
                    for (Specializations specializations1 : profileInit.getMedical().getQualifications())
                    if (specializations1.getName().equals(specializations.getName())) {
                        specializations.setSelected(true);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void initSpzAdapterAdapter() {
        containsSpeciaization();
        SpecializationSpinnerAdapter aa = new SpecializationSpinnerAdapter(getActivity(), new SpecializationSpinnerAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position,boolean isREmove) {
                specialization.setText(profileInit.getSpecializations().get(position).getName());
                specialization.setTextColor(getActivity().getResources().getColor(R.color.black));
                hideSpinnerDropDown(sp_specialization);
                Specializations specializations = profileInit.getSpecializations().get(position);
                List<Specializations> medicalspecializations = profileInit.getMedical().getSpecializations();
                if(medicalspecializations == null) {
                    medicalspecializations = new ArrayList<>();
                    medicalspecializations.add(specializations);
                } else {
                    if(isREmove) {
                        medicalspecializations.remove(position);
                    } else {
                        medicalspecializations.add(specializations);
                    }
                }
                profileInit.getMedical().setSpecializations(medicalspecializations);

            }
        });
        aa.setResults(profileInit.getSpecializations());
        sp_specialization.setAdapter(aa);
    }

    public void initQualificationAdapterAdapter() {
        containsQualifications();
        SpecializationSpinnerAdapter aa = new SpecializationSpinnerAdapter(getActivity(), new SpecializationSpinnerAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position,boolean isREmove) {
                education.setText(profileInit.getQualifications().get(position).getName());
                education.setTextColor(getActivity().getResources().getColor(R.color.black));
                hideSpinnerDropDown(sp_qualification);
                Specializations specializations = profileInit.getQualifications().get(position);
                List<Specializations> medicalspecializations = profileInit.getMedical().getQualifications();
                if(medicalspecializations == null) {
                    medicalspecializations = new ArrayList<>();
                    medicalspecializations.add(specializations);
                } else {
                    if(isREmove) {
                        medicalspecializations.remove(position);
                    } else {
                        medicalspecializations.add(specializations);
                    }
                }
                profileInit.getMedical().setQualifications(medicalspecializations);

            }
        });
        aa.setResults(profileInit.getQualifications());
        sp_qualification.setAdapter(aa);
    }

    public void setData() {
        try {
            reg_year.setText(profileInit.getMedical().getYear());
            reg_council.setText(profileInit.getMedical().getCouncil().getName());
            registration_no.setText(profileInit.getMedical().getNo());
            if(profileInit.getMedical().getPracticingFrom() != null) {
                edt_exp_yrs.setText(profileInit.getMedical().getPracticingFrom());
            }
            if((profileInit.getMedical().getSpecializations().size() != 0)) {
                specialization.setText(profileInit.getMedical().getSpecializations().get(0).getName());
            }
            if(profileInit.getMedical().getQualifications() != null && profileInit.getMedical().getQualifications().size() != 0) {
                education.setText(profileInit.getMedical().getQualifications().get(0).getName());
            }
            if(!profileInit.getMedical().getStatement().isEmpty()) {
                statement.setText(profileInit.getMedical().getStatement());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void updateLabel() {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edt_exp_yrs.setText(sdf.format(myCalendar.getTime()));

    }
    public boolean validate() {
        if(registration_no.getText().toString().isEmpty()) {
            registration_no.setError("Enter Reg No.");
            registration_no.requestFocus();
            return false;
        } else if(reg_council.getText().toString().isEmpty()) {
            reg_council.setError("Enter Reg Council.");
            reg_council.requestFocus();
            return false;
        }else if(reg_year.getText().toString().isEmpty()) {
            reg_year.setError("Enter Reg Council.");
            reg_year.requestFocus();
            return false;
        }  else if(edt_exp_yrs.getText().toString().isEmpty()) {
            edt_exp_yrs.setError("Enter Your Experience");
            edt_exp_yrs.requestFocus();
            return false;
        }else {
            return true;
        }
    }
    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.specialization :
                showPopUpMenu(profileInit.getMedical().getSpecializations(),specialization);
                break;
            case R.id.education :
                showPopUpMenu(profileInit.getMedical().getQualifications());
                break;
            case R.id.reg_year:
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getActivity(),
                        new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        reg_year.setText(Integer.toString(selectedYear));
                        choosenYear = selectedYear;
                    }
                }, choosenYear, 0);

                builder.showYearOnly()
                        .setYearRange(1990, 2030)
                        .build()
                        .show();
                break;
            case R.id.edt_exp_yrs :
                new DatePickerDialog(getActivity(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.rl_specialization:
                sp_specialization.performClick();
                break;
            case R.id.rl_qualification:
                sp_qualification.performClick();
                break;
            case R.id.save:
                if(validate()) {
                    Medical medical = profileInit.getMedical();
                    Specializations specializations = medical.getCouncil();
                    specializations.setName(reg_council.getText().toString());
                    medical.setCouncil(specializations);
                    medical.setNo(registration_no.getText().toString());
                    medical.setYear(reg_year.getText().toString());
                    medical.setPracticingFrom(edt_exp_yrs.getText().toString());
                    medical.setSpecializations(profileInit.getMedical().getSpecializations());
                    if(!statement.getText().toString().isEmpty()) {
                        medical.setStatement(statement.getText().toString());
                    }
                    baseParams.setMedical(medical);
                    doctorSaveListener.onButtonClicked();
                }
                break;
        }

    }
    public void showPopUpMenu(List<Specializations> arrayList, TextView specialization) {
        PopupMenu popup = new PopupMenu(getActivity(), specialization);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.relation_popup, popup.getMenu());
        for(Specializations s : arrayList) {
            popup.getMenu().add(Menu.NONE, 1, Menu.NONE, s.getName());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                specialization.setText(item.getTitle());
                return true;
            }
        });

        popup.show();//showing popup menu
    }
    public void showPopUpMenu(List<Specializations> arrayList) {
        PopupMenu popup = new PopupMenu(getActivity(), education);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.relation_popup, popup.getMenu());
        for(Specializations s : arrayList) {
            popup.getMenu().add(Menu.NONE, 1, Menu.NONE, s.getName());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                education.setText(item.getTitle());
                return true;
            }
        });

        popup.show();//showing popup menu
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