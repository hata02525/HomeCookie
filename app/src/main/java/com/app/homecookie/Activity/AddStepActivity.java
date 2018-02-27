package com.app.homecookie.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Beans.IngredientBean;
import com.app.homecookie.Beans.IngredientsListForSearch;
import com.app.homecookie.Beans.StepBean;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.swipe.SwipeRevealLayout;
import com.app.homecookie.Util.swipe.ViewBinderHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddStepActivity extends AppCompatActivity implements OnNetworkCallBack, View.OnClickListener {


    TextView tv_step;
    TextView tv_header;
    TextView et_search;
    RecyclerView lv_ingridents;
    EditText et_comments;
    Button button_add_ingredients;
    ImageView iv_back;
    Activity activity;
    Progress progress;
    IngredientsAdapter adapter;
    ArrayList<IngredientsListForSearch> ingredientList = new ArrayList<>();
    IngredientsListForSearch searchBean = null;
    ArrayList<String> list;
    String comments = "";
    Intent intent;
    String step = "";
    Typeface face = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step);
        activity = AddStepActivity.this;
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        intent = getIntent();
        int stepNum = intent.getIntExtra(Constants.STEP_NUM, 0) + 1;
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        initView();
        list = new ArrayList<>();
        ingredientList = new ArrayList<>();
        step = "Step " + stepNum + ":";
        tv_step.setText(step);
        tv_step.setTypeface(face);
        et_comments.setTypeface(face);
        et_search.setTypeface(face);
        button_add_ingredients.setTypeface(face);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_step = (TextView) findViewById(R.id.tv_step);
        tv_header = (TextView) findViewById(R.id.tv_header);
        et_search = (TextView) findViewById(R.id.et_search);
        lv_ingridents = (RecyclerView) findViewById(R.id.lv_ingridents);
        et_comments = (EditText) findViewById(R.id.et_comments);
        button_add_ingredients = (Button) findViewById(R.id.button_add_ingredients);
        et_search.setOnClickListener(this);
        button_add_ingredients.setOnClickListener(this);

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (adapter != null) {
            adapter.saveStates(outState);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (adapter != null) {
            adapter.restoreStates(savedInstanceState);
        }
    }

    @Override
    public void
    onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                Helper.hideSoftKeyBoard(activity);
                Intent intent = new Intent(activity, SerachIngredientsActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS);
                break;
            case R.id.button_add_ingredients:
                if (isValid()) {
                    Helper.showSoftKeyBoard(activity);
                    StepBean stepBean = new StepBean();
                    ArrayList<StepBean.Ingredients> list = new ArrayList<StepBean.Ingredients>();

                    for (int i = 0; i < ingredientList.size(); i++) {
                        StepBean.Ingredients ingredients = new StepBean.Ingredients();
                        ingredients.setId(ingredientList.get(i).getId());
                        ingredients.setQty(ingredientList.get(i).getQty());
                        ingredients.setUnit(ingredientList.get(i).getUnit());
                        list.add(ingredients);
                    }
                    stepBean.setDescription(et_comments.getText().toString());
                    stepBean.setIngredientsList(list);
                    Intent intent1 = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.STEP_BEAN, stepBean);
                    intent1.putExtras(bundle);
                    setResult(Constants.REQUEST_CODE_FOR_STEP, intent1);
                    finish();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private boolean isValid() {
        comments = et_comments.getText().toString().trim();
        if (comments.isEmpty()) {
            Toast.makeText(AddStepActivity.this, "Please Write the Instructions in Comment Box", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ingredientList.size() == 0) {
            Toast.makeText(AddStepActivity.this, "Please Select at least One Ingredient", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getUnit().isEmpty()) {
                Toast.makeText(AddStepActivity.this, "Please Select Units for Recipes", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;

    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        if (isSuccess) {

        }
    }

    @Override
    public void onError(String msg) {

    }


    public ArrayList<IngredientsListForSearch> getAllPerviousAdapterValues() {
        ArrayList<IngredientsListForSearch> tempList = new ArrayList<>();
        int childCount = lv_ingridents.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (lv_ingridents.findViewHolderForLayoutPosition(i) instanceof IngredientsAdapter.ViewHolder) {
                IngredientsAdapter.ViewHolder childHolder = (IngredientsAdapter.ViewHolder) lv_ingridents.findViewHolderForLayoutPosition(i);
                IngredientsListForSearch bean = new IngredientsListForSearch();
                bean.setQty(childHolder.et_qty.getText().toString());
                bean.setUnit(childHolder.tv_unit.getText().toString());
                tempList.add(bean);
            }
        }
        return tempList;
    }

    class IngredientsAdapter extends RecyclerView.Adapter {
        int itemPosition;
        String[] qtyValue;
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        ArrayList<IngredientsListForSearch> list;

        public IngredientsAdapter(Activity activity, ArrayList<IngredientsListForSearch> list) {
            this.activity = activity;
            this.list = list;
            qtyValue = new String[list.size()];
            mInflater = LayoutInflater.from(activity);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.ingridents_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            itemPosition = position;
            ingredientList.get(position).setId(list.get(position).getId());
            ingredientList.get(position).setName(list.get(position).getName());

            final ViewHolder holder = (ViewHolder) h;
            final String data = list.get(position).getName();

            holder.et_qty.setText(list.get(position).getQty());
            holder.tv_unit.setText(list.get(position).getUnit());
//            String pId = list.get(position).getId();
//            String pPhoto = list.get(position).getPhoto();
            holder.tv_ingredients_name.setText(data);

            holder.et_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    qtyValue[position] = s.toString();
                    ingredientList.get(position).setQty(s.toString());
//                  IngredientsListForSearch bean = new IngredientsListForSearch();
//                  bean.setUnit(list.get(position).getUnit());
//                  bean.setQty(holder.et_qty.getText().toString());
//                  list.set(position, bean);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            holder.tv_unit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.hideSoftKeyBoard(activity);
                    final JsonObject obj = new JsonObject();
                    if (list.get(position).getQty() != null && !list.get(position).getQty().equalsIgnoreCase("")) {
                        obj.addProperty("quantity", list.get(position).getQty());

                    } else {
                        obj.addProperty("quantity", qtyValue[position]);
                    }
                    progress.show();
                    Ion.with(activity).load(Network.BASE_URL + "getUnitList").setJsonObjectBody(obj).asString().setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            progress.dismiss();
                            if (e == null) {
                                final Dialog unitDialog = new Dialog(activity);
                                unitDialog.setContentView(R.layout.unit_dialog);
                                unitDialog.setTitle("Please Select Units");
                                ListView unitList = (ListView) unitDialog.findViewById(R.id.listview_unit);
                                try {
                                    JSONObject objcet = new JSONObject(result);
                                    JSONArray itemArray = objcet.getJSONArray("response");

                                    final String[] units = new String[itemArray.length()];
                                    for (int i = 0; i < itemArray.length(); i++) {
                                        String value = itemArray.getString(i);
                                        units[i] = value;
                                    }
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, units);
                                    unitList.setAdapter(adapter);
                                    unitDialog.show();
                                    unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                            holder.tv_unit.setText(units[pos]);
                                            ingredientList.get(position).setUnit(units[pos]);
                                            //ingredientList.get(position).setQty(list.get(position).getQty());
                                            unitDialog.dismiss();
                                        }
                                    });
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                Toast.makeText(activity, "Please Define Units First", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            });
            binderHelper.bind(holder.swipeLayout, data);
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private SwipeRevealLayout swipeLayout;
            private FrameLayout deleteLayout;
            private TextView tv_ingredients_name;
            private EditText et_qty;
            private TextView tv_unit;
            private ImageView iv_ingredients;
            private Spinner spinner_unit;

            public ViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
                tv_ingredients_name = (TextView) itemView.findViewById(R.id.tv_ingredients_name);
                iv_ingredients = (ImageView) itemView.findViewById(R.id.iv_ingredients);
                tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
                deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
                spinner_unit = (Spinner) itemView.findViewById(R.id.spinner_unit);
                et_qty = (EditText) itemView.findViewById(R.id.et_qty);

                tv_ingredients_name.setTypeface(face);
                et_qty.setTypeface(face);
                tv_unit.setTypeface(face);

            }

            public void bind(String data) {
                deleteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ingredientList.remove(itemPosition);
                        addIngredients(ingredientList);
                        Toast.makeText(activity, "Ingredient Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    private void addIngredients(ArrayList<IngredientsListForSearch> searchList) {
        lv_ingridents.setHasFixedSize(true);
        lv_ingridents.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new IngredientsAdapter(activity, searchList);
        lv_ingridents.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_FOR_SEARCH_INGREDIENTS && data != null) {
            Bundle extras = data.getExtras();
            searchBean = (IngredientsListForSearch) extras.getSerializable(Constants.INGREDIENT_BEAN_OBJECT);
            // ingredientList.addAll(getAllPerviousAdapterValues());
            ingredientList.add(searchBean);
            addIngredients(ingredientList);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}