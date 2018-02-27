package com.app.homecookie.DatabaseHelper;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Fragments.FriendChatFragment;
import com.app.homecookie.Fragments.GroupChatFragment;
import com.app.homecookie.Fragments.RecentChatFragment;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    TextView tv_recent;
    TextView tv_group;
    TextView tv_friend;
    TextView tv_header;
    ImageView iv_person;
    ViewPager view_pager_chat;
    ImageView iv_back;
    private RelativeLayout rl_previous;
    Activity activity;
    Intent intent;
    Bundle extras;
    View view;
    private int currentItemToSet=0;
    SharedPreference sharedPreference;


    public static ChatFragment newInstance(int fragmentToReplace) {
        Bundle args = new Bundle();
        args.putInt(Constants.FRAGMENT_TO_BE_REPLACE,fragmentToReplace);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            currentItemToSet = getArguments().getInt(Constants.FRAGMENT_TO_BE_REPLACE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        activity = getActivity();
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        sharedPreference = new SharedPreference(activity);
        iv_person = (ImageView) view.findViewById(R.id.iv_person);
        rl_previous = (RelativeLayout) view.findViewById(R.id.rl_previous);
        view_pager_chat = (ViewPager) view.findViewById(R.id.view_pager_chat);
        tv_recent = (TextView) view.findViewById(R.id.tv_recent);
        tv_group = (TextView) view.findViewById(R.id.tv_group);
        tv_friend = (TextView) view.findViewById(R.id.tv_friend);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_recent.setTypeface(face);
        tv_group.setTypeface(face);
        tv_friend.setTypeface(face);
        tv_header.setTypeface(face);


        iv_back = (ImageView) view.findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);

        tv_recent.setOnClickListener(this);
        tv_group.setOnClickListener(this);
        tv_friend.setOnClickListener(this);
        view_pager_chat.setOnPageChangeListener(this);

        RecentChatFragment recentChatFragment = new RecentChatFragment();
        GroupChatFragment groupChatFragment = new GroupChatFragment();
        FriendChatFragment friendChatFragment = new FriendChatFragment();

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(recentChatFragment);
        fragmentArrayList.add(groupChatFragment);
        fragmentArrayList.add(friendChatFragment);
        view_pager_chat = (ViewPager) view.findViewById(R.id.view_pager_chat);
        view_pager_chat.setOffscreenPageLimit(3);
        ChatPagerAdapter adapter = new ChatPagerAdapter(getChildFragmentManager(), activity, fragmentArrayList);
        view_pager_chat.setAdapter(adapter);

        currentItemToSet = sharedPreference.getInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN,0);
        if(currentItemToSet==2){
            view_pager_chat.setCurrentItem(Constants.FRIEND_CHAT_FRAGMENT);
        }else if(currentItemToSet==1){
            view_pager_chat.setCurrentItem(Constants.GROUP_CHAT_FRAGMENT);
        }else{
            view_pager_chat.setCurrentItem(Constants.RECENT_CHAT_FRAGMENT);
        }

        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recent:
                view_pager_chat.setCurrentItem(0);
                break;
            case R.id.tv_group:
                view_pager_chat.setCurrentItem(1);
                break;
            case R.id.tv_friend:
                view_pager_chat.setCurrentItem(2);
                break;
            case R.id.iv_back:
                startActivity(new Intent(activity, HomeActivity.class));
                sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
                activity.finishAffinity();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tv_recent.setBackgroundColor(getResources().getColor(R.color.light_green));
                tv_group.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                tv_friend.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                break;
            case 1:
                tv_recent.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                tv_group.setBackgroundColor(getResources().getColor(R.color.light_green));
                tv_friend.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                break;
            case 2:
                tv_recent.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                tv_group.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                tv_friend.setBackgroundColor(getResources().getColor(R.color.light_green));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class ChatPagerAdapter extends FragmentPagerAdapter {

        Activity activity;
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        public ChatPagerAdapter(FragmentManager fm, Activity activity, ArrayList<Fragment> arrayList) {
            super(fm);
            fragmentArrayList = arrayList;
            this.activity = activity;
        }


        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = fragmentArrayList.get(0);
                    break;
                case 1:
                    fragment = fragmentArrayList.get(1);
                    break;
                case 2:
                    fragment = fragmentArrayList.get(2);
                    break;
            }
            return fragment;
        }
    }
}
