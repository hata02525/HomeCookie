package com.app.homecookie.ChatHelper.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Database Messages Table Schema
 * Created by emp118 on 7/6/2015.
 */
public class MessagesTable {

    // Messages table name
    public static String TABLE_MESSAGES = "messages";

    // Columns of Messages table
    public static String COLUMN_NUMBER = "_number";
    public static String COLUMN_ID = "id";
    public static String COLUMN_USER_ID = "user_id";
    public static String COLUMN_MESSAGE_BODY = "message_body";
    public static String COLUMN_MESSAGE_TIME = "message_time";
    public static String COLUMN_IS_READED = "is_readed";
    public static String COLUMN_IS_MINE = "is_mine";
    public static String COLUMN_IS_PHOTO = "is_photo";
    public static String COLUMN_REQUIRE_UPDATE = "is_require_update";

    // Constants value for accessing Messages table from content provider
    public static final int MESSAGES = 30;
    public static final int MESSAGES_ID = 40;

    // Messages table creation SQL statement
    private static final String MESSAGES_TABLE_CREATE = "create table " + TABLE_MESSAGES
            + "(" + COLUMN_NUMBER + " integer primary key autoincrement, "
            + COLUMN_ID + " text , "
            + COLUMN_USER_ID + " integer, "
            + COLUMN_MESSAGE_BODY + " text, "
            + COLUMN_MESSAGE_TIME + " bigint, "
            + COLUMN_IS_READED + " integer, "
            + COLUMN_IS_MINE + " integer, "
            + COLUMN_IS_PHOTO + " integer, "
            + COLUMN_REQUIRE_UPDATE + " integer, "
            + "UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE" + ");";

    /**
     * Method for creating Messages table inside database
     * @param database writable database for veris application
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(MESSAGES_TABLE_CREATE);
    }

    /**
     * Method for upgrading Messages table by deleting the old one and creating the new table
     * @param database writable database for veris application
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(database);
    }

}
