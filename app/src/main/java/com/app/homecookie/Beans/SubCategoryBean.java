package com.app.homecookie.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fluper on 20/4/17.
 */
public class SubCategoryBean implements Serializable {

    String id;
    String name;
    String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
