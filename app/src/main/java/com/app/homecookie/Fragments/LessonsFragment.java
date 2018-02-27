package com.app.homecookie.Fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.MealListBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 */
public class LessonsFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack {

    private Activity activity;
    private RecyclerView recyclerView;
    private FloatingActionButton fab_create_meal;
    private RelativeLayout rl_no_data;
    private TextView tv_no_data;
    SharedPreference sharedPreference;
    private ImageView iv_filter;
    String userId = "";
    Progress progress;
    ArrayList<MealListBean> mealList = new ArrayList<MealListBean>();
    View view;
    Typeface face = null;
    PopupWindow pw;
    int pageNumber = 0;
    boolean noData = false;
    private boolean isPageNumberUpdated;
    LessonsAdapter adapter;
    private int filter=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lessons, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        sharedPreference = new SharedPreference(activity);
        userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
        adapter = new LessonsAdapter(activity, mealList);
        iv_filter = (ImageView) view.findViewById(R.id.iv_filter);
        iv_filter.setOnClickListener(this);
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        tv_no_data = (TextView) view.findViewById(R.id.tv_no_data);
        tv_no_data.setOnClickListener(this);
        fab_create_meal = (FloatingActionButton) view.findViewById(R.id.fab_create_meal);
        fab_create_meal.setOnClickListener(this);
        if (Network.isConnected(activity)) {
            rl_no_data.setVisibility(View.GONE);
            progress.show();
            JsonObject object = new JsonObject();
            object.addProperty("userId", userId);
            object.addProperty("filterId", String.valueOf(filter));
            object.addProperty("page", String.valueOf(pageNumber));
            Network.setRequestForLessonList(activity, object, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
            tv_no_data.setText("No Internet Connection \n Click To Try Again");
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void initView(ArrayList<MealListBean> mealList) {
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (mealList.size() > 0) {
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            adapter = new LessonsAdapter(activity, mealList);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
            tv_no_data.setText("No Meals Found in Your Meal List\nClick To Try Again");
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!noData) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (dy > 0) {
                            hitapi(filter);
                        }
                    }
                }
            }


        });
    }

    private void hitapi(int filter) {
        progress.show();
        if (!noData && isPageNumberUpdated) {
            JsonObject object = new JsonObject();
            object.addProperty("userId", userId);
            object.addProperty("filterId", String.valueOf(filter));
            object.addProperty("page", String.valueOf(pageNumber));
            Network.setRequestForLessonList(activity, object, this);
            pageNumber++;
            isPageNumberUpdated = true;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_create_meal:
                ((HomeActivity) activity).replaceCreateLessonFragment();
                break;
            case R.id.tv_no_data:
                if (Network.isConnected(activity)) {
                    rl_no_data.setVisibility(View.GONE);
                    progress.show();
                    JsonObject object = new JsonObject();
                    object.addProperty("userId", userId);
                    Network.setRequestForLessonList(activity, object, this);
                } else {
                    rl_no_data.setVisibility(View.VISIBLE);
                    tv_no_data.setText("No Internet Connection \n Click To Try Again");
                }
                break;
            case R.id.iv_filter:
                showFilterPopup(iv_filter);
                break;
            case R.id.popular:
                filter = 1;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;
            case R.id.rating:
                filter = 2;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;
            case R.id.newest:
                filter = 3;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;
            case R.id.nearme:
                filter = 4;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;
            case R.id.category:
                filter = 5;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;
            case R.id.rated:
                filter = 6;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;
            case R.id.purchased:
                filter = 7;
                pageNumber = 0;
                hitapi(filter);
                pw.dismiss();
                break;

        }
    }


    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_LESSON_LIST) {
            progress.dismiss();
            if (pageNumber == 0) {
                mealList = (ArrayList<MealListBean>) data;
                if (mealList.size() == 0) {
                    noData = true;
                    isPageNumberUpdated = false;
                }
                initView(mealList);
                pageNumber++;
                isPageNumberUpdated = true;
            } else {
                ArrayList<MealListBean> mealsList = (ArrayList<MealListBean>) data;
                if (mealsList.size() == 0) {
                    noData = true;
                    isPageNumberUpdated = false;
                }
                mealList.addAll(mealsList);
                adapter.notifyDataSetChanged();
            }
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
    }


    class LessonsAdapter extends RecyclerView.Adapter {

        Activity activity;
        ArrayList<MealListBean> mealList;

        public LessonsAdapter(Activity activity, ArrayList<MealListBean> mealList) {
            this.activity = activity;
            this.mealList = mealList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.meal_and_lessson_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            final String lessonId = mealList.get(position).getMealId();
            final String mealName = mealList.get(position).getTitle();
            String comment = "$" + mealList.get(position).getPrice() + "/pax";
            String comments = String.valueOf(mealList.get(position).getCommentCount());
            holder.tv_meal_name.setText(mealList.get(position).getTitle());
            holder.tv_comments.setText("Comments " + comments);
            holder.tv_price_per_pax.setText(comment);
            String mealphoto = mealList.get(position).getPhoto();
            String userPhoto = mealList.get(position).getUserphoto();
            String rating = mealList.get(position).getAvgMealRating();
            //Helper.setProfilePic(activity, mealphoto, holder.iv_meal);
            Uri mealImageUri = Uri.parse(mealphoto);
            holder.iv_meal.setImageURI(mealImageUri);
            Helper.setProfilePic(activity, userPhoto, holder.iv_user);
            holder.iv_delivery.setVisibility(View.GONE);
            if (mealList.get(position).getLike() == 1) {
                holder.radio_fav.setChecked(true);
            } else {
                holder.radio_fav.setChecked(false);
            }
            if (!rating.isEmpty()) {
                holder.rating_bar_meal.setRating(Float.valueOf(rating));
            }
            holder.iv_recipe.setVisibility(View.GONE);
            holder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) activity).replaceLessonDetailFragment(lessonId);
                }
            });
            String address = mealList.get(position).getAddress();
            holder.tv_address.setText(address);


            holder.radio_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        if (isChecked) {
                            if (Network.isConnected(activity)) {
                                buttonView.setChecked(true);
                                mealList.get(position).setLike(1);
                                callToApi(position, String.valueOf(mealList.get(position).getLike()), mealList);
                            } else {
                                Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!isChecked) {
                            if (Network.isConnected(activity)) {
                                mealList.get(position).setLike(0);
                                buttonView.setChecked(false);
                                callToApi(position, String.valueOf(mealList.get(position).getLike()), mealList);
                            } else {
                                Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        return;
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return mealList.size();
        }


        private class ViewHolder extends RecyclerView.ViewHolder {

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
            CheckBox radio_fav;


            public ViewHolder(View itemView) {
                super(itemView);
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
                tv_price_per_pax.setTypeface(face);
                tv_address.setTypeface(face);
                tv_comments.setTypeface(face);
                tv_meal_name.setTypeface(face);
                ((TextView) itemView.findViewById(R.id.tv_lbl_price)).setTypeface(face);

            }


        }
    }

    private void callToApi(int position, String likeStatus, ArrayList<MealListBean> mealList) {
        progress.show();
        JsonObject object = new JsonObject();
        String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
        object.addProperty("userId", userId);
        object.addProperty("type", Constants.LESSON_TYPE);
        object.addProperty("typeId", mealList.get(position).getMealId());
        object.addProperty("likeStatus", likeStatus);
        Network.requestForLike(activity, object, this);
    }

    private void showFilterPopup(ImageView iv_options) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.lesson_and_meal_filters_popup, null, false);
            //initializing view's components here
            // create a 300px width and 470px height PopupWindow
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            width = (width / 3) + 170;
            Log.e("fdfsd", width + "");
            pw = new PopupWindow(layout, width,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            pw.setAnimationStyle(R.style.popup_anim);
            // display popup
            pw.showAtLocation(iv_options, Gravity.LEFT, 140, 200);


            TextView purchased = (TextView) layout.findViewById(R.id.purchased);
            TextView reated = (TextView) layout.findViewById(R.id.rated);
            TextView category = (TextView) layout.findViewById(R.id.category);
            TextView nearme = (TextView) layout.findViewById(R.id.nearme);
            TextView newest = (TextView) layout.findViewById(R.id.newest);
            TextView rating = (TextView) layout.findViewById(R.id.rating);
            TextView popular = (TextView) layout.findViewById(R.id.rating);
            purchased.setTypeface(face);
            reated.setTypeface(face);
            category.setTypeface(face);
            nearme.setTypeface(face);
            newest.setTypeface(face);
            rating.setTypeface(face);
            popular.setTypeface(face);
            purchased.setOnClickListener(this);
            reated.setOnClickListener(this);
            category.setOnClickListener(this);
            nearme.setOnClickListener(this);
            newest.setOnClickListener(this);
            rating.setOnClickListener(this);
            popular.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
