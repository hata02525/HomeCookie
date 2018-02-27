package com.app.homecookie.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.homecookie.Beans.AddressBean;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.ChatHelper.ChatService;
import com.app.homecookie.ChatHelper.LocalBinder;
import com.app.homecookie.DatabaseHelper.ChatFragment;
import com.app.homecookie.Fragments.AddIngredientsFragment;
import com.app.homecookie.Fragments.AddNewAdressFragment;
import com.app.homecookie.Fragments.BrowseCategoryListFragment;
import com.app.homecookie.Fragments.BrowseFragment;
import com.app.homecookie.Fragments.BrowseRecipesFragment;
import com.app.homecookie.Fragments.CommentFragment;
import com.app.homecookie.Fragments.CreateCategoryFragment;
import com.app.homecookie.Fragments.CreateLessonFragment;
import com.app.homecookie.Fragments.CreateNewMealFragment;
import com.app.homecookie.Fragments.CreateProfileFragment;
import com.app.homecookie.Fragments.DeliveryAddressFragment;
import com.app.homecookie.Fragments.DonateFragment;
import com.app.homecookie.Fragments.EnterCardDetailsFragment;
import com.app.homecookie.Fragments.GroceriesFragment;
import com.app.homecookie.Fragments.HomeCookieDetailsFragment;
import com.app.homecookie.Fragments.HomecookFragment;
import com.app.homecookie.Fragments.LessonDetailFragment;
import com.app.homecookie.Fragments.LessonsFragment;
import com.app.homecookie.Fragments.MealDetailFragment;
import com.app.homecookie.Fragments.MealsFragment;
import com.app.homecookie.Fragments.MyCategoryRecipesFragment;
import com.app.homecookie.Fragments.MyRecipesFragment;
import com.app.homecookie.Fragments.OneToManyChatFragment;
import com.app.homecookie.Fragments.OnetoOneChatFragrment;
import com.app.homecookie.Fragments.PaymentsFragment;
import com.app.homecookie.Fragments.PostFragment;
import com.app.homecookie.Fragments.ViewRecipeFragment;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.Calendar;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isChatFragmentAdded = false;
    private long mBackPressed = 0;
    private long TIME_INTERVAL = 2000;
    TextView tv_browse;
    TextView tv_post;
    TextView tv_my_recipes;
    TextView tv_groceries;
    TextView tv_home;
    AlertDialog alertDialog;
    int colorPrimary;
    int font_n_bg;
    int font_highlight;
    ArrayList<Fragment> fragmentList;
    Typeface face = null;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mhour;
    private int mminute;
    static final int TIME_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID = 0;
    public static final String HOMECOOKE_FRAG_STACK = "homeCoockFragStack";
    private boolean isFromHomeCookProfile = false;
    Activity activity;
    Intent intent;
    private ChatService chatService;
    private boolean isBounded = false;
    SharedPreference sharedPreference;
    private static HomeActivity instance = null;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatService = ((LocalBinder<ChatService>) service).getService();
            isBounded = true;
            Log.e("TAG", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            chatService = null;
            isBounded = false;
            Log.e("TAG", "onServiceConnected");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fresco.initialize(this);
        activity = HomeActivity.this;
        intent = getIntent();
        face = Typeface.createFromAsset(getAssets(), "gotham_normal.TTF");
        sharedPreference = new SharedPreference(activity);
        sharedPreference.putString(Constants.USER_CHAT_ID, "100@localhost");
        sharedPreference.putString(Constants.USER_CHAT_PASSWORD, "123456789");
        doBindService();
        sharedPreference = new SharedPreference(HomeActivity.this);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mhour = c.get(Calendar.HOUR_OF_DAY);
        mminute = c.get(Calendar.MINUTE);
        tv_browse = (TextView) findViewById(R.id.tv_browse);
        tv_post = (TextView) findViewById(R.id.tv_post);
        tv_my_recipes = (TextView) findViewById(R.id.tv_my_recipes);
        tv_groceries = (TextView) findViewById(R.id.tv_groceries);
        tv_home = (TextView) findViewById(R.id.tv_home);
        colorPrimary = getResources().getColor(R.color.colorPrimary);
        font_n_bg = getResources().getColor(R.color.font_n_bg);
        font_highlight = getResources().getColor(R.color.font_highlight);
        tv_browse.setTypeface(face);
        tv_my_recipes.setTypeface(face);
        tv_groceries.setTypeface(face);
        tv_post.setTypeface(face);
        tv_home.setTypeface(face);
        instance = this;

        Log.e("Email", sharedPreference.getString(Constants.USER_EMAIL, ""));
        Log.e("CUSTOMER ID", sharedPreference.getString(Constants.CUSTOMER_ID, ""));



        tv_browse.setOnClickListener(this);
        tv_post.setOnClickListener(this);
        tv_my_recipes.setOnClickListener(this);
        tv_groceries.setOnClickListener(this);
        tv_home.setOnClickListener(this);

        setTextViewDrawableColor(tv_browse, font_highlight);
        isChatFragmentAdded = sharedPreference.getBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
        if (intent.hasExtra(Constants.START_ONE_TO_ONE_CHAT)) {
            String toUserId = intent.getStringExtra(Constants.TO_USER_ID);
            String toUserName = intent.getStringExtra(Constants.TO_USER_NAME);
            String toUserPhoto = intent.getStringExtra(Constants.TO_USER_PHOTO);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TO_USER_ID, toUserId);
            bundle.putString(Constants.TO_USER_NAME, toUserName);
            bundle.putString(Constants.TO_USER_PHOTO, toUserPhoto);
            bundle.putBoolean(Constants.IS_FRIEND_CHAT, true);
            bundle.putBoolean(Constants.IS_RECENT_CHAT, false);
            replaceOneToOneChatFragment(bundle);
        } else {
            if (isChatFragmentAdded) {
                replaceChatFragment(0);
            } else {
                addBrowseFragment();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void addBrowseFragment() {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new BrowseFragment()).addToBackStack(null).commitAllowingStateLoss();
        sharedPreference.putBoolean(Constants.IS_HOME_COOKIE_FRAGMENT_REPLACED, false);
    }

    public void replaceBrowseFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new BrowseFragment()).addToBackStack(null).commitAllowingStateLoss();
        sharedPreference.putBoolean(Constants.IS_HOME_COOKIE_FRAGMENT_REPLACED, false);
    }

    public void replaceBrowseCategoryListFragment(CategoryListBean listBean) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, BrowseCategoryListFragment.newInstance(listBean)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceBrowseRecipesFragment(String categoryid,String CategoryName) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, BrowseRecipesFragment.newInstance(categoryid,CategoryName)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replacePostFragment() {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new PostFragment()).addToBackStack(null).commitAllowingStateLoss();
        sharedPreference.putBoolean(Constants.IS_HOME_COOKIE_FRAGMENT_REPLACED, false);
    }

    public void replaceMyRecipesFragment() {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new MyRecipesFragment()).addToBackStack(null).commitAllowingStateLoss();
        sharedPreference.putBoolean(Constants.IS_HOME_COOKIE_FRAGMENT_REPLACED, false);
    }

    public void replaceGroceriesFragment() {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new GroceriesFragment()).addToBackStack(null).commitAllowingStateLoss();
        sharedPreference.putBoolean(Constants.IS_HOME_COOKIE_FRAGMENT_REPLACED, false);
    }

    public void replaceHomecooksFragment() {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new HomecookFragment()).addToBackStack(null).commit();
    }

    public void replaceCommentFragment(String id, String from) {
        getSupportFragmentManager().beginTransaction().add(R.id.home_cotainer, CommentFragment.newInstance(id, from)).commit();
    }

    public void replaceAddIngredientsFragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new AddIngredientsFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceCreateCategoryFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new CreateCategoryFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceMyRecipesCategoryFragment(String categoryId) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, MyCategoryRecipesFragment.newInstance(categoryId)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceRecipeViewFragment(String id, String name, String posterPhoto, String posterName) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, ViewRecipeFragment.newInstance(id, name, posterPhoto, posterName)).addToBackStack(null).commitAllowingStateLoss();
    }


    public void replaceCreatMealFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new CreateNewMealFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceCreateLessonFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new CreateLessonFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceUpdateProfileFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, CreateProfileFragment.newInstance(true)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceDeliveryAddressFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new DeliveryAddressFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceAddNewAddressFragment(AddressBean bean, boolean b) {
        if (!b) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new AddNewAdressFragment()).addToBackStack(null).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, AddNewAdressFragment.newInstance(bean, b)).addToBackStack(null).commitAllowingStateLoss();
        }
    }

    public void replacePaymentFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new PaymentsFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceEnterCardDetailFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, new EnterCardDetailsFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceChatFragment(int fragmentToReplaced) {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, ChatFragment.newInstance(fragmentToReplaced)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceOneToOneChatFragment(Bundle bundle) {
        getSupportFragmentManager().beginTransaction().add(R.id.home_cotainer, OnetoOneChatFragrment.newInstance(bundle)).addToBackStack(null).commit();
    }

    public void replaceOneToManyChatFragment(Bundle bundle) {
        getSupportFragmentManager().beginTransaction().add(R.id.home_cotainer, OneToManyChatFragment.newInstance(bundle)).addToBackStack(null).commit();
    }

    public void replaceMealsFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, new MealsFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceLessonFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new LessonsFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceDonateFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new DonateFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceMealDetailFragment(String mealId) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, MealDetailFragment.newInstance(mealId)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceLessonDetailFragment(String mealId) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, LessonDetailFragment.newInstance(mealId)).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceHomeCookieDetailsFragment(boolean isMyProfile, String userId) {
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_cotainer, HomeCookieDetailsFragment.newInstance(isMyProfile, userId)).addToBackStack(null).commit();
    }


    public static HomeActivity getInstance() {
        return instance;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_cotainer);
        switch (v.getId()) {
            case R.id.tv_browse:
                if (!(fragment instanceof BrowseFragment)) {
                    changeDrawable();
                    tv_browse.setBackgroundColor(font_n_bg);
                    tv_browse.setTextColor(font_highlight);
                    tv_browse.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.browse, 0, 0);
                    replaceBrowseFragment();
                }
                break;
            case R.id.tv_post:
                if (!(fragment instanceof PostFragment)) {
                    changeDrawable();
                    tv_post.setBackgroundColor(font_n_bg);
                    tv_post.setTextColor(font_highlight);
                    tv_post.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.post, 0, 0);
                    setTextViewDrawableColor(tv_post, font_highlight);
                    replacePostFragment();
                }
                break;
            case R.id.tv_my_recipes:
                if (!(fragment instanceof MyRecipesFragment)) {
                    changeDrawable();
                    tv_my_recipes.setBackgroundColor(font_n_bg);
                    tv_my_recipes.setTextColor(font_highlight);
                    tv_my_recipes.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.myrecipe, 0, 0);
                    setTextViewDrawableColor(tv_my_recipes, font_highlight);
                    replaceMyRecipesFragment();
                }
                break;
            case R.id.tv_groceries:
                if (!(fragment instanceof GroceriesFragment)) {
                    changeDrawable();
                    tv_groceries.setBackgroundColor(font_n_bg);
                    tv_groceries.setTextColor(font_highlight);
                    tv_groceries.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.grociries, 0, 0);
                    setTextViewDrawableColor(tv_groceries, font_highlight);
                    replaceGroceriesFragment();
                }
                // showLogoutDialog();
                break;
            case R.id.tv_home:
                if (!(fragment instanceof HomecookFragment)) {
                    changeDrawable();
                    tv_home.setBackgroundColor(font_n_bg);
                    tv_home.setTextColor(font_highlight);
                    tv_home.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home, 0, 0);
                    setTextViewDrawableColor(tv_home, font_highlight);
                    replaceHomecooksFragment();
                }
                break;
        }
    }

    private void closeActivityWithDoubleClick() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onBackPressed() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.home_cotainer);
        if (visibleFragment instanceof BrowseFragment || visibleFragment instanceof PostFragment ||
                visibleFragment instanceof MyRecipesFragment || visibleFragment instanceof GroceriesFragment) {
            isFromHomeCookProfile = sharedPreference.getBoolean(Constants.IS_FROM_HOME_COOKIE_PROFILE, false);
            if (isFromHomeCookProfile) {
                super.onBackPressed();
            } else {
                closeActivityWithDoubleClick();
            }
            sharedPreference.putBoolean(Constants.IS_FROM_HOME_COOKIE_PROFILE, false);
        } else if (visibleFragment instanceof HomecookFragment) {
            Fragment childFragment = visibleFragment.getFragmentManager().findFragmentById(R.id.frag_container);
            if (childFragment instanceof MealsFragment) {
                closeActivityWithDoubleClick();
            } else if (childFragment instanceof LessonsFragment) {
                replaceMealsFragment();
                HomecookFragment fragment = (HomecookFragment) getSupportFragmentManager().findFragmentById(R.id.home_cotainer);
                fragment.prepareMealFragment();
            } else if (childFragment instanceof DonateFragment) {
                replaceMealsFragment();
                HomecookFragment fragment = (HomecookFragment) getSupportFragmentManager().findFragmentById(R.id.home_cotainer);
                fragment.prepareMealFragment();
            } else if (childFragment instanceof MealDetailFragment) {
                replaceMealsFragment();
            } else if (childFragment instanceof LessonDetailFragment) {
                replaceLessonFragment();
            } else {
                super.onBackPressed();
            }
        } else if (visibleFragment instanceof CommentFragment) {
            super.onBackPressed();
        } else if (visibleFragment instanceof ChatFragment) {
            replaceBrowseFragment();
        } else if (visibleFragment instanceof OnetoOneChatFragrment) {
            boolean isFromFriendChat = sharedPreference.getBoolean(Constants.IS_FRIEND_CHAT, false);
            boolean isFromRecentChat = sharedPreference.getBoolean(Constants.IS_RECENT_CHAT, false);
            if (isFromFriendChat) {
                replaceChatFragment(2);
            }
            if (isFromRecentChat) {
                replaceChatFragment(0);
            }
        } else {
            super.onBackPressed();
        }
    }

    public void changeDrawable() {
        tv_browse.setBackgroundColor(colorPrimary);
        tv_browse.setTextColor(font_n_bg);
        tv_browse.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.browse_white, 0, 0);
        tv_post.setBackgroundColor(colorPrimary);
        tv_post.setTextColor(font_n_bg);
        tv_post.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.post_white, 0, 0);
        tv_my_recipes.setBackgroundColor(colorPrimary);
        tv_my_recipes.setTextColor(font_n_bg);
        tv_my_recipes.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.myrecipe_white, 0, 0);
        tv_groceries.setBackgroundColor(colorPrimary);
        tv_groceries.setTextColor(font_n_bg);
        tv_groceries.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.grociries_white, 0, 0);
        tv_home.setBackgroundColor(colorPrimary);
        tv_home.setTextColor(font_n_bg);
        tv_home.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home_white, 0, 0);

    }

    public void showLogoutDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Log Out!!!")
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
                        new SharedPreference(HomeActivity.this).clear();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        new SharedPreference(activity).putString(Constants.PREVIOUS_USER_USR_ID, userId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }


    private void updateDate() {
        StringBuilder dateBuilder = new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay).append("/")
                .append(mMonth + 1).append("/")
                .append(mYear).append(" ");
        showDialog(TIME_DIALOG_ID);
    }

    public String updatetime() {
        StringBuilder stringBuilder = new StringBuilder()
                .append(pad(mhour)).append(":")
                .append(pad(mminute));
        return stringBuilder.toString();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                }
            };


    // Timepicker dialog generation
    public TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mhour = hourOfDay;
                    mminute = minute;

                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mhour, mminute, false);

        }
        return null;
    }

    public void showDatePickerDialog() {
        showDialog(DATE_DIALOG_ID);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
    }

    void doBindService() {
        bindService(new Intent(HomeActivity.this, ChatService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }


    public ChatService getChatService() {
        return chatService;
    }


    @Override
    public void onStop() {
        super.onStop();
        sharedPreference.putBoolean(Constants.IS_CHAT_FRAGMENT_ADDED, false);
    }
}

