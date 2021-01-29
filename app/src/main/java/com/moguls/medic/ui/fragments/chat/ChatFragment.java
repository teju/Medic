package com.moguls.medic.ui.fragments.chat;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.APIs;
import com.moguls.medic.etc.BaseKeys;
import com.moguls.medic.etc.Helper;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.SharedPreference;
import com.moguls.medic.model.chat.Chat;
import com.moguls.medic.model.chat.Result;
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

    private static final int PHOTO_IMAGE = 1004;
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
    ImageView send_msg,attach,logo;
    private EditText message;
    private SignalRService mService;
    private boolean mBound = false;
    private Uri cameraOutputUri;
    private String real_Path;
    String imageUrl = "";
    private String photo = "";
    private ImageView selectedImage,close;
    private RelativeLayout rlImage,rlchat;

    public void setPastMessages(String pastMessages) {
        PastMessages = pastMessages;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
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
        rlImage = (RelativeLayout)v.findViewById(R.id.rlImage);
        rlchat = (RelativeLayout)v.findViewById(R.id.rlchat);
        header_title = (TextView)v.findViewById(R.id.header_title);
        message = (EditText)v.findViewById(R.id.message);
        send_msg = (ImageView) v.findViewById(R.id.send_msg);
        attach = (ImageView) v.findViewById(R.id.attach);
        selectedImage = (ImageView) v.findViewById(R.id.selectedImage);
        close = (ImageView) v.findViewById(R.id.close);
        logo = (ImageView) v.findViewById(R.id.logo);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        header_title.setText(Name);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        getChatViewModel.loadData(PastMessages,"10",ToUserID,MessageID);
        send_msg.setOnClickListener(this);
        attach.setOnClickListener(this);
        close.setOnClickListener(this);
        Helper.loadImage(getActivity(), imageUrl,
                R.drawable.doctor_profile_pic_default,logo);
        rlchat.setVisibility(View.VISIBLE);
        rlImage.setVisibility(View.GONE);
        chatSettings();
        chatReceiveSettings();
    }

    @Override
    public void onStop() {
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
        super.onStop();
    }
    public void pickImage() {
        cameraOutputUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Intent intent = Helper.getPickIntent(cameraOutputUri,getActivity());
        startActivityForResult(intent, PHOTO_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != 0) {
            Uri imageuri = null;
            if (data != null) {
                imageuri = data.getData();// Get intent
            } else {
                imageuri = cameraOutputUri;
            }
            real_Path = Helper.getRealPathFromUri(getActivity(), imageuri);
            rlImage.setVisibility(View.VISIBLE);
            rlchat.setVisibility(View.GONE);
            selectedImage.setImageURI(imageuri);
        }

    }
    public void chatReceiveSettings() {
        hubConnection.on("ReceiveMessage", (user, message) -> {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new GsonBuilder().create();
                    try {
                        JSONObject newmessage = new JSONObject(message);
                        Result Newchat = gson.fromJson(newmessage.toString(), Result.class);
                        getChatViewModel.chat.getResult().add(getChatViewModel.chat.getResult().size(), Newchat);
                        chatAdapter.mItemList = getChatViewModel.chat.getResult();
                        chatAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }, String.class, String.class);

    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,true));
        if(getChatViewModel.chat != null) {
            chatAdapter = new ChatAdapter( getActivity(),new ChatAdapter.OnClickListner() {
                @Override
                public void OnClick(int position) {

                }
            });
        }
        chatAdapter.mItemList = getChatViewModel.chat.getResult();
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
                rlImage.setVisibility(View.GONE);
                rlchat.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_msg :
                    String sendingMsg = "";
                    if(!message.getText().toString().isEmpty()){
                        sendingMsg = message.getText().toString();
                    }
                    rlImage.setVisibility(View.GONE);
                    rlchat.setVisibility(View.VISIBLE);
                    hubConnection.send("Send", sendingMsg);
                    postSendMessageViewModel.loadData(ToUserID,sendingMsg,real_Path);
                    message.setText("");

                break;
            case R.id.attach:
                pickImage();
                break;
            case R.id.close:
                rlImage.setVisibility(View.GONE);
                rlchat.setVisibility(View.VISIBLE);
                real_Path = null;
                break;
        }
    }
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
