package com.moguls.medic.ui.fragments.doctor;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.model.doctorPatients.Result;
import com.moguls.medic.ui.fragments.MainTabFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.adapters.DoctorPatientListAdapter;
import com.moguls.medic.model.PatientList;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetDoctorPatientsViewModel;
import com.moguls.medic.webservices.PostVerifyOtpViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DoctorPatientListFragment extends BaseFragment {

    private DoctorPatientListAdapter doctorPatientListAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<PatientList> rowsArrayList = new ArrayList<>();
    ArrayList<Result> resultArrayList = new ArrayList<>();
    private TextView header_title;
    public GetDoctorPatientsViewModel getDoctorPatientsViewModel;
    private LoadingCompound ld;
    int pageSize = 10;
    int pageNo = 1;
    private EditText search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_patient_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        search = (EditText)v.findViewById(R.id.search);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        setGetDoctorPatientsAPIObserver();
        setBackButtonToolbarStyleOne(v);
        initScrollListener();
        header_title.setText("My Patients");
        getDoctorPatientsViewModel.loadData(pageNo,pageSize,"");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resultArrayList.clear();
                getDoctorPatientsViewModel.loadData(1,10,s.toString());
            }
        });
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        doctorPatientListAdapter = new DoctorPatientListAdapter(getActivity(),
                new DoctorPatientListAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void OnChatClicked(int position) {
                ChatFragment chatFragment = new ChatFragment();
                //chatFragment.setToUserID(resultArrayList.get(position).getID());
                //chatFragment.setName(resultArrayList.get(position).getName());
                home().setFragment(chatFragment);
            }
        });
        makeArrayList(resultArrayList);
        doctorPatientListAdapter.setmItemList(rowsArrayList);
        recyclerView.setAdapter(doctorPatientListAdapter);
    }

    public void makeArrayList(ArrayList<Result> resultArrayLis) {
        Set<Result> hashresultArrayList = new HashSet(resultArrayLis);
        ArrayList<Result> resultArrayList = new ArrayList<Result>(hashresultArrayList);

        ArrayList<String> listHeader = new ArrayList<String>();
        ArrayList<PatientList> list = new ArrayList<>();
        for (Result r : resultArrayList) {
            listHeader.add(r.getName().substring(0,1));
        }
        HashSet uniqueDate = new HashSet(listHeader);
        ArrayList<String> uniqueheaderList = new ArrayList(uniqueDate);
        Collections.sort(uniqueheaderList);
        Collections.reverseOrder();

        PatientList patientList = new PatientList();
        for(int j =0;j<uniqueheaderList.size();j++) {
            String s = uniqueheaderList.get(j);
            for(int i = 0;i<resultArrayList.size();i++) {
                String nameAlphabet = resultArrayList.get(i).getName().substring(0,1);;
                System.out.println("resultArrayList "+doCheckResult(resultArrayList.get(i).getMobileNo(),list));
                boolean isUnique = doCheckResult(resultArrayList.get(i).getMobileNo(),list);
                if(s.toLowerCase().equals(nameAlphabet.toLowerCase())) {
                    if(doCheck(s,list)) {
                        patientList = new PatientList();
                        patientList.setType(DoctorPatientListAdapter.SECTION_VIEW);
                        patientList.setName(nameAlphabet);
                        patientList.setResult(resultArrayList.get(i));
                        list.add(patientList);
                        if(isUnique) {
                            patientList = new PatientList();
                            patientList.setName(nameAlphabet);
                            patientList.setType(DoctorPatientListAdapter.VIEW_TYPE_ITEM);
                            patientList.setResult(resultArrayList.get(i));
                            list.add(patientList);
                        }

                    } else {
                        if(isUnique) {
                            patientList = new PatientList();
                            patientList.setType(DoctorPatientListAdapter.VIEW_TYPE_ITEM);
                            patientList.setName(nameAlphabet);
                            patientList.setResult(resultArrayList.get(i));
                            list.add(patientList);
                        }

                    }
                }
            }
        }
        rowsArrayList.clear();
        rowsArrayList.addAll(list);
    }

    public boolean doCheck(String s, ArrayList<PatientList> list) {
        boolean isUnique = true;
        for (int k = 0;k<list.size();k++){
            if(list.get(k).getName().equalsIgnoreCase(s)) {
                isUnique = false;
            } else  {
                isUnique = true;
            }
        }
        return isUnique;
    }


    public boolean doCheckResult(String s, ArrayList<PatientList> list) {
        boolean isUnique = true;
        for (int k = 0;k<list.size();k++){
            if(list.get(k).getResult() != null) {
                System.out.println("resultArrayList "+list.get(k).getResult().getMobileNo()
                        +" str "+s+" equalsIgnoreCase "+list.get(k).getResult().getMobileNo().equalsIgnoreCase(s));
                if (list.get(k).getResult().getMobileNo().equalsIgnoreCase(s)) {
                    isUnique = false;
                }
            }
        }
        return isUnique;
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        getDoctorPatientsViewModel.loadData(pageNo,pageSize,"");
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        rowsArrayList.add(null);
        doctorPatientListAdapter.notifyItemInserted(rowsArrayList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                doctorPatientListAdapter.notifyItemRemoved(scrollPosition);
                makeArrayList(resultArrayList);
                doctorPatientListAdapter.setmItemList(rowsArrayList);
                doctorPatientListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public void setGetDoctorPatientsAPIObserver() {
        getDoctorPatientsViewModel = ViewModelProviders.of(this).get(GetDoctorPatientsViewModel.class);
        getDoctorPatientsViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getDoctorPatientsViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoadin) {
                if(!isLoading && isLoadin) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getDoctorPatientsViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getDoctorPatientsViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(isLoading) {
                    if(getDoctorPatientsViewModel.doctorsPatients.getResult().size() != 0) {
                        resultArrayList.addAll(getDoctorPatientsViewModel.doctorsPatients.getResult());
                        loadMore();
                    }
                } else {
                    resultArrayList.addAll(getDoctorPatientsViewModel.doctorsPatients.getResult());
                    initAdapter();
                }
                if(getDoctorPatientsViewModel.doctorsPatients.getResult().size() != 0) {
                    pageNo = pageNo + 1;
                }
            }
        });
    }

}