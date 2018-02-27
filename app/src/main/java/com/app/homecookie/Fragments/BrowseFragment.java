package com.app.homecookie.Fragments;


import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {


    public BrowseFragment() {
        // Required empty public constructor
    }

    CardView card_news_feed;
    ImageView iv_chat;
    ImageView iv_browse;
    ImageView iv_person;
    ImageView iv_featured;
    TextView tv_donate;
    TextView tv_people;
    TextView tv_browse;
    TextView tv_trend;
    TextView tv_featured;
    TextView tv_news_feed;
    TextView tv_header;
    private SharedPreference sharedPreference;
    private Activity activity;
    private Progress progress;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_browse, container, false);
        activity = getActivity();
        initView();
        sharedPreference = new SharedPreference(activity);
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
        }
        tv_browse.setTypeface(face);
        tv_news_feed.setTypeface(face);
        tv_donate.setTypeface(face);
        tv_trend.setTypeface(face);
        tv_featured.setTypeface(face);
        tv_people.setTypeface(face);
        tv_header.setTypeface(face);
        return view;
    }

    private void initView() {
        card_news_feed = (CardView) view.findViewById(R.id.card_news_feed);
        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
        iv_person = (ImageView) view.findViewById(R.id.iv_person);
        iv_browse = (ImageView) view.findViewById(R.id.iv_browse);
        iv_featured = (ImageView) view.findViewById(R.id.iv_featured);
        tv_donate = (TextView) view.findViewById(R.id.tv_donate);
        tv_people = (TextView) view.findViewById(R.id.tv_people);
        tv_browse = (TextView) view.findViewById(R.id.tv_browse);
        tv_trend = (TextView) view.findViewById(R.id.tv_trend);
        tv_featured = (TextView) view.findViewById(R.id.tv_featured);
        tv_news_feed = (TextView) view.findViewById(R.id.tv_news_feed);
        tv_header = (TextView) view.findViewById(R.id.tv_header);

        iv_person.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_browse.setOnClickListener(this);
        iv_featured.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_browse:
                if (Network.isConnected(activity)) {
                    progress.show();
                    Network.requestForCategoryList(activity, this);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_chat:
                if (Network.isConnected(activity)) {
                    new SharedPreference(activity).putInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN, Constants.RECENT_CHAT_FRAGMENT);
                    ((HomeActivity) activity).replaceChatFragment(0);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_person:
                ((HomeActivity) activity).replaceHomeCookieDetailsFragment(true, "");
                break;
            case R.id.iv_featured:
                progress.show();
                String userId = sharedPreference.getString(Constants.USER_USR_ID,"0");
                JsonObject object = new JsonObject();
                object.addProperty("userId",userId);
                Network.requestForFeaturedRecipe(activity,object, this);
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (requestType == Network.REQUEST_TYPE_BROWSE_CATEGORY) {
            CategoryListBean listBean = (CategoryListBean) data;
            ((HomeActivity) activity).replaceBrowseCategoryListFragment(listBean);
        }
        if (requestType == Network.REQUEST_TYPE_FEATURED_RECIPE) {
            progress.dismiss();
            JsonObject obj = (JsonObject) data;
            String recipeId = obj.get("recipeId").getAsString();
            /*result": {
                "recipeId": 1,
                        "photo": "http://67.209.121.170/homecookie/usersRecipe/thumb_1496648337_1496648328.jpeg",
                        "title": "test23",
                        "avgRecipeRating": "3.8000",
                        "usersRecipeRating": 0,
                        "commentCount": 2
            },
            "message": "Featured recipes fetched successfully"*/
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
