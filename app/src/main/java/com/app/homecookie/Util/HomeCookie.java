package com.app.homecookie.Util;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;


/*@ReportsCrashes(mailTo = "sandeepkus022@gmail.com", customReportContent = {

        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,

        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,

        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},

        mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)*/

public class HomeCookie extends MultiDexApplication {
    private static HomeCookie mainApplication;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        mainApplication = this;
        context = getApplicationContext();
//        ACRA.init(this);
    }
    public static Context getApp() {
        return context;
    }
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);

        MultiDex.install(this);
    }


    public static synchronized HomeCookie getInstance() {
        return mainApplication;
    }
}
