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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Activity.MainActivity;
import com.app.homecookie.Beans.AddressBean;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryAddressFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack {

    private TextView tv_header, tv_add;
    private RecyclerView recyclerView;
    private RelativeLayout rl_no_data;
    private ImageView iv_back;
    private View view;
    private Activity activity;
    private Typeface bold;
    private Typeface normal;
    private String userId;
    private SharedPreference sharedpreferenece;
    private Progress progress;
    ArrayList<AddressBean> addressList = new ArrayList<AddressBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delivery_address, container, false);
        activity = getActivity();
        sharedpreferenece = new SharedPreference(activity);
        userId = sharedpreferenece.getString(Constants.USER_USR_ID, "0");
        normal = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        bold = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);
        if (Network.isConnected(activity)) {
            progress.show();
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            Network.requestForAddressList(activity, obj, this);
        } else {
            Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
        setTypeFace();
        return view;
    }

    private void initNoConnectionView() {
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        rl_no_data.setVisibility(View.VISIBLE);
        rl_no_data.setOnClickListener(this);
    }

    private void initView(ArrayList<AddressBean> addressList) {

        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
        AddressRecyclerAdapter adapter = new AddressRecyclerAdapter(activity, addressList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        iv_back.setOnClickListener(this);
    }

    private void setTypeFace() {
        ((TextView) view.findViewById(R.id.tv_header)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_add)).setTypeface(bold);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                ((HomeActivity) activity).replaceAddNewAddressFragment(null, false);
                break;
            case R.id.iv_back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUESET_TYPE_ADDRESS_LIST) {
            progress.dismiss();
            if (isSuccess) {
                addressList = (ArrayList<AddressBean>) data;
                initView(addressList);
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
            if (!isSuccess) {
                progress.dismiss();
                initNoConnectionView();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestType == Network.REQUESET_TYPE_DELETE_ADDRESS) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            ((HomeActivity) activity).replaceDeliveryAddressFragment();
        }

    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressRecyclerAdapter.ViewHolder> {
        Activity activity;
        ArrayList<AddressBean> addressList = new ArrayList<>();

        public AddressRecyclerAdapter(Activity activity, ArrayList<AddressBean> addressList) {
            this.activity = activity;
            this.addressList = addressList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.delivery_address_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.radio_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        unCheckOhter(position);
                    }
                }
            });
            holder.tv_user_name.setText(addressList.get(position).getName());
            holder.tv_user_address.setText(addressList.get(position).getAddress());
            holder.tv_phone.setText(addressList.get(position).getPhoneNumber());
            final String id = String.valueOf(addressList.get(position).getAddressId());
            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.show();
                    showDeleteAddressDialog(id);
                }
            });


            holder.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddressBean bean = addressList.get(position);
                    ((HomeActivity) activity).replaceAddNewAddressFragment(bean, true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return addressList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_user_name;
            TextView tv_user_address;
            TextView tv_phone;
            TextView tv_edit;
            CheckBox radio_selected;
            ImageView iv_delete;
            CardView item_layout;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
                tv_user_address = (TextView) itemView.findViewById(R.id.tv_user_address);
                tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
                tv_edit = (TextView) itemView.findViewById(R.id.tv_edit);
                radio_selected = (CheckBox) itemView.findViewById(R.id.radio_selected);
                iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
                item_layout = (CardView) itemView.findViewById(R.id.item_layout);
                tv_user_name.setTypeface(bold);
                tv_user_address.setTypeface(normal);
                tv_phone.setTypeface(normal);
                tv_edit.setTypeface(normal);
            }
        }
    }


    private void unCheckOhter(int position) {
        View recyclerChildView;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerChildView = recyclerView.getChildAt(i);
            CheckBox checkBox = (CheckBox) recyclerChildView.findViewById(R.id.radio_selected);
            if (i == position) {
            } else {
                checkBox.setChecked(false);
            }
        }
    }


    private void showDeleteAddressDialog(final String addressId) {
        new AlertDialog.Builder(activity)
                .setTitle("Delete Address")
                .setMessage("Are you sure you want to Delete Address?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("addressId", addressId);
                        hitDelete(obj);
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


    private void hitDelete(JsonObject obj) {
        Network.requestForDeleteAddress(activity, obj, this);
    }
}
