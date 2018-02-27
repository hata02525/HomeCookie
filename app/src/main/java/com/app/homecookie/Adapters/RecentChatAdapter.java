package com.app.homecookie.Adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.ChatHelper.model.RecentChatModel;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.swipe.ViewBinderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fluper on 9/6/17.
 */
public class RecentChatAdapter extends RecyclerView.Adapter {
    int itemPosition;
    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    Activity activity;
    String id;
    int pos;
    Typeface face;
    ArrayList<RecentChatModel> recentChatList = new ArrayList<>();
    ArrayList<HashMap<String, String>> onlineUserList = new ArrayList<>();

    public RecentChatAdapter(Activity activity, ArrayList<RecentChatModel> recentChatList, ArrayList<HashMap<String, String>> onlineUserList) {
        this.activity = activity;
        this.recentChatList = recentChatList;
        this.onlineUserList = onlineUserList;
        mInflater = LayoutInflater.from(activity);
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        for (int i = 0; i < recentChatList.size(); i++) {
            for (int j = 0; j < onlineUserList.size(); j++) {
                if(onlineUserList.get(j).get("USER").equalsIgnoreCase(recentChatList.get(i).getReceiverId())){
                    if (onlineUserList.get(j).get("STATUS").equalsIgnoreCase("available")) {
                        recentChatList.get(i).setOnline(true);
                    }
                }
            }
        }

    }

    public RecentChatAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recent_chat_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
        final ViewHolder holder = (ViewHolder) h;

        holder.tv_message.setText(recentChatList.get(position).getLastMessage());
        holder.tv_user.setText(recentChatList.get(position).getReceiverName());
        Helper.setProfilePic(activity, recentChatList.get(position).getReceiverPhoto(), holder.iv_user);
        final String id = recentChatList.get(position).getReceiverId();
        final String name = recentChatList.get(position).getReceiverName();

        final String photo = recentChatList.get(position).getReceiverPhoto();
        if(!recentChatList.get(position).isOnline()) {
            holder.iv_online.setVisibility(View.GONE);
        }
        holder.recent_chat_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TO_USER_ID, id);
                bundle.putString(Constants.TO_USER_NAME, name);
                bundle.putString(Constants.TO_USER_PHOTO, photo);
                bundle.putBoolean(Constants.IS_FRIEND_CHAT, false);
                bundle.putBoolean(Constants.IS_RECENT_CHAT, true);
                ((HomeActivity) activity).replaceOneToOneChatFragment(bundle);
            }
        });
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recentChatList.size();
    }


    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout deleteLayout;
        private TextView tv_user;
        private TextView tv_message;
        private ImageView iv_user;
        private ImageView iv_online;
        private LinearLayout recent_chat_ll;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_user = (TextView) itemView.findViewById(R.id.tv_user);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
            iv_user = (ImageView) itemView.findViewById(R.id.iv_user);
            iv_online = (ImageView) itemView.findViewById(R.id.iv_online);
            recent_chat_ll = (LinearLayout) itemView.findViewById(R.id.recent_chat_ll);
            tv_user.setTypeface(face);
            tv_message.setTypeface(face);
        }

        public void bind(int data) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}