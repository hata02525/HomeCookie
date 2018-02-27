package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Activity.MainActivity;
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
public class LoginFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {


    private Activity activity;
    private View view;

    EditText et_email_or_id;
    EditText et_password;
    TextView tv_forget_password;
    TextView tv_terms;
    Button button_sign_up;
    Button button_sign_in;
    RelativeLayout button_facebook;
    RelativeLayout button_g_plus;


    private String email = "";
    private String password = "";
    private String accessToken = "";
    private Progress progress;
    private SharedPreference sharedPreference;
    private boolean isUniqueIdCreated;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        activity = getActivity();
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        Typeface boldFace = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        initView();
        sharedPreference = new SharedPreference(activity);
        isUniqueIdCreated = sharedPreference.getBoolean(Constants.IS_UNIQUE_ID_CREATED, false);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        et_email_or_id.setTypeface(face);
        et_password.setTypeface(face);
        tv_forget_password.setTypeface(face);
        tv_terms.setTypeface(face);
        button_sign_up.setTypeface(face);
        button_sign_in.setTypeface(face);

        return view;
    }

    private void initView() {
        et_email_or_id = (EditText) view.findViewById(R.id.et_email_or_id);
        et_password = (EditText) view.findViewById(R.id.et_password);
        tv_forget_password = (TextView) view.findViewById(R.id.tv_forget_password);
        tv_terms = (TextView) view.findViewById(R.id.tv_terms);
        button_sign_in = (Button) view.findViewById(R.id.button_sign_in);
        button_sign_up = (Button) view.findViewById(R.id.button_sign_up);
        button_facebook = (RelativeLayout) view.findViewById(R.id.button_facebook);
        button_g_plus = (RelativeLayout) view.findViewById(R.id.button_g_plus);
        button_sign_in.setOnClickListener(this);
        button_sign_up.setOnClickListener(this);
        button_facebook.setOnClickListener(this);
        button_g_plus.setOnClickListener(this);
        tv_terms.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_up:
                ((MainActivity) activity).replaceSignUpFragment();
                break;
            case R.id.button_sign_in:
                Helper.hideSoftKeyBoard(activity);
                if (Network.isConnected(activity)) {
                    accessToken = sharedPreference.getString(Constants.ACCESSTOKEN, accessToken);
                    Log.e("AccessToken", accessToken);
                    progress.show();
                    if (isValid()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("accessToken", accessToken);
                        jsonObject.addProperty("email", email);
                        jsonObject.addProperty("password", password);
                        jsonObject.addProperty("socialtype", "3");
                        jsonObject.addProperty("socialId", "");


                        Network.requestForLogin(activity, jsonObject, this);
                    } else {
                        progress.dismiss();
                    }
                } else {
                    Toast.makeText(activity, "Not Implemented", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forget_password:
                ((MainActivity) activity).replaceForgotPasswordFragment();
                break;
            case R.id.tv_terms:
                ((MainActivity) activity).replaceTermsFragment();
                // showEmailNotFoundDialog();
                break;
            case R.id.button_facebook:
                if (Helper.isConnected(activity)) {
                    ((MainActivity) activity).loginFacebook();
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_g_plus:
                if (Helper.isConnected(activity)) {
                    ((MainActivity) activity).googleSignIn();
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private boolean isValid() {

        email = et_email_or_id.getText().toString();
        password = et_password.getText().toString();


        if (email.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Helper.validEmail(email)) {
            Toast.makeText(activity, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;

        }

        if (password.length() < 8) {
            Toast.makeText(activity, "Password must contains 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (isSuccess) {
            int profileStatus = sharedPreference.getInt(Constants.USER_PROFILE_STATUS, 0);
            if (profileStatus == 1) {
                ((MainActivity) activity).replaceCreateIdFragment();
            }
            if (profileStatus == 2) {
                ((MainActivity) activity).replaceCreateProfileFragment();
            }
            if (profileStatus == 3) {
                sharedPreference.putBoolean(Constants.SESSION, true);
                startActivity(new Intent(activity, HomeActivity.class));
                activity.finishAffinity();
            }
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    private void showEmailNotFoundDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.email_not_found, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button button = (Button) alertDialog.findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
