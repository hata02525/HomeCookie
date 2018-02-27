package com.app.homecookie.Beans;

import java.io.Serializable;

/**
 * Created by fluper on 11/5/17.
 */
public class GroupBean implements Serializable {

    private String GroupName;
    private String groupPhoto;
    private int id;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
