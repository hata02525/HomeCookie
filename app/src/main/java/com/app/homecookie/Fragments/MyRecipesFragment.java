package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.MyRecipesBean;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRecipesFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {
    Activity activity;
    RecyclerView rv_my_recipes;
    RelativeLayout rl_no_internet;
    Progress progress;
    View view;
    MyRecipesAdapter adapter;
    Button button_add_new;

    TextView tv_header;
    ImageView iv_person;
    ImageView iv_chat;
    Typeface face;
    Typeface boldFace;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        boldFace = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_header.setTypeface(face);
        iv_person = (ImageView) view.findViewById(R.id.iv_person);
        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rl_no_internet = (RelativeLayout) view.findViewById(R.id.rl_no_internet);
        if (Network.isConnected(activity)) {
            progress.show();
            JsonObject object = new JsonObject();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
            object.addProperty("userId", userId);
            object.addProperty("personalKeys", "1");
                Network.requestForMyRecipes(activity, object, this);
        } else {
            rl_no_internet.setVisibility(View.VISIBLE);
        }
        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
        }

        iv_chat.setOnClickListener(this);
        iv_person.setOnClickListener(this);

        return view;
    }

    private void initView(View view, MyRecipesBean bean) {
        rv_my_recipes = (RecyclerView) view.findViewById(R.id.rv_my_recipes);
        MyRecipesAdapter adapter = new MyRecipesAdapter(activity, bean);
        rv_my_recipes.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(activity, 2);
        rv_my_recipes.setLayoutManager(glm);
        rv_my_recipes.setAdapter(adapter);
        button_add_new = (Button) view.findViewById(R.id.button_add_new);

    }


    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_MY_RECIPES) {
            MyRecipesBean bean = (MyRecipesBean) data;
            initView(view, bean);
        }
        progress.dismiss();

    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        rl_no_internet.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                activity.finishAffinity();
                break;
            case R.id.iv_chat:
                if (Network.isConnected(activity)) {
                    ((HomeActivity)activity).replaceChatFragment(0);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_person:
                ((HomeActivity)activity).replaceHomeCookieDetailsFragment(true,"");
                break;
        }
    }


    private class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.ViewHolder> {
        Activity activity;
        MyRecipesBean bean;

        public MyRecipesAdapter(Activity activity, MyRecipesBean bean) {
            this.bean = bean;
            this.activity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recipes, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (!bean.getRecipesList().get(position).getCatId().equalsIgnoreCase("-1")) {
                holder.rl_camera.setVisibility(View.GONE);
                holder.tv_cat_title.setVisibility(View.VISIBLE);
                holder.tv_cat_title.setText(bean.getRecipesList().get(position).getUserCatTitle());
                holder.iv_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) activity).replaceMyRecipesCategoryFragment(bean.getRecipesList().get(position).getCatId());
                    }
                });
                String url = bean.getRecipesList().get(position).getUserCatPic();
                Ion.with(activity)
                        .load(url)
                        .asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                holder.iv_category.setImageBitmap(result);
                            }
                        });
            } else {
                holder.rl_camera.setVisibility(View.VISIBLE);
                holder.tv_cat_title.setVisibility(View.GONE);
                holder.iv_category.setVisibility(View.INVISIBLE);
                holder.rl_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) activity).replaceCreateCategoryFragment();
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return bean.getRecipesList().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_cat_title;
            private ImageView iv_category;
            private RelativeLayout rl_camera;
            private TextView tv_camera;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_cat_title = (TextView) itemView.findViewById(R.id.tv_cat_title);
                iv_category = (ImageView) itemView.findViewById(R.id.iv_category);
                rl_camera = (RelativeLayout) itemView.findViewById(R.id.rl_camera);
                tv_camera = (TextView) itemView.findViewById(R.id.tv_camera);
                tv_camera.setTypeface(face);
                tv_cat_title.setTypeface(boldFace);
            }
        }
    }
}
