package com.app.homecookie.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.app.homecookie.Util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
/**
 * Created by appsquadz on 8/6/16.
 */

public class DeviceToken {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9001;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private Activity activity;
    private String regId = "";
    private MyCallback callback;
    private FirebaseMessaging fcm;
    private SharedPreference sharedPreference;


    public DeviceToken(Activity activity, MyCallback callback) {
        this.callback = callback;
        this.activity = activity;
        sharedPreference = SharedPreference.getInstance(activity);
        FirebaseApp.initializeApp(activity);
    }


    public void doRegistration() {
        if (checkPlayServices()) {
            fcm = FirebaseMessaging.getInstance();
            regId = getRegistrationId(activity);
            if (regId.isEmpty()) {
                new DoRegistration().execute();
                Log.e("Registration id", regId);
            } else {
                Log.e("Registration id", regId);
                sharedPreference.putString(Constants.REGISTRATION_ID, regId);
            }
        }
    }

    class DoRegistration extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                if (fcm == null) {
                    fcm = FirebaseMessaging.getInstance();
                }
                //regId =  Settings.Secure.getString(getApplicationContext().getContentResolver()Settings.Secure.ANDROID_ID);
                regId = FirebaseInstanceId.getInstance().getToken();
                sharedPreference.putString(Constants.REGISTRATION_ID, regId);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            callback.onTokenSucess();
            if (regId != null && regId.length() > 0) {
                sharedPreference.putString(Constants.REGISTRATION_ID, regId);
                sharedPreference.putBoolean(Constants.REGISTRATION_ID_OBTAINED,true);
                Log.e("Registration id", regId);
            }
        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }

        return true;
    }

    private String getRegistrationId(Context context) {
        String registrationId = sharedPreference.getString(Constants.REGISTRATION_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = sharedPreference.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }

    }

    public interface MyCallback {
        void onTokenSucess();
    }
}
