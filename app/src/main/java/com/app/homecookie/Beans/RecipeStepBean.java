package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 3/5/17.
 */
public class RecipeStepBean implements Serializable {
    String description;
    ArrayList<Ingredient> list;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Ingredient> getList() {
        return list;
    }

    public void setList(ArrayList<Ingredient> list) {
        this.list = list;
    }

    static class Ingredient implements Serializable{
        int id;
        int qty;
        int unit;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }
    }
}
