package com.app.homecookie.Util;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.app.homecookie.R;

/**
 * Created by fluper on 5/4/17.
 */
public class Progress extends ProgressDialog {
    public Progress(Context context) {
        super(context);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
