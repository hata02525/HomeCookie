package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.IngredientBean;
import com.app.homecookie.Beans.MyCategoryRecipeBean;
import com.app.homecookie.Beans.MyRecipesBean;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.swipe.SwipeRevealLayout;
import com.app.homecookie.Util.swipe.ViewBinderHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCategoryRecipesFragment extends Fragment implements OnNetworkCallBack, OnClickListener{

    RelativeLayout rl_no_internet;
    Progress progress;
    View view;
    Activity activity;
    RecipesAdapter adapter;
    RecyclerView rv_recipes;
    String categoryId;
    ImageView iv_back;
    public static MyCategoryRecipesFragment newInstance(String categoryId) {
        Bundle args = new Bundle();
        args.putString(Constants.CATEGORY_ID,categoryId);
        MyCategoryRecipesFragment fragment = new MyCategoryRecipesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            categoryId = getArguments().getString(Constants.CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_category_recipes, container, false);
        activity = getActivity();
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        rl_no_internet = (RelativeLayout) view.findViewById(R.id.rl_no_internet);
        iv_back = (ImageView)view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        if (Network.isConnected(activity)) {
            progress.show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID,"");
            int id = Integer.parseInt(userId);
            JsonObject object = new JsonObject();
            object.addProperty("categoryId", Integer.parseInt(categoryId));
            object.addProperty("userId", id);
            Network.requestForMyRecipesCategory(activity, object, this);
        } else {
            rl_no_internet.setVisibility(View.VISIBLE);
        }

        return view;
    }


    void initView(View view, MyCategoryRecipeBean bean) {
        rv_recipes = (RecyclerView) view.findViewById(R.id.rv_recipes);
        RecipesAdapter adapter = new RecipesAdapter(activity, bean);
        rv_recipes.setHasFixedSize(true);
        rv_recipes.setLayoutManager(new LinearLayoutManager(activity));
        rv_recipes.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (adapter != null) {
            adapter.restoreStates(outState);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_MY_CATEGORY_RECIPE) {
            MyCategoryRecipeBean bean = (MyCategoryRecipeBean) data;
            initView(view, bean);
        }
        if (requestType == Network.REQUEST_TYPE_DELETE_RECIPE) {
            ((HomeActivity) activity).replaceMyRecipesCategoryFragment(categoryId);
        }
        progress.dismiss();
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                activity.onBackPressed();
                break;
        }
    }

    class RecipesAdapter extends RecyclerView.Adapter {
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        MyCategoryRecipeBean bean;

        public RecipesAdapter(Activity activity, MyCategoryRecipeBean bean) {
            this.activity = activity;
            this.bean = bean;
            mInflater = LayoutInflater.from(activity);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.my_recipes_category_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder h, int position) {
            final ViewHolder holder = (ViewHolder) h;
            final String data = String.valueOf(bean.getList().get(position).getRecipeId());
            holder.tv_recipe_name.setText("" + bean.getList().get(position).getTitle());
            holder.tv_comments.setText("Comments " + bean.getList().get(position).getCommentCount());
            holder.rating_bar_recipe.setClickable(false);
            Double rating = bean.getRecipeRating();
            holder.rating_bar_recipe.setNumStars(rating.intValue());
            String imageUrl = bean.getList().get(position).getPhoto();
            if (Network.isConnected(activity)) {
                Ion.with(activity)
                        .load(imageUrl)
                        .asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                holder.iv_recipes.setImageBitmap(result);
                            }
                        });
            }
            binderHelper.bind(holder.swipeLayout, data);
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            if (bean.getList() != null)
                return bean.getList().size();
            else
                return 0;
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private SwipeRevealLayout swipeLayout;
            private View deleteLayout;
            private TextView tv_recipe_name;
            private TextView tv_comments;
            private ImageView iv_recipes;
            private RatingBar rating_bar_recipe;
            private FrameLayout delete_layout;

            public ViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
                deleteLayout = itemView.findViewById(R.id.tv_ingredients_name);
                tv_recipe_name = (TextView) itemView.findViewById(R.id.tv_recipe_name);
                tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);
                rating_bar_recipe = (RatingBar) itemView.findViewById(R.id.rating_bar_recipe);
                iv_recipes = (ImageView) itemView.findViewById(R.id.iv_recipes);
                delete_layout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
            }

            public void bind(final String data) {
                delete_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Network.isConnected(activity)) {
                            progress.show();

                            callForDeletRecipe(data);


                        } else {
                            Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }

    private void callForDeletRecipe(String data) {
        String userId= new SharedPreference(activity).getString(Constants.USER_USR_ID,"0");
        JsonObject obj = new JsonObject();
        obj.addProperty("recipeId", data);
        obj.addProperty("userId", userId);
        Network.requestForDeleteRecipe(activity, obj, this);
    }

}
