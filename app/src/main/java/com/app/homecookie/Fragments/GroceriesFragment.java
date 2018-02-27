package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Activity.SerachIngredientsActivity;
import com.app.homecookie.Beans.RecipeDetailBean;
import com.app.homecookie.DatabaseHelper.DatabaseHelper;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.swipe.SwipeRevealLayout;
import com.app.homecookie.Util.swipe.ViewBinderHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroceriesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_1;
    private Button button_add_ingredients;
    private TextView tv_clear_list;
    private TextView tv_header;
    private TextView tv_user_lbl;
    private TextView tv_groceries_lbl;
    private Button button_buy_ingredients;
    ArrayList<RecipeDetailBean.IngredientsBean> ingList = new ArrayList<>();
    ArrayList<RecipeDetailBean.IngredientsBean> ingListUser = new ArrayList<>();
    Activity activity;
    DatabaseHelper db;
    RelativeLayout rl_no_data;
    RelativeLayout rl_filter;
    private AlertDialog alertDialog;
    TextView tv_unit;
    EditText et_qty;
    Spinner spinner;
    Progress progress;
    ImageView iv_chat;
    ImageView iv_person;
    Typeface face = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groceries, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
        iv_person = (ImageView) view.findViewById(R.id.iv_person);

        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        rl_filter = (RelativeLayout) view.findViewById(R.id.rl_filter);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView_1 = (RecyclerView) view.findViewById(R.id.recyclerView_1);
        button_add_ingredients = (Button) view.findViewById(R.id.button_add_ingredients);
        button_buy_ingredients = (Button) view.findViewById(R.id.button_buy_ingredients);
        tv_clear_list = (TextView) view.findViewById(R.id.tv_clear_list);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_user_lbl = (TextView) view.findViewById(R.id.tv_user_lbl);
        tv_groceries_lbl = (TextView) view.findViewById(R.id.tv_groceries_lbl);
        tv_clear_list.setOnClickListener(this);
        button_add_ingredients.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        db = new DatabaseHelper(activity);
        if (db != null) {
            ingList = db.getIngredients();
            ingListUser = db.getUserIngredients();
        }
        if (ingList.size() > 0) {
            initView(ingList);
        } else {
            rl_filter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
        }

        if (ingListUser.size() > 0) {
            initUserView(ingListUser);
        } else {
            recyclerView_1.setVisibility(View.GONE);
        }

        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO,"");
        if(userPhoto.contains("http://")){
            Helper.setProfilePic(activity,userPhoto,iv_person);
        }else{
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/"+userPhoto;
            Helper.setProfilePic(activity,userPhotoo,iv_person);
        }
        button_add_ingredients.setTypeface(face);
        button_buy_ingredients.setTypeface(face);
        tv_clear_list.setTypeface(face);
        tv_header.setTypeface(face);
        tv_user_lbl.setTypeface(face);
        tv_groceries_lbl.setTypeface(face);
        iv_person.setOnClickListener(this);

        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void initView(ArrayList<RecipeDetailBean.IngredientsBean> ingList) {
        if (ingList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            IngredientsAdapter adapter = new IngredientsAdapter(activity, ingList);
            recyclerView.setAdapter(adapter);
        } else {
            rl_filter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
        }

    }

    private void initUserView(ArrayList<RecipeDetailBean.IngredientsBean> ingList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_1.setHasFixedSize(true);
        recyclerView_1.setNestedScrollingEnabled(false);
        recyclerView_1.setLayoutManager(new LinearLayoutManager(activity));
        UserIngredientsAdapter adapter = new UserIngredientsAdapter(activity, ingList);
        recyclerView_1.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_ingredients:
                Intent intent = new Intent(activity, SerachIngredientsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_FROM_GROCESSARY, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS);
                break;
            case R.id.tv_unit:
                break;
            case R.id.tv_clear_list:
                db.clearUserDB();
                ((HomeActivity) activity).replaceGroceriesFragment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS && data != null) {
            RecipeDetailBean.IngredientsBean ingredient = new RecipeDetailBean.IngredientsBean();
            Bundle extras = data.getExtras();
            ingredient = (RecipeDetailBean.IngredientsBean) extras.getSerializable(Constants.INGREDIENT_GROCERRY_BEAN);
            db.addIngredients(ingredient);
            ingList = db.getIngredients();
            ((HomeActivity) activity).replaceGroceriesFragment();
        }
    }

    class IngredientsAdapter extends RecyclerView.Adapter {
        int itemPosition;
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        ArrayList<RecipeDetailBean.IngredientsBean> ingList;
        String id;
        int pos;

        public IngredientsAdapter(Activity activity, ArrayList<RecipeDetailBean.IngredientsBean> ingList) {
            this.activity = activity;
            this.ingList = ingList;
            mInflater = LayoutInflater.from(activity);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.grocerry_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            final String data = ingList.get(position).getIngredientId();
            id = data;
            String units = ingList.get(position).getUnit();
            String qty = ingList.get(position).getQty();
            String name = ingList.get(position).getName();
            holder.tv_qty.setText(qty + " " + units);
            holder.tv_name.setText(name);
            binderHelper.bind(holder.swipeLayout, data);
            pos = position;
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeDetailBean.IngredientsBean ingredient = new RecipeDetailBean.IngredientsBean();
                    ArrayList<RecipeDetailBean.IngredientsBean> ingList1 = db.getIngredients();
                    if (ingList1.size() > 0) {
                        ingredient = db.getSingleIngredients(data);
                        boolean i = db.addIngredientsToUserDb(ingredient);
                        int j = db.deleteIngredient(ingredient);
                        ingListUser = db.getUserIngredients();
                        ingList = db.getIngredients();
                        ((HomeActivity) activity).replaceGroceriesFragment();
                    } else {
                        Toast.makeText(activity, "Allready Added", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptionDialog(ingList.get(position).getName(), ingList.get(position).getIngredientId());
                }
            });
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return ingList.size();
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private SwipeRevealLayout swipeLayout;
            private FrameLayout deleteLayout;
            private TextView tv_qty;
            private TextView tv_name;
            private LinearLayout rl_item;
            private ImageView iv_options;


            public ViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
                tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
                rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item);
                iv_options = (ImageView) itemView.findViewById(R.id.iv_options);
                tv_qty.setTypeface(face);
                tv_name.setTypeface(face);
            }

            public void bind(String data) {
                deleteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = db.deleteIngredient(ingList.get(pos));
                        if (i > 0) {
                            ingList.remove(pos);
                            Toast.makeText(activity, "Ingredient Deleted Successfully", Toast.LENGTH_SHORT).show();
                            ((HomeActivity) activity).replaceGroceriesFragment();
                        } else {
                            Toast.makeText(activity, "Ingredient not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }


    class UserIngredientsAdapter extends RecyclerView.Adapter {
        int itemPosition;
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        ArrayList<RecipeDetailBean.IngredientsBean> ingList;
        String id;
        int pos;

        public UserIngredientsAdapter(Activity activity, ArrayList<RecipeDetailBean.IngredientsBean> ingList) {
            this.activity = activity;
            this.ingList = ingList;
            mInflater = LayoutInflater.from(activity);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.grocerry_user_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            final String data = ingList.get(position).getIngredientId();
            id = data;
            String units = ingList.get(position).getUnit();
            String qty = ingList.get(position).getQty();
            String name = ingList.get(position).getName();
            holder.tv_qty.setText(qty + " " + units);
            holder.tv_name.setText(name);

            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeDetailBean.IngredientsBean ingredient = new RecipeDetailBean.IngredientsBean();
                    ArrayList<RecipeDetailBean.IngredientsBean> ingList1 = db.getUserIngredients();
                    if (ingList1.size() > 0) {
                        ingredient = db.getSingleIngredientFromUserDb(data);
                        boolean i = db.addIngredients(ingredient);
                        int j = db.deleteIngredientFromUserDb(ingredient);
                        ingListUser = db.getUserIngredients();
                        ingList = db.getIngredients();
                        ((HomeActivity) activity).replaceGroceriesFragment();
                    } else {
                        Toast.makeText(activity, "Allready Added", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return ingList.size();
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private SwipeRevealLayout swipeLayout;
            private TextView tv_qty;
            private TextView tv_name;
            private ImageView iv_options;
            private FrameLayout deleteLayout;
            private LinearLayout rl_item;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
                rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item);
                tv_qty.setTypeface(face);
                tv_name.setTypeface(face);
            }

            public void bind(String data) {
                deleteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = db.deleteIngredientFromUserDb(ingList.get(pos));
                        if (i > 0) {
                            ingList.remove(pos);
                            Toast.makeText(activity, "Ingredient Deleted Successfully", Toast.LENGTH_SHORT).show();
                            ((HomeActivity) activity).replaceGroceriesFragment();
                        } else {
                            Toast.makeText(activity, "Ingredient not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }


    private void showOptionDialog(final String name, final String id) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        final LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ingredients_option_edit_layout, null);
        dialogBuilder.setView(dialogView);
        TextView tv_ing_name = (TextView) dialogView.findViewById(R.id.tv_ing_name);
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        TextView tv_update = (TextView) dialogView.findViewById(R.id.tv_update);
        tv_unit = (TextView) dialogView.findViewById(R.id.tv_unit);
        et_qty = (EditText) dialogView.findViewById(R.id.et_qty);
        ((TextView)dialogView.findViewById(R.id.tv_main)).setTypeface(face);
        ((TextView)dialogView.findViewById(R.id.lbl_ing)).setTypeface(face);
        ((TextView)dialogView.findViewById(R.id.lbl_qty)).setTypeface(face);
        ((TextView)dialogView.findViewById(R.id.tv_unit_lbl)).setTypeface(face);
        tv_ing_name.setTypeface(face);
        tv_cancel.setTypeface(face);
        tv_update.setTypeface(face);
        et_qty.setTypeface(face);
        et_qty.setTypeface(face);

        tv_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String qty = et_qty.getText().toString().trim();
                if (!qty.isEmpty())
                    callUnitApi(qty);
                else
                    Toast.makeText(activity, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
            }
        });
        tv_ing_name.setText(name);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = et_qty.getText().toString();
                if (!qty.isEmpty()) {
                    String unit = tv_unit.getText().toString();
                    RecipeDetailBean.IngredientsBean ingredient = new RecipeDetailBean.IngredientsBean();
                    ingredient.setIngredientId(id);
                    ingredient.setName(name);
                    ingredient.setQty(qty);
                    ingredient.setUnit(unit);
                    ingredient.setPhoto("");
                    int i = db.updateIngredient(ingredient);
                    if (i > 0) {
                        ingList = db.getIngredients();
                        initView(ingList);
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                } else {
                    Toast.makeText(activity, "Please Enter Quantity", Toast.LENGTH_SHORT).show();

                }
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    private void callUnitApi(String qty) {
        progress.show();
        JsonObject obj = new JsonObject();
        obj.addProperty("quantity", qty);
        Ion.with(activity).load(Network.BASE_URL + "getUnitList").setJsonObjectBody(obj).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                progress.dismiss();
                if (e == null) {
                    final Dialog unitDialog = new Dialog(activity);
                    unitDialog.setContentView(R.layout.unit_dialog);
                    unitDialog.setTitle("Please Select Units");
                    ListView unitList = (ListView) unitDialog.findViewById(R.id.listview_unit);
                    try {
                        JSONObject objcet = new JSONObject(result);
                        JSONArray itemArray = objcet.getJSONArray("response");

                        final String[] units = new String[itemArray.length()];
                        for (int i = 0; i < itemArray.length(); i++) {
                            String value = itemArray.getString(i);
                            units[i] = value;
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, units);
                        unitList.setAdapter(adapter);
                        unitDialog.show();
                        unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                tv_unit.setText(units[pos]);
                                unitDialog.dismiss();
                            }
                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, "Please Define Units First", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}
