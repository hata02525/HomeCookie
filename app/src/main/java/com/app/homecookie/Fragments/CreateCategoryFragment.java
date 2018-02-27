package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.ImagePicker;
import com.app.homecookie.Util.OnImagePicked;
import com.app.homecookie.Util.Progress;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCategoryFragment extends Fragment implements OnNetworkCallBack, OnImagePicked, View.OnClickListener {

    EditText et_category_title;
    ImageView iv_category;
    RelativeLayout rl_camera_dashed;
    TextView tv_header;
    TextView tv_lbl;
    TextView tv_camera;
    Button button_save;
    Button button_cancel;

    private Progress progress;
    File imgFile;
    private Activity activity;
    String catName = "";
    ImagePicker imagePicker;
    Typeface face = null;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_category, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        initView();
        imagePicker = new ImagePicker(activity, this);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        tv_header.setTypeface(face);
        et_category_title.setTypeface(face);
        tv_camera.setTypeface(face);
        button_save.setTypeface(face);
        button_cancel.setTypeface(face);


        return view;
    }

    private void initView() {
        et_category_title = (EditText) view.findViewById(R.id.et_category_title);
        iv_category = (ImageView) view.findViewById(R.id.iv_category);
        rl_camera_dashed = (RelativeLayout) view.findViewById(R.id.rl_camera_dashed);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_lbl = (TextView) view.findViewById(R.id.tv_lbl);
        tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        button_save = (Button) view.findViewById(R.id.button_save);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
        button_save.setOnClickListener(this);
        iv_category.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_category:
                imagePicker.showImagePickerDialog();
                break;
            case R.id.button_save:
                Helper.hideSoftKeyBoard(activity);
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        Network.requestForCreateCategory(activity, catName, imgFile, this);
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_cancel:
                ((HomeActivity) activity).replaceCreateCategoryFragment();
                break;
            case R.id.iv_back:
                ((HomeActivity) activity).replaceMyRecipesFragment();
                break;
        }
    }

    private boolean isValid() {
        catName = et_category_title.getText().toString().trim();
        if (TextUtils.isEmpty(catName)) {
            Toast.makeText(activity, "Please Enter the category title", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imgFile == null) {
            Toast.makeText(activity, "Please Select Category Picture", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                imgFile = imagePicker.takenCameraPicture(activity);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (e == null) {
                        iv_category.setImageBitmap(result);
                    } else {
                        iv_category.setImageDrawable(null);
                    }
                }
            });
            rl_camera_dashed.setVisibility(View.GONE);
        }

        if (requestCode == Constants.GALLERY_REQUEST && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                try {
                    Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    if (picturePath == null) {
                        picturePath = selectedImage.getPath();
                    }
                    imgFile = new File(picturePath);
                    iv_category.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e == null) {
                                iv_category.setImageBitmap(result);
                            } else {
                                iv_category.setImageDrawable(null);
                            }
                        }
                    });
                    rl_camera_dashed.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        ((HomeActivity) activity).replaceMyRecipesFragment();
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCameraClicked(Intent intent) {
        startActivityForResult(intent, Constants.CAMERA_REQUEST);

    }

    @Override
    public void onGalleryClicked(Intent intent) {
        startActivityForResult(intent, Constants.GALLERY_REQUEST);
    }


}
