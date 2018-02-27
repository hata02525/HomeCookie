package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.homecookie.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonateFragment extends Fragment {

    private TextView tv_news_feed;
    private RecyclerView recyclerView;
    private Activity activity;
    Typeface face;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_donate, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        DonateAdapter donateAdapter = new DonateAdapter();
        recyclerView.setAdapter(donateAdapter);
        tv_news_feed = (TextView) view.findViewById(R.id.tv_news_feed);
        tv_news_feed.setTypeface(face);
        return view;
    }

    class DonateAdapter extends RecyclerView.Adapter<DonateAdapter.ViewHolder>{

        @Override
        public DonateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.donate_item_layout, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DonateAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_donate_1;
            TextView tv_donate_2;
            TextView tv_donate_3;
            TextView tv_donate_4;
            SimpleDraweeView iv_meal;
            ImageView iv_user;
            ImageView iv_recipe;
            ImageView iv_delivery;
            RatingBar rating_bar_meal;
            TextView tv_meal_name;
            TextView tv_price_per_pax;
            TextView tv_address;
            TextView tv_comments;
            LinearLayout ll_item;
            LinearLayout ll_like;
            CheckBox radio_fav;

            public ViewHolder(View v) {
                super(v);
                tv_donate_1 = (TextView) v.findViewById(R.id.tv_donate_1);
                tv_donate_2 = (TextView) v.findViewById(R.id.tv_donate_2);
                tv_donate_3 = (TextView) v.findViewById(R.id.tv_donate_3);
                tv_donate_4 = (TextView) v.findViewById(R.id.tv_donate_4);
                tv_donate_1.setTypeface(face);
                tv_donate_2.setTypeface(face);
                tv_donate_3.setTypeface(face);
                tv_donate_4.setTypeface(face);
                iv_meal = (SimpleDraweeView) itemView.findViewById(R.id.iv_meal);
                iv_user = (ImageView) itemView.findViewById(R.id.iv_user);
                iv_recipe = (ImageView) itemView.findViewById(R.id.iv_recipe);
                iv_delivery = (ImageView) itemView.findViewById(R.id.iv_delivery);
                rating_bar_meal = (RatingBar) itemView.findViewById(R.id.rating_bar_meal);
                tv_meal_name = (TextView) itemView.findViewById(R.id.tv_meal_name);
                tv_price_per_pax = (TextView) itemView.findViewById(R.id.tv_price_per_pax);
                tv_address = (TextView) itemView.findViewById(R.id.tv_address);
                tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);
                ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
                radio_fav = (CheckBox) itemView.findViewById(R.id.radio_fav);
                ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);
                tv_price_per_pax.setTypeface(face);
                tv_address.setTypeface(face);
                tv_comments.setTypeface(face);
                tv_meal_name.setTypeface(face);
                ((TextView) itemView.findViewById(R.id.tv_lbl_price)).setTypeface(face);
            }
        }
    }
}
