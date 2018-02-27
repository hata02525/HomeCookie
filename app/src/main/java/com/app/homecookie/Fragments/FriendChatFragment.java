package com.app.homecookie.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.AddFriendActivity;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.FriendListBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.swipe.ViewBinderHelper;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendChatFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack {
    View view;
    Activity activity;
    RecyclerView recyclerView_group;
    FriendChatAdapter adapter;
    Button button_add_friend;
    Progress progress;
    RelativeLayout rl_no_data;
    TextView tv_try_again;
    ArrayList<FriendListBean> friendList = new ArrayList<FriendListBean>();
    Typeface face =null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friend_chat, container, false);
        activity = getActivity();
        face =  Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        recyclerView_group = (RecyclerView) view.findViewById(R.id.recyclerView_group);
        button_add_friend = (Button) view.findViewById(R.id.button_add_friend);
        button_add_friend.setTypeface(face);
        button_add_friend.setOnClickListener(this);
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        tv_try_again = (TextView) view.findViewById(R.id.tv_try_again);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        if (Network.isConnected(activity)) {
            progress.show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            Network.requestForFriendList(activity, obj, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
            tv_try_again.setText("You Are No Connected. \n Please Try Again");
        }

        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    private void initView(ArrayList<FriendListBean> friendList) {
        recyclerView_group.setHasFixedSize(true);
        recyclerView_group.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new FriendChatAdapter(activity,friendList);
        recyclerView_group.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_friend:
                Intent intent = new Intent(activity, AddFriendActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_USER_FRIEND_LIST) {
            progress.dismiss();
            friendList = (ArrayList<FriendListBean>) data;
            initView(friendList);
        }
        if(requestType==Network.REQUEEST_TYPE_DELETE_FRIEND){
            Intent intent = new Intent(activity, HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.IS_FROM_ADD_FRIEND,true);
            intent.putExtras(bundle);
            startActivity(intent);
            activity.finish();
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    class FriendChatAdapter extends RecyclerView.Adapter {
        int itemPosition;
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        String id;
        int pos;
        ArrayList<FriendListBean> friendList = new ArrayList<>();
        public FriendChatAdapter(Activity activity, ArrayList<FriendListBean> friendList) {
            this.activity = activity;
            mInflater = LayoutInflater.from(activity);
            this.friendList = friendList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.friends_chat_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            final String id = String.valueOf(friendList.get(position).getId());
            final String name = friendList.get(position).getFirstName()+" "+friendList.get(position).getLastName();
            final String photo = friendList.get(position).getPhoto();
            holder.tv_user_name.setText(name);
            Helper.setProfilePic(activity,photo,holder.iv_user_profile);
            holder.rl_open_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.TO_USER_ID,id);
                    bundle.putString(Constants.TO_USER_NAME,name);
                    bundle.putString(Constants.TO_USER_PHOTO,photo);
                    bundle.putBoolean(Constants.IS_FRIEND_CHAT,true);
                    bundle.putBoolean(Constants.IS_RECENT_CHAT,false);
                    ((HomeActivity)activity).replaceOneToOneChatFragment(bundle);
                }
            });
            holder.bind(position);

        }

        @Override
        public int getItemCount() {
            return friendList.size();
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private FrameLayout deleteLayout;
            private LinearLayout rl_open_chat;
            private TextView tv_user_name;
            private ImageView iv_user_profile;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
                iv_user_profile = (ImageView) itemView.findViewById(R.id.iv_user_profile);
                deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
                rl_open_chat = (LinearLayout) itemView.findViewById(R.id.rl_open_chat);

                tv_user_name.setTypeface(face);
            }

            public void bind(final int data) {
                deleteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            int pos = data;
                            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID,"0");
                            String  friendId = String.valueOf(friendList.get(pos).getId());
                            JsonObject jsonObject  = new JsonObject();
                            jsonObject.addProperty("userId",userId);
                            jsonObject.addProperty("friendId",friendId);

                            if(Network.isConnected(activity)){
                                progress.show();
                                hitDeleteFriendApi(jsonObject);

                            }else{
                                Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                            }
                    }
                });

            }
        }
    }



    private void hitDeleteFriendApi(JsonObject jsonObject){
        Network.requestForDeleteFriend(activity,jsonObject,this);
    }
}
