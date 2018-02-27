package com.app.homecookie.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.homecookie.ChatHelper.model.MessageModel;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;


import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ${Devendra} on 4/5/2016.
 */
public class ChatAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<MessageModel> chatMessageList;
    Activity activity;
    String senderPic = "";
    Typeface face = null;

    public ChatAdapter(Activity activity, ArrayList<MessageModel> list, String toUserPhoto) {
        this.activity = activity;
        chatMessageList = list;
        senderPic = toUserPhoto;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");

    }

    public ChatAdapter() {
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageModel message = (MessageModel) chatMessageList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chat_item, null);

        RelativeLayout ll_sender = (RelativeLayout) vi.findViewById(R.id.ll_sender);
        RelativeLayout ll_owner = (RelativeLayout) vi.findViewById(R.id.ll_owner);
        TextView tv_message_sender = (TextView) vi.findViewById(R.id.tv_message_sender);
        TextView tv_message_owner = (TextView) vi.findViewById(R.id.tv_message_owner);
        ImageView iv_sender = (ImageView) vi.findViewById(R.id.iv_sender);
        ImageView iv_owner = (ImageView) vi.findViewById(R.id.iv_owner);
        tv_message_owner.setTypeface(face);
        tv_message_sender.setTypeface(face);
        // if message is mine then align to right
        if (message.isMine()) {
            ll_owner.setVisibility(View.VISIBLE);
            tv_message_owner.setVisibility(View.VISIBLE);
            iv_owner.setVisibility(View.VISIBLE);
            ll_sender.setVisibility(View.GONE);
            iv_sender.setVisibility(View.GONE);
            tv_message_sender.setVisibility(View.GONE);
            tv_message_owner.setText(message.getMsg());
            String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
            Helper.setProfilePic(activity, userPhoto, iv_owner);
        }
        // If not mine then align to left
        else {
            ll_sender.setVisibility(View.VISIBLE);
            iv_sender.setVisibility(View.VISIBLE);
            tv_message_sender.setVisibility(View.VISIBLE);
            ll_owner.setVisibility(View.GONE);
            tv_message_owner.setVisibility(View.GONE);
            iv_owner.setVisibility(View.GONE);
            tv_message_sender.setText(message.getMsg());
            Helper.setProfilePic(activity, senderPic, iv_sender);
        }
        return vi;
    }

    public void add(MessageModel object) {
        chatMessageList.add(object);
    }
}