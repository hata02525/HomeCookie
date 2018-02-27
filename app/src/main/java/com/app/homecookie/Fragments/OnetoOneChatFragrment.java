package com.app.homecookie.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.homecookie.Adapters.ChatAdapter;
import com.app.homecookie.ChatHelper.ChatService;
import com.app.homecookie.ChatHelper.CommonMethods;
import com.app.homecookie.ChatHelper.RecentDb;
import com.app.homecookie.ChatHelper.model.MessageModel;
import com.app.homecookie.ChatHelper.model.RecentChatModel;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.chatstates.ChatState;

import java.util.ArrayList;
import java.util.Random;

public class OnetoOneChatFragrment extends Fragment implements View.OnClickListener, TextWatcher {

    private SQLiteDatabase mydb;
    private LinearLayout ll_user;
    private ListView recyclerView;
    private EditText et_message;
    private TextView tv_user_name;
    private ImageView iv_photo;
    private ImageView iv_send;
    private ImageView iv_back;
    private Activity activity;
    private String fromUserId;
    private String toUserId;
    private String fromUserPhoto;
    private String toUserPhoto;
    private String toUserName;
    private TextView tv_name;
    private String message = "";
    private String name = "";
    private Random random;
    public static ArrayList<MessageModel> chatlist = new ArrayList<>();
    public static ChatAdapter chatAdapter = new ChatAdapter();
    private SharedPreference sharedPreference;
    Typeface face;
    private RecentDb recentDb;
    String senderUser;
    String userPhoto;
    String tableId;
    boolean isFromFriendChat;
    boolean isFromRecentChat;

    public static OnetoOneChatFragrment newInstance(Bundle bundle) {
        OnetoOneChatFragrment fragment = new OnetoOneChatFragrment();
        Bundle args = bundle;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            toUserId = bundle.getString(Constants.TO_USER_ID);
            toUserName = bundle.getString(Constants.TO_USER_NAME);
            toUserPhoto = bundle.getString(Constants.TO_USER_PHOTO);
            isFromFriendChat = bundle.getBoolean(Constants.IS_FRIEND_CHAT);
            isFromRecentChat = bundle.getBoolean(Constants.IS_RECENT_CHAT);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_oneto_one_chat_fragrment, container, false);
        activity = getActivity();
        sharedPreference = new SharedPreference(activity);
        sharedPreference.putString(Constants.RECEIVER_ID, toUserId);
        sharedPreference.putBoolean(Constants.IS_FRIEND_CHAT, isFromFriendChat);
        sharedPreference.putBoolean(Constants.IS_RECENT_CHAT, isFromRecentChat);
        senderUser = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
        recentDb = new RecentDb(activity);
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        userPhoto = sharedPreference.getString(Constants.USER_PHOTO, "");
        random = new Random();
        ll_user = (LinearLayout) view.findViewById(R.id.ll_user);
        ll_user.setVisibility(View.GONE);
        recyclerView = (ListView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
        recyclerView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        recyclerView.setStackFromBottom(true);
        chatlist = new ArrayList<MessageModel>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist, toUserPhoto);

        int myId = Integer.parseInt(senderUser);
        int otherId = Integer.parseInt(toUserId);
        if (myId > otherId) {
            tableId = senderUser.concat(toUserId);
        } else {
            tableId = toUserId.concat(senderUser);
        }
        if (isTableExists(tableId)) {
            loadDataFromLocal(tableId);
        }

        recyclerView.setAdapter(chatAdapter);
        iv_photo = (ImageView) view.findViewById(R.id.iv_photo);

        Helper.setProfilePic(activity, toUserPhoto, iv_photo);

        et_message = (EditText) view.findViewById(R.id.et_message);
        iv_send = (ImageView) view.findViewById(R.id.iv_send);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_user_name.setText(toUserName);
        tv_name.setText(toUserName);
        tv_user_name.setTypeface(face);
        tv_name.setTypeface(face);
        et_message.setTypeface(face);
        //  et_message.addTextChangedListener(this);
        iv_send.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send:
                message = et_message.getText().toString();
                if (!message.trim().isEmpty()) {
                    sendTextMessage(toUserId);
                }
                break;
            case R.id.iv_back:
                activity.onBackPressed();
                break;
        }
    }


    public void sendTextMessage(String receiverUser) {
        String name = sharedPreference.getString(Constants.USER_F_NAME, "") + " " + sharedPreference.getString(Constants.USER_L_NAME, "");
        String message = et_message.getEditableText().toString();

        if (!message.equalsIgnoreCase("")) {
            int messageId = random.nextInt(1000);
            MessageModel.LastMessageBean lastMessageBean = new MessageModel.LastMessageBean();
            lastMessageBean.setId(String.valueOf(messageId));
            lastMessageBean.setMessageBody(message);

            chatlist.add(new MessageModel(name, lastMessageBean, userPhoto, senderUser, toUserId, messageId, true, "TEXT", senderUser, message));
            et_message.setText("");
            chatAdapter.notifyDataSetChanged();
            ChatService.xmpp.sendMessage(new MessageModel(name, lastMessageBean, userPhoto, senderUser, toUserId, messageId, true, "TEXT", senderUser, message));
            CommonMethods commonMethods = new CommonMethods(activity);
            commonMethods.createTable(tableId);
            commonMethods.insertIntoTable(tableId, senderUser, receiverUser, message, "m", "TEXT");
            chatAdapter.notifyDataSetChanged();

            RecentDb recentDb = new RecentDb(activity);
            if (recentDb != null) {
                RecentChatModel model = new RecentChatModel(Integer.parseInt(tableId), toUserId,
                        toUserName, toUserPhoto, message);
                boolean isInserted = recentDb.insertNewChatToDb(model);
                if (!isInserted) {
                    recentDb.updateChat(model);
                }
                ArrayList<RecentChatModel> recievedChat = new ArrayList<>();
                recievedChat.add(model);
                if (RecentChatFragment.recentChatList != null)
                    RecentChatFragment.recentChatList = recievedChat;
                if (RecentChatFragment.adapter != null)
                    RecentChatFragment.adapter.notifyDataSetChanged();

            }
        }
    }


    public void sendTypingStatus(String receiverUser, int status, String message) {
            /*
            String name = sharedPreference.getString(Constants.USER_F_NAME, "") + " " + sharedPreference.getString(Constants.USER_L_NAME, "");
            String messages = message;

            if (!message.equalsIgnoreCase("")) {
                int messageId = random.nextInt(1000);
                MessageModel.LastMessageBean lastMessageBean = new MessageModel.LastMessageBean();

                ChatService.xmpp.sendMessage(new MessageModel(name, lastMessageBean, userPhoto, senderUser, toUserId, status, true, "TEXT", senderUser, message));
            }*/
    }

    public boolean isTableExists(String tableName) {
        mydb = activity.openOrCreateDatabase(CommonMethods.DBNAME, Context.MODE_PRIVATE, null);
        Cursor cursor = mydb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void loadDataFromLocal(String tablename) {
        String tblname = "'" + tablename + "'";
        boolean w = false;
        mydb = activity.openOrCreateDatabase(CommonMethods.DBNAME, Context.MODE_PRIVATE, null);
        Cursor allrows = mydb.rawQuery("SELECT * FROM " + tblname, null);
        System.out.println("COUNT : " + allrows.getCount());
        Integer cindex = allrows.getColumnIndex("sender");
        Integer cindex1 = allrows.getColumnIndex("receiver");
        Integer cindext2 = allrows.getColumnIndex("msg");
        Integer cindex3 = allrows.getColumnIndex("who");
        Integer cindex4 = allrows.getColumnIndex("type");
        System.out.print(cindex + "\n" + cindex1 + "\n" + cindext2 + "\n" + cindex3);
        if (allrows.moveToFirst()) {
            do {
                String sender = allrows.getString(allrows.getColumnIndex("sender"));
                String receiver = allrows.getString(allrows.getColumnIndex("receiver"));
                String msg = allrows.getString(allrows.getColumnIndex("msg"));
                String who = allrows.getString(allrows.getColumnIndex("who"));
                String type = allrows.getString(allrows.getColumnIndex("type"));
                if (who.equals("m")) {
                    w = true;
                } else if (who.equals("r")) {
                    w = false;
                }
                MessageModel.LastMessageBean lastMessageBean = new MessageModel.LastMessageBean();
                lastMessageBean.setId(String.valueOf(random.nextInt(1000)));
                lastMessageBean.setMessageBody(msg);

                MessageModel messageModel = new MessageModel(name, lastMessageBean, userPhoto, sender, toUserId, random.nextInt(1000), w, "TEXT", sender, msg);
                chatAdapter.add(messageModel);

            }
            while (allrows.moveToNext());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        sendTypingStatus(toUserId, -1, "typing...");

    }

    @Override
    public void afterTextChanged(Editable s) {
        sendTypingStatus(toUserId, -2, "");
    }
}
