package com.app.homecookie.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.CreateGroupActivity;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.GroupBean;
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
public class GroupChatFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack {
    View view;
    Activity activity;
    RecyclerView recyclerView_group;
    GroupChatAdapter adapter;
    FloatingActionButton fab_add_group;
    Progress progress;
    RelativeLayout rl_no_data;
    TextView tv_try_again;
    ArrayList<GroupBean> groupList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group, container, false);
        activity = getActivity();
        recyclerView_group = (RecyclerView) view.findViewById(R.id.recyclerView_group);
        fab_add_group = (FloatingActionButton) view.findViewById(R.id.fab_add_group);
        fab_add_group.setOnClickListener(this);
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        tv_try_again = (TextView) view.findViewById(R.id.tv_try_again);
        rl_no_data.setOnClickListener(this);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        if (Network.isConnected(activity)) {
            progress.show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            Network.requestForGetGroup(activity, obj, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
            tv_try_again.setText("You Are No Connected. \n Please Try Again");
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    private void initView(ArrayList<GroupBean> groupList) {
        recyclerView_group.setHasFixedSize(true);
        recyclerView_group.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GroupChatAdapter(activity, groupList);
        recyclerView_group.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_group:
                Intent intent = new Intent(activity, CreateGroupActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_GET_GROUP) {
            progress.dismiss();
            groupList = (ArrayList<GroupBean>) data;
            initView(groupList);
        }
        if (requestType == Network.REQUEST_TYPE_THE_DELETE_GROUP) {
            progress.dismiss();
            activity.onBackPressed();
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    class GroupChatAdapter extends RecyclerView.Adapter {
        int itemPosition;
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        String id;
        int pos;
        ArrayList<GroupBean> groupList = new ArrayList<>();

        public GroupChatAdapter(Activity activity, ArrayList<GroupBean> groupList) {
            this.activity = activity;
            this.groupList = groupList;
            mInflater = LayoutInflater.from(activity);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.friends_chat_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            final String id = String.valueOf(groupList.get(position).getId());
            final String photo = groupList.get(position).getGroupPhoto();
            final String name = groupList.get(position).getGroupName();
            Helper.setProfilePic(activity, photo, holder.iv_user_profile);
            holder.tv_user_name.setText(name);
            holder.rl_open_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.GROUP_ID,id);
                    bundle.putString(Constants.GROUP_PHOTO,photo);
                    bundle.putString(Constants.GROUP_NAME,name);
                    ((HomeActivity)activity).replaceOneToManyChatFragment(bundle);

                    /*ChatService.xmpp.joinGroup(id);*/
                    //Toast.makeText(activity, "item clicked", Toast.LENGTH_SHORT).show();
                }
            });

            holder.bind(id);
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private FrameLayout deleteLayout;
            private ImageView iv_user_profile;
            private TextView tv_user_name;
            private RelativeLayout main_layout;
            private LinearLayout rl_open_chat;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_user_profile = (ImageView) itemView.findViewById(R.id.iv_user_profile);
                tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
                deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
                main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
                rl_open_chat = (LinearLayout) itemView.findViewById(R.id.rl_open_chat);
                Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
                tv_user_name.setTypeface(face);

            }

            public void bind(final String data) {
                deleteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = data;
                        JsonObject object = new JsonObject();
                        object.addProperty("groupId", id);
                        hitDeleteGroupApi(object);
                    }
                });

            }
        }
    }


    private void hitDeleteGroupApi(JsonObject object) {
        progress.show();
        Network.requestForDeleteGroup(activity, object, this);
    }
}



