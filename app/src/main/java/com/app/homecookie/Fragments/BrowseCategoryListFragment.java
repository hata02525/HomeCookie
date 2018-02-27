package com.app.homecookie.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.Beans.RecipesBean;
import com.app.homecookie.Beans.SubCategoryBean;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonObject;


public class BrowseCategoryListFragment extends Fragment implements OnNetworkCallBack, View.OnClickListener {

    ListView lv_category;
    ImageView iv_back;
    TextView tv_header;
    CategoryListBean categoryListBean;
    private Activity activity;
    Progress progress;
    int categoryid;
    SubCategoryBean subCategoryBean = null;
    String categroyId = "";

    public static BrowseCategoryListFragment newInstance(CategoryListBean bean) {
        BrowseCategoryListFragment fragment = new BrowseCategoryListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.CATEGORY_LIST_BEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryListBean = (CategoryListBean) getArguments().getSerializable(Constants.CATEGORY_LIST_BEAN);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_browse_category_list, container, false);
        initView();
        activity = getActivity();
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        tv_header.setTypeface(face);
        CategoryListAdapter adapter = new CategoryListAdapter(activity, categoryListBean);
        lv_category.setAdapter(adapter);

        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Network.isConnected(activity)) {
                    categroyId = categoryListBean.getCategoryList().get(position).getCatId();
                    String categoryName = categoryListBean.getCategoryList().get(position).getCatName();
                    ((HomeActivity) activity).replaceBrowseRecipesFragment(categroyId,categoryName);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void initView() {
        lv_category = (ListView) view.findViewById(R.id.lv_category);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        iv_back.setOnClickListener(this);
    }

    private void hitApi(int position) {
        categroyId = categoryListBean.getCategoryList().get(position).getCatId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("categoryId", categroyId);
        Network.requestForSubCategory(activity, jsonObject, this);
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (isSuccess) {
            if (requestType == Network.REQUEST_TYPE_BROWSE_SUB_CATEGORY) {
                subCategoryBean = (SubCategoryBean) data;
                JsonObject object = new JsonObject();
                String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
                object.addProperty("userId", userId);
                object.addProperty("categoryId", categroyId);
                object.addProperty("subcategoryId", "0");
                Network.requestForGetRecipes(activity, object, this);
            }
            if (requestType == Network.REQUEST_TYPE_BROWSE_RECIPE) {
                progress.dismiss();
                RecipesBean recipesBean = (RecipesBean) data;
                // ((HomeActivity) activity).replaceBrowseRecipesFragment(subCategoryBean, recipesBean, Integer.parseInt(categroyId));
            }
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ((HomeActivity) activity).replaceBrowseFragment();
        }
    }


    class CategoryListAdapter extends BaseAdapter {
        Activity activity;
        CategoryListBean categoryList;

        public CategoryListAdapter(Activity activity, CategoryListBean categoryList) {
            this.activity = activity;
            this.categoryList = categoryList;
        }

        @Override
        public int getCount() {
            return categoryList.getCategoryList().size();
        }

        @Override
        public Object getItem(int position) {

            return categoryList.getCategoryList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).
                        inflate(R.layout.sub_categories_background, parent, false);
            }
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
            tv_name.setTypeface(face);
            tv_name.setText(categoryList.getCategoryList().get(position).getCatName());
            return convertView;

        }
    }

}
