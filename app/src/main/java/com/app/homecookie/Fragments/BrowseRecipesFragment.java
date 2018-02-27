package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.RecipesBean;
import com.app.homecookie.Beans.SubCategoryBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseRecipesFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {


    private String categoryId;
    private String categoryName;
    private Activity activity;
    SubCategoryBean subCategoryBean = new SubCategoryBean();
    RecipesBean recipesBean = new RecipesBean();

    RecyclerView rv_sub_category;
    RecyclerView rv_recipes;
    ImageView iv_back;
    TextView tv_header;
    TextView tv_category_name;
    Progress progress;
    SharedPreference sharedPreference;
    String userId;
    int pos;
    ArrayList<SubCategoryBean> subCategoryList = new ArrayList<SubCategoryBean>();
    ArrayList<RecipesBean> recipesList = new ArrayList<RecipesBean>();


    public static BrowseRecipesFragment newInstance(String categoryId, String categoryName) {
        BrowseRecipesFragment fragment = new BrowseRecipesFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CATEGORY_ID, categoryId);
        args.putString(Constants.CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.CATEGORY_ID);
            categoryName = getArguments().getString(Constants.CATEGORY_NAME);

        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_browse_recipes, container, false);
        initializeAllViews();
        tv_category_name.setText(categoryName);
        activity = getActivity();
        pos = 0;
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        sharedPreference = new SharedPreference(activity);
        userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
        if (Network.isConnected(activity)) {
            progress.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("categoryId", categoryId);
            Network.requestForSubCategory(activity, jsonObject, this);
        } else {

        }


        return view;
    }

    private void initializeAllViews() {
        rv_sub_category = (RecyclerView) view.findViewById(R.id.rv_sub_category);
        rv_recipes = (RecyclerView) view.findViewById(R.id.rv_recipes);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_category_name = (TextView) view.findViewById(R.id.tv_category_name);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    private void initView(ArrayList<SubCategoryBean> subCategoryList, ArrayList<RecipesBean> recipesList) {
        rv_sub_category.setNestedScrollingEnabled(false);
        rv_sub_category.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rv_sub_category.setLayoutManager(llm);
        SubCategoriesAdapter adapter = new SubCategoriesAdapter(activity, subCategoryList, Integer.parseInt(categoryId));
        rv_sub_category.setAdapter(adapter);
        if (recipesList.size() > 0) {
            rv_recipes.setNestedScrollingEnabled(false);
            rv_recipes.setHasFixedSize(true);
            GridLayoutManager gdm = new GridLayoutManager(activity, 2);
            rv_recipes.setLayoutManager(gdm);
            RecipesAdapter adapter1 = new RecipesAdapter(activity, recipesList);
            rv_recipes.setAdapter(adapter1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (isSuccess) {
            if (requestType == Network.REQUEST_TYPE_BROWSE_SUB_CATEGORY) {
                subCategoryList = (ArrayList<SubCategoryBean>) data;
                JsonObject object = new JsonObject();
                object.addProperty("userId", userId);
                object.addProperty("categoryId", categoryId);
                object.addProperty("subcategoryId", "0");
                Network.requestForGetRecipes(activity, object, this);
            }
            if (requestType == Network.REQUEST_TYPE_BROWSE_RECIPE) {
                progress.dismiss();
                recipesList = (ArrayList<RecipesBean>) data;
                initView(subCategoryList, recipesList);
            }

        }
        if (requestType == Network.REQUEST_TYPE_LIKE) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }


    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder> {
        Activity activity;
        int categoryId;
        ArrayList<SubCategoryBean> subCategoryList = new ArrayList<>();

        public SubCategoriesAdapter(Activity activity, ArrayList<SubCategoryBean> subCategoryList, int categoryId) {
            this.activity = activity;
            this.subCategoryList = subCategoryList;
            this.categoryId = categoryId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategories_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            if (pos == position) {
                holder.tv_sub_category.setTextColor(activity.getResources().getColor(R.color.light_green));
            }

            holder.tv_sub_category.setText(subCategoryList.get(position).getName());
            String imgUrl = subCategoryList.get(position).getImage();

            holder.card_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Network.isConnected(activity)) {
                        progress.show();
                        hitRecipeList(position);
                        pos = position;
                        holder.tv_sub_category.setTextColor(activity.getResources().getColor(R.color.light_green));
                    } else {
                        Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return subCategoryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_sub_category;
            TextView tv_sub_category;
            CardView card_category;

            public ViewHolder(View v) {
                super(v);
                iv_sub_category = (ImageView) v.findViewById(R.id.iv_sub_category);
                tv_sub_category = (TextView) v.findViewById(R.id.tv_sub_category);
                card_category = (CardView) v.findViewById(R.id.card_category);
            }
        }

    }

    private void hitRecipeList(int position) {
        if (position == 0) {
            JsonObject object = new JsonObject();
            object.addProperty("userId", userId);
            object.addProperty("categoryId", categoryId);
            object.addProperty("subcategoryId", "0");
            Network.requestForGetRecipes(activity, object, this);
        } else {
            JsonObject object = new JsonObject();
            object.addProperty("userId", userId);
            object.addProperty("categoryId", categoryId);
            object.addProperty("subcategoryId", subCategoryList.get(position).getId());
            Network.requestForGetRecipes(activity, object, this);
        }
    }

    class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

        Activity activity;
        ArrayList<RecipesBean> recipesList = new ArrayList<>();

        public RecipesAdapter(Activity activity, ArrayList<RecipesBean> recipesList) {
            this.activity = activity;
            this.recipesList = recipesList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final int recipeId = recipesList.get(position).getId();
            final String recipeName = recipesList.get(position).getTitle();
            holder.ratingBar.setActivated(false);
            holder.ratingBar.setRating(recipesBean.getRecipeRating());
            holder.tv_recipe_name.setText(recipesList.get(position).getTitle());
            if (recipesList.get(position).getLike() == 1) {
                holder.radio_fav.setChecked(true);
            } else {
                holder.radio_fav.setChecked(false);
            }
            final String imgUrl = recipesList.get(position).getPhoto();
            final String posterPhoto = recipesList.get(position).getUserPhoto();
            if (!imgUrl.isEmpty())
                Helper.setProfilePic(activity, imgUrl, holder.iv_recipe);
            if (!posterPhoto.isEmpty())
                Helper.setProfilePic(activity, posterPhoto, holder.iv_poster);
            holder.radio_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (Network.isConnected(activity)) {
                            buttonView.setChecked(true);
                            recipesList.get(position).setLike(1);
                            callToApi(position, String.valueOf(recipesList.get(position).getLike()), recipesList);
                        } else {
                            Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (!isChecked) {
                        if (Network.isConnected(activity)) {
                            recipesList.get(position).setLike(0);
                            buttonView.setChecked(false);
                            callToApi(position, String.valueOf(recipesList.get(position).getLike()), recipesList);
                        } else {
                            Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            });

            Ion.with(activity)
                    .load(posterPhoto)
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e == null)
                                holder.iv_poster.setImageBitmap(result);
                            else
                                holder.iv_poster.setImageDrawable(null);
                        }
                    });
            final String fullName = recipesList.get(position).getFirstName() + " " + recipesList.get(position).getLastName();
            holder.rl_recipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //activity.startActivity(new Intent(activity, RecipeViewActivity.class));
                    if (Network.isConnected(activity)) {
                        ((HomeActivity) activity).replaceRecipeViewFragment(String.valueOf(recipeId), recipeName, posterPhoto, fullName);
                    } else {
                        Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            String occupation = recipesList.get(position).getOccupation();
            holder.tv_poster_name.setText(fullName);
            holder.tv_poster_dsc.setText(occupation);
        }

        @Override
        public long getItemId(int position) {
            return recipesList.get(position).getId();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return recipesList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_recipe;
            CheckBox radio_fav;
            TextView tv_recipe_name;
            RatingBar ratingBar;
            ImageView iv_poster;
            TextView tv_poster_name;
            TextView tv_poster_dsc;
            RelativeLayout rl_recipe;

            public ViewHolder(View v) {
                super(v);
                rl_recipe = (RelativeLayout) v.findViewById(R.id.rl_recipe);
                iv_recipe = (ImageView) v.findViewById(R.id.iv_recipe);
                radio_fav = (CheckBox) v.findViewById(R.id.radio_fav);
                tv_recipe_name = (TextView) v.findViewById(R.id.tv_recipe_name);
                tv_poster_name = (TextView) v.findViewById(R.id.tv_poster_name);
                tv_poster_dsc = (TextView) v.findViewById(R.id.tv_poster_dsc);
                ratingBar = (RatingBar) v.findViewById(R.id.rating_bar_recipe);
                iv_poster = (ImageView) v.findViewById(R.id.iv_poster);

            }
        }

    }


    private void callToApi(int position, String likeStatus, ArrayList<RecipesBean> recipesList) {
        progress.show();
        JsonObject object = new JsonObject();
        String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
        object.addProperty("userId", userId);
        object.addProperty("type", Constants.RECIPE_TYPE);
        object.addProperty("typeId", recipesList.get(position).getId());
        object.addProperty("likeStatus", likeStatus);
        Network.requestForLike(activity, object, this);
    }


}
