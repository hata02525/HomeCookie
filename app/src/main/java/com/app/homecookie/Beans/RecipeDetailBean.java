package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fluper on 8/5/17.
 */
public class RecipeDetailBean implements Serializable {
    private String recipePhoto;
    private String title;
    private String description;
    private String youtubeLink;
    private String userId;
    private int commentCount;
    private int follow;
    private int like;
    private float avgRecipeRating;
    private float usersRecipeRating;
   // private List<?> comments;
    private List<String> instructions;
    private List<IngredientsBean> ingredients;
    private List<Comments> commentList;

    public String getRecipePhoto() {
        return recipePhoto;
    }

    public void setRecipePhoto(String recipePhoto) {
        this.recipePhoto = recipePhoto;
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

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public List<Comments> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comments> commentList) {
        this.commentList = commentList;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public List<IngredientsBean> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsBean> ingredients) {
        this.ingredients = ingredients;
    }

    public float getAvgRecipeRating() {
        return avgRecipeRating;
    }

    public void setAvgRecipeRating(float avgRecipeRating) {
        this.avgRecipeRating = avgRecipeRating;
    }

    public float getUsersRecipeRating() {
        return usersRecipeRating;
    }

    public void setUsersRecipeRating(float usersRecipeRating) {
        this.usersRecipeRating = usersRecipeRating;
    }

    public static class IngredientsBean implements Serializable{
        private String ingredientId;
        private String qty;
        private String unit;
        private String name;
        private String photo;

        public String getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(String ingredientId) {
            this.ingredientId = ingredientId;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    public static class Comments implements Serializable{


        private String photo;
        private String commentText;
        private String created_at;
        private String userid;

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

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
