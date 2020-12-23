package com.moguls.medic.ui.fragments.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.ui.adapters.ChatAdapter;
import com.moguls.medic.ui.service.SignalRService;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetChatViewModel;
import com.moguls.medic.webservices.PostSendMessageViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Single;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.http.Request;


public class ChatFragment extends BaseFragment implements View.OnClickListener {

    private DrawerLayout drawer;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private TextView header_title;
    private GetChatViewModel getChatViewModel;
    private PostSendMessageViewModel postSendMessageViewModel;
    private LoadingCompound ld;
    String PastMessages = "false";
    String ToUserID = "";
    ImageView send_msg;
    private EditText message;
    private SignalRService mService;
    private boolean mBound = false;
    private HubConnection hubConnection;

    public void setPastMessages(String pastMessages) {
        PastMessages = pastMessages;
    }

    public void setToUserID(String toUserID) {
        ToUserID = toUserID;
    }

    public void setMessageID(String messageID) {
        MessageID = messageID;
    }

    String MessageID = "";

    public void setName(String name) {
        Name = name;
    }

    String Name = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chat, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetChatAPIObserver();
        setSendMessageAPIObserver();
        setBackButtonToolbarStyleOne(v);
        header_title = (TextView)v.findViewById(R.id.header_title);
        message = (EditText)v.findViewById(R.id.message);
        send_msg = (ImageView) v.findViewById(R.id.send_msg);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        header_title.setText(Name);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        getChatViewModel.loadData(PastMessages,"10",ToUserID,MessageID);
        send_msg.setOnClickListener(this);
        chatSettings();
    }

    @Override
    public void onStop() {
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
        super.onStop();
    }

    public void chatSettings() {
//        try {
//
//            hubConnection = HubConnectionBuilder.create(APIs.ChatUrl)
//                    .withAccessTokenProvider(Single.defer(() -> {
//                        // Your logic here.
//                        return Single.just("Bearer "
//                                + SharedPreference.getString(getActivity(), BaseKeys.Authorization));
//                    })).build();
//            hubConnection.on("ReceiveMessage", (message) -> {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }, String.class);
//            new HubConnectionTask().execute(hubConnection);
//        } catch (Exception e){
//
//        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), SignalRService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,true));
        if(getChatViewModel.chat != null) {
            chatAdapter = new ChatAdapter(getChatViewModel.chat.getResult(), new ChatAdapter.OnClickListner() {
                @Override
                public void OnClick(int position) {

                }
            });
        }
        recyclerView.setAdapter(chatAdapter);
    }

    public void setGetChatAPIObserver() {
        getChatViewModel = ViewModelProviders.of(this).get(GetChatViewModel.class);
        getChatViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getChatViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getChatViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getChatViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getChatViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
            }
        });
    }

    public void setSendMessageAPIObserver() {
        postSendMessageViewModel = ViewModelProviders.of(this).get(PostSendMessageViewModel.class);
        postSendMessageViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postSendMessageViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postSendMessageViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postSendMessageViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postSendMessageViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                message.setText("");
                getChatViewModel.loadData(PastMessages,"10",ToUserID,MessageID);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_msg :
                if (mBound) {
                // Call a method from the SignalRService.
                // However, if this call were something that might hang, then this request should
                // occur in a separate thread to avoid slowing down the activity performance.
                    if(!message.getText().toString().isEmpty()){
                      //  hubConnection.send("Send", message);

                        // postSendMessageViewModel.loadData(ToUserID,message.getText().toString(),new JSONObject());
                        mService.sendMessage(message.getText().toString());

                    }
            }

                break;
        }
    }
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            hubConnection.start().blockingAwait();
            return null;
        }
    }
}
