package com.app.homecookie.Fragments;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Adapters.RecentChatAdapter;
import com.app.homecookie.ChatHelper.ChatService;
import com.app.homecookie.ChatHelper.CommonMethods;
import com.app.homecookie.ChatHelper.HomeCookieXMPP;
import com.app.homecookie.ChatHelper.RecentDb;
import com.app.homecookie.ChatHelper.model.RecentChatModel;
import com.app.homecookie.R;
import org.jivesoftware.smack.roster.Roster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentChatFragment extends Fragment {


    View view;
    TextView tv_no_data;
    RelativeLayout rl_no_data;
    public static RecyclerView recyclerView_recent;
    Activity activity;
    public static RecentChatAdapter adapter = new RecentChatAdapter();
    RecentDb recentDb;
    private SQLiteDatabase db;
    public static ArrayList<RecentChatModel> recentChatList = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> onlineUserList = new ArrayList<>();
    Typeface face;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        activity = getActivity();

        onlineUserList = HomeCookieXMPP.getOnline();
        Log.e("ONLINE USERS", String.valueOf(onlineUserList));
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_no_data);
        recyclerView_recent = (RecyclerView) view.findViewById(R.id.recyclerView_recent);
        recentDb = new RecentDb(activity);
        tv_no_data = (TextView) view.findViewById(R.id.tv_no_data);
        tv_no_data.setTypeface(face);
        if (isTableExists()) {
            recentChatList = recentDb.getAllRecentchat();
        }
        if (recentChatList.size() > 0) {
            rl_no_data.setVisibility(View.GONE);
            recyclerView_recent.setHasFixedSize(true);
            recyclerView_recent.setLayoutManager(new LinearLayoutManager(activity));
            adapter = new RecentChatAdapter(activity, recentChatList,onlineUserList);
            recyclerView_recent.setAdapter(adapter);
        } else {
            recyclerView_recent.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
        }

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    public boolean isTableExists() {
        db = activity.openOrCreateDatabase(RecentDb.DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + RecentDb.TABLE_RECENT_CHATS + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }


}
