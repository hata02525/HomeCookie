package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 21/4/17.
 */
public class IngredientBean implements Serializable {

    private int id;
    private String name;
    private String photo;

    private ArrayList<IngredientBean> ingredientList;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<IngredientBean> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<IngredientBean> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
