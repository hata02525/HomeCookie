package com.app.homecookie.ChatHelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Util.Constants;

public class ChatService extends Service {

    private static final String DOMAIN = Config.DOMAIN_URL;
    public static HomeCookieXMPP xmpp;
    public static ConnectivityManager cm;
    public String userId="0";
    public String password="0";
    private SharedPreference sharedPreference;
    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<ChatService>(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreference = new SharedPreference(ChatService.this);
        userId = sharedPreference.getString(Constants.USER_USR_ID,"0");
        password = sharedPreference.getString(Constants.ACCESSTOKEN,"0");
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmpp = HomeCookieXMPP.getInstance(ChatService.this, DOMAIN, HomeCookieUtils.getUserId(userId),password);
        xmpp.connect("On_Create");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.disconnect();
    }
}