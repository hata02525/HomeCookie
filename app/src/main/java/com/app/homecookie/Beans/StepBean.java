package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 3/5/17.
 */
public class StepBean implements Serializable {
    String Description;
    ArrayList<StepBean.Ingredients> ingredientsList;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public static class Ingredients implements Serializable{
        String id;
        String qty;
        String unit;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}
