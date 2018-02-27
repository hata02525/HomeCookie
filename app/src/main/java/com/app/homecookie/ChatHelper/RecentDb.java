package com.app.homecookie.ChatHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.homecookie.ChatHelper.model.RecentChatModel;

import java.util.ArrayList;

/**
 * Created by fluper on 8/6/17.
 */
public class RecentDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "recentChatManager";
    public static final String TABLE_RECENT_CHATS = "recentChat";

    private static final String KEY_ID = "id";
    private static final String KEY_RECEIVER_ID = "receiverId";
    private static final String KEY_RECEIVER_NAME = "receiver";
    private static final String KEY_RECEIVER_PHOTO = "receiverPhoto";
    private static final String KEY_LAST_MESSAGE = "lastMessage";


    private static final String CREATE_RECENT_CHAT_TABLE = "CREATE TABLE " + TABLE_RECENT_CHATS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_RECEIVER_ID + " TEXT,"
            + KEY_RECEIVER_NAME + " TEXT," + KEY_RECEIVER_PHOTO + " TEXT," + KEY_LAST_MESSAGE + " TEXT" + ")";

    public RecentDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECENT_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_CHATS);
        onCreate(db);
    }


    public boolean insertNewChatToDb(RecentChatModel recentChatModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = recentChatModel.getId();
        String receiverId = recentChatModel.getReceiverId();
        String receiverName = recentChatModel.getReceiverName();
        String receiverPhoto = recentChatModel.getReceiverPhoto();
        String lastMessage = recentChatModel.getLastMessage();
        values.put(KEY_ID, id);
        values.put(KEY_RECEIVER_ID, receiverId);
        values.put(KEY_RECEIVER_NAME, receiverName);
        values.put(KEY_RECEIVER_PHOTO, receiverPhoto);
        values.put(KEY_LAST_MESSAGE, lastMessage);
        if (db.insert(TABLE_RECENT_CHATS, null, values) == -1) {
            return false;
        }
        db.close();
        return true;
    }


    public ArrayList<RecentChatModel> getAllRecentchat() {
        ArrayList<RecentChatModel> recentChatList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_RECENT_CHATS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecentChatModel recentChatModel = new RecentChatModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4));
                // Adding ingredients to list
                recentChatList.add(recentChatModel);
            } while (cursor.moveToNext());
        }

        return recentChatList;
    }

    public int updateChat(RecentChatModel recentChatModel) {
        int updateRow = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = recentChatModel.getId();
        String receiverId = recentChatModel.getReceiverId();
        String receiverName = recentChatModel.getReceiverName();
        String receiverPhoto = recentChatModel.getReceiverPhoto();
        String lastMessage = recentChatModel.getLastMessage();
        values.put(KEY_ID, id);
        values.put(KEY_RECEIVER_ID, receiverId);
        if(!receiverName.isEmpty()) {
            values.put(KEY_RECEIVER_NAME, receiverName);
        }
        if (!receiverPhoto.isEmpty()) {
            values.put(KEY_RECEIVER_PHOTO, receiverPhoto);
        }
        values.put(KEY_LAST_MESSAGE, lastMessage);
        updateRow = db.update(TABLE_RECENT_CHATS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(recentChatModel.getId())});
        db.close();
        return updateRow;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_RECENT_CHATS;
        db.execSQL(clearDBQuery);
        db.close();
    }
}
