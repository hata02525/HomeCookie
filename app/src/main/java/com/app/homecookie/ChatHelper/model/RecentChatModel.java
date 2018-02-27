package com.app.homecookie.ChatHelper.model;

import java.io.Serializable;

/**
 * Created by fluper on 8/6/17.
 */
public class RecentChatModel implements Serializable {
    private int id;
    private String receiverId;
    private String receiverName;
    private String receiverPhoto;
    private String lastMessage;
    private boolean isOnline;

    public RecentChatModel(int id, String receiverId, String receiverName, String receiverPhoto, String lastMessage) {
        this.id = id;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverPhoto = receiverPhoto;
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhoto() {
        return receiverPhoto;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
