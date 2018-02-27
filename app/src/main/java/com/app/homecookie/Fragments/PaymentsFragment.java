package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Activity.MainActivity;
import com.app.homecookie.Beans.CardBean;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;

import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, OnNetworkCallBack {
    private RecyclerView recyclerView;
    private View view;
    private Activity activity;
    private Typeface bold;
    private Typeface normal;
    private LinearLayout ll_cash, ll_net_banking, ll_atm;
    private CheckBox cb_atm, cb_cash, cb_net_banking;
    private RelativeLayout rl_no_data;
    private TextView tv_no_data;
    private Progress progress;
    private ArrayList<CardBean> cardList = new ArrayList<>();

    public static PaymentsFragment newInstance() {

        Bundle args = new Bundle();

        PaymentsFragment fragment = new PaymentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payments, container, false);
        activity = getActivity();
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        normal = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        bold = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        initView();
        setTypeFace();
        if (Network.isConnected(activity)) {
            progress.show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
            JsonObject object = new JsonObject();
            object.addProperty("userId", userId);
            Network.requestForGetCardList(activity, object, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
        }


        return view;
    }

    private void setTypeFace() {
        ((TextView) view.findViewById(R.id.tv_header)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_lbl_saved_card)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_or)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_atm)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_net_banking)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_cash_on_delivery)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_no_data)).setTypeface(bold);
        ((Button) view.findViewById(R.id.button_next)).setTypeface(bold);
    }

    private void setCardList(ArrayList<CardBean> cardList) {
        recyclerView.setVisibility(View.VISIBLE);
        rl_no_data.setVisibility(View.GONE);
        recyclerView.setNestedScrollingEnabled(false);
        CardRecyclerAdapter adapter = new CardRecyclerAdapter(activity, cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        cb_atm = (CheckBox) view.findViewById(R.id.cb_atm);
        cb_cash = (CheckBox) view.findViewById(R.id.cb_cash);
        cb_net_banking = (CheckBox) view.findViewById(R.id.cb_net_banking);
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        tv_no_data = (TextView) view.findViewById(R.id.tv_no_data);

        ll_cash = (LinearLayout) view.findViewById(R.id.ll_cash);
        ll_net_banking = (LinearLayout) view.findViewById(R.id.ll_net_banking);
        ll_atm = (LinearLayout) view.findViewById(R.id.ll_atm);

        ll_cash.setOnClickListener(this);
        ll_net_banking.setOnClickListener(this);
        ll_atm.setOnClickListener(this);

        cb_atm.setOnCheckedChangeListener(this);
        cb_cash.setOnCheckedChangeListener(this);
        cb_net_banking.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.cb_atm && isChecked) {
                cb_cash.setChecked(false);
                cb_net_banking.setChecked(false);
            }
            if (buttonView.getId() == R.id.cb_cash) {
                cb_atm.setChecked(false);
                cb_net_banking.setChecked(false);
            }
            if (buttonView.getId() == R.id.cb_net_banking) {
                cb_atm.setChecked(false);
                cb_cash.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cash:
                // ((HomeActivity) activity).replaceEnterCardDetailFragment();
                break;
            case R.id.ll_net_banking:
                // ((HomeActivity) activity).replaceEnterCardDetailFragment();
                break;
            case R.id.ll_atm:
                ((HomeActivity) activity).replaceEnterCardDetailFragment();
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (requestType == Network.REQUEST_TYPE_GET_CARD_LIST) {
            cardList = (ArrayList<CardBean>) data;
            setCardList(cardList);
        }
        if (requestType == Network.REQUEST_TYPE_DELETE_CARD) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
            JsonObject object = new JsonObject();
            object.addProperty("userId", userId);
            Network.requestForGetCardList(activity, object, this);
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        if (msg.equalsIgnoreCase("No Card Found")) {
            recyclerView.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }

    }


    class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder> {
        Activity activity;
        ArrayList<CardBean> cardList = new ArrayList<>();

        public CardRecyclerAdapter(Activity activity, ArrayList<CardBean> cardList) {
            this.cardList = cardList;
            this.activity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.card_detail_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_card_name.setText(cardList.get(position).getCardName());
            final String cardNumber = "************" + cardList.get(position).getCardNumber();
            holder.tv_card_number.setText(cardNumber);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmDialog(cardList.get(position).getCardId());
                }
            });
            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        unCheckOhter(position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_card_name;
            TextView tv_card_number;
            ImageView iv_delete;
            CheckBox cb_selected;
            CardView item_layout;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_card_name = (TextView) itemView.findViewById(R.id.tv_card_name);
                tv_card_number = (TextView) itemView.findViewById(R.id.tv_card_number);
                iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
                cb_selected = (CheckBox) itemView.findViewById(R.id.cb_selected);
                item_layout = (CardView) itemView.findViewById(R.id.item_layout);
                tv_card_name.setTypeface(normal);
                tv_card_number.setTypeface(normal);
            }
        }
    }

    private void unCheckOhter(int position) {
        View recyclerChildView;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerChildView = recyclerView.getChildAt(i);
            CheckBox checkBox = (CheckBox) recyclerChildView.findViewById(R.id.cb_selected);
            if (i == position) {
            } else {
                checkBox.setChecked(false);
            }
        }
    }


    public void showDeleteConfirmDialog(final String cardId) {
        new AlertDialog.Builder(activity)
                .setTitle("Delete!!!")
                .setMessage("Are you sure you want to Delete this Card?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progress.show();
                        String customerId = new SharedPreference(activity).getString(Constants.CUSTOMER_ID, "0");
                        JsonObject object = new JsonObject();
                        object.addProperty("customerId", customerId);
                        object.addProperty("cardId", cardId);
                        callDeleteCard(object);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void callDeleteCard(JsonObject object) {
        Network.requestForDeleteCard(activity, object, this);
    }

}
