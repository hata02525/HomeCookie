package com.app.homecookie.Util;

import java.io.Serializable;

/**
 * Created by fluper on 13/5/17.
 */
public class TimeUtils implements Serializable {




    public static String getChatDate(long chatDate) {
        return String.valueOf(chatDate);
    }

    public static String getChatTime(long chatTime) {
        return String.valueOf(chatTime);
    }
}
