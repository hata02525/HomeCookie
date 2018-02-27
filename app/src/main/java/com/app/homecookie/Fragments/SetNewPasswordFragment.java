package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class SetNewPasswordFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {


    EditText et_password;
    EditText et_confirm_password;
    TextView tv_header;
    Button button_submit;
    String password = "";
    Activity activity;
    Progress progress;
    String verificationKey = "";
    Typeface typeface;


    public static SetNewPasswordFragment newInstance(String verificationKey) {
        Bundle args = new Bundle();
        args.putString(Constants.VERIFICATION_KEY, verificationKey);
        SetNewPasswordFragment fragment = new SetNewPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            verificationKey = getArguments().getString(Constants.VERIFICATION_KEY);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_set_new_password, container, false);
        typeface = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        initView();
        activity = getActivity();
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        button_submit.setTypeface(typeface);
        et_confirm_password.setTypeface(typeface);
        et_password.setTypeface(typeface);
        tv_header.setTypeface(typeface);
        return view;
    }

    private void initView() {
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_confirm_password = (EditText) view.findViewById(R.id.et_confirm_password);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        button_submit = (Button) view.findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                Helper.hideSoftKeyBoard(activity);
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        JsonObject obj = new JsonObject();
                        obj.addProperty("verificationKey", verificationKey);
                        obj.addProperty("password", password);
                        Network.requestForSetNewPassword(activity, obj, this);
                    }

                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private boolean isValid() {

        password = et_password.getText().toString();

        String confirmPassword = et_confirm_password.getText().toString();

        if (password.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(activity, "Password must contains 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (confirmPassword.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please Confirm Password", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(activity, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
        ((MainActivity) activity).replaceLoginFragment();
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


}
