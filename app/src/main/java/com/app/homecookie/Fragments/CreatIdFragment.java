package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class CreatIdFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {


    public CreatIdFragment() {
        // Required empty public constructor
    }

    private Activity activity;
    SharedPreference sharedPreference;
    String userId = "";
    EditText et_email_or_id;
    Button button_submit;
    private Progress progress;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_creat_id, container, false);
        initView();
        activity = getActivity();
        et_email_or_id = (EditText) view.findViewById(R.id.et_email_or_id);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        return view;
    }

    private void initView() {
       et_email_or_id = (EditText) view.findViewById(R.id.et_email_or_id);
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
                sharedPreference = new SharedPreference(activity);
                String accessToken = sharedPreference.getString(Constants.ACCESSTOKEN, "");
                userId = et_email_or_id.getText().toString();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", userId);

                if (Helper.isConnected(activity)) {
                    progress.show();
                    Network.requestForCreateId(activity, jsonObject, this);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (isSuccess) {
            sharedPreference.putString(Constants.UNIQUE_ID, userId);
            sharedPreference.putBoolean(Constants.IS_UNIQUE_ID_CREATED, true);
            ((MainActivity) activity).replaceCreateProfileFragment();
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
