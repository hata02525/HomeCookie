package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 28/4/17.
 */
public class MyRecipesBean implements Serializable {
    String catId;
    String UserCatTitle;
    String userCatPic;
    ArrayList<MyRecipesBean> recipesList;
    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getUserCatTitle() {
        return UserCatTitle;
    }

    public void setUserCatTitle(String userCatTitle) {
        UserCatTitle = userCatTitle;
    }

    public String getUserCatPic() {
        return userCatPic;
    }

    public void setUserCatPic(String userCatPic) {
        this.userCatPic = userCatPic;
    }

    public ArrayList<MyRecipesBean> getRecipesList() {
        return recipesList;
    }

    public void setRecipesList(ArrayList<MyRecipesBean> recipesList) {
        this.recipesList = recipesList;
    }
}
