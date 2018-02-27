package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.UserDetailBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.facebook.datasource.SimpleDataSource;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCookieDetailsFragment extends Fragment implements View.OnClickListener, OnNetworkCallBack {

    View view;
    private ScrollView mainLayout;
    private ImageView iv_user, iv_back;
    private TextView tv_name, tv_age, tv_occ, tv_description, tv_no_data, tv_header;
    private RatingBar rating_bar_recipe;
    private Button button_chat, button_follow, button_edit, button_logout, button_post_recipe,
            button_post_meal, button_post_lesson, button_delivery, button_add_payment;
    private RecyclerView recipes_recycler_view, meals_recycler_view, lessons_recycler_view;
    private RelativeLayout rl_no_data;
    Activity activity;
    Typeface normal = null;
    Typeface bold = null;
    boolean isMyProfile;
    String userId = "0";
    String profileId = "";
    Progress progress;
    UserDetailBean bean = new UserDetailBean();
    ArrayList<UserDetailBean.RecipesBean> recipesList = new ArrayList<UserDetailBean.RecipesBean>();
    ArrayList<UserDetailBean.LessonsBean> lessonList = new ArrayList<UserDetailBean.LessonsBean>();
    ArrayList<UserDetailBean.MealsBean> mealsList = new ArrayList<UserDetailBean.MealsBean>();
    private SharedPreference sharedPreference;

    public static HomeCookieDetailsFragment newInstance(boolean isMyProfile, String userId) {
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_MY_PROFILE, isMyProfile);
        args.putString(Constants.FRIEND_ID, userId);
        HomeCookieDetailsFragment fragment = new HomeCookieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isMyProfile = getArguments().getBoolean(Constants.IS_MY_PROFILE);
            profileId = getArguments().getString(Constants.FRIEND_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_cooike_details, container, false);
        activity = getActivity();
        normal = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        bold = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        sharedPreference = new SharedPreference(activity);

        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        tv_no_data = (TextView) view.findViewById(R.id.tv_no_data);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        mainLayout = (ScrollView) view.findViewById(R.id.main_layout);

        SharedPreference sharedPreference = new SharedPreference(activity);
        userId = sharedPreference.getString(Constants.USER_USR_ID, "0");


        if (Network.isConnected(activity)) {
            if (isMyProfile && profileId.isEmpty()) {
                progress.show();
                JsonObject object = new JsonObject();
                object.addProperty("profileId", userId);
                Network.requestForUserDetails(activity, object, this, isMyProfile);
            }
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
            tv_no_data.setOnClickListener(this);

        }

        setTypeFace();
        return view;
    }


    private void setTypeFace() {
        ((TextView) view.findViewById(R.id.tv_name)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_occ)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_age)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_lbl_name)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_lbl_age)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_lbl_occ)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_lbl_desc)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_lbl_home_cook_rating)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_description)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_lbl_my_recipes)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_lbl_my_meals)).setTypeface(bold);
        ((TextView) view.findViewById(R.id.tv_lbl_my_lessons)).setTypeface(bold);
        ((Button) view.findViewById(R.id.button_chat)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_edit)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_logout)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_post_recipe)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_post_meal)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_post_lesson)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_delivery)).setTypeface(normal);
        ((Button) view.findViewById(R.id.button_add_payment)).setTypeface(normal);
        ((TextView) view.findViewById(R.id.tv_header)).setTypeface(normal);


    }


    private void initView(UserDetailBean bean) {
        mainLayout.setVisibility(View.VISIBLE);
        iv_user = (ImageView) view.findViewById(R.id.iv_user);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_occ = (TextView) view.findViewById(R.id.tv_occ);
        tv_description = (TextView) view.findViewById(R.id.tv_description);
        rating_bar_recipe = (RatingBar) view.findViewById(R.id.rating_bar_recipe);
        button_chat = (Button) view.findViewById(R.id.button_chat);
        button_follow = (Button) view.findViewById(R.id.button_follow);
        button_edit = (Button) view.findViewById(R.id.button_edit);
        button_logout = (Button) view.findViewById(R.id.button_logout);
        button_post_recipe = (Button) view.findViewById(R.id.button_post_recipe);
        button_post_meal = (Button) view.findViewById(R.id.button_post_meal);
        button_post_lesson = (Button) view.findViewById(R.id.button_post_lesson);
        button_add_payment = (Button) view.findViewById(R.id.button_add_payment);
        button_delivery = (Button) view.findViewById(R.id.button_delivery);

        String date = bean.getDob();
        String dob = getDob(date);

        tv_name.setText(bean.getFirstName() + " " + bean.getLastName());
        tv_age.setText(dob);
        tv_occ.setText(bean.getOccupation());
        tv_description.setText(bean.getDescription());

        String userPhoto = sharedPreference.getString(Constants.USER_PHOTO,"");
        Helper.setProfilePic(activity, userPhoto, iv_user);

        recipes_recycler_view = (RecyclerView) view.findViewById(R.id.recipes_recycler_view);
        meals_recycler_view = (RecyclerView) view.findViewById(R.id.meals_recycler_view);
        lessons_recycler_view = (RecyclerView) view.findViewById(R.id.lessons_recycler_view);


        if (isMyProfile) {
            rating_bar_recipe.setVisibility(View.GONE);
            button_chat.setVisibility(View.GONE);
            button_follow.setVisibility(View.GONE);
        }


        recipesList = (ArrayList<UserDetailBean.RecipesBean>) bean.getRecipes();
        mealsList = (ArrayList<UserDetailBean.MealsBean>) bean.getMeals();
        lessonList = (ArrayList<UserDetailBean.LessonsBean>) bean.getLessons();

        if (recipesList != null) {
            LinearLayoutManager llm1 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            RecipeAdapter recipeAdapter = new RecipeAdapter(activity, recipesList);
            recipes_recycler_view.setLayoutManager(llm1);
            recipes_recycler_view.setAdapter(recipeAdapter);
        }
        if (mealsList != null) {
            LinearLayoutManager llm2 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            MealsAdapter mealsAdapter = new MealsAdapter(activity, mealsList);
            meals_recycler_view.setLayoutManager(llm2);
            meals_recycler_view.setAdapter(mealsAdapter);
        }
        if (lessonList != null) {
            LinearLayoutManager llm3 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            LessonsAdapter lessonsAdapter = new LessonsAdapter(activity, lessonList);
            lessons_recycler_view.setLayoutManager(llm3);
            lessons_recycler_view.setAdapter(lessonsAdapter);
        }
        button_edit.setOnClickListener(this);
        button_logout.setOnClickListener(this);
        button_delivery.setOnClickListener(this);
        button_add_payment.setOnClickListener(this);
        button_post_recipe.setOnClickListener(this);
        button_post_meal.setOnClickListener(this);
        button_post_lesson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                ((HomeActivity) activity).showLogoutDialog(activity);
                break;
            case R.id.button_edit:
                ((HomeActivity) activity).replaceUpdateProfileFragment();
                break;
            case R.id.button_delivery:
                ((HomeActivity) activity).replaceDeliveryAddressFragment();
                break;
            case R.id.button_add_payment:
                ((HomeActivity) activity).replacePaymentFragment();
                break;
            case R.id.button_post_recipe:
                ((HomeActivity) activity).replacePostFragment();
                sharedPreference.putBoolean(Constants.IS_FROM_HOME_COOKIE_PROFILE, true);
                break;
            case R.id.button_post_meal:
                ((HomeActivity) activity).replaceCreatMealFragment();
                break;
            case R.id.button_post_lesson:
                ((HomeActivity) activity).replaceCreateLessonFragment();
                break;
        }

    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_USER_DETAIL) {
            progress.dismiss();
            bean = (UserDetailBean) data;
            initView(bean);
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
    }


    class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
        Activity activity;
        ArrayList<UserDetailBean.RecipesBean> recipesList = new ArrayList<UserDetailBean.RecipesBean>();

        public RecipeAdapter(Activity activity, ArrayList<UserDetailBean.RecipesBean> recipesList) {
            this.activity = activity;
            this.recipesList = recipesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.item_layout_for_meal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String recipeId = String.valueOf(recipesList.get(position).getRecipeId());
            String imageUri = recipesList.get(position).getRecipePhoto();
            Uri recipeImageUri = Uri.parse(imageUri);
            holder.iv_item.setImageURI(recipeImageUri);
        }

        @Override
        public int getItemCount() {
            return recipesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CardView item;
            private SimpleDraweeView iv_item;

            public ViewHolder(View view) {
                super(view);
                item = (CardView) view.findViewById(R.id.item);
                iv_item = (SimpleDraweeView) view.findViewById(R.id.iv_item);
            }
        }

    }

    class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {
        Activity activity;
        ArrayList<UserDetailBean.MealsBean> mealsList = new ArrayList<UserDetailBean.MealsBean>();

        public MealsAdapter(Activity activity, ArrayList<UserDetailBean.MealsBean> mealsList) {
            this.activity = activity;
            this.mealsList = mealsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.item_layout_for_meal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String mealId = mealsList.get(position).getMealId();
            String imageUri = mealsList.get(position).getMealPhoto();
            Uri mealsImageUri = Uri.parse(imageUri);
            holder.iv_item.setImageURI(mealsImageUri);
        }

        @Override
        public int getItemCount() {
            return mealsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CardView item;
            private ImageView iv_item;

            public ViewHolder(View view) {
                super(view);
                item = (CardView) view.findViewById(R.id.item);
                iv_item = (SimpleDraweeView) view.findViewById(R.id.iv_item);
            }
        }

    }

    class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {
        Activity activity;

        ArrayList<UserDetailBean.LessonsBean> lessonList = new ArrayList<UserDetailBean.LessonsBean>();

        public LessonsAdapter(Activity activity, ArrayList<UserDetailBean.LessonsBean> lessonList) {
            this.activity = activity;
            this.lessonList = lessonList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.item_layout_for_meal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String lessonid = lessonList.get(position).getLessonId();
            String imageUri = lessonList.get(position).getLessonPhoto();
            Uri lessonImageUri = Uri.parse(imageUri);
            holder.iv_item.setImageURI(lessonImageUri);
        }

        @Override
        public int getItemCount() {
            return lessonList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CardView item;
            private SimpleDraweeView iv_item;

            public ViewHolder(View view) {
                super(view);
                item = (CardView) view.findViewById(R.id.item);
                iv_item = (SimpleDraweeView) view.findViewById(R.id.iv_item);
            }
        }

    }


    private String getDob(String date) {
        String dob = "";
        try {
            Date currentDate = new Date();
            Date dateOfBirth = new SimpleDateFormat("yyyy-mm-dd").parse(date);
            String yearOfBirth = String.valueOf(currentDate.getYear() - dateOfBirth.getYear());
            dob = yearOfBirth;
        } catch (ParseException e) {
            dob = date;
            e.printStackTrace();

        }
        return dob;
    }
}
