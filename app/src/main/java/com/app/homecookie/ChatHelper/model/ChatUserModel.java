package com.app.homecookie.ChatHelper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emp118 on 6/7/2016.
 */
public class ChatUserModel implements Parcelable {

    private int id;
    private String url;
    private String name;
    private String displayPic;
    private int isMyMate;
    private int unreadCount;
    private int isPrivate;

    public ChatUserModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(String displayPic) {
        this.displayPic = displayPic;
    }

    public boolean isMyMate() {
        return isMyMate == 1;
    }

    public void setMyMate(boolean myMate) {
        isMyMate = myMate ? 1 : 0;
    }

    public boolean isPrivate() {
        return isPrivate == 1;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate ? 1 : 0;
    }



    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(unreadCount);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(displayPic);
        dest.writeInt(isMyMate);
        dest.writeInt(isPrivate);
    }

    public static final Creator<ChatUserModel> CREATOR = new Creator<ChatUserModel>() {

        @Override
        public ChatUserModel createFromParcel(Parcel source) {
            return new ChatUserModel(source);
        }

        @Override
        public ChatUserModel[] newArray(int size) {
            return new ChatUserModel[size];
        }
    };

    private ChatUserModel(Parcel source) {
        id = source.readInt();
        unreadCount = source.readInt();
        url = source.readString();
        name = source.readString();
        displayPic = source.readString();
        isMyMate = source.readInt();
        isPrivate = source.readInt();

    }
}
