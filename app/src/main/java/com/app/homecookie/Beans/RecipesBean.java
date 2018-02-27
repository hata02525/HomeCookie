package com.app.homecookie.Beans;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 20/4/17.
 */
public class RecipesBean implements Serializable {

    private int id;
    private String UserId;
    private String photo;
    private String PublicPrivate;
    private String title;
    private String description;
    private String firstName;
    private String lastName;
    private String userPhoto;
    private String occupation;
    private int like;
    private int recipeRating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPublicPrivate() {
        return PublicPrivate;
    }

    public void setPublicPrivate(String PublicPrivate) {
        this.PublicPrivate = PublicPrivate;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(int recipeRating) {
        this.recipeRating = recipeRating;
    }
}
