package com.app.homecookie.Helper;

/**
 * Created by Sandeep Kushwah on 29-10-2016.
 */


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.app.homecookie.Util.Constants;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class Helper {
    /**
     * ----------Internet Checking Methdo-------------
     ***/

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ----------Location Checking Methdo-------------
     ***/

    public static boolean isLocationOn(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ----------Manifest Permissions Checking Methods-------------
     ***/
    public static boolean isLocataionPermissionEnable(final Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * ----------Manifest Permissions Checking Methods End-------------
     ***/


    public static boolean validEmail(String email) {
        if (email.length() < 3 || email.length() > 265)
            return false;
        else {
            if (email.matches(Constants.EMAIL_PATTERN)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isValidYoutubeUrl(String url) {
        String regExp = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com)";
        if (url.isEmpty()) {
            return false;
        }
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            return false;
        }
        return true;
    }

    public static boolean validMobile(String mobile) {
        if (mobile.length() < 3 || mobile.length() > 15)
            return false;
        else {
            if (mobile.matches(Constants.MOBILE_PATTERN)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isNumber(String number) {
        boolean flag = true;
        try {
            Long.parseLong(number);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static void hideSoftKeyBoard(Activity ctx) {
        View focusedView = ctx.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static void showSoftKeyBoard(Activity ctx) {
        View focusedView = ctx.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            // imm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.SHOW_FORCED);
        }
    }

    public static void setProfilePic(Context context, final String pic_url, final ImageView img) {
        Ion.with(context).load(pic_url).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap bitmap) {
                if (e == null) {
                    img.setImageBitmap(bitmap);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
