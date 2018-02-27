package com.app.homecookie.ChatHelper.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Database User Table Schema
 * Created by emp118 on 7/6/2015.
 */
public class UserTable {

    // Users table name
    public static String TABLE_USERS = "users";

    // Columns of users table
    public static String COLUMN_ID = "id";
    public static String COLUMN_URL = "url";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_DISPLAY_PICTURE = "display_picture";
    public static String COLUMN_IS_PRIVATE = "is_private";
    public static String COLUMN_IS_MY_MATE = "is_my_mate";

    // Constants value for accessing userss table from content provider
    public static final int USERS = 10;
    public static final int USER_ID = 20;

    // Users table creation SQL statement
    private static final String USER_TABLE_CREATE = "create table " + TABLE_USERS
            + "(" + COLUMN_ID + " integer primary key, "
            + COLUMN_URL + " text, "
            + COLUMN_NAME + " text, "
            + COLUMN_DISPLAY_PICTURE + " text, "
            + COLUMN_IS_PRIVATE + " integer, "
            + COLUMN_IS_MY_MATE + " integer, "
            + "UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE" + ");";

    /**
     * Method for creating users table inside database
     * @param database writable database for veris application
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(USER_TABLE_CREATE);
    }

    /**
     * Method for upgrading users table by deleting the old one and creating the new table
     * @param database writable database for veris application
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(database);
    }

}
