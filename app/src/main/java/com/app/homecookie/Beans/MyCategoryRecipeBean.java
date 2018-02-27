package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 28/4/17.
 */
public class MyCategoryRecipeBean implements Serializable {
    private int recipeId;
    private String photo;
    private String title;
    private double recipeRating;
    private int commentCount;
    private ArrayList<MyCategoryRecipeBean> list;
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(double recipeRating) {
        this.recipeRating = recipeRating;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<MyCategoryRecipeBean> getList() {
        return list;
    }

    public void setList(ArrayList<MyCategoryRecipeBean> list) {
        this.list = list;
    }
}
