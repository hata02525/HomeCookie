package com.app.homecookie.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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


public class ForgotPasswordVerifyCodeFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {

    EditText et_code;
    TextView tv_header;
    Button button_submit;

    Activity activity;
    Progress progress;
    String verificationKey = "";
    Typeface typeface;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password_verify_code, container, false);
        activity = getActivity();
        typeface = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        button_submit.setTypeface(typeface);
        tv_header.setTypeface(typeface);
        et_code.setTypeface(typeface);
        return view;
    }

    private void initView() {
        et_code = (EditText) view.findViewById(R.id.et_code);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        button_submit = (Button) view.findViewById(R.id.button_submit);
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
                        Network.requestForVerifyKey(activity, obj, this);
                    }

                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private boolean isValid() {
        verificationKey = et_code.getText().toString().trim();
        if (verificationKey.isEmpty()) {
            Toast.makeText(activity, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        ((MainActivity) activity).replaceSetNewPasswordFragment(verificationKey);
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


}
