package com.app.homecookie.ChatHelper;

/**
 * Created by fluper on 12/5/17.
 */
public class HomeCookieUtils {


    public static boolean isUserLogin;
    public static boolean isUserLogined(ChatService context) {
            return isUserLogin;
    }

    public static String getUserId(String id){
       String userChatId = id+"@"+Config.DOMAIN_URL;
        return userChatId;
    }
}
