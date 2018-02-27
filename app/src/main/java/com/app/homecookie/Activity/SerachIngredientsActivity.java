package com.app.homecookie.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Beans.IngredientBean;
import com.app.homecookie.Beans.IngredientsListForSearch;
import com.app.homecookie.Beans.RecipeDetailBean;
import com.app.homecookie.DatabaseHelper.DatabaseHelper;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;
import java.util.ArrayList;

public class SerachIngredientsActivity extends AppCompatActivity implements OnNetworkCallBack {
    private boolean isFromServer;
    Activity activity;
    Intent intent;
    Bundle extras;
    ArrayList<IngredientBean> ingredientList;
    EditText et_search;
    IngredientBean bean;
    ListView lv_search;
    boolean isExist;
    IngredientsListForSearch searchBean = null;
    String searchQuerry = "";
    AlertDialog alertDialog;
    Progress progress;
    boolean isFromGrocessary = false;
    Typeface face=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach_ingredients);
        activity = SerachIngredientsActivity.this;
        face =  Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        intent = getIntent();
        extras = intent.getExtras();
        if(intent.hasExtra(Constants.IS_FROM_GROCESSARY))
        isFromGrocessary = extras.getBoolean(Constants.IS_FROM_GROCESSARY);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        initView();

    }

    private void initView() {
        lv_search = (ListView) findViewById(R.id.lv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setTypeface(face);
        et_search.requestFocus();
        Helper.showSoftKeyBoard(activity);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    if (Network.isConnected(activity)) {
                        searchQuerry = s.toString();
                        callToSearch(searchQuerry);
                    } else {
                        Toast.makeText(SerachIngredientsActivity.this, "Can't Get Ingredients Please Check Your Internet Connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String pId = searchBean.getSearchList().get(position).getId();
                String pName = searchBean.getSearchList().get(position).getName();
                String photo = searchBean.getSearchList().get(position).getPhoto();
                String qty = searchBean.getSearchList().get(position).getQty();
                String unit = searchBean.getSearchList().get(position).getUnit();
                if (isFromServer) {

                    if (isFromGrocessary) {
                        Bundle bundle = new Bundle();
                        RecipeDetailBean.IngredientsBean ingBean = new RecipeDetailBean.IngredientsBean();
                        ingBean.setIngredientId(pId);
                        ingBean.setName(pName);
                        ingBean.setQty("0");
                        ingBean.setUnit("unit");
                        bundle.putSerializable(Constants.INGREDIENT_GROCERRY_BEAN, ingBean);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS, intent);
                        finish();
                    } else {
                        Bundle bundle = new Bundle();
                        IngredientsListForSearch ingredientSearchResult = new IngredientsListForSearch();
                        ingredientSearchResult.setId(pId);
                        ingredientSearchResult.setPhoto(photo);
                        ingredientSearchResult.setName(pName);
                        ingredientSearchResult.setQty(qty);
                        ingredientSearchResult.setUnit(unit);
                        bundle.putSerializable(Constants.INGREDIENT_BEAN_OBJECT, ingredientSearchResult);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS, intent);
                        finish();
                    }


                } else {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("name", pName);
                    obj.addProperty("photo", "dummy.png");
                    obj.addProperty("price", "100");
                    callToAddIngredients(obj);
                }
            }
        });

    }

    private void callToAddIngredients(JsonObject obj) {
        if (Network.isConnected(activity)) {
            Network.requestForCreateIngredients(activity, obj, this);
        } else {
            Toast.makeText(SerachIngredientsActivity.this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUESET_TYPE_SEARCH_INGREDIENTS) {
            searchBean = (IngredientsListForSearch) data;
            ArrayList<IngredientsListForSearch> searchList = searchBean.getSearchList();
            if (searchList.size() > 0) {
                searchBean.setSearchList(searchList);
                SearchListAdapter searchListAdapter = new SearchListAdapter(activity, searchBean.getSearchList(), true);
                lv_search.setAdapter(searchListAdapter);
                lv_search.setVisibility(View.VISIBLE);
            } else {
                searchBean = new IngredientsListForSearch();
                ArrayList<IngredientsListForSearch> list = new ArrayList<>();
                searchBean.setName(searchQuerry);
                searchBean.setId("-1");
                searchBean.setPhoto("");
                searchBean.setQty("1");
                searchBean.setUnit("");
                searchList.add(searchBean);
                searchBean.setSearchList(searchList);
                SearchListAdapter searchListAdapter = new SearchListAdapter(activity, searchBean.getSearchList(), false);
                lv_search.setAdapter(searchListAdapter);
                lv_search.setVisibility(View.VISIBLE);
            }
        }

        if (requestType == Network.REQUESET_TYPE_CREATE_INGREDIENTS) {
            searchBean = (IngredientsListForSearch) data;
            ArrayList<IngredientsListForSearch> searchList = searchBean.getSearchList();
            String pId = searchBean.getSearchList().get(0).getId();
            String pName = searchBean.getSearchList().get(0).getName();
            String photo = searchBean.getSearchList().get(0).getPhoto();
            String qty = searchBean.getSearchList().get(0).getQty();
            String unit = searchBean.getSearchList().get(0).getUnit();

            if (isFromGrocessary) {
                Bundle bundle = new Bundle();
                RecipeDetailBean.IngredientsBean ingBean = new RecipeDetailBean.IngredientsBean();
                ingBean.setIngredientId(pId);
                ingBean.setName(pName);
                ingBean.setQty("0");
                ingBean.setUnit("unit");
                bundle.putSerializable(Constants.INGREDIENT_GROCERRY_BEAN, ingBean);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS, intent);
                finish();
            } else {
                Bundle bundle = new Bundle();
                IngredientsListForSearch ingredientSearchResult = new IngredientsListForSearch();
                ingredientSearchResult.setId(pId);
                ingredientSearchResult.setPhoto(photo);
                ingredientSearchResult.setName(pName);
                ingredientSearchResult.setQty(qty);
                ingredientSearchResult.setUnit(unit);
                bundle.putSerializable(Constants.INGREDIENT_BEAN_OBJECT, ingredientSearchResult);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS, intent);
                finish();
            }

        }
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(SerachIngredientsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void callToSearch(String s) {
        JsonObject obj = new JsonObject();
        obj.addProperty("ingredientName", s);
        Network.requestForIngridientsList(activity, obj, this);
    }

    class SearchListAdapter extends BaseAdapter {
        ArrayList<IngredientsListForSearch> bean;
        Activity activity;


        public SearchListAdapter(Activity activity, ArrayList<IngredientsListForSearch> bean, boolean isExist) {
            this.activity = activity;
            this.bean = bean;
            isFromServer = isExist;

            if (!isFromServer) {

            }
        }

        @Override
        public int getCount() {
            return bean.size();
        }

        @Override
        public Object getItem(int position) {
            return bean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).
                        inflate(R.layout.ingrideints_search_view, parent, false);
            }
            TextView tv_ingredients = (TextView) convertView.findViewById(R.id.tv_ingredients);
            tv_ingredients.setTypeface(face);
            ImageView iv_set_ingredients = (ImageView) convertView.findViewById(R.id.iv_set_ingredients);
            tv_ingredients.setText(bean.get(position).getName());
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
