package com.moguls.medic.ui.fragments.tabs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moguls.medic.R;
import com.moguls.medic.callback.NotifyListener;
import com.moguls.medic.etc.LoadingCompound;
import com.moguls.medic.etc.RecyclerTouchListener;
import com.moguls.medic.ui.adapters.ChatListAdapter;
import com.moguls.medic.ui.settings.BaseFragment;
import com.moguls.medic.ui.fragments.chat.ChatFragment;
import com.moguls.medic.webservices.BaseViewModel;
import com.moguls.medic.webservices.GetMyChatListViewModel;
import com.moguls.medic.webservices.PostSetAsDeleteViewModel;
import com.moguls.medic.webservices.PostSetAsReadViewModel;

import java.util.ArrayList;


public class ChatListFragment extends BaseFragment implements ChatListAdapter.OnClickListner {

    private DrawerLayout drawer;
    private ChatListAdapter chatAdapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    private TextView header_title,no_list;
    private RecyclerTouchListener touchListener;
    private int selectedPos = -1;

    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }
    private GetMyChatListViewModel getMyChatListViewModel;
    private PostSetAsReadViewModel postSetAsReadViewModel;
    private PostSetAsDeleteViewModel postSetAsDeleteViewModel;
    private LoadingCompound ld;

    private BottomNavigationView bottomNavigation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetMyChatAPIObserver();
        setAsReadAPIObserver();
        setAsDeleteAPIObserver();

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        header_title = (TextView)v.findViewById(R.id.header_title);
        no_list = (TextView)v.findViewById(R.id.no_list);
        ld = (LoadingCompound)v.findViewById(R.id.ld);
        setBackButtonToolbarStyleOne(v);
        header_title.setText("My Chats");
        getMyChatListViewModel.loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        getMyChatListViewModel.loadData();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(getMyChatListViewModel.myChat != null) {
            chatAdapter = new ChatListAdapter(getMyChatListViewModel.myChat.getResult(), this,getActivity());
        }
        recyclerView.setAdapter(chatAdapter);
        touchListener = new RecyclerTouchListener(getActivity(),recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task)
                .setSwipeable(R.id.cv, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                if(getMyChatListViewModel.myChat.getResult().size() != 0) {
                                    postSetAsDeleteViewModel.loadData(getMyChatListViewModel.myChat.getResult().get(position).getID(),"");
                                }
                                break;
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
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
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }
    private void populateData() {
        int i = 0;
        while (i < 10) {
            rowsArrayList.add("Item " + i);
            i++;
        }
    }

    private void loadMore() {
        rowsArrayList.add(null);
        chatAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                chatAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                chatAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }
    public void setGetMyChatAPIObserver() {
        getMyChatListViewModel = ViewModelProviders.of(this).get(GetMyChatListViewModel.class);
        getMyChatListViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        getMyChatListViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        getMyChatListViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        getMyChatListViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        getMyChatListViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                initAdapter();
                if(getMyChatListViewModel.myChat.getResult().size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    no_list.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    no_list.setVisibility(View.GONE);
                }
            }
        });
    }
    public void setAsReadAPIObserver() {
        postSetAsReadViewModel = ViewModelProviders.of(this).get(PostSetAsReadViewModel.class);
        postSetAsReadViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postSetAsReadViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postSetAsReadViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postSetAsReadViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postSetAsReadViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setName(getMyChatListViewModel.myChat.getResult().get(selectedPos).getName());
                chatFragment.setToUserID(getMyChatListViewModel.myChat.getResult().get(selectedPos).getID());
                home().setFragment(chatFragment);
            }
        });
    }

    public void setAsDeleteAPIObserver() {
        postSetAsDeleteViewModel = ViewModelProviders.of(this).get(PostSetAsDeleteViewModel.class);
        postSetAsDeleteViewModel.errorMessage.observe(this, new Observer<BaseViewModel.ErrorMessageModel>() {
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
        postSetAsDeleteViewModel.isOauthExpired.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                logOut(getActivity());
            }
        });
        postSetAsDeleteViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    ld.showLoadingV2();
                } else {
                    ld.hide();
                }
            }
        });
        postSetAsDeleteViewModel.isNetworkAvailable.observe(this, obsNoInternet);
        postSetAsDeleteViewModel.getTrigger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                getMyChatListViewModel.loadData();
            }
        });
    }

    @Override
    public void OnClick(int position) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedPos = position;
                if(getMyChatListViewModel.myChat.getResult().size() != 0) {
                    postSetAsReadViewModel.loadData(getMyChatListViewModel.myChat.getResult().get(position).getID(),"");
                }
            }
        },500);
    }

    @Override
    public void onBackTriggered() {
        super.onBackTriggered();
        bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }
}