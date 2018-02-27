package com.app.homecookie.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.homecookie.Beans.AddressBean;
import com.app.homecookie.Beans.CardBean;
import com.app.homecookie.Beans.CategoryListBean;
import com.app.homecookie.Beans.CommentBean;
import com.app.homecookie.Beans.FriendListBean;
import com.app.homecookie.Beans.GroupBean;
import com.app.homecookie.Beans.GroupCreateDetailsBean;
import com.app.homecookie.Beans.IngredientsListForSearch;
import com.app.homecookie.Beans.MealDetailsBean;
import com.app.homecookie.Beans.MealListBean;
import com.app.homecookie.Beans.MyCategoryRecipeBean;
import com.app.homecookie.Beans.MyRecipesBean;
import com.app.homecookie.Beans.RecipeDetailBean;
import com.app.homecookie.Beans.RecipesBean;
import com.app.homecookie.Beans.SubCategoryBean;
import com.app.homecookie.Beans.UserDetailBean;
import com.app.homecookie.ChatHelper.RecentDb;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fluper on 30/3/17.
 */

public class Network {

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

    /****
     * ---------ALL WEB SERVICES URLS------
     *****/
    //  public static final String BASE_URL = "http://54.186.161.138/Homecookie/api/";
    public static final String BASE_URL = "http://67.209.121.170/homecookie/api/";
    //public static final String BASE_URL = "http://flupertech.com/Homecookie/api/";
    public static final String REQUEST_FOR_LOGIN = BASE_URL + "login";
    public static final String REQUEST_FOR_SIGNUP = BASE_URL + "signup";
    public static final String REQUEST_FOR_CREATE_USER_ID = BASE_URL + "create_userId";
    public static final String REQUEST_FOR_CREATE_USER_PROFILE = BASE_URL + "updateProfile";
    public static final String REQUEST_FOR_BROWSE_CATEGORY = BASE_URL + "categoriesList";
    public static final String REQUEST_FOR_BROWSE_SUB_CATEGORY = BASE_URL + "subcategorylist";
    public static final String REQUEST_FOR_BROWSE_RECIPE = BASE_URL + "getRecipes";
    public static final String REQUEST_FOR_VEIW_RECIPE = BASE_URL + "recipeview";
    public static final String REQUEST_FOR_ADD_RECIPE_TO_FAV = BASE_URL + "addToFavourite";
    public static final String REQUEST_FOR_INGRIDIENTS_LIST = BASE_URL + "ingredientList";
    public static final String REQUEST_FOR_MY_RECIPES = BASE_URL + "getUserCategories";
    public static final String REQUEST_FOR_CREATE_CATEGORY = BASE_URL + "createUserCategory";
    public static final String REQUEST_FOR_MY_CATEGORY_RECIPE = BASE_URL + "getRecipesCategory";
    public static final String REQUEST_FOR_DELETE_RECIPE = BASE_URL + "deleteRecipe";
    public static final String REQUESET_FOR_CREATE_INGREDIENTS = BASE_URL + "createIngredient";
    public static final String REQUESET_FOR_SEARCH_INGREDIENTS = BASE_URL + "searchIngredientList";
    public static final String REQUEST_FOR_FORGOT_PASSWORD = BASE_URL + "forgetPassword";
    public static final String REQUEST_FOR_VERIFY_KEY = BASE_URL + "verifyKey";
    public static final String REQUEST_FOR_SET_NEW_PASSWORD = BASE_URL + "setPassword";
    public static final String REQUEST_FOR_CREATE_RECIPE = BASE_URL + "createrecipe";
    public static final String REQUEST_FOR_RECIPE_DETAIL = BASE_URL + "getRecipesDetails";
    public static final String REQUEST_FOR_ADD_GROCERRIES = BASE_URL + "addGroceries";
    public static final String REQUEST_FOR_COMMENT = BASE_URL + "comment";
    public static final String REQUEST_FOR_FRIEND_LIST = BASE_URL + "searchUnknownUsers";
    public static final String REQUEST_FOR_ADD_TO_FRIEND_LIST = BASE_URL + "addToFriendList";
    public static final String REQUEST_FOR_USER_FRIEND_LIST = BASE_URL + "getFriendList";
    public static final String REQUEST_FOR_CREATE_GROUP = BASE_URL + "createGroup";
    public static final String REQUEST_FOR_GROUPS = BASE_URL + "getGroups";
    public static final String RATE_THE_RECIPE_MEAL_LESSON = BASE_URL + "rate";
    public static final String REQUEST_FOR_THE_DELETE_GROUP = BASE_URL + "deleteGroup";
    public static final String REQUEEST_FOR_DELETE_FRIEND = BASE_URL + "deleteFriend";
    public static final String REQUEST_FOR_MEAL_LIST = BASE_URL + "getMealList";
    public static final String REQUEST_FOR_LESSON_LIST = BASE_URL + "lessonList";
    public static final String REQUEST_FOR_CREATE_MEAL = BASE_URL + "createMeal";
    public static final String REQUEST_FOR_CREATE_LESSON = BASE_URL + "createLesson";
    public static final String REQUEST_FOR_MEAL_DETAIL = BASE_URL + "mealDetail";
    public static final String REQUEST_FOR_LESSON_DETAIL = BASE_URL + "lessonDetails";
    public static final String REQUEST_FOR_VIEW_COMMENT = BASE_URL + "viewComment";
    public static final String REQUEST_FOR_DELETE_COMMENT = BASE_URL + "deleteComment";
    public static final String REQUEST_FOR_REPORT_COMMENT = BASE_URL + "reportComment";
    public static final String REQUEST_FOR_LIKE = BASE_URL + "like";
    public static final String REQUEST_FOR_USER_DETAIL = BASE_URL + "getUserProfile";
    public static final String REQUEST_FOR_ADD_UPDATE_ADDRESS = BASE_URL + "saveAddress";
    public static final String REQUESET_FOR_ADDRESS_LIST = BASE_URL + "viewAddress";
    public static final String REQUESET_FOR_DELETE_ADDRESS = BASE_URL + "deleteAddress";
    public static final String REQUEST_FOR_ADD_CARD = BASE_URL + "addCard";
    public static final String REQUEST_FOR_DELETE_CARD = BASE_URL + "deleteCard";
    public static final String REQUEST_FOR_GET_CARD_LIST = BASE_URL + "getCardList";
    public static final String REQUEST_FOR_FEATURED_RECIPE = BASE_URL + "getFeaturedRecipe";
    public static final String REQUEST_FOR_ADD_RECIPE_TO_USER_CATEGORY = BASE_URL + "addRecipeToUserCategory";
    public static final String REQUEST_FOR_MEAL_BUY = BASE_URL + "mealBuy";
    public static final String REQUEST_FOR_LESSON_SIGN_UP = BASE_URL + "lessonSignup";

    /****** ---------ALL WEB SERVICES URLS REGARDING CONSTANTS----****/

    public static final int REQUEST_TYPE_LOGIN = 1;
    public static final int REQUEST_TYPE_SIGN_UP = 2;
    public static final int REQUEST_TYPE_CREATE_USER_ID = 3;
    public static final int REQUEST_TYPE_CREATE_USER_PROFILE = 4;
    public static final int REQUEST_TYPE_BROWSE_CATEGORY = 5;
    public static final int REQUEST_TYPE_BROWSE_SUB_CATEGORY = 6;
    public static final int REQUEST_TYPE_BROWSE_RECIPE = 7;
    public static final int REQUEST_TYPE_ADD_RECIPE_TO_FAV = 8;
    public static final int REQUEST_TYPE_INGRIDIENTS_LIST = 9;
    public static final int REQUEST_TYPE_ADD_INGRIDIENTS = 10;
    public static final int REQUEST_TYPE_MY_RECIPES = 11;
    public static final int REQUEST_TYPE_CREATE_CATEGORY = 12;
    public static final int REQUEST_TYPE_MY_CATEGORY_RECIPE = 13;
    public static final int REQUEST_TYPE_DELETE_RECIPE = 14;
    public static final int REQUESET_TYPE_CREATE_INGREDIENTS = 15;
    public static final int REQUESET_TYPE_SEARCH_INGREDIENTS = 16;
    public static final int REQUEST_TYPE_FORGOT_PASSWORD = 17;
    public static final int REQUEST_TYPE_VERIFY_KEY = 18;
    public static final int REQUEST_TYPE_SET_NEW_PASSWORD = 19;
    public static final int REQUEST_TYPE_CREATE_RECIPE = 20;
    public static final int REQUEST_TYPE_RECIPE_DETAIL = 21;
    public static final int REQUEST_TYPE_ADD_GROCERRIES = 22;
    public static final int REQUEST_TYPE_COMMENT = 23;
    public static final int REQUEST_TYPE_FRIEND_LIST = 24;
    public static final int REQUEST_TYPE_ADD_TO_FRIEND_LIST = 25;
    public static final int REQUEST_TYPE_USER_FRIEND_LIST = 26;
    public static final int REQUEST_TYPE_CREATE_GROUP = 27;
    public static final int REQUEST_TYPE_GET_GROUP = 28;
    public static final int REQUEST_RATE_THE_RECIPE = 28;
    public static final int REQUEST_TYPE_THE_DELETE_GROUP = 29;
    public static final int REQUEEST_TYPE_DELETE_FRIEND = 30;
    public static final int REQUEST_TYPE_MEAL_LIST = 31;
    public static final int REQUEST_TYPE_LESSON_LIST = 32;
    public static final int REQUEST_TYPE_CREATE_MEAL = 33;
    public static final int REQUEST_TYPE_CREATE_LESSON = 34;
    public static final int REQUEST_TYPE_MEAL_DETAIL = 35;
    public static final int REQUEST_TYPE_LESSON_DETAIL = 36;
    public static final int REQUEST_TYPE_VIEW_COMMENT = 37;
    public static final int REQUEST_TYPE_DELETE_COMMENT = 38;
    public static final int REQUEST_TYPE_REPORT_COMMENT = 39;
    public static final int REQUEST_TYPE_LIKE = 40;
    public static final int REQUEST_TYPE_USER_DETAIL = 41;
    public static final int REQUEST_TYPE_ADD_UPDATE_ADDRESS = 42;
    public static final int REQUESET_TYPE_ADDRESS_LIST = 43;
    public static final int REQUESET_TYPE_DELETE_ADDRESS = 44;
    public static final int REQUEST_TYPE_ADD_CARD = 45;
    public static final int REQUEST_TYPE_GET_CARD_LIST = 46;
    public static final int REQUEST_TYPE_DELETE_CARD = 47;
    public static final int REQUEST_TYPE_FEATURED_RECIPE = 48;
    public static final int REQUEST_TYPE_ADD_RECIPE_TO_USER_CATEGORY = 49;
    public static final int REQUEST_TYPE_MEAL_BUY = 50;
    public static final int REQUEST_TYPE_LESSON_SIGN_UP = 51;


    public static void requestForLogin(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {

        Ion.with(context)
                .load(REQUEST_FOR_LOGIN)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            int status = result.get("status").getAsInt();
                            String message = result.get("message").getAsString();
                            if (status == 1) {
                                JsonArray resultArray = result.get("result").getAsJsonArray();
                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject object = resultArray.get(i).getAsJsonObject();
                                    JsonElement accessTokenElement = object.get("accessToken");
                                    JsonElement profileStatusEelement = object.get("profileStatus");
                                    JsonElement photoElement = object.get("photo");
                                    JsonElement idElement = object.get("Id");
                                    JsonElement userIdElement = object.get("userId");
                                    JsonElement usersEmailElement = object.get("usersEmail");
                                    JsonElement firstNameElement = object.get("firstName");
                                    JsonElement lastNameElement = object.get("lastName");
                                    JsonElement genderElement = object.get("lastName");
                                    JsonElement dobElement = object.get("dob");
                                    JsonElement aboutElement = object.get("aboutMe");
                                    JsonElement occupationElement = object.get("occupation");
                                    JsonElement customerId = object.get("customerId");

                                    String profileStatus = returnEmptyString(profileStatusEelement);
                                    String accessToken = returnEmptyString(accessTokenElement);
                                    String photo = returnEmptyString(photoElement);
                                    String Id = returnEmptyString(idElement);
                                    String userId = returnEmptyString(userIdElement);
                                    String usersEmail = returnEmptyString(usersEmailElement);
                                    String firstName = returnEmptyString(firstNameElement);
                                    String lastName = returnEmptyString(lastNameElement);
                                    String gender = returnEmptyString(genderElement);
                                    String dob = returnEmptyString(dobElement);
                                    String occupation = returnEmptyString(occupationElement);
                                    String aboutMe = returnEmptyString(aboutElement);
                                    SharedPreference sharedPreference = new SharedPreference(context);
                                    sharedPreference.putString(Constants.ACCESSTOKEN, accessToken);
                                    sharedPreference.putInt(Constants.USER_PROFILE_STATUS, Integer.parseInt(profileStatus));
                                    sharedPreference.putString(Constants.USER_PHOTO, photo);
                                    sharedPreference.putString(Constants.USER_F_NAME, firstName);
                                    sharedPreference.putString(Constants.USER_L_NAME, lastName);
                                    sharedPreference.putString(Constants.USER_DOB, dob);
                                    sharedPreference.putString(Constants.USER_OCC, occupation);
                                    sharedPreference.putString(Constants.USER_ABOUT_ME, aboutMe);
                                    sharedPreference.putString(Constants.USER_GENDER, gender);
                                    sharedPreference.putString(Constants.USER_OCC, occupation);
                                    sharedPreference.putString(Constants.USER_USR_ID, Id);
                                    sharedPreference.putString(Constants.CUSTOMER_ID, returnEmptyString(customerId));


                                    String previousUserId = sharedPreference.getString(Constants.PREVIOUS_USER_USR_ID, "0");
                                    if (!previousUserId.equalsIgnoreCase(Id)) {
                                        RecentDb recentDb = new RecentDb(context);
                                        // context.deleteDatabase(CommonMethods.DBNAME);
                                        recentDb.clearDatabase();
                                        context.openOrCreateDatabase(RecentDb.DATABASE_NAME, Context.MODE_PRIVATE, null);
                                    }
                                }
                                listener.onSuccess(REQUEST_TYPE_SIGN_UP, true, message, jsonObject);
                            } else {
                                listener.onError(message);
                            }
                        } else {
                            if (e instanceof com.google.gson.JsonParseException) {

                                listener.onError("Server Under Maintenance");
                            }
                            if (e instanceof java.util.concurrent.TimeoutException) {
                                listener.onError("Oops! Time Out");
                            }
                        }
                    }
                });

    }


    public static void requestForSignup(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_SIGNUP)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            int status = result.get("status").getAsInt();
                            String message = result.get("message").getAsString();
                            if (status == 1) {
                                JsonArray resultArray = result.get("result").getAsJsonArray();
                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject object = resultArray.get(i).getAsJsonObject();
                                    JsonElement accessTokenElement = object.get("accessToken");
                                    JsonElement profileStatusEelement = object.get("profileStatus");
                                    JsonElement photoElement = object.get("profileStatus");
                                    JsonElement idElement = object.get("Id");
                                    JsonElement userIdElement = object.get("userId");
                                    JsonElement usersEmailElement = object.get("usersEmail");
                                    JsonElement firstNameElement = object.get("firstName");
                                    JsonElement lastNameElement = object.get("lastName");
                                    JsonElement genderElement = object.get("lastName");
                                    JsonElement dobElement = object.get("dob");
                                    JsonElement aboutElement = object.get("aboutMe");
                                    JsonElement occupationElement = object.get("occupation");
                                    JsonElement customerId = object.get("customerId");
                                    String profileStatus = returnEmptyString(profileStatusEelement);
                                    String accessToken = returnEmptyString(accessTokenElement);
                                    String photo = returnEmptyString(photoElement);
                                    String Id = returnEmptyString(idElement);
                                    String userId = returnEmptyString(userIdElement);
                                    String usersEmail = returnEmptyString(usersEmailElement);
                                    String firstName = returnEmptyString(firstNameElement);
                                    String lastName = returnEmptyString(lastNameElement);
                                    String gender = returnEmptyString(genderElement);
                                    String dob = returnEmptyString(dobElement);
                                    String occupation = returnEmptyString(occupationElement);
                                    String aboutMe = returnEmptyString(aboutElement);
                                    SharedPreference sharedPreference = new SharedPreference(context);
                                    sharedPreference.putString(Constants.ACCESSTOKEN, accessToken);
                                    sharedPreference.putInt(Constants.USER_PROFILE_STATUS, Integer.parseInt(profileStatus));
                                    sharedPreference.putString(Constants.USER_PHOTO, photo);
                                    sharedPreference.putString(Constants.USER_F_NAME, firstName);
                                    sharedPreference.putString(Constants.USER_L_NAME, lastName);
                                    sharedPreference.putString(Constants.USER_DOB, dob);
                                    sharedPreference.putString(Constants.USER_OCC, occupation);
                                    sharedPreference.putString(Constants.USER_ABOUT_ME, aboutMe);
                                    sharedPreference.putString(Constants.USER_GENDER, gender);
                                    sharedPreference.putString(Constants.USER_OCC, occupation);
                                    sharedPreference.putString(Constants.USER_USR_ID, Id);
                                    sharedPreference.putString(Constants.CUSTOMER_ID, returnEmptyString(customerId));

                                }
                                listener.onSuccess(REQUEST_TYPE_SIGN_UP, true, message, jsonObject);
                            } else {
                                listener.onError(message);
                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForCreateId(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_CREATE_USER_ID)
                .setTimeout(60 * 1000)
                .setHeader("accessToken", new SharedPreference(context).getString(Constants.ACCESSTOKEN, ""))
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            int status = result.get("status").getAsInt();
                            String message = result.get("message").getAsString();
                            if (status == 1) {
                                JsonArray resultArray = result.get("result").getAsJsonArray();
                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject object = resultArray.get(i).getAsJsonObject();
                                    JsonElement accessTokenElement = object.get("accessToken");
                                    JsonElement profileStatusEelement = object.get("profileStatus");
                                    JsonElement photoElement = object.get("profileStatus");
                                    JsonElement idElement = object.get("Id");
                                    JsonElement userIdElement = object.get("userId");
                                    JsonElement usersEmailElement = object.get("usersEmail");
                                    JsonElement firstNameElement = object.get("firstName");
                                    JsonElement lastNameElement = object.get("lastName");
                                    JsonElement genderElement = object.get("lastName");
                                    JsonElement dobElement = object.get("dob");
                                    JsonElement aboutElement = object.get("aboutMe");
                                    JsonElement occupationElement = object.get("occupation");
                                    String profileStatus = returnEmptyString(profileStatusEelement);
                                    String accessToken = returnEmptyString(accessTokenElement);
                                    String photo = returnEmptyString(photoElement);
                                    String Id = returnEmptyString(idElement);
                                    String userId = returnEmptyString(userIdElement);
                                    String usersEmail = returnEmptyString(usersEmailElement);
                                    String firstName = returnEmptyString(firstNameElement);
                                    String lastName = returnEmptyString(lastNameElement);
                                    String gender = returnEmptyString(genderElement);
                                    String dob = returnEmptyString(dobElement);
                                    String occupation = returnEmptyString(occupationElement);
                                    String aboutMe = returnEmptyString(aboutElement);
                                    SharedPreference sharedPreference = new SharedPreference(context);
                                    sharedPreference.putString(Constants.ACCESSTOKEN, accessToken);
                                    sharedPreference.putInt(Constants.USER_PROFILE_STATUS, Integer.parseInt(profileStatus));
                                    sharedPreference.putString(Constants.USER_PHOTO, photo);
                                    sharedPreference.putString(Constants.USER_F_NAME, firstName);
                                    sharedPreference.putString(Constants.USER_L_NAME, lastName);
                                    sharedPreference.putString(Constants.USER_DOB, dob);
                                    sharedPreference.putString(Constants.USER_OCC, occupation);
                                    sharedPreference.putString(Constants.USER_ABOUT_ME, aboutMe);
                                    sharedPreference.putString(Constants.USER_GENDER, gender);
                                    sharedPreference.putString(Constants.USER_OCC, occupation);
                                    sharedPreference.putString(Constants.USER_USR_ID, Id);
                                }
                                listener.onSuccess(REQUEST_TYPE_SIGN_UP, true, message, jsonObject);
                            } else {
                                listener.onError(message);
                            }
                        } else {
                            listener.onError("" + String.valueOf(e));
                        }
                    }
                });
    }


    public static void requestForCreateUserProfile(final Context context, String fName, String lName,
                                                   String gender, String dateTosend, String occupation, String aboutMe, File imgFile, final OnNetworkCallBack listener) {
        JsonObject object = new JsonObject();
        object.addProperty("firstName", fName);
        object.addProperty("lastName", lName);
        object.addProperty("gender", gender);
        object.addProperty("dob", dateTosend);
        object.addProperty("occupation", occupation);
        object.addProperty("aboutMe", aboutMe);

        if (imgFile != null) {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_USER_PROFILE)
                    .setTimeout(60 * 1000)
                    .setHeader("accessToken", new SharedPreference(context).getString(Constants.ACCESSTOKEN, ""))
                    .setMultipartFile("image", imgFile)
                    .setMultipartParameter("json", object.toString())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                int status = result.get("status").getAsInt();
                                String message = result.get("message").getAsString();
                                if (status == 1) {
                                    JsonArray resultArray = result.get("result").getAsJsonArray();
                                    for (int i = 0; i < resultArray.size(); i++) {
                                        JsonObject object = resultArray.get(i).getAsJsonObject();
                                        JsonElement accessTokenElement = object.get("accessToken");
                                        JsonElement profileStatusEelement = object.get("profileStatus");
                                        JsonElement photoElement = object.get("photo");
                                        JsonElement idElement = object.get("Id");
                                        JsonElement userIdElement = object.get("userId");
                                        JsonElement usersEmailElement = object.get("usersEmail");
                                        JsonElement firstNameElement = object.get("firstName");
                                        JsonElement lastNameElement = object.get("lastName");
                                        JsonElement genderElement = object.get("gender");
                                        JsonElement dobElement = object.get("dob");
                                        JsonElement aboutElement = object.get("aboutMe");
                                        JsonElement occupationElement = object.get("occupation");
                                        String profileStatus = returnEmptyString(profileStatusEelement);
                                        String accessToken = returnEmptyString(accessTokenElement);
                                        String photo = returnEmptyString(photoElement);
                                        String Id = returnEmptyString(idElement);
                                        String userId = returnEmptyString(userIdElement);
                                        String usersEmail = returnEmptyString(usersEmailElement);
                                        String firstName = returnEmptyString(firstNameElement);
                                        String lastName = returnEmptyString(lastNameElement);
                                        String gender = returnEmptyString(genderElement);
                                        String dob = returnEmptyString(dobElement);
                                        String occupation = returnEmptyString(occupationElement);
                                        String aboutMe = returnEmptyString(aboutElement);

                                        SharedPreference sharedPreference = new SharedPreference(context);
                                        sharedPreference.putString(Constants.ACCESSTOKEN, accessToken);
                                        sharedPreference.putInt(Constants.USER_PROFILE_STATUS, Integer.parseInt(profileStatus));
                                        sharedPreference.putString(Constants.USER_PHOTO, photo);
                                        sharedPreference.putString(Constants.USER_F_NAME, firstName);
                                        sharedPreference.putString(Constants.USER_L_NAME, lastName);
                                        sharedPreference.putString(Constants.USER_DOB, dob);
                                        sharedPreference.putString(Constants.USER_OCC, occupation);
                                        sharedPreference.putString(Constants.USER_ABOUT_ME, aboutMe);
                                        sharedPreference.putString(Constants.USER_GENDER, gender);
                                        sharedPreference.putString(Constants.USER_OCC, occupation);
                                        sharedPreference.putString(Constants.USER_USR_ID, Id);

                                    }
                                    listener.onSuccess(REQUEST_TYPE_SIGN_UP, true, message, result);
                                } else {
                                    listener.onError(message);
                                }
                            } else {
                                listener.onError("Network Error");
                            }
                        }
                    });
        } else {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_USER_PROFILE)
                    .setTimeout(60 * 1000)
                    .setHeader("accessToken", new SharedPreference(context).getString(Constants.ACCESSTOKEN, ""))
                    .setMultipartParameter("image", null)
                    .setMultipartParameter("json", object.toString())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                int status = result.get("status").getAsInt();
                                String message = result.get("message").getAsString();
                                if (status == 1) {
                                    JsonArray resultArray = result.get("result").getAsJsonArray();
                                    for (int i = 0; i < resultArray.size(); i++) {
                                        JsonObject object = resultArray.get(i).getAsJsonObject();
                                        JsonElement accessTokenElement = object.get("accessToken");
                                        JsonElement profileStatusEelement = object.get("profileStatus");
                                        JsonElement photoElement = object.get("photo");
                                        JsonElement idElement = object.get("Id");
                                        JsonElement userIdElement = object.get("userId");
                                        JsonElement usersEmailElement = object.get("usersEmail");
                                        JsonElement firstNameElement = object.get("firstName");
                                        JsonElement lastNameElement = object.get("lastName");
                                        JsonElement genderElement = object.get("gender");
                                        JsonElement dobElement = object.get("dob");
                                        JsonElement aboutElement = object.get("aboutMe");
                                        JsonElement occupationElement = object.get("occupation");
                                        String profileStatus = returnEmptyString(profileStatusEelement);
                                        String accessToken = returnEmptyString(accessTokenElement);
                                        String photo = returnEmptyString(photoElement);
                                        String Id = returnEmptyString(idElement);
                                        String userId = returnEmptyString(userIdElement);
                                        String usersEmail = returnEmptyString(usersEmailElement);
                                        String firstName = returnEmptyString(firstNameElement);
                                        String lastName = returnEmptyString(lastNameElement);
                                        String gender = returnEmptyString(genderElement);
                                        String dob = returnEmptyString(dobElement);
                                        String occupation = returnEmptyString(occupationElement);
                                        String aboutMe = returnEmptyString(aboutElement);

                                        SharedPreference sharedPreference = new SharedPreference(context);
                                        sharedPreference.putString(Constants.ACCESSTOKEN, accessToken);
                                        sharedPreference.putInt(Constants.USER_PROFILE_STATUS, Integer.parseInt(profileStatus));
                                        sharedPreference.putString(Constants.USER_F_NAME, firstName);
                                        sharedPreference.putString(Constants.USER_L_NAME, lastName);
                                        sharedPreference.putString(Constants.USER_DOB, dob);
                                        sharedPreference.putString(Constants.USER_OCC, occupation);
                                        sharedPreference.putString(Constants.USER_ABOUT_ME, aboutMe);
                                        sharedPreference.putString(Constants.USER_GENDER, gender);
                                        sharedPreference.putString(Constants.USER_OCC, occupation);
                                        sharedPreference.putString(Constants.USER_USR_ID, Id);
                                    }
                                    listener.onSuccess(REQUEST_TYPE_SIGN_UP, true, message, result);
                                } else {
                                    listener.onError(message);
                                }
                            } else {
                                listener.onError("" + String.valueOf(e));
                            }
                        }
                    });

        }
    }


    public static void requestForCategoryList(final Context context, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_BROWSE_CATEGORY)
                .setTimeout(60 * 1000)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resultArray = resulObj.get("response").getAsJsonArray();
                                    CategoryListBean categoryListBean = null;
                                    ArrayList<CategoryListBean> categoryList = new ArrayList<CategoryListBean>();
                                    for (int i = 0; i < resultArray.size(); i++) {
                                        categoryListBean = new CategoryListBean();
                                        JsonObject categoryObject = resultArray.get(i).getAsJsonObject();
                                        JsonElement catIdElement = categoryObject.get("catId");
                                        JsonElement catNameElement = categoryObject.get("CatName");
                                        categoryListBean.setCatId(returnEmptyString(catIdElement));
                                        categoryListBean.setCatName(returnEmptyString(catNameElement));
                                        categoryList.add(categoryListBean);
                                        JsonElement subcategoriesElement = categoryObject.get("subcategories");
                                        JsonArray subCategoryArray = subcategoriesElement.getAsJsonArray();
                                        CategoryListBean.SubcategoriesBean subcategoriesBean = null;
                                        CategoryListBean.SubcategoriesBean bean = new CategoryListBean.SubcategoriesBean();
                                        bean.setSubcat_name("All");
                                        bean.setCatId("0");
                                        ArrayList<CategoryListBean.SubcategoriesBean> subcategoreisList = new ArrayList<CategoryListBean.SubcategoriesBean>();
                                        subcategoreisList.add(bean);
                                        if (subCategoryArray.size() > 0) {
                                            for (int j = 0; j < subCategoryArray.size(); j++) {
                                                subcategoriesBean = new CategoryListBean.SubcategoriesBean();
                                                JsonObject subCategoryObj = subCategoryArray.get(j).getAsJsonObject();
                                                JsonElement subCatIdElement = subCategoryObj.get("subcatId");
                                                JsonElement subcategoryImageElement = subCategoryObj.get("subcategoryImage");
                                                JsonElement subcat_nameElement = subCategoryObj.get("subcat_name");
                                                subcategoriesBean.setCatId(returnEmptyString(subCatIdElement));
                                                subcategoriesBean.setSubcategoryImage(returnEmptyString(subcategoryImageElement));
                                                subcategoriesBean.setSubcat_name(returnEmptyString(subcat_nameElement));
                                                subcategoreisList.add(subcategoriesBean);
                                                subcategoreisList.add(subcategoriesBean);
                                            }
                                            categoryListBean.setSubcategoriesList(subcategoreisList);
                                        } else {
                                            categoryListBean.setSubcategoriesList(subcategoreisList);
                                        }
                                        categoryListBean.setCategoryList(categoryList);
                                    }

                                    categoryListBean.setCategoryList(categoryList);
                                    listener.onSuccess(REQUEST_TYPE_BROWSE_CATEGORY, true, message, categoryListBean);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForPostCategoryList(final Context context, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_BROWSE_CATEGORY)
                .setTimeout(60 * 1000)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resultArray = resulObj.get("response").getAsJsonArray();
                                    CategoryListBean categoryListBean = null;
                                    ArrayList<CategoryListBean> categoryList = new ArrayList<CategoryListBean>();
                                    for (int i = 0; i < resultArray.size(); i++) {
                                        categoryListBean = new CategoryListBean();
                                        JsonObject categoryObject = resultArray.get(i).getAsJsonObject();
                                        JsonElement catIdElement = categoryObject.get("catId");
                                        JsonElement catNameElement = categoryObject.get("CatName");
                                        categoryListBean.setCatId(returnEmptyString(catIdElement));
                                        categoryListBean.setCatName(returnEmptyString(catNameElement));
                                        categoryList.add(categoryListBean);
                                        JsonElement subcategoriesElement = categoryObject.get("subcategories");
                                        JsonArray subCategoryArray = subcategoriesElement.getAsJsonArray();
                                        CategoryListBean.SubcategoriesBean subcategoriesBean = null;
                                        ArrayList<CategoryListBean.SubcategoriesBean> subcategoreisList = new ArrayList<CategoryListBean.SubcategoriesBean>();
                                        if (subCategoryArray.size() > 0) {
                                            for (int j = 0; j < subCategoryArray.size(); j++) {
                                                subcategoriesBean = new CategoryListBean.SubcategoriesBean();
                                                JsonObject subCategoryObj = subCategoryArray.get(j).getAsJsonObject();
                                                JsonElement subCatIdElement = subCategoryObj.get("subcatId");
                                                JsonElement subcategoryImageElement = subCategoryObj.get("subcategoryImage");
                                                JsonElement subcat_nameElement = subCategoryObj.get("subcat_name");
                                                subcategoriesBean.setCatId(returnEmptyString(subCatIdElement));
                                                subcategoriesBean.setSubcategoryImage(returnEmptyString(subcategoryImageElement));
                                                subcategoriesBean.setSubcat_name(returnEmptyString(subcat_nameElement));
                                                subcategoreisList.add(subcategoriesBean);

                                            }
                                        }
                                        categoryListBean.setSubcategoriesList(subcategoreisList);
                                    }

                                    categoryListBean.setCategoryList(categoryList);
                                    listener.onSuccess(REQUEST_TYPE_BROWSE_CATEGORY, true, message, categoryListBean);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForSubCategory(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_BROWSE_SUB_CATEGORY)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resultArray = resulObj.get("response").getAsJsonArray();
                                    ArrayList<SubCategoryBean> subCategoryList = new ArrayList<SubCategoryBean>();
                                    SubCategoryBean bean1 = new SubCategoryBean();
                                    bean1.setName("All");
                                    bean1.setId("0");
                                    bean1.setImage("dummy.png");
                                    subCategoryList.add(0, bean1);
                                    SubCategoryBean bean = null;
                                    if (resultArray.size() > 0) {
                                        for (int i = 0; i < resultArray.size(); i++) {
                                            bean = new SubCategoryBean();
                                            JsonObject object = resultArray.get(i).getAsJsonObject();
                                            JsonElement catIdElement = object.get("subcatId");
                                            JsonElement catElement = object.get("subcat_name");
                                            JsonElement imageElement = object.get("subcategoryImage");
                                            String catName = returnEmptyString(catElement);
                                            String catid = returnEmptyString(catIdElement);
                                            String image = returnEmptyString(imageElement);
                                            bean.setId(catid);
                                            bean.setName(catName);
                                            bean.setImage(image);
                                            subCategoryList.add(bean);

                                        }
                                        listener.onSuccess(REQUEST_TYPE_BROWSE_SUB_CATEGORY, true, message, subCategoryList);
                                    } else {
                                        listener.onSuccess(REQUEST_TYPE_BROWSE_SUB_CATEGORY, true, message, subCategoryList);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void setRequestForForgotPassword(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_FORGOT_PASSWORD)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    int status = resulObj.get("status").getAsInt();
                                    if (status == 1) {
                                        listener.onSuccess(REQUEST_TYPE_SET_NEW_PASSWORD, true, message, message);
                                    } else {
                                        listener.onError(message);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForAddRecipeToFav(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_ADD_RECIPE_TO_FAV)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    int status = resulObj.get("status").getAsInt();
                                    if (status == 1) {
                                        listener.onSuccess(REQUEST_TYPE_ADD_RECIPE_TO_FAV, true, message, message);
                                    } else {
                                        listener.onError(message);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForVerifyKey(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_VERIFY_KEY)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    int status = resulObj.get("status").getAsInt();
                                    if (status == 1) {
                                        listener.onSuccess(REQUEST_TYPE_VERIFY_KEY, true, message, message);
                                    } else {
                                        listener.onError(message);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForSetNewPassword(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_SET_NEW_PASSWORD)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    int status = resulObj.get("status").getAsInt();
                                    if (status == 1) {
                                        listener.onSuccess(REQUEST_TYPE_SET_NEW_PASSWORD, true, message, message);
                                    } else {
                                        listener.onError(message);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForGetRecipes(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_BROWSE_RECIPE)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    JsonElement messageElement = resulObj.get("message");
                                    String message = returnEmptyString(messageElement);
                                    JsonArray resultArray = resulObj.get("response").getAsJsonArray();
                                    ArrayList<RecipesBean> recipesList = new ArrayList<RecipesBean>();
                                    if (resultArray.size() > 0) {
                                        RecipesBean bean = null;
                                        for (int i = 0; i < resultArray.size(); i++) {
                                            bean = new RecipesBean();
                                            JsonObject object = resultArray.get(i).getAsJsonObject();
                                            JsonElement recipeIdElement = object.get("id");
                                            JsonElement recipePhotoElement = object.get("photo");
                                            JsonElement recipeTitleElement = object.get("title");
                                            JsonElement posterIdElement = object.get("UserId");
                                            JsonElement likeElement = object.get("like");
                                            JsonElement publice = object.get("PublicPrivate");
                                            JsonElement desc = object.get("description");
                                            JsonElement fname = object.get("firstName");
                                            JsonElement lname = object.get("lastName");
                                            JsonElement userPhoto = object.get("userPhoto");
                                            JsonElement rating = object.get("recipeRating");
                                            JsonElement occupation = object.get("occupation");
                                            bean.setId(Integer.parseInt(returnEmptyString(recipeIdElement)));
                                            bean.setPhoto(returnEmptyString(recipePhotoElement));
                                            bean.setTitle(returnEmptyString(recipeTitleElement));
                                            bean.setLike(Integer.parseInt(returnEmptyString(likeElement)));
                                            bean.setUserId(returnEmptyString(posterIdElement));
                                            bean.setPublicPrivate(returnEmptyString(publice));
                                            bean.setDescription(returnEmptyString(desc));
                                            bean.setFirstName(returnEmptyString(fname));
                                            bean.setLastName(returnEmptyString(lname));
                                            bean.setUserPhoto(returnEmptyString(userPhoto));
                                            bean.setRecipeRating(Integer.parseInt(returnEmptyString(rating)));
                                            bean.setOccupation(returnEmptyString(occupation));
                                            recipesList.add(bean);

                                        }

                                        listener.onSuccess(REQUEST_TYPE_BROWSE_RECIPE, true, message, recipesList);
                                    } else {
                                        listener.onSuccess(REQUEST_TYPE_BROWSE_RECIPE, true, message, recipesList);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForIngridientsList(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUESET_FOR_SEARCH_INGREDIENTS)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resultArray = resulObj.get("response").getAsJsonArray();
                                    if (resultArray.size() > 0) {
                                        ArrayList<IngredientsListForSearch> ingredientList = new ArrayList<IngredientsListForSearch>();
                                        IngredientsListForSearch bean = null;
                                        for (int i = 0; i < resultArray.size(); i++) {
                                            bean = new IngredientsListForSearch();
                                            JsonObject object = resultArray.get(i).getAsJsonObject();
                                            JsonElement idElememnt = object.get("id");
                                            JsonElement nameElement = object.get("name");
                                            JsonElement photoElement = object.get("photo");
                                            bean.setId(returnEmptyString(idElememnt));
                                            bean.setName(returnEmptyString(nameElement));
                                            bean.setPhoto(returnEmptyString(photoElement));
                                            bean.setQty("1");
                                            bean.setUnit("");
                                            ingredientList.add(bean);
                                        }
                                        bean.setSearchList(ingredientList);
                                        listener.onSuccess(REQUESET_TYPE_SEARCH_INGREDIENTS, true, message, bean);
                                    } else {
                                        IngredientsListForSearch bean = new IngredientsListForSearch();
                                        bean.setSearchList(new ArrayList<IngredientsListForSearch>());
                                        listener.onSuccess(REQUESET_TYPE_SEARCH_INGREDIENTS, true, message, bean);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForCreateCategory(final Context context, final String userCatTitle, final File fileName, final OnNetworkCallBack listener) {

        JSONObject object = new JSONObject();
        try {
            String userId = new SharedPreference(context).getString(Constants.USER_USR_ID, "");
            object.put("UserId", userId);
            object.put("UserCatTitle", userCatTitle);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonObject object1 = (JsonObject) parser.parse(object.toString());
//        JsonObject obj = new JsonObject();
//        obj.addProperty("UserId", "15");
//        obj.addProperty("UserId", "15");
        Ion.with(context)
                .load(REQUEST_FOR_CREATE_CATEGORY)
                .setTimeout(60 * 1000)
                .setMultipartFile("UserCatPic", fileName)
                .setMultipartParameter("json", object1.toString())
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_CREATE_CATEGORY, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForCreateRecipe(final Context context, final JsonObject jsonObject, final File fileName, final OnNetworkCallBack listener) {

        Ion.with(context)
                .load(REQUEST_FOR_CREATE_RECIPE)
                .setTimeout(60 * 1000)
                .setMultipartFile("photo", fileName)
                .setMultipartParameter("json", jsonObject.toString())
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_CREATE_RECIPE, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForcreateGroup(final Context context, final JsonObject jsonObject, final File fileName, final OnNetworkCallBack listener) {
        if (fileName != null) {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_GROUP)
                    .setTimeout(60 * 1000)
                    .setMultipartFile("photo", fileName)
                    .setMultipartParameter("json", jsonObject.toString())
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e == null) {
                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject resulObj = result.getResult();
                                        JsonArray resultArray = resulObj.get("result").getAsJsonArray();
                                        String message = resulObj.get("message").getAsString();
                                        if (resultArray.size() > 0) {
                                            GroupCreateDetailsBean groupCreateDetailsBean = new GroupCreateDetailsBean();
                                            GroupCreateDetailsBean.GroupDetailsBean groupDetailsBean = null;
                                            ArrayList<GroupCreateDetailsBean.GroupDetailsBean> groupDetailsBeanArrayList =
                                                    new ArrayList<GroupCreateDetailsBean.GroupDetailsBean>();
                                            for (int i = 0; i < resultArray.size(); i++) {
                                                groupDetailsBean = new GroupCreateDetailsBean.GroupDetailsBean();
                                                JsonObject object = resultArray.get(i).getAsJsonObject();
                                                JsonObject groupDetailsObject = object.getAsJsonObject("groupDetails");
                                                JsonElement groupNameElement = groupDetailsObject.get("groupName");
                                                JsonElement groupPhotoElement = groupDetailsObject.get("groupPhoto");
                                                JsonElement groupIdElement = groupDetailsObject.get("groupId");

                                                groupDetailsBean.setGroupName(returnEmptyString(groupNameElement));
                                                groupDetailsBean.setGroupPhoto(returnEmptyString(groupPhotoElement));
                                                groupDetailsBean.setGroupId(returnEmptyString(groupIdElement));

                                                JsonArray memerDetailsJsonArray = object.get("memerDetails").getAsJsonArray();
                                                if (memerDetailsJsonArray.size() > 0 ) {
                                                    GroupCreateDetailsBean.MemerDetailsBean memerDetailsBean = null;
                                                    ArrayList<GroupCreateDetailsBean.MemerDetailsBean> memerDetailsBeanArrayList =
                                                            new ArrayList<GroupCreateDetailsBean.MemerDetailsBean>();
                                                    for (int j = 0; j < memerDetailsJsonArray.size(); j++) {
                                                        memerDetailsBean = new GroupCreateDetailsBean.MemerDetailsBean();
                                                        JsonObject object1 = memerDetailsJsonArray.get(j).getAsJsonObject();
                                                        JsonElement firstNameElement = object1.get("firstName");
                                                        JsonElement lastNameElement = object1.get("lastName");
                                                        JsonElement photoElement = object1.get("photo");
                                                        JsonElement memberIdElement = object1.get("memberId");

                                                        memerDetailsBean.setFirstName(returnEmptyString(firstNameElement));
                                                        memerDetailsBean.setLastName(returnEmptyString(lastNameElement));
                                                        memerDetailsBean.setPhoto(returnEmptyString(photoElement));
                                                        memerDetailsBean.setMemberId(returnEmptyString(memberIdElement));
                                                        memerDetailsBeanArrayList.add(memerDetailsBean);
                                                    }
                                                    groupCreateDetailsBean.setMemerDetails(memerDetailsBeanArrayList);
                                                }
                                            } groupCreateDetailsBean.setGroupDetails(groupDetailsBean);

                                            listener.onSuccess(REQUEST_TYPE_CREATE_GROUP, true, message, groupCreateDetailsBean);
                                        }
                                        break;
                                    case 201:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 204:
                                        listener.onError("Network Problem");
                                        break;
                                    case 400:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 401:
                                        listener.onError("Network Problem");
                                        break;
                                    default:
                                        listener.onError("Network Problem");
                                        break;

                                }
                            } else {
                                listener.onError("Network Problem");
                            }
                        }
                    });
        } else {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_GROUP)
                    .setTimeout(60 * 1000)
                    .setMultipartParameter("json", jsonObject.toString())
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e == null) {
                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject resulObj = result.getResult();
                                        JsonArray resultArray = resulObj.get("result").getAsJsonArray();
                                        String message = resulObj.get("message").getAsString();
                                        if (resultArray.size() > 0) {
                                            GroupCreateDetailsBean groupCreateDetailsBean = new GroupCreateDetailsBean();
                                            GroupCreateDetailsBean.GroupDetailsBean groupDetailsBean = null;
                                            ArrayList<GroupCreateDetailsBean.GroupDetailsBean> groupDetailsBeanArrayList =
                                                    new ArrayList<GroupCreateDetailsBean.GroupDetailsBean>();
                                            for (int i = 0; i < resultArray.size(); i++) {
                                                groupDetailsBean = new GroupCreateDetailsBean.GroupDetailsBean();
                                                JsonObject object = resultArray.get(i).getAsJsonObject();
                                                JsonObject groupDetailsObject = object.getAsJsonObject("groupDetails");
                                                JsonElement groupNameElement = groupDetailsObject.get("groupName");
                                                JsonElement groupPhotoElement = groupDetailsObject.get("groupPhoto");
                                                JsonElement groupIdElement = groupDetailsObject.get("groupId");

                                                groupDetailsBean.setGroupName(returnEmptyString(groupNameElement));
                                                groupDetailsBean.setGroupPhoto(returnEmptyString(groupPhotoElement));
                                                groupDetailsBean.setGroupId(returnEmptyString(groupIdElement));

                                                JsonArray memerDetailsJsonArray = object.get("memerDetails").getAsJsonArray();
                                                if (memerDetailsJsonArray.size() > 0 ) {
                                                    GroupCreateDetailsBean.MemerDetailsBean memerDetailsBean = null;
                                                    ArrayList<GroupCreateDetailsBean.MemerDetailsBean> memerDetailsBeanArrayList =
                                                            new ArrayList<GroupCreateDetailsBean.MemerDetailsBean>();
                                                    for (int j = 0; j < memerDetailsJsonArray.size(); j++) {
                                                        memerDetailsBean = new GroupCreateDetailsBean.MemerDetailsBean();
                                                        JsonObject object1 = memerDetailsJsonArray.get(j).getAsJsonObject();
                                                        JsonElement firstNameElement = object1.get("firstName");
                                                        JsonElement lastNameElement = object1.get("lastName");
                                                        JsonElement photoElement = object1.get("photo");
                                                        JsonElement memberIdElement = object1.get("memberId");

                                                        memerDetailsBean.setFirstName(returnEmptyString(firstNameElement));
                                                        memerDetailsBean.setLastName(returnEmptyString(lastNameElement));
                                                        memerDetailsBean.setPhoto(returnEmptyString(photoElement));
                                                        memerDetailsBean.setMemberId(returnEmptyString(memberIdElement));
                                                        memerDetailsBeanArrayList.add(memerDetailsBean);
                                                    }
                                                    groupCreateDetailsBean.setMemerDetails(memerDetailsBeanArrayList);
                                                }
                                            } groupCreateDetailsBean.setGroupDetails(groupDetailsBean);

                                            listener.onSuccess(REQUEST_TYPE_CREATE_GROUP, true, message, groupCreateDetailsBean);
                                        }
                                        break;
                                    case 201:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 204:
                                        listener.onError("Network Problem");
                                        break;
                                    case 400:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 401:
                                        listener.onError("Network Problem");
                                        break;
                                    default:
                                        listener.onError("Network Problem");
                                        break;

                                }
                            } else {
                                listener.onError("Network Problem");
                            }
                        }
                    });
        }
    }


    public static void requestForMyRecipes(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_MY_RECIPES)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray myRecipesArray = resulObj.get("response").getAsJsonArray();
                                    MyRecipesBean bean = null;
                                    MyRecipesBean bean1 = new MyRecipesBean();

                                    ArrayList<MyRecipesBean> myRecipesList = new ArrayList<MyRecipesBean>();
                                    bean1.setCatId("-1");
                                    myRecipesList.add(0, bean1);
                                    if (myRecipesArray.size() > 0) {
                                        for (int i = 0; i < myRecipesArray.size(); i++) {
                                            bean = new MyRecipesBean();
                                            JsonObject object = myRecipesArray.get(i).getAsJsonObject();
                                            JsonElement catIdElement = object.get("catId");
                                            JsonElement catTitleElement = object.get("UserCatTitle");
                                            JsonElement catImageElement = object.get("UserCatPic");
                                            bean.setCatId(returnEmptyString(catIdElement));
                                            bean.setUserCatTitle(returnEmptyString(catTitleElement));
                                            bean.setUserCatPic(returnEmptyString(catImageElement));
                                            myRecipesList.add(bean);
                                        }
                                        bean.setRecipesList(myRecipesList);
                                        listener.onSuccess(REQUEST_TYPE_MY_RECIPES, true, message, bean);
                                    } else {
                                        bean1.setRecipesList(myRecipesList);
                                        listener.onSuccess(REQUEST_TYPE_MY_RECIPES, true, message, bean1);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForMyRecipesCategory(final Context context, final JsonObject jsonObject, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_MY_CATEGORY_RECIPE)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray myRecipesArray = resulObj.get("result").getAsJsonArray();
                                    MyCategoryRecipeBean bean = new MyCategoryRecipeBean();
                                    ArrayList<MyCategoryRecipeBean> arrayList = new ArrayList<MyCategoryRecipeBean>();
                                    if (myRecipesArray.size() > 0) {
                                        for (int i = 0; i < myRecipesArray.size(); i++) {
                                            JsonObject object = myRecipesArray.get(i).getAsJsonObject();
                                            JsonElement recipeidElement = object.get("recipeId");
                                            JsonElement recipeTitleElement = object.get("title");
                                            JsonElement recipePhotoElement = object.get("photo");
                                            JsonElement commentElement = object.get("commentCount");
                                            JsonElement ratingElement = object.get("avgRecipeRating");
                                            bean.setRecipeId(Integer.parseInt(returnEmptyString(recipeidElement)));
                                            bean.setPhoto(returnEmptyString(recipePhotoElement));
                                            bean.setTitle(returnEmptyString(recipeTitleElement));
                                            bean.setCommentCount(Integer.parseInt(returnEmptyString(commentElement)));
                                            bean.setRecipeRating(Double.parseDouble(returnEmptyString(ratingElement)));
                                            arrayList.add(bean);
                                        }
                                        bean.setList(arrayList);
                                        listener.onSuccess(REQUEST_TYPE_MY_CATEGORY_RECIPE, true, message, bean);
                                    } else {
                                        listener.onError("No Recipes Found For this Category");
                                    }
                                    listener.onSuccess(REQUEST_TYPE_MY_CATEGORY_RECIPE, true, message, bean);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForDeleteRecipe(final Context context, final JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_DELETE_RECIPE)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_DELETE_RECIPE, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForCreateIngredients(Context context, JsonObject object, final OnNetworkCallBack listener) {

        Ion.with(context)
                .load(REQUESET_FOR_CREATE_INGREDIENTS)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    break;
                                case 201:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonObject responseObject = resulObj.get("response").getAsJsonObject();
                                    JsonElement idElement = responseObject.get("id");
                                    JsonElement photoElement = responseObject.get("photo");
                                    JsonElement nameElement = responseObject.get("name");
                                    String name = returnEmptyString(nameElement);
                                    String id = returnEmptyString(idElement);
                                    String photo = returnEmptyString(photoElement);
                                    ArrayList<IngredientsListForSearch> ingredientList = new ArrayList<IngredientsListForSearch>();
                                    IngredientsListForSearch bean = new IngredientsListForSearch();
                                    bean.setId(id);
                                    bean.setName(name);
                                    bean.setPhoto(photo);
                                    bean.setQty("1");
                                    bean.setUnit("");
                                    ingredientList.add(bean);
                                    bean.setSearchList(ingredientList);
                                    listener.onSuccess(REQUESET_TYPE_CREATE_INGREDIENTS, true, message, bean);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void getRecipeDetail(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_RECIPE_DETAIL)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonObject responseObj = resulObj.get("response").getAsJsonObject();
                                    RecipeDetailBean bean = new RecipeDetailBean();
                                    JsonElement title = responseObj.get("title");
                                    JsonElement recipePhoto = responseObj.get("recipePhoto");
                                    JsonElement description = responseObj.get("description");
                                    JsonElement youtubeLink = responseObj.get("youtubeLink");
                                    JsonElement userId = responseObj.get("userId");
                                    JsonElement like = responseObj.get("like");
                                    JsonElement commentCount = responseObj.get("commentCount");
                                    JsonElement avgRecipeRating = responseObj.get("avgRecipeRating");
                                    JsonElement usersRecipeRating = responseObj.get("usersRecipeRating");

                                    bean.setTitle(returnEmptyString(title));
                                    bean.setRecipePhoto(returnEmptyString(recipePhoto));
                                    bean.setDescription(returnEmptyString(description));
                                    bean.setYoutubeLink(returnEmptyString(youtubeLink));
                                    bean.setUserId(returnEmptyString(userId));
                                    bean.setLike(Integer.parseInt(returnEmptyString(like)));
                                    bean.setCommentCount(Integer.parseInt(returnEmptyString(commentCount)));
                                    bean.setAvgRecipeRating(Float.parseFloat(returnEmptyString(avgRecipeRating)));
                                    bean.setUsersRecipeRating(Float.parseFloat(returnEmptyString(usersRecipeRating)));
                                    JsonArray ingredientArray = responseObj.get("ingredients").getAsJsonArray();
                                    ArrayList<RecipeDetailBean.IngredientsBean> ingList = new ArrayList<RecipeDetailBean.IngredientsBean>();
                                    if (ingredientArray.size() > 0) {
                                        for (int i = 0; i < ingredientArray.size(); i++) {
                                            JsonObject ingObj = ingredientArray.get(i).getAsJsonObject();
                                            RecipeDetailBean.IngredientsBean ingBean = new RecipeDetailBean.IngredientsBean();
                                            JsonElement ingId = ingObj.get("ingredientId");
                                            JsonElement qty = ingObj.get("qty");
                                            JsonElement unit = ingObj.get("unit");
                                            JsonElement name = ingObj.get("name");
                                            ingBean.setIngredientId(returnEmptyString(ingId));
                                            ingBean.setQty(returnEmptyString(qty));
                                            ingBean.setUnit(returnEmptyString(unit));
                                            ingBean.setName(returnEmptyString(name));
                                            ingList.add(ingBean);
                                        }

                                        bean.setIngredients(ingList);
                                    }
                                    JsonArray instructionsArray = responseObj.get("instructions").getAsJsonArray();
                                    ArrayList<String> instructionsList = new ArrayList<String>();
                                    if (instructionsArray.size() > 0) {
                                        for (int j = 0; j < instructionsArray.size(); j++) {
                                            JsonElement insElement = instructionsArray.get(j);
                                            String instruction = (j + 1) + ". " + returnEmptyString(insElement);
                                            instructionsList.add(instruction);
                                        }
                                        bean.setInstructions(instructionsList);
                                    }

                                   /* JsonArray commentsArray = responseObj.get("comments").getAsJsonArray();
                                    ArrayList<RecipeDetailBean.Comments> commentList = new ArrayList<>();
                                    if (commentsArray.size() > 0) {
                                        for (int k = 0; k < commentsArray.size(); k++) {
                                            JsonObject comments = commentsArray.get(k).getAsJsonObject();
                                            RecipeDetailBean.Comments commentBean = new RecipeDetailBean.Comments();
                                            JsonElement photo = comments.get("photo");
                                            JsonElement commentText = comments.get("commentText");
                                            JsonElement id = comments.get("userId");
                                            commentBean.setPhoto(returnEmptyString(photo));
                                            commentBean.setCommentText(returnEmptyString(commentText));
                                            commentList.add(commentBean);
                                        }
                                        bean.setCommentList(commentList);
                                    }*/
                                    listener.onSuccess(REQUEST_TYPE_RECIPE_DETAIL, true, message, bean);
                                    break;
                                case 201:

                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForAddGrocerries(Context context, JsonObject object, final OnNetworkCallBack listener) {

        Ion.with(context)
                .load(REQUEST_FOR_ADD_GROCERRIES)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    break;
                                case 201:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_ADD_GROCERRIES, true, message, message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForComment(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_COMMENT)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                                 @Override
                                 public void onCompleted(Exception e, Response<JsonObject> result) {
                                     if (e == null) {
                                         switch (result.getHeaders().code()) {
                                             case 200:
                                                 JsonObject resulObj = result.getResult();
                                                 String message = resulObj.get("message").getAsString();
                                                 listener.onSuccess(REQUEST_TYPE_COMMENT, true, message, message);
                                                 break;
                                             case 201:
                                                 listener.onError("Network Problem");
                                                 break;
                                             case 204:
                                                 listener.onError("Network Problem");
                                                 break;
                                             case 400:
                                                 resulObj = result.getResult();
                                                 message = resulObj.get("message").getAsString();
                                                 listener.onError(message);
                                                 break;
                                             case 401:
                                                 listener.onError("Network Problem");
                                                 break;
                                             default:
                                                 listener.onError("Network Problem");
                                                 break;

                                         }
                                     } else

                                     {
                                         listener.onError("Network Problem");
                                     }
                                 }
                             }

                );

    }


    public static void requestForAddFriend(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_ADD_TO_FRIEND_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_ADD_TO_FRIEND_LIST, true, message, message);
                                    break;
                                case 201:
                                    listener.onError("Network Problem");
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForFriendSuggestions(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_FRIEND_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray responseArray = resulObj.get("response").getAsJsonArray();
                                    if (responseArray.size() > 0) {
                                        ArrayList<FriendListBean> friendList = new ArrayList<FriendListBean>();
                                        for (int i = 0; i < responseArray.size(); i++) {
                                            FriendListBean bean = new FriendListBean();
                                            JsonObject obj = responseArray.get(i).getAsJsonObject();
                                            JsonElement firstName = obj.get("firstName");
                                            JsonElement lastName = obj.get("lastName");
                                            JsonElement photo = obj.get("photo");
                                            JsonElement id = obj.get("id");

                                            bean.setId(returnEmptyString(id));
                                            bean.setPhoto(returnEmptyString(photo));
                                            bean.setFirstName(returnEmptyString(firstName));
                                            bean.setLastName(returnEmptyString(lastName));

                                            friendList.add(bean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_FRIEND_LIST, true, message, friendList);

                                    } else {
                                        listener.onError("No Friends Suggestion Found For You");
                                    }
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForFriendList(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_USER_FRIEND_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray responseArray = resulObj.get("response").getAsJsonArray();
                                    if (responseArray.size() > 0) {
                                        ArrayList<FriendListBean> friendList = new ArrayList<FriendListBean>();
                                        for (int i = 0; i < responseArray.size(); i++) {
                                            FriendListBean bean = new FriendListBean();
                                            JsonObject obj = responseArray.get(i).getAsJsonObject();
                                            JsonElement firstName = obj.get("firstName");
                                            JsonElement lastName = obj.get("lastName");
                                            JsonElement photo = obj.get("userPhoto");
                                            JsonElement id = obj.get("userId");
                                            if (returnEmptyString(id).isEmpty()) {
                                                bean.setId("0");
                                            } else {
                                                bean.setId(returnEmptyString(id));
                                            }
                                            bean.setPhoto(returnEmptyString(photo));
                                            bean.setFirstName(returnEmptyString(firstName));
                                            bean.setLastName(returnEmptyString(lastName));
                                            bean.setFriend(false);

                                            friendList.add(bean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_USER_FRIEND_LIST, true, message, friendList);

                                    } else {
                                        listener.onError("No Friends Suggestion Found For You");
                                    }
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForGetGroup(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_GROUPS)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray responseArray = resulObj.get("response").getAsJsonArray();
                                    if (responseArray.size() > 0) {
                                        ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
                                        for (int i = 0; i < responseArray.size(); i++) {
                                            GroupBean bean = new GroupBean();
                                            JsonObject obj = responseArray.get(i).getAsJsonObject();
                                            JsonElement name = obj.get("GroupName");
                                            JsonElement photo = obj.get("groupPhoto");
                                            JsonElement id = obj.get("id");
                                            bean.setId(Integer.parseInt(returnEmptyString(id)));
                                            bean.setGroupPhoto(returnEmptyString(photo));
                                            bean.setGroupName(returnEmptyString(name));
                                            groupList.add(bean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_GET_GROUP, true, message, groupList);

                                    } else {
                                        listener.onError("No Friends Suggestion Found For You");
                                    }
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForRating(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(RATE_THE_RECIPE_MEAL_LESSON)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_RATE_THE_RECIPE, true, message, message);
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }


    public static void requestForDeleteGroup(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_THE_DELETE_GROUP)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_THE_DELETE_GROUP, true, message, message);
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }


    public static void requestForDeleteFriend(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEEST_FOR_DELETE_FRIEND)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEEST_TYPE_DELETE_FRIEND, true, message, message);
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }


    public static void setRequestForMealList(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_MEAL_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resopnseArray = resulObj.get("response").getAsJsonArray();
                                    ArrayList<MealListBean> mealList = new ArrayList<MealListBean>();
                                    if (resopnseArray.size() > 0) {
                                        for (int i = 0; i < resopnseArray.size(); i++) {
                                            JsonObject obj = resopnseArray.get(i).getAsJsonObject();
                                            JsonElement mealId = obj.get("mealId");
                                            JsonElement photo = obj.get("photo");
                                            JsonElement title = obj.get("title");
                                            JsonElement price = obj.get("price");
                                            JsonElement latitude = obj.get("latitude");
                                            JsonElement longitude = obj.get("longitude");
                                            JsonElement userphoto = obj.get("userphoto");
                                            JsonElement commentCount = obj.get("commentCount");
                                            JsonElement avgMealRating = obj.get("avgMealRating");
                                            JsonElement userMealRating = obj.get("userMealRating");
                                            JsonElement address = obj.get("address");
                                            JsonElement like = obj.get("like");
                                            MealListBean bean = new MealListBean();
                                            bean.setMealId(returnEmptyString(mealId));
                                            bean.setPhoto(returnEmptyString(photo));
                                            bean.setPrice(returnEmptyString(price));
                                            bean.setTitle(returnEmptyString(title));
                                            bean.setLatitude(returnEmptyString(latitude));
                                            bean.setLongitude(returnEmptyString(longitude));
                                            bean.setUserphoto(returnEmptyString(userphoto));
                                            bean.setCommentCount(Integer.parseInt(returnEmptyString(commentCount)));
                                            bean.setAvgMealRating(returnEmptyString(avgMealRating));
                                            bean.setUserMealRating(returnEmptyString(userMealRating));
                                            bean.setAddress(returnEmptyString(address));
                                            bean.setLike(Integer.parseInt(returnEmptyString(like)));
                                            mealList.add(bean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_MEAL_LIST, true, message, mealList);
                                    } else {
                                        listener.onSuccess(REQUEST_TYPE_MEAL_LIST, true, message, mealList);
                                    }
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }


    public static void setRequestForLessonList(Context context, JsonObject object, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_LESSON_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(object)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resopnseArray = resulObj.get("response").getAsJsonArray();
                                    ArrayList<MealListBean> mealList = new ArrayList<MealListBean>();
                                    if (resopnseArray.size() > 0) {
                                        for (int i = 0; i < resopnseArray.size(); i++) {
                                            JsonObject obj = resopnseArray.get(i).getAsJsonObject();
                                            JsonElement mealId = obj.get("lessonId");
                                            JsonElement photo = obj.get("photo");
                                            JsonElement title = obj.get("title");
                                            JsonElement price = obj.get("price");
                                            JsonElement latitude = obj.get("latitude");
                                            JsonElement longitude = obj.get("longitude");
                                            JsonElement userphoto = obj.get("userphoto");
                                            JsonElement commentCount = obj.get("commentCount");
                                            JsonElement avgMealRating = obj.get("avgLessonRating");
                                            JsonElement userMealRating = obj.get("userLessonRating");
                                            JsonElement address = obj.get("address");
                                            JsonElement like = obj.get("like");
                                            MealListBean bean = new MealListBean();
                                            bean.setMealId(returnEmptyString(mealId));
                                            bean.setPhoto(returnEmptyString(photo));
                                            bean.setPrice(returnEmptyString(price));
                                            bean.setTitle(returnEmptyString(title));
                                            bean.setLatitude(returnEmptyString(latitude));
                                            bean.setLongitude(returnEmptyString(longitude));
                                            bean.setUserphoto(returnEmptyString(userphoto));
                                            bean.setCommentCount(Integer.parseInt(returnEmptyString(commentCount)));
                                            bean.setAvgMealRating(returnEmptyString(avgMealRating));
                                            bean.setUserMealRating(returnEmptyString(userMealRating));
                                            bean.setAddress(returnEmptyString(address));
                                            bean.setLike(Integer.parseInt(returnEmptyString(like)));
                                            mealList.add(bean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_LESSON_LIST, true, message, mealList);
                                    } else {
                                        listener.onSuccess(REQUEST_TYPE_LESSON_LIST, true, message, mealList);
                                    }
                                    break;
                                case 201:
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            Log.e("Error :", String.valueOf(e));
                            listener.onError("Network Problem");
                        }
                    }
                });

    }


    public static void requestForCreateMeal(final Context context, File imgFile, JsonObject obj, final OnNetworkCallBack listener) {
        if (imgFile != null) {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_MEAL)
                    .setTimeout(60 * 1000)
                    .setMultipartFile("photo", imgFile)
                    .setMultipartParameter("json", obj.toString())
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e == null) {
                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject resulObj = result.getResult();
                                        String message = resulObj.get("message").getAsString();
                                        listener.onSuccess(REQUEST_TYPE_CREATE_MEAL, true, message, resulObj);
                                        break;
                                    case 201:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 204:
                                        listener.onError("Network Problem");
                                        break;
                                    case 400:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 401:
                                        listener.onError("Network Problem");
                                        break;
                                    default:
                                        listener.onError("Network Problem");
                                        break;

                                }
                            } else {
                                listener.onError("Network Problem");
                            }
                        }
                    });
        } else {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_MEAL)
                    .setTimeout(60 * 1000)
                    .setHeader("accessToken", new SharedPreference(context).getString(Constants.ACCESSTOKEN, ""))
                    .setMultipartParameter("photo", null)
                    .setMultipartParameter("json", obj.toString())
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e == null) {
                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject resulObj = result.getResult();
                                        String message = resulObj.get("message").getAsString();
                                        listener.onSuccess(REQUEST_TYPE_CREATE_MEAL, true, message, resulObj);
                                        break;
                                    case 201:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 204:
                                        listener.onError("Network Problem");
                                        break;
                                    case 400:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 401:
                                        listener.onError("Network Problem");
                                        break;
                                    default:
                                        listener.onError("Network Problem");
                                        break;

                                }
                            } else {
                                listener.onError("Network Problem");
                            }
                        }
                    });
        }
    }


    public static void requestForCreateLesson(final Context context, File imgFile, JsonObject obj, final OnNetworkCallBack listener) {
        if (imgFile != null) {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_LESSON)
                    .setTimeout(60 * 1000)
                    .setMultipartFile("photo", imgFile)
                    .setMultipartParameter("json", obj.toString())
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e == null) {
                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject resulObj = result.getResult();
                                        String message = resulObj.get("message").getAsString();
                                        listener.onSuccess(REQUEST_TYPE_CREATE_LESSON, true, message, resulObj);
                                        break;
                                    case 201:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 204:
                                        listener.onError("Network Problem");
                                        break;
                                    case 400:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 401:
                                        listener.onError("Network Problem");
                                        break;
                                    default:
                                        listener.onError("Network Problem");
                                        break;

                                }
                            } else {
                                listener.onError("Network Problem");
                            }
                        }
                    });
        } else {
            Ion.with(context)
                    .load(REQUEST_FOR_CREATE_LESSON)
                    .setTimeout(60 * 1000)
                    .setHeader("accessToken", new SharedPreference(context).getString(Constants.ACCESSTOKEN, ""))
                    .setMultipartParameter("photo", null)
                    .setMultipartParameter("json", obj.toString())
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if (e == null) {
                                switch (result.getHeaders().code()) {
                                    case 200:
                                        JsonObject resulObj = result.getResult();
                                        String message = resulObj.get("message").getAsString();
                                        listener.onSuccess(REQUEST_TYPE_CREATE_LESSON, true, message, resulObj);
                                        break;
                                    case 201:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 204:
                                        listener.onError("Network Problem");
                                        break;
                                    case 400:
                                        resulObj = result.getResult();
                                        message = resulObj.get("message").getAsString();
                                        listener.onError(message);
                                        break;
                                    case 401:
                                        listener.onError("Network Problem");
                                        break;
                                    default:
                                        listener.onError("Network Problem");
                                        break;

                                }
                            } else {
                                listener.onError("Network Problem");
                            }
                        }
                    });
        }
    }

    public static void requestForMealDetail(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_MEAL_DETAIL)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonObject response = resulObj.get("response").getAsJsonObject();
                                    if (response != null) {
                                        JsonElement mealId = response.get("mealId");
                                        JsonElement userId = response.get("userId");
                                        JsonElement photo = response.get("photo");
                                        JsonElement publicPrivate = response.get("publicPrivate");
                                        JsonElement userCatId = response.get("userCatId");
                                        JsonElement title = response.get("title");
                                        JsonElement description = response.get("description");
                                        JsonElement price = response.get("price");
                                        JsonElement date = response.get("date");
                                        JsonElement longitude = response.get("longitude");
                                        JsonElement latitude = response.get("latitude");
                                        JsonElement userPhoto = response.get("userPhoto");
                                        JsonElement userFirstName = response.get("userFirstName");
                                        JsonElement userLastName = response.get("userLastName");
                                        JsonElement avgMealRating = response.get("avgMealRating");
                                        JsonElement userMealRating = response.get("userMealRating");
                                        JsonElement categoryName = response.get("categoryName");
                                        JsonElement servingQty = response.get("serviceQty");
                                        JsonElement commentCount = response.get("commentCount");
                                        JsonElement like = response.get("like");
                                        MealDetailsBean bean = new MealDetailsBean();
                                        bean.setMealId(returnEmptyString(mealId));
                                        bean.setUserId(returnEmptyString(userId));
                                        bean.setPhoto(returnEmptyString(photo));
                                        bean.setPublicPrivate(returnEmptyString(publicPrivate));
                                        bean.setTitle(returnEmptyString(title));
                                        bean.setUserCatId(returnEmptyString(userCatId));
                                        bean.setDescription(returnEmptyString(description));
                                        bean.setPrice(returnEmptyString(price));
                                        bean.setDate(returnEmptyString(date));
                                        bean.setLongitude(returnEmptyString(longitude));
                                        bean.setLatitude(returnEmptyString(latitude));
                                        bean.setUserPhoto(returnEmptyString(userPhoto));
                                        bean.setUserFirstName(returnEmptyString(userFirstName));
                                        bean.setUserLastName(returnEmptyString(userLastName));
                                        bean.setAvgMealRating(returnEmptyString(avgMealRating));
                                        bean.setUserMealRating(returnEmptyString(userMealRating));
                                        bean.setCategoryName(returnEmptyString(categoryName));
                                        bean.setServiceQty(returnEmptyString(servingQty));
                                        bean.setCommentCount(Integer.parseInt(returnEmptyString(commentCount)));
                                        bean.setLike(Integer.parseInt(returnEmptyString(like)));
                                        listener.onSuccess(REQUEST_TYPE_MEAL_DETAIL, true, message, bean);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForLessonDetail(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_LESSON_DETAIL)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonObject response = resulObj.get("response").getAsJsonObject();
                                    if (response != null) {
                                        JsonElement mealId = response.get("lessonId");
                                        JsonElement userId = response.get("userId");
                                        JsonElement photo = response.get("photo");
                                        JsonElement publicPrivate = response.get("publicPrivate");
                                        // JsonElement userCatId = response.get("userCatId");
                                        JsonElement title = response.get("title");
                                        JsonElement description = response.get("description");
                                        JsonElement price = response.get("price");
                                        JsonElement date = response.get("date");
                                        JsonElement longitude = response.get("longitude");
                                        JsonElement latitude = response.get("latitude");
                                        JsonElement userPhoto = response.get("userPhoto");
                                        JsonElement userFirstName = response.get("userFirstName");
                                        JsonElement userLastName = response.get("userLastName");
                                        JsonElement avgMealRating = response.get("avgLessonRating");
                                        JsonElement userMealRating = response.get("userLessonRating");
                                        JsonElement categoryName = response.get("categoryName");
                                        JsonElement servingQty = response.get("serviceQty");
                                        JsonElement commentCount = response.get("commentCount");
                                        JsonElement like = response.get("like");
                                        MealDetailsBean bean = new MealDetailsBean();
                                        bean.setMealId(returnEmptyString(mealId));
                                        bean.setUserId(returnEmptyString(userId));
                                        bean.setPhoto(returnEmptyString(photo));
                                        bean.setPublicPrivate(returnEmptyString(publicPrivate));
                                        bean.setTitle(returnEmptyString(title));
                                        bean.setCommentCount(Integer.parseInt(returnEmptyString(commentCount)));
                                        // bean.setUserCatId(returnEmptyString(userCatId));
                                        bean.setDescription(returnEmptyString(description));
                                        bean.setPrice(returnEmptyString(price));
                                        bean.setDate(returnEmptyString(date));
                                        bean.setLongitude(returnEmptyString(longitude));
                                        bean.setLatitude(returnEmptyString(latitude));
                                        bean.setUserPhoto(returnEmptyString(userPhoto));
                                        bean.setUserFirstName(returnEmptyString(userFirstName));
                                        bean.setUserLastName(returnEmptyString(userLastName));
                                        bean.setServiceQty(returnEmptyString(servingQty));
                                        bean.setAvgMealRating(returnEmptyString(avgMealRating));
                                        bean.setUserMealRating(returnEmptyString(userMealRating));
                                        bean.setCategoryName(returnEmptyString(categoryName));
                                        bean.setLike(Integer.parseInt(returnEmptyString(like)));
                                        listener.onSuccess(REQUEST_TYPE_LESSON_DETAIL, true, message, bean);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForViewComment(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_VIEW_COMMENT)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    ArrayList<CommentBean> commentList = new ArrayList<CommentBean>();
                                    JsonArray resultArray = resulObj.get("result").getAsJsonArray();
                                    if (resultArray.size() > 0) {
                                        for (int k = 0; k < resultArray.size(); k++) {
                                            JsonObject comments = resultArray.get(k).getAsJsonObject();
                                            CommentBean commentBean = new CommentBean();
                                            JsonElement commenterPhoto = comments.get("photo");
                                            JsonElement commentText = comments.get("commentText");
                                            JsonElement timeDate = comments.get("dateTime");
                                            JsonElement userId = comments.get("userId");
                                            JsonElement commentId = comments.get("commentId");
                                            commentBean.setPhoto(returnEmptyString(commenterPhoto));
                                            commentBean.setCommentText(returnEmptyString(commentText));
                                            commentBean.setDateTime(returnEmptyString(timeDate));
                                            commentBean.setCommentId(returnEmptyString(commentId));
                                            commentBean.setUserId(returnEmptyString(userId));
                                            commentList.add(commentBean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_VIEW_COMMENT, true, message, commentList);

                                    } else {

                                        listener.onSuccess(REQUEST_TYPE_VIEW_COMMENT, false, message, commentList);
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForDeleteComment(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_DELETE_COMMENT)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_DELETE_COMMENT, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForReportComment(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_REPORT_COMMENT)
                .setTimeout(60 * 1000)
                .setHeader("accessToken", new SharedPreference(context).getString(Constants.ACCESSTOKEN, ""))
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_REPORT_COMMENT, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static void requestForLike(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_LIKE)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_LIKE, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requestForUserDetails(final Context context, JsonObject obj, final OnNetworkCallBack listener, final boolean isMyProfile) {
        Ion.with(context)
                .load(REQUEST_FOR_USER_DETAIL)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonObject obj = resulObj.get("result").getAsJsonObject();
                                    JsonElement firstName = obj.get("firstName");
                                    JsonElement lastName = obj.get("lastName");
                                    JsonElement description = obj.get("description");
                                    JsonElement dob = obj.get("dob");
                                    JsonElement email = obj.get("email");
                                    JsonElement photo = obj.get("photo");
                                    JsonElement userId = obj.get("userId");
                                    JsonElement uniqueId = obj.get("uniqueId");
                                    JsonElement followCount = obj.get("followCount");
                                    JsonElement occupation = obj.get("occupation");
                                    JsonElement recipes = obj.get("recipes");
                                    JsonElement lessons = obj.get("recipes");
                                    JsonElement meals = obj.get("recipes");
                                    UserDetailBean bean = new UserDetailBean();

                                    if (!isMyProfile) {
                                        JsonElement followStatus = obj.get("followStatus");
                                        bean.setFollowStatus(Integer.parseInt(returnEmptyString(followStatus)));
                                    }
                                    bean.setFirstName(returnEmptyString(firstName));
                                    bean.setLastName(returnEmptyString(lastName));
                                    bean.setDescription(returnEmptyString(description));
                                    bean.setDob(returnEmptyString(dob));
                                    bean.setEmail(returnEmptyString(email));
                                    bean.setPhoto(returnEmptyString(photo));
                                    bean.setUserId(returnEmptyString(userId));
                                    bean.setUniqueId(returnEmptyString(uniqueId));
                                    bean.setFollowCount(Integer.parseInt(returnEmptyString(followCount)));
                                    bean.setOccupation(returnEmptyString(occupation));

                                    if (recipes.isJsonArray()) {
                                        JsonArray recipesArray = obj.get("recipes").getAsJsonArray();
                                        if (recipesArray.size() > 0) {
                                            ArrayList<UserDetailBean.RecipesBean> recipesList = new ArrayList<UserDetailBean.RecipesBean>();
                                            UserDetailBean.RecipesBean recipesBean = null;
                                            for (int i = 0; i < recipesArray.size(); i++) {
                                                JsonElement recipePhoto = recipesArray.get(i).getAsJsonObject().get("recipePhoto");
                                                JsonElement recipeId = recipesArray.get(i).getAsJsonObject().get("recipeId");
                                                recipesBean = new UserDetailBean.RecipesBean();
                                                recipesBean.setRecipeId(Integer.parseInt(returnEmptyString(recipeId)));
                                                recipesBean.setRecipePhoto(returnEmptyString(recipePhoto));
                                                recipesList.add(recipesBean);
                                            }
                                            bean.setRecipes(recipesList);
                                        }
                                    }
                                    if (lessons.isJsonArray()) {
                                        JsonArray lessonsArray = obj.get("lessons").getAsJsonArray();
                                        if (lessonsArray.size() > 0) {
                                            ArrayList<UserDetailBean.LessonsBean> lessonList = new ArrayList<UserDetailBean.LessonsBean>();
                                            UserDetailBean.LessonsBean lessonBean = null;
                                            for (int i = 0; i < lessonsArray.size(); i++) {
                                                JsonElement lessonPhoto = lessonsArray.get(i).getAsJsonObject().get("lessonPhoto");
                                                JsonElement lessonId = lessonsArray.get(i).getAsJsonObject().get("lessonId");
                                                lessonBean = new UserDetailBean.LessonsBean();
                                                lessonBean.setLessonId(returnEmptyString(lessonId));
                                                lessonBean.setLessonPhoto(returnEmptyString(lessonPhoto));
                                                lessonList.add(lessonBean);
                                            }
                                            bean.setLessons(lessonList);
                                        }
                                    }
                                    if (meals.isJsonArray()) {
                                        JsonArray mealsArray = obj.get("meals").getAsJsonArray();
                                        if (mealsArray.size() > 0) {
                                            ArrayList<UserDetailBean.MealsBean> mealsList = new ArrayList<UserDetailBean.MealsBean>();
                                            UserDetailBean.MealsBean mealsBean = new UserDetailBean.MealsBean();
                                            for (int i = 0; i < mealsArray.size(); i++) {
                                                JsonElement mealPhoto = mealsArray.get(i).getAsJsonObject().get("mealPhoto");
                                                JsonElement mealId = mealsArray.get(i).getAsJsonObject().get("mealId");
                                                mealsBean.setMealId(returnEmptyString(mealId));
                                                mealsBean.setMealPhoto(returnEmptyString(mealPhoto));
                                                mealsList.add(mealsBean);
                                            }
                                            bean.setMeals(mealsList);
                                        }
                                    }
                                    listener.onSuccess(REQUEST_TYPE_USER_DETAIL, true, message, bean);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }

    public static void requesetForAddUpdateAddress(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_ADD_UPDATE_ADDRESS)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_ADD_UPDATE_ADDRESS, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });

    }


    public static void requestForAddressList(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUESET_FOR_ADDRESS_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                                 @Override
                                 public void onCompleted(Exception e, Response<JsonObject> result) {
                                     if (e == null) {
                                         switch (result.getHeaders().code()) {
                                             case 200:
                                                 JsonObject resulObj = result.getResult();
                                                 String message = resulObj.get("message").getAsString();
                                                 JsonElement response = resulObj.get("response");
                                                 if (response.isJsonArray()) {
                                                     JsonArray responseArry = response.getAsJsonArray();
                                                     if (responseArry.size() > 0) {
                                                         ArrayList<AddressBean> addressList = new ArrayList<AddressBean>();
                                                         AddressBean bean = null;
                                                         for (int i = 0; i < responseArry.size(); i++) {
                                                             JsonObject object = responseArry.get(i).getAsJsonObject();
                                                             JsonElement addressId = object.get("addressId");
                                                             JsonElement name = object.get("name");
                                                             JsonElement phoneNumber = object.get("phoneNumber");
                                                             JsonElement pincode = object.get("pincode");
                                                             JsonElement address = object.get("address");
                                                             JsonElement city = object.get("city");
                                                             JsonElement state = object.get("state");
                                                             JsonElement country = object.get("country");

                                                             bean = new AddressBean();

                                                             bean.setAddressId(Integer.parseInt(returnEmptyString(addressId)));
                                                             bean.setName(returnEmptyString(name));
                                                             bean.setPhoneNumber(returnEmptyString(phoneNumber));
                                                             bean.setPincode(returnEmptyString(pincode));
                                                             bean.setAddress(returnEmptyString(address));
                                                             bean.setCity(returnEmptyString(city));
                                                             bean.setState(returnEmptyString(state));
                                                             bean.setCountry(returnEmptyString(country));

                                                             addressList.add(bean);
                                                         }
                                                         listener.onSuccess(REQUESET_TYPE_ADDRESS_LIST, true, message, addressList);
                                                     } else {
                                                         listener.onSuccess(REQUESET_TYPE_ADDRESS_LIST, false, message, resulObj);
                                                     }
                                                 }
                                                 break;
                                             case 201:
                                                 resulObj = result.getResult();
                                                 message = resulObj.get("message").getAsString();
                                                 listener.onError(message);
                                                 break;
                                             case 204:
                                                 listener.onError("Network Problem");
                                                 break;
                                             case 400:
                                                 resulObj = result.getResult();
                                                 message = resulObj.get("message").getAsString();
                                                 listener.onError(message);
                                                 break;
                                             case 401:
                                                 listener.onError("Network Problem");
                                                 break;
                                             default:
                                                 listener.onError("Network Problem");
                                                 break;
                                         }
                                     } else {
                                         listener.onError("Network Problem");
                                     }
                                 }
                             }

                );

    }

    public static void requestForDeleteAddress(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUESET_FOR_DELETE_ADDRESS)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUESET_TYPE_DELETE_ADDRESS, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForAddCard(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_ADD_CARD)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_ADD_CARD, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForGetCardList(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_GET_CARD_LIST)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    JsonArray resultArray = resulObj.getAsJsonArray("result");
                                    if (resultArray.isJsonArray() && resultArray.size() > 0) {
                                        ArrayList<CardBean> cardList = new ArrayList<>();
                                        CardBean bean = null;
                                        for (int i = 0; i < resultArray.size(); i++) {
                                            bean = new CardBean();
                                            String cardId = returnEmptyString(resultArray.get(i).getAsJsonObject().get("card_id"));
                                            String cardNumber = returnEmptyString(resultArray.get(i).getAsJsonObject().get("card_number"));
                                            String cardName = returnEmptyString(resultArray.get(i).getAsJsonObject().get("brand"));
                                            bean.setCardId(cardId);
                                            bean.setCardName(cardName);
                                            bean.setCardNumber(cardNumber);
                                            cardList.add(bean);
                                        }
                                        listener.onSuccess(REQUEST_TYPE_GET_CARD_LIST, true, message, cardList);
                                    } else {
                                        listener.onError("No Card Found");
                                    }
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }

    public static void requestForDeleteCard(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_DELETE_CARD)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_DELETE_CARD, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }
    public static void requestForFeaturedRecipe(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_FEATURED_RECIPE)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    JsonObject resultData = resulObj.get("result").getAsJsonObject();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_FEATURED_RECIPE, true, message, resultData);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }
    public static void addRecipeToUserCategory(final Context context, JsonObject obj, final OnNetworkCallBack listener) {
        Ion.with(context)
                .load(REQUEST_FOR_ADD_RECIPE_TO_USER_CATEGORY)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(obj)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if (e == null) {
                            switch (result.getHeaders().code()) {
                                case 200:
                                    JsonObject resulObj = result.getResult();
                                    String message = resulObj.get("message").getAsString();
                                    listener.onSuccess(REQUEST_TYPE_ADD_RECIPE_TO_USER_CATEGORY, true, message, resulObj);
                                    break;
                                case 201:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 204:
                                    listener.onError("Network Problem");
                                    break;
                                case 400:
                                    resulObj = result.getResult();
                                    message = resulObj.get("message").getAsString();
                                    listener.onError(message);
                                    break;
                                case 401:
                                    listener.onError("Network Problem");
                                    break;
                                default:
                                    listener.onError("Network Problem");
                                    break;

                            }
                        } else {
                            listener.onError("Network Problem");
                        }
                    }
                });
    }


    public static String returnEmptyString(JsonElement s) {
        if (s.isJsonNull()) {
            return "";
        } else {
            return s.getAsString();
        }

    }



}