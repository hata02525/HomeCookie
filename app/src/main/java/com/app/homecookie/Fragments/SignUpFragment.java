package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.MainActivity;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {


    public SignUpFragment() {
        // Required empty public constructor
    }

    EditText et_email_or_id;
    EditText et_password;
    EditText et_confirm_password;
    Button button_sign_in;
    Button button_sign_up;
    TextView tv_acc_exist;

    private Progress progress;
    private Activity activity;
    private View view;

    private String email = "";
    private String password = "";
    private String userType = "user";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        activity = getActivity();
        initView();
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        Typeface boldFace = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        et_email_or_id.setTypeface(face);
        et_password.setTypeface(face);
        et_confirm_password.setTypeface(face);
        button_sign_in.setTypeface(face);
        button_sign_up.setTypeface(face);
        tv_acc_exist.setTypeface(face);
        return view;
    }

    private void initView() {
        et_email_or_id = (EditText) view.findViewById(R.id.et_email_or_id);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_confirm_password = (EditText) view.findViewById(R.id.et_confirm_password);
        button_sign_in = (Button) view.findViewById(R.id.button_sign_in);
        button_sign_up = (Button) view.findViewById(R.id.button_sign_up);
        tv_acc_exist = (TextView) view.findViewById(R.id.tv_acc_exist);

        button_sign_in.setOnClickListener(this);
        button_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_up:
                Helper.hideSoftKeyBoard(activity);
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("email", email);
                        jsonObject.addProperty("password", password);
                        jsonObject.addProperty("userType", userType);
                        Network.requestForSignup(activity, jsonObject, this);
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_sign_in:
                ((MainActivity) activity).replaceLoginFragment();
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

        String confirmPassword = et_confirm_password.getText().toString();

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
        if (confirmPassword.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equalsIgnoreCase(confirmPassword)) {
            et_confirm_password.setError("Passwords are not matching");
            return false;
        }

        return true;

    }


    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (isSuccess) {
            ((MainActivity) activity).replaceCreateIdFragment();
        }

    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
