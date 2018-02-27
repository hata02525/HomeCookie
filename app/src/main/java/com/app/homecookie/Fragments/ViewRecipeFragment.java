package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.CommentActivity;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Activity.VideoPlayerActivity;
import com.app.homecookie.Beans.MyRecipesBean;
import com.app.homecookie.Beans.RecipeDetailBean;
import com.app.homecookie.DatabaseHelper.DatabaseHelper;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRecipeFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private Activity activity;
    private AlertDialog alertDialog;
    private String recipeId = "";
    private String recipeName = "";
    private LinearLayout ll_no_internet;
    private CheckBox radio_fav;
    private ImageView iv_recipe;
    private ImageView iv_pic;
    private ImageView iv_cart;
    private ImageView iv_poster;
    private ImageView iv_youtube;
    private TextView tv_recipe_name;
    private TextView tv_poster_name;
    private TextView tv_comments;
    private TextView tv_recipe_title;
    private TextView tv_desc;
    private TextView tv_ingredients_expand;
    private TextView tv_instructions_expand;
    private TextView tv_comments_expand;
    private TextView tv_total_rating;
    private RelativeLayout rl_instructions;
    private RelativeLayout rl_comments;
    private ScrollView main_layout;
    private LinearLayout ll_ingredients;
    private LinearLayout ll_instruction;
    private LinearLayout ll_comments_view;
    private LinearLayout ll_rate;
    private LinearLayout ll_comments;
    private LinearLayout rl_choose_category;
    private TextView tv_choose_category;
    private Button btn_post;
    private Button button_unfollow;
    private Button button_chat;
    private EditText et_comment;

    // private LinearLayout main_layout;
    RecipeDetailBean bean = new RecipeDetailBean();
    ArrayList<RecipeDetailBean.IngredientsBean> ingList = new ArrayList<RecipeDetailBean.IngredientsBean>();
    ArrayList<RecipeDetailBean.Comments> commentList = new ArrayList<RecipeDetailBean.Comments>();
    ArrayList<String> instructionList = new ArrayList<>();
    DatabaseHelper db;
    Progress progress;
    View view;
    int like = 0;
    float rating;
    private RatingBar rb_recipe_total;
    private ImageView iv_back;
    String userId;
    String comment = "";
    Typeface face = null;
    SharedPreference sharedPreference;
    String isToRate = "";
    String posterPhoto = "";
    String posterName = "";
    String occupation = "";
    String categoryId = "0";
    String youtubeLink = "";

    public static ViewRecipeFragment newInstance(String id, String name, String posterPhoto, String posterName) {
        Bundle args = new Bundle();
        args.putString(Constants.RECIPE_ID, id);
        args.putString(Constants.RECIPE_NAME, name);
        args.putString(Constants.POSTER_NAME, posterName);
        args.putString(Constants.POSTER_PHOTO, posterPhoto);
        ViewRecipeFragment fragment = new ViewRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getString(Constants.RECIPE_ID);
            recipeName = getArguments().getString(Constants.RECIPE_NAME);
            posterName = getArguments().getString(Constants.POSTER_NAME);
            posterPhoto = getArguments().getString(Constants.POSTER_PHOTO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_recipe, container, false);
        activity = getActivity();
        sharedPreference = new SharedPreference(activity);
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
        db = new DatabaseHelper(activity);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        main_layout = (ScrollView) view.findViewById(R.id.main_layout);
        userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
        if (Network.isConnected(activity)) {
            progress.show();
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            obj.addProperty("recipeId", Integer.parseInt(recipeId));
            Network.getRecipeDetail(activity, obj, this);
        } else {
            ll_no_internet = (LinearLayout) view.findViewById(R.id.ll_no_internet);
            ll_no_internet.setVisibility(View.VISIBLE);
            ll_no_internet.setOnClickListener(this);
        }
        return view;
    }

    private void initView(View view, RecipeDetailBean bean) {
        btn_post = (Button) view.findViewById(R.id.btn_post);
        button_unfollow = (Button) view.findViewById(R.id.button_unfollow);
        button_chat = (Button) view.findViewById(R.id.button_chat);
        radio_fav = (CheckBox) view.findViewById(R.id.radio_fav);
        radio_fav.setOnCheckedChangeListener(this);
        iv_recipe = (ImageView) view.findViewById(R.id.iv_recipe);
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_poster = (ImageView) view.findViewById(R.id.iv_poster);
        iv_youtube = (ImageView) view.findViewById(R.id.iv_youtube);
        iv_poster.setColorFilter(activity.getResources().getColor(R.color.font_highlight), PorterDuff.Mode.SRC_ATOP);
        iv_cart = (ImageView) view.findViewById(R.id.iv_cart);
        tv_recipe_name = (TextView) view.findViewById(R.id.tv_recipe_name);
        tv_total_rating = (TextView) view.findViewById(R.id.tv_total_rating);
        tv_poster_name = (TextView) view.findViewById(R.id.tv_poster_name);
        tv_comments = (TextView) view.findViewById(R.id.tv_comments);
        tv_recipe_title = (TextView) view.findViewById(R.id.tv_recipe_title);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        ll_ingredients = (LinearLayout) view.findViewById(R.id.ll_ingredients);
        ll_instruction = (LinearLayout) view.findViewById(R.id.ll_instruction);
        ll_comments_view = (LinearLayout) view.findViewById(R.id.ll_comments_view);
        ll_rate = (LinearLayout) view.findViewById(R.id.ll_rate);
        ll_comments = (LinearLayout) view.findViewById(R.id.ll_comments);
        rl_choose_category = (LinearLayout) view.findViewById(R.id.rl_choose_category);
        tv_choose_category = (TextView) view.findViewById(R.id.tv_choose_category);
        rl_instructions = (RelativeLayout) view.findViewById(R.id.rl_instructions);
        tv_ingredients_expand = (TextView) view.findViewById(R.id.tv_ingredients_expand);
        tv_instructions_expand = (TextView) view.findViewById(R.id.tv_instructions_expand);
        tv_ingredients_expand = (TextView) view.findViewById(R.id.tv_ingredients_expand);
        tv_comments_expand = (TextView) view.findViewById(R.id.tv_comments_expand);
        rb_recipe_total = (RatingBar) view.findViewById(R.id.rb_recipe_total);
        et_comment = (EditText) view.findViewById(R.id.et_comment);
        tv_poster_name.setText(posterName);
        Helper.setProfilePic(activity, posterPhoto, iv_poster);
        int totalComment = bean.getCommentCount();
        isToRate = String.valueOf(bean.getUsersRecipeRating());
        sharedPreference.putInt(Constants.COMMENT_COUNT, totalComment);
        tv_comments_expand.setText("Comments(" + String.valueOf(totalComment) + ")");
        tv_comments.setText("Comments(" + String.valueOf(totalComment) + ")");
        tv_comments_expand.setOnClickListener(this);
        tv_ingredients_expand.setOnClickListener(this);
        rl_instructions.setOnClickListener(this);
        iv_cart.setOnClickListener(this);
        rb_recipe_total.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        ll_rate.setOnClickListener(this);
        rl_choose_category.setOnClickListener(this);
        iv_youtube.setOnClickListener(this);
        youtubeLink = bean.getYoutubeLink();

        if (ingList != null) {
            prepareIngredientView(ingList);
        }

        if (instructionList != null) {
            prepareInstructionView(instructionList);
        }

        /*if (commentList != null) {
            prepareCommentsView(commentList);
        }*/
        tv_recipe_title.setText(recipeName);
        tv_desc.setText(bean.getTitle());
        String recipPhoto = bean.getRecipePhoto();
        Helper.setProfilePic(activity, recipPhoto, iv_recipe);

        like = bean.getLike();
        if (like == 0) {
            radio_fav.setChecked(false);
        }
        if (like == 1) {
            radio_fav.setChecked(true);
        }
        tv_comments.setText("Comments(" + bean.getCommentCount() + ")");
        tv_comments_expand.setText("Comments(" + bean.getCommentCount() + ")");
        int avgRecipeRating = (int) bean.getUsersRecipeRating();

        rb_recipe_total.setRating(bean.getAvgRecipeRating());
        tv_total_rating.setText(String.valueOf(avgRecipeRating));


        tv_desc.setTypeface(face);
        tv_poster_name.setTypeface(face);
        tv_comments.setTypeface(face);
        tv_recipe_title.setTypeface(face);
        tv_ingredients_expand.setTypeface(face);
        tv_instructions_expand.setTypeface(face);
        tv_ingredients_expand.setTypeface(face);
        tv_comments_expand.setTypeface(face);
        tv_total_rating.setTypeface(face);
        button_unfollow.setTypeface(face);
        button_chat.setTypeface(face);
        et_comment.setTypeface(face);
        btn_post.setTypeface(face);

        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_pic);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_pic);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                activity.onBackPressed();
                break;
            case R.id.iv_cart:
                if (Network.isConnected(activity)) {
                  /*  progress.show();
                    JsonObject obj = new JsonObject();
                    obj.addProperty("userId", userId);
                    obj.addProperty("recipeId", Integer.parseInt(recipeId));
                    Network.requestForAddGrocerries(activity, obj, this);*/
                    if (ingList != null) {
                        showAddIngridentsToGroceryList(ingList);
                        // showAddedToGrocerryDialog();
                    } else {
                        Toast.makeText(activity, "No Ingredients Found for this Recipe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_no_internet:
                ((HomeActivity) activity).replaceRecipeViewFragment(String.valueOf(recipeId), recipeName, recipeId, recipeName);
                break;
            case R.id.tv_ingredients_expand:
                if (ingList != null) {
                    if (ll_ingredients.isShown()) {
                        tv_ingredients_expand.setText("Ingredients: [+]");
                        ll_ingredients.setVisibility(View.GONE);
                    } else {
                        tv_ingredients_expand.setText("Ingredients: [-]");
                        ll_ingredients.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(activity, "No Ingredients Found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_instructions:
                if (instructionList != null) {
                    if (ll_instruction.isShown()) {
                        ll_instruction.setVisibility(View.GONE);
                        tv_instructions_expand.setText("Instructions: [+]");
                    } else {
                        ll_instruction.setVisibility(View.VISIBLE);
                        tv_instructions_expand.setText("Instructions: [-]");
                    }
                } else {
                    Toast.makeText(activity, "No Ingredients Found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_comments_expand:
                Intent intent = new Intent(activity, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MEAL_ID, recipeId);
                bundle.putString(Constants.TYPE_ID, "0");
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.COMMENT_COUNT_CODE);
                break;
            case R.id.ll_rate:
                if (isToRate.equalsIgnoreCase("0.0")) {
                    showRatRecipeDialog();
                } else {
                    Toast.makeText(activity, "You Have Already Given Rating", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_choose_category:
                Helper.hideSoftKeyBoard(activity);
                JsonObject object = new JsonObject();
                final String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
                object.addProperty("userId", userId);
                object.addProperty("personalKeys", "0");
                progress.show();
                Ion.with(activity).load(Network.REQUEST_FOR_MY_RECIPES).setJsonObjectBody(object).asJsonObject().withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        progress.dismiss();
                        if (e == null) {
                            final Dialog unitDialog = new Dialog(activity);
                            unitDialog.setContentView(R.layout.unit_dialog);
                            unitDialog.setTitle("Please Select Category");
                            ListView unitList = (ListView) unitDialog.findViewById(R.id.listview_unit);

                            JsonObject resulObj = result.getResult();
                            String message = resulObj.get("message").getAsString();
                            JsonArray myRecipesArray = resulObj.get("response").getAsJsonArray();
                            MyRecipesBean bean = null;
                            final List<MyRecipesBean> myRecipesList = new ArrayList<MyRecipesBean>();
                            final List<String> categoryList = new ArrayList<String>();

                            if (myRecipesArray.size() > 0) {
                                for (int i = 0; i < myRecipesArray.size(); i++) {
                                    bean = new MyRecipesBean();
                                    JsonObject object = myRecipesArray.get(i).getAsJsonObject();
                                    JsonElement catIdElement = object.get("catId");
                                    JsonElement catTitleElement = object.get("UserCatTitle");
                                    JsonElement catImageElement = object.get("UserCatPic");
                                    bean.setCatId(Network.returnEmptyString(catIdElement));
                                    bean.setUserCatTitle(Network.returnEmptyString(catTitleElement));
                                    bean.setUserCatPic(Network.returnEmptyString(catImageElement));
                                    categoryList.add(Network.returnEmptyString(catTitleElement));
                                    myRecipesList.add(bean);
                                }

                            }
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, categoryList);
                            unitList.setAdapter(adapter);
                            if (categoryList.size() > 0)
                                unitDialog.show();
                            else
                                Toast.makeText(activity, "You Did Not Created Any Category Yet", Toast.LENGTH_SHORT).show();
                            unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                    tv_choose_category.setText(categoryList.get(pos));
                                    id = Long.parseLong(myRecipesList.get(pos).getCatId());
                                    JsonObject json = new JsonObject();
                                    json.addProperty("userId", userId);
                                    json.addProperty("categoryId", id);
                                    json.addProperty("recipeId", recipeId);
                                    unitDialog.dismiss();
                                    addRecipeToUserCategory(json);

                                }
                            });
                        } else {
                            Toast.makeText(activity, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;
            case R.id.iv_youtube:
                if (youtubeLink.isEmpty()) {
                    Toast.makeText(activity, "Sorry No Video Found for this Recipe", Toast.LENGTH_SHORT).show();
                } else {

                }
                startActivity(new Intent(activity, VideoPlayerActivity.class));
                break;
        }

    }


    private void showAddedToGrocerryDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.show_added_to_grocessary, null);
        TextView tv_grocery_added = (TextView) dialogView.findViewById(R.id.tv_grocery_added);
        tv_grocery_added.setTypeface(face);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (requestType == Network.REQUEST_TYPE_RECIPE_DETAIL) {
            //      Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            main_layout.setVisibility(View.VISIBLE);
            bean = (RecipeDetailBean) data;
            ingList = (ArrayList<RecipeDetailBean.IngredientsBean>) bean.getIngredients();
            instructionList = (ArrayList<String>) bean.getInstructions();
            //commentList = (ArrayList<RecipeDetailBean.Comments>) bean.getCommentList();
            initView(view, bean);
        }
        if (requestType == Network.REQUEST_TYPE_ADD_RECIPE_TO_FAV) {
            //Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }
        if (requestType == Network.REQUEST_TYPE_ADD_GROCERRIES) {
            //  showAddedToGrocerryDialog();
        }
        if (requestType == Network.REQUEST_RATE_THE_RECIPE) {

            isToRate = String.valueOf(rating);
            tv_total_rating.setText(isToRate);
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }
        if (requestType == Network.REQUEST_TYPE_COMMENT) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            ((HomeActivity) activity).replaceRecipeViewFragment(String.valueOf(recipeId), recipeName, recipeId, recipeName);
        }
        if (requestType == Network.REQUEST_TYPE_LIKE) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }

        if (requestType == Network.REQUEST_TYPE_ADD_RECIPE_TO_USER_CATEGORY) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
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

    private void callToApi(String likeStatus) {
        progress.show();
        JsonObject object = new JsonObject();
        String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
        object.addProperty("userId", userId);
        object.addProperty("type", Constants.RECIPE_TYPE);
        object.addProperty("typeId", recipeId);
        object.addProperty("likeStatus", likeStatus);
        Network.requestForLike(activity, object, this);
    }

    private void prepareInstructionView(ArrayList<String> instructionsList) {
        LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < instructionsList.size(); i++) {
            View v = vi.inflate(R.layout.recipe_instruction_layout, null);
            TextView tv_instructions = (TextView) v.findViewById(R.id.tv_instructions);
            tv_instructions.setTypeface(face);
            String instructions = instructionsList.get(i);
            tv_instructions.setText(instructions);
            ll_instruction.addView(v);
        }
    }

    private void prepareIngredientView(ArrayList<RecipeDetailBean.IngredientsBean> ingList) {
        LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < ingList.size(); i++) {
            View v = vi.inflate(R.layout.recipe_ingredient_layout, null);
            LinearLayout rl_ing = (LinearLayout) v.findViewById(R.id.rl_ing);
            TextView tv_ing_name = (TextView) v.findViewById(R.id.tv_ing_name);
            TextView tv_ing_qty = (TextView) v.findViewById(R.id.tv_ing_qty);
            if (i > 0) {
                if (!(i / 2 == 0)) {
                    rl_ing.setBackgroundColor(activity.getResources().getColor(R.color.white));
                }
            } else {
                rl_ing.setBackgroundColor(activity.getResources().getColor(R.color.white));
            }
            tv_ing_name.setTypeface(face);
            tv_ing_qty.setTypeface(face);
            String ingQty = ingList.get(i).getQty() + " " + ingList.get(i).getUnit();
            String ingName = ingList.get(i).getName();
            tv_ing_name.setText(ingName);
            tv_ing_qty.setText(ingQty);
            ll_ingredients.addView(v);

        }
    }

    private void prepareCommentsView(ArrayList<RecipeDetailBean.Comments> commentList) {
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
/*
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
            try {
                Date date1 = sdf.parse(date);
                String formattedDate = format.format(date1);
                tv_date_time.setText(formattedDate);
            } catch (ParseException e) {
                tv_date_time.setText( commentList.get(i).getCreated_at());
                e.printStackTrace();
            }
*/

            tv_comment.setTypeface(face);
            tv_date_time.setTypeface(face);
            tv_comment.setText(commentText);
            Helper.setProfilePic(activity, photo, iv_pic);

            iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            ll_comments.addView(v);

        }
    }

    private void addIngredientToDb(ArrayList<RecipeDetailBean.IngredientsBean> ingList) {
        boolean isAddedToDb = false;
        for (int i = 0; i < ingList.size(); i++) {
            isAddedToDb = db.addIngredients(ingList.get(i));
        }
        if (isAddedToDb) {
            showAddedToGrocerryDialog();
        } else {
            Toast.makeText(activity, "Already Added to Grocerry List", Toast.LENGTH_SHORT).show();
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
                        object.addProperty("typeId", recipeId);
                        object.addProperty("ratingCount", rb_rate_recipe.getRating());
                        object.addProperty("type", "0");
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
        if (Network.isConnected(activity)) {
            progress.show();
            Network.requestForRating(activity, object, this);
        } else {
            Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int comment = sharedPreference.getInt(Constants.COMMENT_COUNT, 0);
        tv_comments_expand.setText("Comments(" + String.valueOf(comment) + ")");
        tv_comments.setText("Comments(" + String.valueOf(comment) + ")");
    }

    private void showAddIngridentsToGroceryList(final ArrayList<RecipeDetailBean.IngredientsBean> addressId) {
        new AlertDialog.Builder(activity)
                .setTitle("Add Ingredients to Grocery List")
                .setMessage("Are you sure you want to Add Ingredients to Grocery List?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addIngredientToDb(ingList);
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


    private void addRecipeToUserCategory(JsonObject object) {
        progress.show();
        Network.addRecipeToUserCategory(activity, object, this);


    }

}
