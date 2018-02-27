package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 1/5/17.
 */
public class IngredientsListForSearch implements Serializable {
    String id;
    String name;
    String photo;
    String qty;
    String unit;
    ArrayList<IngredientsListForSearch> searchList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ArrayList<IngredientsListForSearch> getSearchList() {
        return searchList;
    }

    public void setSearchList(ArrayList<IngredientsListForSearch> searchList) {
        this.searchList = searchList;
    }
}
