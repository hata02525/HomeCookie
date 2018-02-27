package com.app.homecookie.Beans;

import java.io.Serializable;

/**
 * Created by fluper on 17/5/17.
 */
public class MealListBean implements Serializable{
    private String photo;
    private String title;
    private String price;
    private String latitude;
    private String longitude;
    private String mealId;
    private String userphoto;
    private String address;
    private int commentCount;
    private String avgMealRating;
    private Object userMealRating;
    private int like;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getAvgMealRating() {
        return avgMealRating;
    }

    public void setAvgMealRating(String avgMealRating) {
        this.avgMealRating = avgMealRating;
    }

    public Object getUserMealRating() {
        return userMealRating;
    }

    public void setUserMealRating(Object userMealRating) {
        this.userMealRating = userMealRating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
