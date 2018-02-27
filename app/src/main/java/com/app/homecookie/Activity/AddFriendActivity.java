package com.app.homecookie.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddFriendActivity extends AppCompatActivity implements OnNetworkCallBack, View.OnClickListener, TextWatcher {
    String friendId;
    RecyclerView recyclerView_add_friend;
    Activity activity;
    EditText et_search;
    RelativeLayout rl_no_data;
    TextView tv_try_again;
    TextView tv_header;
    TextView tv_lbl;
    Progress progress;
    ImageView iv_back;
    ImageView iv_person;
    ArrayList<FriendListBean> friendList = new ArrayList<FriendListBean>();
    Typeface face = null;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        activity = AddFriendActivity.this;
        sharedPreference = new SharedPreference(activity);
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        recyclerView_add_friend = (RecyclerView) findViewById(R.id.recyclerView_add_friend);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(this);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        tv_try_again = (TextView) findViewById(R.id.tv_try_again);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_lbl = (TextView) findViewById(R.id.tv_lbl);
        tv_header.setTypeface(face);
        tv_lbl.setTypeface(face);
        et_search.setTypeface(face);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_person = (ImageView) findViewById(R.id.iv_person);
        rl_no_data.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        if (Network.isConnected(activity)) {
            progress.show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            Network.requestForFriendSuggestions(activity, obj, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
            tv_try_again.setText("You Are No Connected. \n Please Try Again");
        }

        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
        }


    }


    private void initView(ArrayList<FriendListBean> list) {
        rl_no_data.setVisibility(View.GONE);
        recyclerView_add_friend.setNestedScrollingEnabled(false);
        AddFriendAdapter adapter = new AddFriendAdapter(activity, list);
        recyclerView_add_friend.setHasFixedSize(true);
        recyclerView_add_friend.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView_add_friend.setAdapter(adapter);
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_FRIEND_LIST) {
            progress.dismiss();
            friendList = (ArrayList<FriendListBean>) data;
            initView(friendList);
        }
        if (requestType == Network.REQUEST_TYPE_ADD_TO_FRIEND_LIST) {
            startActivity(new Intent(this, AddFriendActivity.class));
            finish();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_no_data:
                finish();
                startActivity(new Intent(this, AddFriendActivity.class));
                break;
            case R.id.iv_back:
                finish();
                new SharedPreference(activity).putInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN, Constants.FRIEND_CHAT_FRAGMENT);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString();
        ArrayList<FriendListBean> filteredList = new ArrayList<FriendListBean>();
        if (s.length() >= 0) {
            for (int i = 0; i < friendList.size(); i++) {
                String name = friendList.get(i).getFirstName() + " " + friendList.get(i).getLastName();
                if (name.toLowerCase().contains(query)) {
                    filteredList.add(friendList.get(i));
                }
                initView(filteredList);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    class AddFriendAdapter extends RecyclerView.Adapter {
        int itemPosition;
        ArrayList<FriendListBean> friendList = new ArrayList<>();
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        String id;
        int pos;

        public AddFriendAdapter(Activity activity, ArrayList<FriendListBean> friendList) {
            this.activity = activity;
            mInflater = LayoutInflater.from(activity);
            this.friendList = friendList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.add_friend_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            String name = friendList.get(position).getFirstName() + " " + friendList.get(position).getLastName();
            String photo = friendList.get(position).getPhoto();
            holder.tv_name.setText(name);
            Helper.setProfilePic(activity, photo, holder.iv_photo);
            holder.button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.hideSoftKeyBoard(activity);
                    String id = String.valueOf(friendList.get(position).getId());
                    String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
                    JsonObject obj = new JsonObject();
                    obj.addProperty("userId", userId);
                    obj.addProperty("friendId", id);
                    hitApi(obj);
                }
            });
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
            ImageView iv_photo;
            TextView tv_name;
            Button button_add;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                button_add = (Button) itemView.findViewById(R.id.button_add);
                tv_name.setTypeface(face);
                button_add.setTypeface(face);
            }

        }
    }

    private void hitApi(JsonObject obj) {
        progress.show();
        Network.requestForAddFriend(activity, obj, this);
    }

    @Override
    public void onBackPressed() {
        new SharedPreference(activity).putInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN, Constants.FRIEND_CHAT_FRAGMENT);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
        startActivity(intent);
    }
}
