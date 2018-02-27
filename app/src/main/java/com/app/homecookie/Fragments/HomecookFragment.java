package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomecookFragment extends Fragment implements View.OnClickListener {
    Activity activity;
    //private ViewPager viewPager;
    private RelativeLayout frag_container;
    public TextView tv_meals;
    public TextView tv_lessons;
    public TextView tv_donate;
    public TextView tv_header;
    public static final String HOMECOOKE_FRAG_STACK = "homeCoockFragStack";
    private ImageView iv_person;
    private ImageView iv_chat;
    Typeface face = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homecook, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        tv_meals = (TextView) view.findViewById(R.id.tv_meals);
        tv_donate = (TextView) view.findViewById(R.id.tv_donate);
        tv_lessons = (TextView) view.findViewById(R.id.tv_lessons);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        iv_person = (ImageView) view.findViewById(R.id.iv_person);
        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
        tv_meals.setOnClickListener(this);
        tv_donate.setOnClickListener(this);
        tv_lessons.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        tv_meals.setTypeface(face);
        tv_donate.setTypeface(face);
        tv_lessons.setTypeface(face);
        tv_header.setTypeface(face);
        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
        }
        getFragmentManager().beginTransaction().add(R.id.frag_container, new MealsFragment()).addToBackStack(HOMECOOKE_FRAG_STACK).commit();
        iv_person.setOnClickListener(this);
        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    @Override
    public void onClick(View v) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frag_container);
        switch (v.getId()) {
            case R.id.tv_meals:
                if (!(fragment instanceof MealsFragment)) {
                    tv_meals.setBackgroundColor(getResources().getColor(R.color.light_green));
                    tv_lessons.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                    tv_donate.setBackgroundColor(getResources().getColor(R.color.dim_yellow));

                    tv_meals.setTextColor(getResources().getColor(R.color.white));
                    tv_lessons.setTextColor(getResources().getColor(R.color.black_text));
                    tv_donate.setTextColor(getResources().getColor(R.color.black_text));

                    ((HomeActivity) activity).replaceMealsFragment();
                }
                break;
            case R.id.tv_lessons:
                if (!(fragment instanceof LessonsFragment)) {
                    tv_meals.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                    tv_lessons.setBackgroundColor(getResources().getColor(R.color.light_green));
                    tv_donate.setBackgroundColor(getResources().getColor(R.color.dim_yellow));

                    tv_meals.setTextColor(getResources().getColor(R.color.black_text));
                    tv_lessons.setTextColor(getResources().getColor(R.color.white));
                    tv_donate.setTextColor(getResources().getColor(R.color.black_text));
                    ((HomeActivity) activity).replaceLessonFragment();
                }
                break;
            case R.id.tv_donate:
                if (!(fragment instanceof DonateFragment)) {
                    tv_meals.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                    tv_lessons.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
                    tv_donate.setBackgroundColor(getResources().getColor(R.color.light_green));

                    tv_meals.setTextColor(getResources().getColor(R.color.black_text));
                    tv_lessons.setTextColor(getResources().getColor(R.color.black_text));
                    tv_donate.setTextColor(getResources().getColor(R.color.white));

                    ((HomeActivity) activity).replaceDonateFragment();
                }
                break;
            case R.id.iv_chat:
                if (Network.isConnected(activity)) {
                    ((HomeActivity) activity).replaceChatFragment(0);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_person:
                ((HomeActivity) activity).replaceHomeCookieDetailsFragment(true, "");
                break;
        }
    }


    public void prepareMealFragment() {
        tv_meals.setBackgroundColor(getResources().getColor(R.color.light_green));
        tv_lessons.setBackgroundColor(getResources().getColor(R.color.dim_yellow));
        tv_donate.setBackgroundColor(getResources().getColor(R.color.dim_yellow));

        tv_meals.setTextColor(getResources().getColor(R.color.white));
        tv_lessons.setTextColor(getResources().getColor(R.color.black_text));
        tv_donate.setTextColor(getResources().getColor(R.color.black_text));
    }

}
