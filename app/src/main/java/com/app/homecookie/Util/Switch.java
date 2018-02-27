package com.app.homecookie.Util;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
/**
 * Created by fluper on 5/5/17.
 */

public class Switch extends SwitchCompat {

    private OnCheckedChangeListener listener;

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.setOnCheckedChangeListener(null);
        super.onRestoreInstanceState(state);
        super.setOnCheckedChangeListener(listener);
    }

    public void setCheckedSilent(boolean checked) {
        super.setOnCheckedChangeListener(null);
        super.setChecked(checked);
        super.setOnCheckedChangeListener(listener);
    }
}