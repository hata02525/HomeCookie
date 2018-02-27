package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.CircleImageView;
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
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProfileFragment extends Fragment implements OnNetworkCallBack, OnImagePicked, View.OnClickListener {

    ImageView iv_user_profile;
    EditText et_first_name;
    EditText et_last_name;
    EditText et_occupation;
    EditText et_about_me;
    TextView et_dob;
    TextView tv_header;
    RadioButton radio_male;

    RadioButton radio_female;
    Button button_submit;
    private DatePickerDialog datePickerDialog;
    private int mYear, mMonth, mDay;


    private View view;
    private Activity activity;

    String selectedDate = "";
    String fName = "";
    String lName = "";
    String dob = "";
    String occupation = "";
    String aboutMe = "";
    String gender = "";
    String dateTosend = "";
    private Progress progress;
    File imgFile;
    ImagePicker imagePicker;

    private boolean isToUpdate;
    private boolean isProfileChanged;
    SharedPreference sharedPreference;

    private String accessToken = "";

    public static CreateProfileFragment newInstance(boolean isToUpdate) {
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_TO_UPDATE, isToUpdate);
        CreateProfileFragment fragment = new CreateProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isToUpdate = getArguments().getBoolean(Constants.IS_TO_UPDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_profile, container, false);
        activity = getActivity();
        initializeView();
        sharedPreference = new SharedPreference(activity);
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        Typeface boldFace = Typeface.createFromAsset(activity.getAssets(), "gotham_bold.TTF");
        imagePicker = new ImagePicker(activity, this);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        radio_male.setChecked(true);

        et_first_name.setTypeface(face);
        et_last_name.setTypeface(face);
        et_occupation.setTypeface(face);
        et_dob.setTypeface(face);
        et_about_me.setTypeface(face);
        radio_male.setTypeface(face);
        radio_female.setTypeface(face);
        button_submit.setTypeface(face);
        tv_header.setTypeface(face);

        if (isToUpdate) {
            initView();
        }
        return view;
    }

    private void initializeView() {
        iv_user_profile = (ImageView) view.findViewById(R.id.iv_user_profile);
        et_first_name = (EditText) view.findViewById(R.id.et_first_name);
        et_last_name = (EditText) view.findViewById(R.id.et_last_name);
        et_occupation = (EditText) view.findViewById(R.id.et_occupation);
        et_about_me = (EditText) view.findViewById(R.id.et_about_me);
        et_dob = (TextView) view.findViewById(R.id.et_dob);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        radio_male = (RadioButton) view.findViewById(R.id.radio_male);
        radio_female = (RadioButton) view.findViewById(R.id.radio_female);
        button_submit = (Button) view.findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);
        et_dob.setOnClickListener(this);
        iv_user_profile.setOnClickListener(this);
    }

    private void initView() {
        button_submit.setText("Update Profile");
        tv_header.setText("Update Profile");
        String userPhoto = sharedPreference.getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_user_profile);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_user_profile);
        }
        String fName = sharedPreference.getString(Constants.USER_F_NAME, "");
        String lName = sharedPreference.getString(Constants.USER_L_NAME, "");
        String userGender = sharedPreference.getString(Constants.USER_GENDER, "");
        String userDob = sharedPreference.getString(Constants.USER_DOB, "");
        String occupation = sharedPreference.getString(Constants.USER_OCC, "");
        String aboutMe = sharedPreference.getString(Constants.USER_ABOUT_ME, "");
        accessToken = sharedPreference.getString(Constants.ACCESSTOKEN, "");

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/mm/yyyy");
            Date date = sdf1.parse(userDob);
            String dateToSet = sdf2.format(date);
            et_dob.setText(dateToSet);
        } catch (Exception e) {
            et_dob.setText(userDob);
        }

        if (userGender.equalsIgnoreCase("female")) {
            radio_female.setChecked(true);
        } else {
            radio_male.setChecked(true);
        }

        et_first_name.setText(fName);
        et_last_name.setText(lName);
        et_occupation.setText(occupation);
        et_about_me.setText(aboutMe);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                Helper.hideSoftKeyBoard(activity);
                if (isValid()) {
                    if (Network.isConnected(activity)) {
                        progress.show();
                        if (imgFile != null)
                            Network.requestForCreateUserProfile(activity, fName, lName, gender, dateTosend, occupation, aboutMe, imgFile, this);
                        else
                            Network.requestForCreateUserProfile(activity, fName, lName, gender, dateTosend, occupation, aboutMe, null, this);
                    } else {
                        Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.et_dob:
                showDateDialog();
                break;
            case R.id.iv_user_profile:
                Helper.hideSoftKeyBoard(activity);
                imagePicker.showImagePickerDialog();
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void showDateDialog() {
        datePickerDialog = new DatePickerDialog(activity, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthValue = "", dayValue = "";
                if (month < 10) {

                    monthValue = "0" + (month + 1);
                } else {
                    monthValue = "" + (month + 1);
                }
                if (dayOfMonth < 10) {
                    dayValue = "0" + dayOfMonth;
                } else {
                    dayValue = "" + dayOfMonth;
                }
                dateTosend = year + "-" + monthValue + "-" + dayValue;
                selectedDate = dayValue + "/" + monthValue + "/" + year;
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int currentYear = calendar.get(java.util.Calendar.YEAR);
                int approxAge = currentYear - year;
                if (approxAge < 7) {
                    Toast.makeText(activity, "Your Age Must be 8 Years Older be to Registered", Toast.LENGTH_SHORT).show();
                } else {
                    et_dob.setText(selectedDate);

                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private boolean isValid() {
        if (radio_male.isChecked()) {
            gender = "male";
        } else if (radio_female.isChecked()) {
            gender = "female";
        } else {
            gender = "";
        }

        dob = selectedDate;
        if (isToUpdate) {
            String dateOfBirth = et_dob.getText().toString();
            try {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/mm/yyyy");
                Date date = sdf2.parse(dateOfBirth);
                dateTosend = sdf1.format(date);
            } catch (Exception e) {
                dateTosend = dateOfBirth;
            }
        }
        fName = et_first_name.getText().toString();
        lName = et_last_name.getText().toString();
        occupation = et_occupation.getText().toString();
        aboutMe = et_about_me.getText().toString();
        selectedDate = et_dob.getText().toString().trim();
        if (TextUtils.isEmpty(fName)) {
            Toast.makeText(activity, "Please Enter First Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(lName)) {
            Toast.makeText(activity, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(activity, "Please Enter Your DOB", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(activity, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }

       /* if (TextUtils.isEmpty(occupation)) {
            Toast.makeText(activity, "Please Write Your Occupation", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(aboutMe)) {
            Toast.makeText(activity, "Please Write Something About You", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(imgFile==null){
            Toast.makeText(activity, "Please Select Profile Picture", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }


    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (isSuccess) {
            if (!isToUpdate) {
                new SharedPreference(activity).putBoolean(Constants.SESSION, true);
                startActivity(new Intent(activity, HomeActivity.class));
                activity.finishAffinity();
            } else {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                initView();
            }
        }
        if (isToUpdate) {
            activity.onBackPressed();
        }


    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                try {
                    imgFile = ImagePicker.takenCameraPicture(activity);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    //   Bitmap photo = BitmapFactory.decodeFile(imgFile.getPath(), options);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e == null) {
                                iv_user_profile.setImageBitmap(result);
                                isProfileChanged = true;
                            } else {
                                iv_user_profile.setImageDrawable(null);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(activity, "You Didn't Select any image", Toast.LENGTH_SHORT).show();
            }
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
                    iv_user_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            iv_user_profile.setImageBitmap(result);
                            isProfileChanged = true;
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onCameraClicked(Intent cameraIntent) {
        startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST);
    }

    @Override
    public void onGalleryClicked(Intent intent) {
        startActivityForResult(intent, Constants.GALLERY_REQUEST);
    }
}
