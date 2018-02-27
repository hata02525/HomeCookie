package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fluper on 19/5/17.
 */
public class MealDetailsBean implements Serializable {
    private String mealId;
    private String userId;
    private String photo;
    private String publicPrivate;
    private String userCatId;
    private String title;
    private String description;
    private String price;
    private String serviceQty;
    private String date;
    private String longitude;
    private String latitude;
    private String created_at;
    private String updated_at;
    private int commentCount;
    private String userPhoto;
    private String userFirstName;
    private String userLastName;
    private String avgMealRating;
    private String userMealRating;
    private String categoryName;
    private String address;
    private int like;
    private List<Comments> commentList;


    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPublicPrivate() {
        return publicPrivate;
    }

    public void setPublicPrivate(String publicPrivate) {
        this.publicPrivate = publicPrivate;
    }

    public String getUserCatId() {
        return userCatId;
    }

    public void setUserCatId(String userCatId) {
        this.userCatId = userCatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServiceQty() {
        return serviceQty;
    }

    public void setServiceQty(String serviceQty) {
        this.serviceQty = serviceQty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
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

    public void setUserMealRating(String userMealRating) {
        this.userMealRating = userMealRating;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Comments> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comments> commentList) {
        this.commentList = commentList;
    }


    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public static class Comments implements Serializable{


        private String photo;
        private String commentText;
        private String created_at;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCommentText() {
            return commentText;
        }

        public void setCommentText(String commentText) {
            this.commentText = commentText;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

}
