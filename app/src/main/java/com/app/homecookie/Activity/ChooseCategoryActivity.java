package com.app.homecookie.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.app.homecookie.Adapters.ChooseCategoryAdapter;
import com.app.homecookie.Adapters.ChildAdapter;
//import com.app.homecookie.Adapters.NewAdapter;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class ChooseCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle extras;
    Intent intent;
    Activity activity;
    Progress progress;
    LinearLayout ll_category_container;
    TextView tv_set_data;
    TextView tv_header;
    ImageView iv_back;

    int count;
    //ExpandableListView lv_category;
    private CategoryListBean bean;
    public CategoryListBean bean1 = new CategoryListBean();
    private ArrayList<CategoryListBean> categoryListt = new ArrayList<>();
    ArrayList<CategoryListBean.SubcategoriesBean> subcategortListt = new ArrayList<>();
    int j = -1;
    int k = -1;
    ArrayList<CategoryListBean> categoryList = new ArrayList<>();
    HashMap<String, ArrayList<CategoryListBean.SubcategoriesBean>> subCategoriesList = new HashMap<>();
    Typeface face = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        activity = ChooseCategoryActivity.this;
        initView();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        intent = getIntent();
        extras = intent.getExtras();
        bean = (CategoryListBean) extras.getSerializable(Constants.CATEGORY_LIST_BEAN);
        tv_header.setTypeface(face);
        tv_set_data.setTypeface(face);
        categoryList = bean.getCategoryList();
        for (int i = 0; i < categoryList.size(); i++) {
            ArrayList<CategoryListBean.SubcategoriesBean> subCategoryList = new ArrayList<>();
            subCategoryList = categoryList.get(i).getSubcategoriesList();
            subCategoriesList.put(categoryList.get(i).getCatId(), subCategoryList);
        }
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < categoryList.size(); i++) {
            View v = vi.inflate(R.layout.category_subcategory_search, null);
            TextView textView = (TextView) v.findViewById(R.id.tv_categroy);
            textView.setTypeface(face);
            textView.setText(categoryList.get(i).getCatName());
            final CheckBox cb_choose_subcategory = (CheckBox) v.findViewById(R.id.cb_choose_subcategory);
            final LinearLayout ll_sub_category_container = (LinearLayout) v.findViewById(R.id.ll_sub_category_container);
            addSubCategoryList(i, categoryList.get(i).getCatId(), ll_sub_category_container);
            final int finalI = i;
            cb_choose_subcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (count < 3) {
                            count++;
                            ll_sub_category_container.setVisibility(View.VISIBLE);
                            categoryListt.add(categoryList.get(finalI));
                        } else {
                            Toast.makeText(ChooseCategoryActivity.this, "Can not add more than 3 categories", Toast.LENGTH_SHORT).show();
                            cb_choose_subcategory.setChecked(false);
                        }
                    } else {
                        ll_sub_category_container.setVisibility(View.GONE);
                        ListIterator<CategoryListBean.SubcategoriesBean> iter = subcategortListt.listIterator();
                        count--;
                        categoryListt.remove(count);
                    }
                }
            });
            bean1.setCategoryList(categoryListt);
            ll_category_container.addView(v);
        }
    }

    private void initView() {
        ll_category_container = (LinearLayout) findViewById(R.id.ll_category_container);
        tv_set_data = (TextView) findViewById(R.id.tv_set_data);
        tv_header = (TextView) findViewById(R.id.tv_header);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_set_data.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.CHOOSE_CATEGORY_LIST_BEAN, bean1);
                intent.putExtras(bundle);
                setResult(Constants.REQUEST_CODE_FOR_CATEGORY, intent);
                finish();
                break;
            case R.id.tv_set_data:
                Intent intent1 = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(Constants.CHOOSE_CATEGORY_LIST_BEAN, bean1);
                intent1.putExtras(bundle1);
                setResult(Constants.REQUEST_CODE_FOR_CATEGORY, intent1);
                finish();
                break;
        }
    }

    private void addSubCategoryList(int position, final String categoryid, LinearLayout layoutId) {
        LayoutInflater v = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < subCategoriesList.get(categoryid).size(); i++) {
            View view = v.inflate(R.layout.sub_category_text, null);
            TextView tv_sub_category = (TextView) view.findViewById(R.id.tv_sub_category);
            tv_sub_category.setTypeface(face);
            CheckBox checkbox_choose_sub_category = (CheckBox) view.findViewById(R.id.checkbox_choose_sub_category);
            tv_sub_category.setText(subCategoriesList.get(categoryid).get(i).getSubcat_name());
            final int finalI = i;
            checkbox_choose_sub_category.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        subcategortListt.add(subCategoriesList.get(categoryid).get(finalI));
                    } else {
                        /*ListIterator<CategoryListBean.SubcategoriesBean> iter = subcategortListt.listIterator();
                        while (iter.hasNext()) {
                            if (subcategortListt.get(finalI).getSubcatId().equalsIgnoreCase(subCategoriesList.get(categoryid).get(finalI1).getSubcatId())) {
                                subcategortListt.remove(finalI);
                            }
                        }*/
                    }
                }
            });

            layoutId.addView(view);
        }
        bean1.setSubcategoriesList(subcategortListt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
