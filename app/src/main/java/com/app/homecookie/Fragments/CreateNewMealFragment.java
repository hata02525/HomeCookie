package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.homecookie.Activity.ChooseCategoryActivity;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.Beans.StepBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.ImagePicker;
import com.app.homecookie.Util.OnImagePicked;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.slidedatetimepicker.SlideDateTimeListener;
import com.app.homecookie.Util.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewMealFragment extends Fragment implements View.OnClickListener, OnImagePicked, OnNetworkCallBack {
    Activity activity;
    EditText et_title, et_desc, et_price_pax;
    TextView tv_servings, tv_date_time, tv_public_or_private, tv_location, tv_choose_category,tv_header,tv_add_new,tv_camera;
    Button button_choose_location, button_save;
    ImageView iv_down_public, iv_recipe, iv_camera;
    Spinner spinner;
    Progress progress;
    ImagePicker imagePicker;
    File imgFile;
    RelativeLayout rl_camera_dashed, rl_choose_category;
    CategoryListBean bean1;
    private ArrayList<CategoryListBean> categoryList = new ArrayList<>();
    private ArrayList<CategoryListBean.SubcategoriesBean> subcategoriesList = new ArrayList<>();

    String title = "";
    String description = "";
    String price = "";
    String servings = "";
    String dateTimetoSend = "";
    String addresss;
    int publicPrivate;
    String categories = "";
    String latitude;
    String longitude;
    String userCatId = "";
    String address="";
    Typeface face;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_meal, container, false);
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(),"gotham_normal.TTF");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        imagePicker = new ImagePicker(activity, this);
        button_choose_location = (Button) view.findViewById(R.id.button_choose_location);
        button_choose_location.setOnClickListener(this);
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        et_price_pax = (EditText) view.findViewById(R.id.et_price_pax);
        tv_choose_category = (TextView) view.findViewById(R.id.tv_choose_category);
        tv_servings = (TextView) view.findViewById(R.id.tv_servings);
        tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
        tv_public_or_private = (TextView) view.findViewById(R.id.tv_public_or_private);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_add_new = (TextView) view.findViewById(R.id.tv_add_new);
        tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        button_choose_location = (Button) view.findViewById(R.id.button_choose_location);
        button_save = (Button) view.findViewById(R.id.button_save);
        iv_down_public = (ImageView) view.findViewById(R.id.iv_down_public);
        iv_recipe = (ImageView) view.findViewById(R.id.iv_recipe);
        iv_camera = (ImageView) view.findViewById(R.id.iv_camera);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        rl_camera_dashed = (RelativeLayout) view.findViewById(R.id.rl_camera_dashed);
        rl_choose_category = (RelativeLayout) view.findViewById(R.id.rl_choose_category);
        rl_choose_category.setOnClickListener(this);
        iv_down_public.setOnClickListener(this);
        iv_recipe.setOnClickListener(this);
        tv_servings.setOnClickListener(this);
        tv_date_time.setOnClickListener(this);
        button_save.setOnClickListener(this);

        ArrayList arrayList = new ArrayList();
        arrayList.add("Public");
        arrayList.add("Private");

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(activity, R.layout.public_private_spiner, arrayList);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    tv_public_or_private.setText("Public");
                }
                if (position == 1) {
                    tv_public_or_private.setText("Private");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_public_or_private.setText("Public");
            }
        });

        tv_header.setTypeface(face);
        tv_add_new.setTypeface(face);
        button_save.setTypeface(face);
        tv_camera.setTypeface(face);
        tv_public_or_private.setTypeface(face);
        et_title.setTypeface(face);
        tv_choose_category.setTypeface(face);
        et_desc.setTypeface(face);
        et_price_pax.setTypeface(face);
        tv_servings.setTypeface(face);
        tv_date_time.setTypeface(face);
        tv_location.setTypeface(face);
        button_choose_location.setTypeface(face);
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_choose_location:
                progress.show();
                openPlacePicker();
                break;
            case R.id.iv_down_public:
                spinner.performClick();
                break;
            case R.id.tv_servings:
                final Dialog servingDialog = new Dialog(activity);
                servingDialog.setContentView(R.layout.unit_dialog);
                servingDialog.setTitle("Please Select Servings");
                ListView unitList = (ListView) servingDialog.findViewById(R.id.listview_unit);
                // final String[] units = new String[12];
                final ArrayList<String> units = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    units.add(i, String.valueOf(i + 1));
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, units);
                unitList.setAdapter(adapter);
                servingDialog.show();
                unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        tv_servings.setText(units.get(pos));
                        servingDialog.dismiss();
                    }
                });
                break;
            case R.id.iv_recipe:
                Helper.hideSoftKeyBoard(activity);
                imagePicker.showImagePickerDialog();
                break;
            case R.id.rl_choose_category:
                if (Network.isConnected(activity)) {
                    progress.show();
                    Network.requestForCategoryList(activity, this);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_date_time:
                new SlideDateTimePicker.Builder(getFragmentManager())
                        .setListener(listener)
                        .setMinDate(new Date())
                        .setInitialDate(new Date())
                        .build()
                        .show();
                break;
            case R.id.button_save:
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        JsonObject jsonObj = prepareJson();
                        Network.requestForCreateMeal(activity, imgFile, jsonObj, this);
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private boolean isValid() {
        title = et_title.getText().toString().trim();
        description = et_desc.getText().toString();
        price = et_price_pax.getText().toString();
        servings = tv_servings.getText().toString();
        addresss = tv_location.getText().toString();
        categories = tv_choose_category.getText().toString().trim();

        if (tv_public_or_private.getText().toString().trim().equalsIgnoreCase("Public")) {
            publicPrivate = 0;
        } else {
            publicPrivate = 1;
        }
        if (imgFile == null) {
            Toast.makeText(activity, "Please Select Image for Meal", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (title.isEmpty()) {
            Toast.makeText(activity, "Please Enter Title for the Meal", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categories.isEmpty()) {
            Toast.makeText(activity, "Please Select Category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.isEmpty()) {
            Toast.makeText(activity, "Please Enter Some Description About the Meal", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(price.isEmpty()){
            Toast.makeText(activity, "Please Enter Price For the Meal", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(servings.isEmpty()){
            Toast.makeText(activity, "Please Enter Servings For the Meal", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateTimetoSend.isEmpty()) {
            Toast.makeText(activity, "Please Choose Date and Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (addresss.isEmpty()) {
            Toast.makeText(activity, "Please Choose Location", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void openPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(activity), Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            progress.dismiss();
            if (resultCode == activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, activity);
                String toastMsg = String.format("Place: %s", place.getName());
                String address = (String) place.getAddress();
                if(address == null){
                    Toast.makeText(activity, "You have Choosen an Empty Address. Please Try Again", Toast.LENGTH_SHORT).show();
                }else{
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText(address);
                    LatLng latlng = place.getLatLng();
                    latitude = String.valueOf(latlng.latitude);
                    longitude = String.valueOf(latlng.longitude);
                }

            }
        }
        if (requestCode == Constants.CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    imgFile = ImagePicker.takenCameraPicture(activity);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap photo = BitmapFactory.decodeFile(imgFile.getPath(), options);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e == null) {
                                iv_recipe.setImageBitmap(result);
                            } else {
                                iv_recipe.setImageDrawable(null);
                            }
                        }
                    });
                    iv_camera.setVisibility(View.GONE);
                    rl_camera_dashed.setVisibility(View.GONE);

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
                    iv_recipe.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            iv_recipe.setImageBitmap(result);
                        }
                    });
                    iv_camera.setVisibility(View.GONE);
                    rl_camera_dashed.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == Constants.REQUEST_CODE_FOR_CATEGORY && data != null) {
            Bundle extras = data.getExtras();
            bean1 = (CategoryListBean) extras.getSerializable(Constants.CHOOSE_CATEGORY_LIST_BEAN);
            categoryList = bean1.getCategoryList();
            subcategoriesList = bean1.getSubcategoriesList();
            StringBuilder categories = new StringBuilder();
            if (bean1 != null && bean1.getCategoryList() != null)
                for (int i = 0; i < bean1.getCategoryList().size(); i++) {
                    String catagory = bean1.getCategoryList().get(i).getCatName() + ",  ";
                    categories.append(catagory);

                }
            for (int i = 0; i < bean1.getSubcategoriesList().size(); i++) {
                String catagory = bean1.getSubcategoriesList().get(i).getSubcat_name() + ",  ";
                categories.append(catagory);
            }

            tv_choose_category.setText(categories);
        }
    }

    @Override
    public void onCameraClicked(Intent intent) {
        startActivityForResult(intent, Constants.CAMERA_REQUEST);
    }

    @Override
    public void onGalleryClicked(Intent intent) {
        startActivityForResult(intent, Constants.GALLERY_REQUEST);
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {


        if (requestType == Network.REQUEST_TYPE_BROWSE_CATEGORY) {
            progress.dismiss();
            CategoryListBean listBean = (CategoryListBean) data;
            final Dialog unitDialog = new Dialog(activity);
            unitDialog.setContentView(R.layout.unit_dialog);
            unitDialog.setTitle("Please Select Category for Meal");
            ListView unitList = (ListView) unitDialog.findViewById(R.id.listview_unit);
            final ArrayList<CategoryListBean> units = listBean.getCategoryList();
            final ArrayList<String> unit = new ArrayList<>();
            for (int i = 0; i < units.size(); i++) {
                unit.add(units.get(i).getCatName());
            }

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, unit);
            unitList.setAdapter(adapter);
            unitDialog.show();

            unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tv_choose_category.setText(unit.get(position));
                    userCatId = units.get(position).getCatId();
                    unitDialog.dismiss();
                }
            });


        }

        if (requestType == Network.REQUEST_TYPE_CREATE_MEAL) {
            progress.dismiss();
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            ((HomeActivity) activity).replaceCreatMealFragment();
        }

    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

    }


    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(String.valueOf(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            dateTimetoSend = sdf.format(date);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
            try {
                date = format.parse(String.valueOf(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateTime = format.format(date);
            tv_date_time.setText(dateTime);
        }

        @Override
        public void onDateTimeCancel() {
            Toast.makeText(activity, "Please Select Date and Time", Toast.LENGTH_SHORT).show();
        }
    };

    private JsonObject prepareJson() {
        JSONObject object = new JSONObject();

        try {
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
            object.put("userId", userId);
            object.put("publicPrivate", String.valueOf(publicPrivate));
            object.put("categoryId", userCatId);
            object.put("title", title);
            object.put("description", description);
            object.put("price", price);
            object.put("serviceQty", Integer.parseInt(servings));
            object.put("date", dateTimetoSend);
            object.put("longitude", longitude);
            object.put("latitude", latitude);
            object.put("address", addresss);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(object.toString());
        return jsonObject;

    }
}
