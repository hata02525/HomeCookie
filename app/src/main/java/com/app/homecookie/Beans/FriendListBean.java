package com.app.homecookie.Beans;

import java.io.Serializable;

/**
 * Created by fluper on 10/5/17.
 */
public class FriendListBean implements Serializable {
    private String firstName;
    private String lastName;
    private String photo;
    private String id;
    private boolean isFriend;


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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
