package com.app.homecookie.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by fluper on 16/5/17.
 */
public class ImagePicker {
    private static Activity activity;
    private static String DEFAULT_FOLDER_NAME = "HomeCookie";
    private static final String KEY_PHOTO_URI = "photo_uri";
    private static final String KEY_LAST_CAMERA_PHOTO = "last_photo";
    private static final String KEY_TYPE = "type";
    private static SharedPreference editor;
    private static AlertDialog alertDialog;
    private static Uri outputFileUri = null;
    private static OnImagePicked listener;

    public ImagePicker(Activity activity, OnImagePicked listener) {
        this.activity = activity;
        this.listener = listener;
        editor = new SharedPreference(activity);
    }

    public static File tempImageDirectory(@NonNull Context context) {
        File privateTempDir = new File(Environment.getExternalStorageDirectory(), DEFAULT_FOLDER_NAME);
        if (!privateTempDir.exists()) privateTempDir.mkdirs();
        return privateTempDir;
    }

    public static File getCameraPicturesLocation(@NonNull Context context) throws IOException {
        File dir = tempImageDirectory(context);
        return File.createTempFile(Environment.getExternalStorageState(), ".jpg", dir);
    }


    public static Uri createCameraPictureFile(@NonNull Context context) throws IOException {
        File imagePath = getCameraPicturesLocation(context);
        Uri uri = getUriToFile(context, imagePath);
        editor.putString(KEY_PHOTO_URI, uri.toString());
        editor.putString(KEY_LAST_CAMERA_PHOTO, imagePath.toString());
        return uri;
    }

    private static Uri getUriToFile(@NonNull Context context, @NonNull File file) {
        return Uri.fromFile(file);

    }

    @Nullable
    public static File takenCameraPicture(Context context) throws IOException, URISyntaxException {
        String lastCameraPhoto = editor.getString(KEY_LAST_CAMERA_PHOTO, null);
        if (lastCameraPhoto != null) {
            return new File(lastCameraPhoto);
        } else {
            return null;
        }
    }


    public static void showImagePickerDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.image_chooser_dialog, null);
        dialogBuilder.setView(dialogView);

        LinearLayout ll_camera = (LinearLayout) dialogView.findViewById(R.id.ll_open_camera);
        LinearLayout ll_gallery = (LinearLayout) dialogView.findViewById(R.id.ll_open_gallery);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        ll_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    alertDialog.dismiss();
                    outputFileUri = createCameraPictureFile(activity);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    listener.onCameraClicked(cameraIntent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        ll_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                listener.onGalleryClicked(intent);
            }
        });


    }
    public static void dismissImagePickerDialog() {
        alertDialog.dismiss();
    }


}
