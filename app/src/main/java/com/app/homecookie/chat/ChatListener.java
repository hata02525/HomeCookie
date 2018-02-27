package com.app.homecookie.chat;


import com.app.homecookie.dto.Chat;

/**
 * Created by Avinash on 8/2/15.
 */
public interface ChatListener {

    public void processMessage(Chat chat);
}
