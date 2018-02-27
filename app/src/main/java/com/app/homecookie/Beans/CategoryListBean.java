package com.app.homecookie.Beans;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fluper on 20/4/17.
 */
public class CategoryListBean implements Serializable {
    private String catId;
    private String CatName;
    private boolean isCategoryChoosed;
    private ArrayList<CategoryListBean> categoryList;
    private ArrayList<SubcategoriesBean> subcategoriesList;
    private HashMap<String, ArrayList<SubcategoriesBean>> subCategoryDetail;
    public ArrayList<Integer> list = new ArrayList<>();
    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String CatName) {
        this.CatName = CatName;
    }

    public ArrayList<CategoryListBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryListBean> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<SubcategoriesBean> getSubcategoriesList() {
        return subcategoriesList;
    }

    public void setSubcategoriesList(ArrayList<SubcategoriesBean> subcategoriesList) {
        this.subcategoriesList = subcategoriesList;
    }

    public HashMap<String, ArrayList<SubcategoriesBean>> getSubCategoryDetail() {
        return subCategoryDetail;
    }

    public void setSubCategoryDetail(HashMap<String, ArrayList<SubcategoriesBean>> subCategoryDetail) {
        this.subCategoryDetail = subCategoryDetail;
    }

    public boolean isCategoryChoosed() {
        return isCategoryChoosed;
    }

    public void setCategoryChoosed(boolean categoryChoosed) {
        isCategoryChoosed = categoryChoosed;
    }

    public static class SubcategoriesBean implements Serializable {
     String catId;
     String subcatId;
     String subcategoryImage;
     String subcat_name;
        private boolean isSubCategoryChoosed;
        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getSubcatId() {
            return subcatId;
        }

        public void setSubcatId(String subcatId) {
            this.subcatId = subcatId;
        }

        public String getSubcategoryImage() {
            return subcategoryImage;
        }

        public void setSubcategoryImage(String subcategoryImage) {
            this.subcategoryImage = subcategoryImage;
        }

        public String getSubcat_name() {
            return subcat_name;
        }

        public void setSubcat_name(String subcat_name) {
            this.subcat_name = subcat_name;
        }

        public boolean isSubCategoryChoosed() {
            return isSubCategoryChoosed;
        }

        public void setSubCategoryChoosed(boolean subCategoryChoosed) {
            isSubCategoryChoosed = subCategoryChoosed;
        }
    }
}
