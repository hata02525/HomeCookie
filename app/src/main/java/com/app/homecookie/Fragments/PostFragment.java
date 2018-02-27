package com.app.homecookie.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Activity.AddStepActivity;
import com.app.homecookie.Activity.ChooseCategoryActivity;
import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Adapters.ChildAdapter;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.Beans.IngredientsListForSearch;
import com.app.homecookie.Beans.RecipesBean;
import com.app.homecookie.Beans.StepBean;
import com.app.homecookie.Beans.SubCategoryBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.ImagePicker;
import com.app.homecookie.Util.OnImagePicked;
import com.app.homecookie.Util.Progress;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements OnNetworkCallBack, OnImagePicked, View.OnClickListener {
    ImageView iv_down_public;
    ImageView iv_back;
    Button post;
    Button btn_add_step;
    Spinner spinner;
    TextView tv_header;
    TextView tv_public_or_private;
    TextView tv_camera;
    TextView rl_instruction;
    RelativeLayout rl_choose_category;
    RelativeLayout rl_camera_dashed;
    ImageView iv_camera;
    TextView tv_choose_category;
    EditText et_title;
    EditText et_desc;
    EditText et_youtube_link;
    TextView et_survings;
    RecyclerView recyclerView;
    Progress progress;
    private Activity activity;
    File imgFile;
    SubCategoryBean subCategoryBean;
    CategoryListBean bean1;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<StepBean> stepBeenList = new ArrayList<>();
    String recipeTitle = "";
    String descption = "";
    String categories = "";
    String youtubeLink = "";
    String servings = "";
    StepBean stepBean = new StepBean();
    int publicPrivate = 0;
    private ArrayList<CategoryListBean> categoryList = new ArrayList<>();
    private ArrayList<CategoryListBean.SubcategoriesBean> subcategoriesList = new ArrayList<>();
    private ImageView iv_recipe;
    Typeface face = null;
    ImagePicker imagePicker;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);
        initializeAllViews();
        activity = getActivity();
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        imagePicker = new ImagePicker(activity, this);
        iv_recipe = (ImageView) view.findViewById(R.id.iv_recipe);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
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
        tv_camera.setTypeface(face);
        tv_public_or_private.setTypeface(face);
        et_title.setTypeface(face);
        tv_choose_category.setTypeface(face);
        et_desc.setTypeface(face);
        et_youtube_link.setTypeface(face);
        et_survings.setTypeface(face);
        rl_instruction.setTypeface(face);
        btn_add_step.setTypeface(face);
        post.setTypeface(face);
        tv_header.setTypeface(face);

        init();
        return view;

    }

    private void initializeAllViews() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        post = (Button) view.findViewById(R.id.post);
        btn_add_step = (Button) view.findViewById(R.id.btn_add_step);
        ;
        spinner = (Spinner) view.findViewById(R.id.spinner);
        tv_header = (TextView) view.findViewById(R.id.tv_header);
        tv_public_or_private = (TextView) view.findViewById(R.id.tv_public_or_private);
        tv_camera  = (TextView) view.findViewById(R.id.tv_camera);
        tv_choose_category  = (TextView) view.findViewById(R.id.tv_choose_category);
        rl_instruction  = (TextView) view.findViewById(R.id.rl_instruction);
        et_survings = (TextView) view.findViewById(R.id.et_survings);
        rl_choose_category = (RelativeLayout) view.findViewById(R.id.rl_choose_category);
        rl_camera_dashed = (RelativeLayout) view.findViewById(R.id.rl_camera_dashed);
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        et_youtube_link = (EditText) view.findViewById(R.id.et_youtube_link);
        iv_camera = (ImageView) view.findViewById(R.id.iv_camera);
        iv_down_public = (ImageView) view.findViewById(R.id.iv_down_public);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_recipe = (ImageView) view.findViewById(R.id.iv_recipe);
        btn_add_step.setOnClickListener(this);
        iv_down_public.setOnClickListener(this);
        rl_choose_category.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        post.setOnClickListener(this);
        et_survings.setOnClickListener(this);
        iv_recipe.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void init() {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_step:
                Intent intent = new Intent(activity, AddStepActivity.class);
                int stepNum = stepBeenList.size();
                intent.putExtra(Constants.STEP_NUM, stepNum);
                startActivityForResult(intent, Constants.REQUEST_CODE_FOR_STEP);
//                startActivity(new Intent(activity, AddStepActivity.class));
                break;
            case R.id.iv_down_public:
                spinner.performClick();
                break;
            case R.id.rl_choose_category:
                if (Network.isConnected(activity)) {
                    progress.show();
                    Network.requestForPostCategoryList(activity, this);
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_recipe:
                Helper.hideSoftKeyBoard(activity);
                imagePicker.showImagePickerDialog();
                break;
            case R.id.iv_back:
                ((HomeActivity) activity).replaceBrowseFragment();
                break;
            case R.id.post:
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        JsonObject object = prepareJson();
                        Network.requestForCreateRecipe(activity, object, imgFile, this);
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_survings:
                final Dialog servingDialog = new Dialog(activity);
                servingDialog.setContentView(R.layout.unit_dialog);
                servingDialog.setTitle("Please Select Units");
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
                        et_survings.setText(units.get(pos));
                        servingDialog.dismiss();
                    }
                });
                break;
        }
    }

    void onClic(View view) {

    }

    private JsonObject prepareJson() {
        JSONObject object = new JSONObject();

        JSONArray categoriesArray = new JSONArray();
        JSONArray subCategoriesArray = new JSONArray();
        try {
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "");
            object.put("title", recipeTitle);
            object.put("userId", userId);
            object.put("PublicPrivate", publicPrivate);
            object.put("youtubeLink", youtubeLink);
            object.put("description", descption);
            object.put("servingQty", servings);
            for (int j = 0; j < categoryList.size(); j++) {
                categoriesArray.put(Integer.parseInt(categoryList.get(j).getCatId()));
            }

            JSONArray stepsArray = new JSONArray();
            for (int k = 0; k < stepBeenList.size(); k++) {
                JSONObject stepObj = new JSONObject();
                StepBean bean = stepBeenList.get(k);
                stepObj.put("description", bean.getDescription());
                JSONArray ingredientArray = new JSONArray();
                ArrayList<StepBean.Ingredients> ingredientList = stepBeenList.get(k).getIngredientsList();
                for (int i = 0; i < ingredientList.size(); i++) {
                    JSONObject obj = new JSONObject();
                    StepBean.Ingredients ingredients = ingredientList.get(i);
                    obj.put("ingredientId", ingredients.getId());
                    obj.put("qty", ingredients.getQty());
                    obj.put("unit", ingredients.getUnit());
                    ingredientArray.put(i, obj);
                }
                stepObj.put("ingredient", ingredientArray);
                stepsArray.put(stepObj);
            }
            for (int k = 0; k < subcategoriesList.size(); k++) {
                subCategoriesArray.put(Integer.parseInt(subcategoriesList.get(k).getCatId()));
            }
            object.put("categoryId", categoriesArray);
            object.put("subcategoryId", subCategoriesArray);
            object.put("steps", stepsArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(object.toString());
        return jsonObject;

    }

    private boolean isValid() {
        recipeTitle = et_title.getText().toString();
        categories = tv_choose_category.getText().toString().trim();
        descption = et_desc.getText().toString().trim();
        servings = et_survings.getText().toString().trim();
        youtubeLink = et_youtube_link.getText().toString().trim();
        if (tv_public_or_private.getText().toString().trim().equalsIgnoreCase("Public")) {
            publicPrivate = 0;
        } else {
            publicPrivate = 1;
        }
        if (imgFile == null) {
            Toast.makeText(activity, "Please Select Image for Recipe", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (recipeTitle.isEmpty()) {
            Toast.makeText(activity, "Please Enter Title for the Recipes", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categories.isEmpty()) {
            Toast.makeText(activity, "Please Select Category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descption.isEmpty()) {
            Toast.makeText(activity, "Please Enter Some Description about Recipe", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!youtubeLink.isEmpty()) {
            if (!Helper.isValidYoutubeUrl(youtubeLink)) {
                Toast.makeText(activity, "Please Enter a Valid Youtube Video Url", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (stepBeenList.size() == 0) {
            Toast.makeText(activity, "Please Add Instruction", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private Intent getCropIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    imgFile = ImagePicker.takenCameraPicture(activity);
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
        if (requestCode == Constants.REQUEST_CODE_FOR_STEP && data != null) {
            Bundle extras = data.getExtras();
            stepBean = (StepBean) extras.getSerializable(Constants.STEP_BEAN);
            stepBeenList.add(stepBean);
            String desc = stepBean.getDescription();
            list.add(desc);
            ChildAdapter adapter = new ChildAdapter(activity, list);
            recyclerView.setAdapter(adapter);
        }

    }


    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (isSuccess) {
            if (requestType == Network.REQUEST_TYPE_BROWSE_CATEGORY) {
                progress.dismiss();
                CategoryListBean categoryListBean = (CategoryListBean) data;
                Bundle bundle = new Bundle();
                Intent intent = new Intent(activity, ChooseCategoryActivity.class);
                bundle.putSerializable(Constants.CATEGORY_LIST_BEAN, categoryListBean);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.REQUEST_CODE_FOR_CATEGORY);
            }
            if (requestType == Network.REQUEST_TYPE_BROWSE_RECIPE) {
                progress.dismiss();
                RecipesBean recipesBean = (RecipesBean) data;
                Intent intent = new Intent(activity, ChooseCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.SUB_CATEGORY_LIST_BEAN, subCategoryBean);
                bundle.putSerializable(Constants.RECIPES_BEAN, recipesBean);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
            if (requestType == Network.REQUEST_TYPE_CREATE_RECIPE) {
                progress.dismiss();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                ((HomeActivity) activity).replacePostFragment();
            }
        }
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