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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Beans.AddressBean;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewAdressFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack {


    private View view;
    private Activity activity;
    private Typeface bold;
    private Typeface normal;
    private EditText et_name, et_phone, et_pin, et_city, et_state, et_address, et_country;
    private RelativeLayout rl_choose_country;
    private Button button_save, button_cancel;
    private ImageView iv_back;
    private String userId = "";
    private String name = "";
    private String phoneNumber = "";
    private String pincode = "";
    private String address = "";
    private String city = "";
    private String state = "";
    private String country = "";
    private String addressId = "";

    private SharedPreference sharedPreference;
    private Progress progress;
    private AddressBean bean = new AddressBean();
    private boolean isToUpdate = false;

    public static AddNewAdressFragment newInstance(AddressBean bean, boolean isToUpdate) {
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_TO_UPDATE, isToUpdate);
        args.putSerializable(Constants.ADDRESS_BEAN, bean);
        AddNewAdressFragment fragment = new AddNewAdressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isToUpdate = getArguments().getBoolean(Constants.IS_TO_UPDATE);
            bean = (AddressBean) getArguments().getSerializable(Constants.ADDRESS_BEAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_adress, container, false);
        activity = getActivity();
        sharedPreference = new SharedPreference(activity);
        normal = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        bold = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        initView();
        setTypeFace();
        if (isToUpdate) {
            setAllValues();
        }
        return view;
    }

    private void setAllValues() {
        button_save.setText("Update");
        button_cancel.setVisibility(View.GONE);
        name = bean.getName();
        phoneNumber = bean.getPhoneNumber();
        pincode = bean.getPincode();
        address = bean.getAddress();
        city = bean.getCity();
        state = bean.getState();
        country = bean.getCountry();

        et_name.setText(name);
        et_phone.setText(phoneNumber);
        et_pin.setText(pincode);
        et_address.setText(address);
        et_city.setText(city);
        et_state.setText(state);
        et_country.setText(country);
        addressId = String.valueOf(bean.getAddressId());
    }

    private void setTypeFace() {
        ((EditText) view.findViewById(R.id.et_name)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_pin)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_phone)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_city)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_state)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_address)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_country)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_header)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_save)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_cancel)).setTypeface(normal);
    }

    private void initView() {
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_pin = (EditText) view.findViewById(R.id.et_pin);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_city = (EditText) view.findViewById(R.id.et_city);
        et_state = (EditText) view.findViewById(R.id.et_state);
        et_address = (EditText) view.findViewById(R.id.et_address);
        et_country = (EditText) view.findViewById(R.id.et_country);
        button_save = (Button) view.findViewById(R.id.button_save);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        button_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }


    private boolean isValid() {
        userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
        name = et_name.getText().toString().trim();
        phoneNumber = et_phone.getText().toString().trim();
        address = et_address.getText().toString();
        city = et_city.getText().toString();
        pincode = et_pin.getText().toString().trim();
        state = et_state.getText().toString();
        country = et_country.getText().toString();
        addressId = String.valueOf(bean.getAddressId());

        if (name.isEmpty()) {
            Toast.makeText(activity, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(activity, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (city.isEmpty()) {
            Toast.makeText(activity, "Please Enter Your City Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pincode.isEmpty()) {
            Toast.makeText(activity, "Please Enter Your Pin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (state.isEmpty()) {
            Toast.makeText(activity, "Please Enter Your State Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (country.isEmpty()) {
            Toast.makeText(activity, "Please Enter Your Country Name", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        JsonObject obj = new JsonObject();
                        if (isToUpdate) {
                            obj.addProperty("addressId", addressId);
                        }
                        obj.addProperty("userId", userId);
                        obj.addProperty("name", name);
                        obj.addProperty("phoneNumber", phoneNumber);
                        obj.addProperty("pincode", pincode);
                        obj.addProperty("address", address);
                        obj.addProperty("city", city);
                        obj.addProperty("state", state);
                        obj.addProperty("country", country);
                        Network.requesetForAddUpdateAddress(activity, obj, this);
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back:
                activity.onBackPressed();
                break;
            case R.id.button_cancel:
                clearFormData();
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_ADD_UPDATE_ADDRESS) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            clearFormData();
        }
    }

    private void clearFormData() {
        et_name.setText("");
        et_pin.setText("");
        et_phone.setText("");
        et_city.setText("");
        et_state.setText("");
        et_address.setText("");
        et_country.setText("");
    }


    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
