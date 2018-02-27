package com.app.homecookie.Util;

/**
 * Created by fluper on 4/4/17.
 */
public class Constants {
    /*------------------ALICATION CONSTANTS-------------------*/
    public static final String SESSSION = "session";
    public static final String NO_INTERNET = "No Internet";

    /*------------------INTERNET CONSTANTS-------------------*/


    /*------------------VALIDATION CONSTANTS-------------------*/
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String MOBILE_PATTERN = "^[1-9][0-9]{9}$";
    public static final String SESSION = "session";
    public static final String IS_FROM_GROCESSARY = "isFromGrocessary";
    public static final String AM = "AM";
    public static final String PM = "PM";
    public static final String VERIFICATION_KEY = "verification_key";
    /*--------------------User Details-------------------*/
    public static final String USER_USR_ID = "user_usr_id";
    public static final String PREVIOUS_USER_USR_ID = "previous_user_usr_id";
    public static final String USER_F_NAME = "user_f_name";
    public static final String USER_L_NAME = "user_l_name";
    public static final String USER_DOB = "dob";
    public static final String USER_EMAIL = "email";
    public static final String USER_GENDER = "gender";
    public static final String USER_OCC = "user_occ";
    public static final String USER_ABT = "about_me";
    public static final String USER_ID = "user_id";
    public static final String USER_ABOUT_ME = "about_me";
    public static final String REGISTRATION_ID = "registration_id";
    public static final String REGISTRATION_ID_OBTAINED = "registration_id_obtained";
    public static final String USER_PROFILE_STATUS = "user_profile_status";
    public static final String USER_PHOTO = "user_profile_photo";
    public static final int PROFILE_STATUS = 0;
    public static final String IS_UNIQUE_ID_CREATED = "isUserIdCreated";
    public static final String UNIQUE_ID = "uniqueId";
    public static final String ACCESSTOKEN = "accessToken";
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final int CAMERA_REQUEST = 1001;
    public static final int GALLERY_REQUEST = 1002;
    public static final int PLACE_PICKER_REQUEST = 2536;
    public static final int CROP_FROM_CAMERA = 2;
    public static final int PERMISSION_LOCATION_REQUEST_CODE = 007;
    public static final int PERMISSION_CMERA_REQUEST_CODE = 006;
    public static final int PERMISSION_STORAGE_REQUEST_CODE = 005;
    public static final int REQUEST_CODE_FOR_SEARCH_INGREDIENTS = 1025;
    public static final int REQUEST_CODE_FOR_CATEGORY = 199;
    public static final int REQUEST_CODE_FOR_STEP = 101;
    public static final int FRAGMENT_TO_REPLACE = 6598;
    public static final String FRAGMENT_TO_BE_REPLACE = "fragmentToBeReplace";
    public static final String TYPE_ID = "typeid";
    public static final String RECIPE_TYPE = "0";
    public static final String LESSON_TYPE = "2";
    public static final String MEAL_TYPE = "1";
    public static final String POSTER_NAME = "posterName";
    public static final String POSTER_PHOTO = "posterPhoto";
    public static final String POSTER_OCC = "posterOccupation";
    public static final String RECEIVER_ID = "receiverId";
    public static final String IS_FRIEND_CHAT = "isFromFriendChat";
    public static final String IS_RECENT_CHAT = "isRecentChat";
    public static final String NOTIFICATION_USER = "notificationUserId";
    public static final String START_ONE_TO_ONE_CHAT = "startOneToOneChat";
    public static final CharSequence NEW_MESSAGE = "HomeCookie : New Message" ;
    public static final String CUSTOMER_ID = "customerId";
    public static final String GROUP_NAME = "goupName" ;
    public static final String GROUP_PHOTO = "groupPhoto";
    public static final String GROUP_ID = "groupId";
    public static String IS_FROM_ADD_FRIEND = "isFromAddFriend";
    public static String IS_FROM_COMMENT = "isFromComment";
    public static String IS_FROM_CREATE_GROUP = "isFromCreateGroup";
    public static String IS_MY_PROFILE = "isMyProfile";
    public static String IS_HOME_COOKIE_FRAGMENT_REPLACED = "isHomeCookFragmentReplaced";
    public static String MEAL_ID = "mealId";
    public static String FRIEND_ID = "friendId";
    public static String RECIPE_LIKE_STATUS = "recipeLikeStatus";
    public static String IS_TO_UPDATE = "isToUpdate";
    public static String IS_FROM_HOME_COOKIE_PROFILE = "isFromHomeCookieProfile";



    /*------------------Broadcasts Action----------------*/

    /*-------------------API_KEYS----------------*/


    /***************
     * -----Bean Constants------
     ************/
    public static final String CHOOSE_CATEGORY_LIST_BEAN = "choose_category_list_bean";
    public static final String CATEGORY_LIST_BEAN = "category_list_bean";
    public static final String SUB_CATEGORY_LIST_BEAN = "sub_category_list_bean";
    public static final String RECIPES_BEAN = "recipes_bean";
    public static final String INGREDIENT_BEAN = "ingredient_bean";
    public static final String INGREDIENT_GROCERRY_BEAN = "ingredient_grocerry_bean";
    public static final String INGREDIENT_BEAN_OBJECT = "ingredient_bean_object";
    public static final String ADDRESS_BEAN = "AddressBean";
    public static final String STEP_BEAN = "step_bean";
    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String STEP_NUM = "step_num";


    public static final String COMMENTIG_FROM = "from";
    /****************
     * -----------Chat Constants---------------
     ************//////////
    public static final String USER = "user";
    public static final String KEY_FROM_NOTIFICATION = "keyFromNotification";
    public static final String KEY_FROM_NOTIFICATION_FOR_CHAT = "keyFromNotifictaion";
    public static final String USER_CHAT_ID = "userchatid";
    public static final String USER_CHAT_PASSWORD = "userchatpassword";
    public static final String TO_USER_ID = "toUserId";
    public static final String TO_USER_NAME = "toUserName";


    public static String CHAT_FRAGMENT_TO_BE_SHOWN = "chatFragmentTobeShown";
    public static final String TO_USER_PHOTO = "toUserPhoto";
    public static final String IS_CHAT_FRAGMENT_ADDED = "isChatFragmentAdded";
    public static String COMMENT_COUNT = "comment_count";
    public static int COMMENT_COUNT_CODE = 1234;
    public static int RECENT_CHAT_FRAGMENT = 0;
    public static int GROUP_CHAT_FRAGMENT = 1;
    public static int FRIEND_CHAT_FRAGMENT = 2;


}
