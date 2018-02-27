package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fluper on 1/6/17.
 */
public class UserDetailBean implements Serializable {
    private String firstName;
    private String lastName;
    private String description;
    private String dob;
    private String email;
    private String photo;
    private String userId;
    private String uniqueId;
    private int followCount;
    private int followStatus;
    private String avgUserRating;
    private String occupation;
    private List<RecipesBean> recipes;
    private List<LessonsBean> lessons;
    private List<MealsBean> meals;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public String getAvgUserRating() {
        return avgUserRating;
    }

    public void setAvgUserRating(String avgUserRating) {
        this.avgUserRating = avgUserRating;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<RecipesBean> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipesBean> recipes) {
        this.recipes = recipes;
    }

    public List<LessonsBean> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonsBean> lessons) {
        this.lessons = lessons;
    }

    public List<MealsBean> getMeals() {
        return meals;
    }

    public void setMeals(List<MealsBean> meals) {
        this.meals = meals;
    }

    public static class RecipesBean {
        private String recipePhoto;
        private int recipeId;

        public String getRecipePhoto() {
            return recipePhoto;
        }

        public void setRecipePhoto(String recipePhoto) {
            this.recipePhoto = recipePhoto;
        }

        public int getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(int recipeId) {
            this.recipeId = recipeId;
        }
    }

    public static class LessonsBean {
        private String lessonPhoto;
        private String lessonId;

        public String getLessonPhoto() {
            return lessonPhoto;
        }

        public void setLessonPhoto(String lessonPhoto) {
            this.lessonPhoto = lessonPhoto;
        }

        public String getLessonId() {
            return lessonId;
        }

        public void setLessonId(String lessonId) {
            this.lessonId = lessonId;
        }
    }

    public static class MealsBean {
        private String mealPhoto;
        private String mealId;

        public String getMealPhoto() {
            return mealPhoto;
        }

        public void setMealPhoto(String mealPhoto) {
            this.mealPhoto = mealPhoto;
        }

        public String getMealId() {
            return mealId;
        }

        public void setMealId(String mealId) {
            this.mealId = mealId;
        }
    }
}
