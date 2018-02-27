package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.CommentActivity;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.MealDetailsBean;
import com.app.homecookie.Beans.MealListBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class LessonDetailFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {
    ImageView iv_meal;
    ImageView iv_pic;
    ImageView iv_poster;
    TextView tv_desc;
    TextView tv_poster_name;
    TextView tv_total_rating;
    TextView tv_date_time;
    TextView tv_category_label;
    TextView tv_price_lbl;
    TextView tv_category;
    TextView tv_price;
    TextView tv_location_lbl;
    TextView tv_dsc;
    TextView tv_servings_lbl;
    TextView tv_servings;
    TextView tv_comments_expand;
    Button button_chat;
    Button button_cancel_order;
    Button btn_post;
    Button button_place_order;
    private TextView tv_comments;
    private EditText et_comment;
    private RelativeLayout rl_comments;
    private LinearLayout ll_comments_view;
    private LinearLayout ll_comments;
    private LinearLayout ll_rating;
    ScrollView main_layout;
    SupportMapFragment map_frame;
    RatingBar rb_rate_recipe;
    View view;
    Activity activity;
    GoogleMap mMap;
    String mealId;
    String userId;
    SharedPreference sharedPreference;
    RelativeLayout rl_no_data;
    Progress progress;
    LatLng latlng;
    CheckBox radio_fav;
    MealDetailsBean bean = new MealDetailsBean();
    ArrayList<MealDetailsBean.Comments> commentList = new ArrayList<MealDetailsBean.Comments>();
    String comment = "";
    Typeface face = null;
    private AlertDialog alertDialog;
    float rating;
    String isToRate;

    public static LessonDetailFragment newInstance(String lessonId) {
        LessonDetailFragment fragment = new LessonDetailFragment();
        Bundle args = new Bundle();
        args.putString(Constants.MEAL_ID, lessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = getArguments().getString(Constants.MEAL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meal_detail, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        main_layout = (ScrollView) view.findViewById(R.id.main_layout);
        sharedPreference = new SharedPreference(activity);
        userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
        map_frame = new SupportMapFragment();
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        rl_no_data.setOnClickListener(this);
        if (Network.isConnected(activity)) {
            progress.show();
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            obj.addProperty("lessonId", mealId);
            Network.requestForLessonDetail(activity, obj, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void initView(MealDetailsBean bean) {
        main_layout.setVisibility(View.VISIBLE);
        btn_post = (Button) view.findViewById(R.id.btn_post);
        button_place_order = (Button) view.findViewById(R.id.button_place_order);
        TextView tv_comments = (TextView) view.findViewById(R.id.tv_comments);
        ll_comments_view = (LinearLayout) view.findViewById(R.id.ll_comments_view);
        ll_comments = (LinearLayout) view.findViewById(R.id.ll_comments);
        ll_rating = (LinearLayout) view.findViewById(R.id.ll_rating);
        RelativeLayout rl_comments = (RelativeLayout) view.findViewById(R.id.rl_comments);
        LinearLayout ll_comments_view = (LinearLayout) view.findViewById(R.id.ll_comments_view);
        LinearLayout ll_comments = (LinearLayout) view.findViewById(R.id.ll_comments);
        button_chat = (Button) view.findViewById(R.id.button_chat);
        button_place_order = (Button) view.findViewById(R.id.button_place_order);
        button_cancel_order = (Button) view.findViewById(R.id.button_cancel_order);
        tv_poster_name = (TextView) view.findViewById(R.id.tv_poster_name);
        tv_total_rating = (TextView) view.findViewById(R.id.tv_total_rating);
        tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
        tv_category_label = (TextView) view.findViewById(R.id.tv_category_label);
        tv_price_lbl = (TextView) view.findViewById(R.id.tv_price_lbl);
        tv_category = (TextView) view.findViewById(R.id.tv_category);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_location_lbl = (TextView) view.findViewById(R.id.tv_location_lbl);
        tv_servings_lbl = (TextView) view.findViewById(R.id.tv_servings_lbl);
        tv_servings = (TextView) view.findViewById(R.id.tv_servings);
        tv_dsc = (TextView) view.findViewById(R.id.tv_dsc);
        tv_comments_expand = (TextView) view.findViewById(R.id.tv_comments_expand);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        iv_meal = (ImageView) view.findViewById(R.id.iv_meal);
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        iv_poster = (ImageView) view.findViewById(R.id.iv_poster);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        rb_rate_recipe = (RatingBar) view.findViewById(R.id.rb_recipe_total);
        radio_fav = (CheckBox) view.findViewById(R.id.radio_fav);
        radio_fav.setOnCheckedChangeListener(this);
        Helper.setProfilePic(activity, bean.getPhoto(), iv_meal);
        Helper.setProfilePic(activity, bean.getUserPhoto(), iv_poster);
        tv_desc.setText(bean.getTitle());
        tv_poster_name.setText(bean.getUserFirstName() + " " + bean.getUserLastName());
        tv_category.setText(bean.getCategoryName());
        tv_price.setText(bean.getPrice() + "/pax");
        tv_dsc.setText("Description :" + bean.getDescription());
        tv_servings.setText(bean.getServiceQty());
        String date = bean.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
        try {
            Date date1 = sdf.parse(date);
            String formattedDate = format.format(date1);
            tv_date_time.setText(formattedDate);
        } catch (ParseException e) {
            tv_date_time.setText(bean.getDate());
            e.printStackTrace();
        }
        Double lat = Double.valueOf(bean.getLatitude());
        Double lng = Double.valueOf(bean.getLongitude());
        latlng = new LatLng(lat, lng);
        getChildFragmentManager().beginTransaction().add(R.id.map_frame, map_frame).commitAllowingStateLoss();
        if (map_frame != null) {
            map_frame.getMapAsync(this);
        }

        if (commentList != null) {
            prepareCommentsView(commentList);
        }
        if (bean.getLike() == 1) {
            radio_fav.setChecked(true);
        } else {
            radio_fav.setChecked(false);
        }
        isToRate = bean.getAvgMealRating();
        String rating = bean.getAvgMealRating();
        if (!rating.isEmpty()) {
            rb_rate_recipe.setRating(Float.valueOf(rating));
            tv_total_rating.setText(String.valueOf(rating));
        }

        int totalComment = bean.getCommentCount();
        sharedPreference.putInt(Constants.COMMENT_COUNT, totalComment);
        tv_comments_expand.setText("Comments(" + String.valueOf(totalComment) + ")");

        tv_desc.setTypeface(face);
        tv_poster_name.setTypeface(face);
        tv_total_rating.setTypeface(face);
        tv_date_time.setTypeface(face);
        tv_category_label.setTypeface(face);
        tv_category.setTypeface(face);
        tv_dsc.setTypeface(face);
        button_chat.setTypeface(face);
        button_place_order.setTypeface(face);
        tv_comments_expand.setTypeface(face);
        tv_price_lbl.setTypeface(face);
        tv_price.setTypeface(face);
        tv_comments_expand.setTypeface(face);
        et_comment.setTypeface(face);
        btn_post.setTypeface(face);
        tv_location_lbl.setTypeface(face);
        button_cancel_order.setTypeface(face);
        tv_servings_lbl.setTypeface(face);
        tv_servings.setTypeface(face);
        ll_rating.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        tv_comments_expand.setText("Comments(" + bean.getCommentCount() + ")");
        tv_comments_expand.setOnClickListener(this);


        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_pic);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_pic);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.setMyLocationEnabled(false);
        LatLng loc = null;
        if (latlng.toString().equalsIgnoreCase("lat/lng: (0.0,0.0)")) {
            loc = new LatLng(28.6127630, 77.3871320);
        } else {
            loc = latlng;
        }
        mMap.addMarker(new MarkerOptions().position(loc).title("New Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
        // CameraUpdateFactory.newLatLngZoom(loc, 12.0f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_no_data:
                break;
            case R.id.tv_comments_expand:
                Intent intent = new Intent(activity, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MEAL_ID, mealId);
                bundle.putString(Constants.TYPE_ID, "2");
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.COMMENT_COUNT_CODE);
                break;
            case R.id.ll_rating:
                if (isToRate.equalsIgnoreCase("0.00")) {
                    showRatRecipeDialog();
                } else {
                    Toast.makeText(activity, "You Have Already Given Rating", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_LESSON_DETAIL) {
            progress.dismiss();
            bean = (MealDetailsBean) data;
            commentList = (ArrayList<MealDetailsBean.Comments>) bean.getCommentList();
            initView(bean);
        }
        if (requestType == Network.REQUEST_TYPE_COMMENT) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            ((HomeActivity) activity).replaceLessonDetailFragment(mealId);
        }
        if (requestType == Network.REQUEST_RATE_THE_RECIPE) {
            progress.dismiss();
            rb_rate_recipe.setRating(rating);
            isToRate = String.valueOf(rating);
            tv_total_rating.setText(isToRate);
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }

        if (requestType == Network.REQUEST_TYPE_LIKE) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        rl_no_data.setVisibility(View.VISIBLE);
    }

    private boolean isValidComment() {
        comment = et_comment.getText().toString().trim();
        if (comment.isEmpty()) {
            Toast.makeText(activity, "Please Enter a Comment First", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void prepareCommentsView(ArrayList<MealDetailsBean.Comments> commentList) {
        LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < commentList.size(); i++) {
            View v = vi.inflate(R.layout.recipe_comment_layout, null);
            ImageView iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
            EditText tv_comment = (EditText) v.findViewById(R.id.tv_comment);
            TextView tv_date_time = (TextView) v.findViewById(R.id.tv_date_time);
            ImageView iv_options = (ImageView) v.findViewById(R.id.iv_options);
            String commentText = commentList.get(i).getCommentText();
            String photo = commentList.get(i).getPhoto();
            String date = commentList.get(i).getCreated_at();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
            try {
                Date date1 = sdf.parse(date);
                String formattedDate = format.format(date1);
                tv_date_time.setText(formattedDate);
            } catch (ParseException e) {
                tv_date_time.setText(commentList.get(i).getCreated_at());
                e.printStackTrace();
            }

            String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
            if (userPhoto.contains("http://")) {
                Helper.setProfilePic(activity, photo, iv_pic);
            } else {
                String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
                Helper.setProfilePic(activity, userPhotoo, iv_pic);
            }


            tv_comment.setTypeface(face);
            tv_date_time.setTypeface(face);
            tv_comment.setText(commentText);
            ll_comments.addView(v);

        }
    }

    private void showRatRecipeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rat_recipe_dialog, null);
        Button btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        ((TextView) dialogView.findViewById(R.id.tv_header)).setTypeface(face);
        btn_confirm.setTypeface(face);
        btn_cancel.setTypeface(face);
        final RatingBar rb_rate_recipe = (RatingBar) dialogView.findViewById(R.id.rb_rate_recipe);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Network.isConnected(activity)) {
                    rating = rb_rate_recipe.getRating();
                    if (rating > 0) {
                        progress.show();
                        alertDialog.dismiss();
                        JsonObject object = new JsonObject();
                        object.addProperty("userId", userId);
                        object.addProperty("typeId", mealId);
                        object.addProperty("ratingCount", rb_rate_recipe.getRating());
                        object.addProperty("type", "2");
                        hitRatingApi(object);
                    } else {
                        Toast.makeText(activity, "Please Provide Rating", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hitRatingApi(JsonObject object) {
        progress.show();
        Network.requestForRating(activity, object, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int comment = sharedPreference.getInt(Constants.COMMENT_COUNT, 0);
        tv_comments_expand.setText("Comments(" + String.valueOf(comment) + ")");
    }


    private void callToApi(String likeStatus) {
        progress.show();
        JsonObject object = new JsonObject();
        String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
        object.addProperty("userId", userId);
        object.addProperty("type", Constants.LESSON_TYPE);
        object.addProperty("typeId", mealId);
        object.addProperty("likeStatus", likeStatus);
        Network.requestForLike(activity, object, this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isPressed()) {
            String isLike = String.valueOf(bean.getLike());
            if (isLike.equalsIgnoreCase("0")) {
                callToApi("1");
            }
            if (isLike.equalsIgnoreCase("1")) {
                callToApi("0");
            }
            if (isLike.equalsIgnoreCase("0")) {
                bean.setLike(1);
            } else {
                bean.setLike(0);
            }
        }
    }
}
