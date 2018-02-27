package com.app.homecookie.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.ChooseCategoryActivity;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.app.homecookie.Adapters.ChooseCategoryAdapter.ViewHolder.*;

/**
 * Created by fluper on 1/5/17.
 */
public class ChooseCategoryAdapter extends BaseExpandableListAdapter {
    CategoryListBean bean;
    Activity activity;
    ArrayList<CategoryListBean> categoryList = new ArrayList<>();
    HashMap<String, ArrayList<CategoryListBean.SubcategoriesBean>> subCategoriesList = new HashMap<>();

    ;

/*
    public ChooseCategoryAdapter(Activity activity, ArrayList<CategoryListBean> categoryList,
                                 HashMap<String, ArrayList<CategoryListBean.SubcategoriesBean>> subCategoriesList) {

        this.activity = activity;
        this.categoryList = categoryList;
        this.subCategoriesList = subCategoriesList;
        this.bean1 = ((ChooseCategoryActivity) activity).bean1;
    }*/

    public ChooseCategoryAdapter(Activity activity, CategoryListBean bean) {
        this.activity = activity;
        this.bean = bean;
        categoryList = bean.getCategoryList();
        for(int i = 0;i<categoryList.size();i++){
            ArrayList<CategoryListBean.SubcategoriesBean> subCategoryList = new ArrayList<>();
            subCategoryList = categoryList.get(i).getSubcategoriesList();
            subCategoriesList.put(categoryList.get(i).getCatId(),subCategoryList);
        }
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subCategoriesList.get(categoryList.get(groupPosition).getCatId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.get(groupPosition).getCatName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subCategoriesList.get(categoryList.get(groupPosition).getCatId()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getGroupTypeCount() {
        return super.getGroupTypeCount();
    }

    @Override
    public int getChildTypeCount() {
        return super.getChildTypeCount();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return super.getGroupType(groupPosition);
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        final String categoryName = (String) getGroup(groupPosition);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.category_subcategory_search, null);
            viewHolder.cb_choose_subcategory = (CheckBox) convertView.findViewById(R.id.cb_choose_subcategory);
            viewHolder.tv_categroy = (TextView) convertView.findViewById(R.id.tv_categroy);
            viewHolder.tv_categroy.setTypeface(null, Typeface.BOLD);
            viewHolder.cb_choose_subcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPositon = (int) buttonView.getTag();
                    categoryList.get(getPositon).setCategoryChoosed(buttonView.isChecked());
                    if(!isExpanded)((ExpandableListView)parent).expandGroup(groupPosition);
                    else ((ExpandableListView)parent).collapseGroup(groupPosition);
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tv_categroy,viewHolder.tv_categroy);
            convertView.setTag(R.id.cb_choose_subcategory,viewHolder.cb_choose_subcategory);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cb_choose_subcategory.setTag(groupPosition);
        viewHolder.tv_categroy.setText(categoryName);
        viewHolder.cb_choose_subcategory.setChecked(categoryList.get(groupPosition).isCategoryChoosed());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String subCategroyName = subCategoriesList.get(categoryList.get(groupPosition).getCatId()).get(childPosition).getSubcat_name();
        final ViewHolder.SubcategoryHolder viewHolder1;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sub_category_text, null);
            viewHolder1 = new SubcategoryHolder();
            viewHolder1.checkbox_choose_sub_category = (CheckBox) convertView.findViewById(R.id.checkbox_choose_sub_category);
            viewHolder1.tv_sub_category = (TextView) convertView.findViewById(R.id.tv_sub_category);
            viewHolder1.checkbox_choose_sub_category.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (int) buttonView.getTag();
                    subCategoriesList.get(categoryList.get(groupPosition).getCatId()).get(getPosition).setSubCategoryChoosed(buttonView.isChecked());

                }
            });
            convertView.setTag(viewHolder1);
        } else {
            viewHolder1 = (ViewHolder.SubcategoryHolder) convertView.getTag();
        }

        viewHolder1.checkbox_choose_sub_category.setTag(childPosition);
        viewHolder1.tv_sub_category.setText(subCategroyName);
        viewHolder1.checkbox_choose_sub_category.setChecked(subCategoriesList.get(categoryList.get(groupPosition).getCatId()).get(childPosition).isSubCategoryChoosed());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tv_categroy;
        CheckBox cb_choose_subcategory;
        static class SubcategoryHolder{
                public TextView tv_sub_category;
                public CheckBox checkbox_choose_sub_category;

        }
    }
}
