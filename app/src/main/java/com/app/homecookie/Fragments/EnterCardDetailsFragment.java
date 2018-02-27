package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.MainActivity;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.StripeConstants;
import com.google.gson.JsonObject;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.RequestOptions;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterCardDetailsFragment extends Fragment implements View.OnClickListener, TextWatcher, OnNetworkCallBack {
    View view;
    ImageView iv_yyyy_down;
    ImageView iv_mm_down;
    Activity activity;
    Typeface normal = null;
    Typeface bold = null;
    boolean isValidCard;
    private ImageView iv_back;
    private TextView tv_header;
    private EditText et_card_number, et_mm, et_yy, et_cvv;
    private Button button_pay;
    Stripe stripe;
    Integer amount;
    String name;
    Card card;
    Token tok;
    String cardNumber = "";
    String expMonth = "";
    String expYear = "";
    String cvc = "";
    private Progress progress;
    final int[] len = {0};
    StringBuilder cardString;
    DatePickerDialog datePickerDialog;
    static Dialog d;
    int year = 0;
    int month = 0;
    java.util.Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_enter_card_details, container, false);
        activity = getActivity();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        normal = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);


        initView();
        stripe = new Stripe(activity);
        return view;

    }

    private void addCard() {
        if (isValidCard()) {
            progress.show();
            card = new Card("4242424242424242", Integer.parseInt("4"), Integer.parseInt("2021"), "123");
            isValidCard = card.validateCard();
            if (isValidCard) {
                stripe.createToken(card, StripeConstants.API_KEY, new TokenCallback() {
                    public void onSuccess(Token token) {
                        String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
                        String customerId = new SharedPreference(activity).getString(Constants.CUSTOMER_ID, "");
                        JsonObject object = new JsonObject();
                        object.addProperty("userId", userId);
                        object.addProperty("sourceToken", "tok_visa");
                        object.addProperty("customerId", customerId);
                        hitAddtoCardApi(object);
                    }

                    public void onError(Exception error) {
                        Toast.makeText(activity, "Error Occured while Verifying Your Card", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progress.dismiss();
                Toast.makeText(activity, "These Card Details Are Not Valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidCard() {
        cardNumber = et_card_number.getText().toString().trim().replace("-", "");
        expMonth = et_mm.getText().toString().trim();
        expYear = et_yy.getText().toString().trim();
        cvc = et_cvv.getText().toString();

        if (cardNumber.isEmpty() || cardNumber.length() < 14) {
            Toast.makeText(activity, "Please Enter a Valid Card Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (expMonth.isEmpty()) {
            Toast.makeText(activity, "Please Enter Expiry Month of Your Card", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (expYear.isEmpty()) {
            Toast.makeText(activity, "Please Enter Expiry Year of Your Card", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cvc.isEmpty()) {
            Toast.makeText(activity, "Please Enter Three Digits Printed Behind Your Card", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initView() {
        button_pay = (Button) view.findViewById(R.id.button_pay);
        button_pay.setOnClickListener(this);
        tv_header = ((TextView) view.findViewById(R.id.tv_header));
        et_card_number = ((EditText) view.findViewById(R.id.et_card_number));
        et_mm = ((EditText) view.findViewById(R.id.et_mm));
        et_yy = ((EditText) view.findViewById(R.id.et_yy));
        et_cvv = ((EditText) view.findViewById(R.id.et_cvv));
        iv_yyyy_down = (ImageView) view.findViewById(R.id.iv_yyyy_down);
        iv_mm_down = (ImageView) view.findViewById(R.id.iv_mm_down);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_yyyy_down.setOnClickListener(this);
        iv_mm_down.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        et_yy.setText(String.valueOf(year));
        if (month < 10) {
            et_mm.setText(String.valueOf("0" + month));
        } else {
            et_mm.setText(String.valueOf(month));
        }
        ((TextView) view.findViewById(R.id.tv_header)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_card_number)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_mm)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_yy)).setTypeface(normal);
        ((EditText) view.findViewById(R.id.et_cvv)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_pay)).setTypeface(normal);
        et_card_number.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pay:
                Helper.hideSoftKeyBoard(activity);
                addCard();
                break;
            case R.id.iv_yyyy_down:
                showYearDialog();
                break;
            case R.id.iv_mm_down:
                showMonthDialog();
                break;
            case R.id.iv_back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String ss = et_card_number.getText().toString().trim();
        if (ss.length() == 1) {
            if (ss.equals("-"))
                et_card_number.setText("");
            else
                return;
        } else {
            return;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        et_card_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                } else {
                    cardNumber = et_card_number.getText().toString();
                    len[0] = cardNumber.length() + 1;
                    if (len[0] % 5 == 0) {
                        et_card_number.append("-");
                    }

                }
                return false;
            }
        });
    }

    public void showMonthDialog() {
        final Dialog d = new Dialog(activity);
        d.setTitle("Pick Expiry Month");
        d.setContentView(R.layout.yeardialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(12);
        nopicker.setMinValue(1);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(month);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                int month = nopicker.getValue();
                if (month < 10) {
                    et_mm.setText(String.valueOf("0" + nopicker.getValue()));
                } else {
                    et_mm.setText(String.valueOf(nopicker.getValue()));
                }
                month = nopicker.getValue();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }


    public void showYearDialog() {

        final Dialog d = new Dialog(activity);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.yeardialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year + 50);
        nopicker.setMinValue(year - 50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_yy.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }


    private String getMonth(int month) {
        String monthName = "";
        switch (month) {
            case 1:

                break;

        }


        return String.valueOf(month);
    }

    private void replaceKeyCodeSubStractWithEmptyString() {

    }


    private void hitAddtoCardApi(JsonObject object) {
        Network.requestForAddCard(activity, object, this);
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
