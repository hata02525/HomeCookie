package com.app.homecookie.Activity;

import android.annotation.SuppressLint;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.homecookie.Beans.FriendListBean;
import com.app.homecookie.Beans.GroupCreateDetailsBean;
import com.app.homecookie.ChatFragment;
import com.app.homecookie.Helper.Helper;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.UiThreadExecutor;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.ImagePicker;
import com.app.homecookie.Util.OnImagePicked;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.Util.swipe.ViewBinderHelper;
import com.app.homecookie.XmppChatManager;
import com.app.homecookie.chat.ChatListener;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class CreateGroupActivity extends AppCompatActivity implements OnNetworkCallBack, View.OnClickListener, TextWatcher, OnImagePicked, ChatListener {

    EditText et_search;
    EditText et_name;
    RecyclerView recyclerView;
    Activity activity;
    Progress progress;
    AddFriendAdapter adapter;
    RelativeLayout rl_no_data;
    TextView tv_try_again;
    TextView tv_header;
    TextView tv_add_photoes;
    ImageView iv_back;
    ImageView iv_person;
    RelativeLayout rl_pic;
    ArrayList<FriendListBean> friendList = new ArrayList<FriendListBean>();
    ArrayList<FriendListBean> friendAddedtoGroup = new ArrayList<FriendListBean>();
    int pos;
    ImageView iv_group_photo;
    Uri outputFileUri = null;
    SharedPreference editor;
    AlertDialog alertDialog;
    File imgFile;
    Button button_save;
    private static String DEFAULT_FOLDER_NAME = "HomeCookie";
    private static final String KEY_PHOTO_URI = "photo_uri";
    private static final String KEY_LAST_CAMERA_PHOTO = "last_photo";
    private static final String KEY_TYPE = "type";
    String groupName = "";
    Typeface face = null;
    ImagePicker imagePicker;
    private MultiUserChat mchat;


    ListeningScheduledExecutorService backgroundExecutor;
    private ChatFragment chatFragment;
    ListeningExecutorService uiExecutor;
    private XmppChatManager chatManager;

    private ChatListener chatListener;
    SharedPreference preference;

    private static CreateGroupActivity instance = null;

    public CreateGroupActivity() {

    }

    @SuppressLint("ValidFragment")
    public CreateGroupActivity(XmppChatManager chatManager, ChatListener chatListener) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        activity = CreateGroupActivity.this;
        instance = this;
        preference = SharedPreference.getInstance(this);
        imagePicker = new ImagePicker(activity, this);
        editor = new SharedPreference(activity);
        face = Typeface.createFromAsset(activity.getAssets(), "gotham_normal.TTF");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setOnClickListener(this);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        rl_pic = (RelativeLayout) findViewById(R.id.rl_pic);
        tv_try_again = (TextView) findViewById(R.id.tv_try_again);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_add_photoes = (TextView) findViewById(R.id.tv_add_photoes);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_group_photo = (ImageView) findViewById(R.id.iv_group_photo);
        button_save = (Button) findViewById(R.id.button_save);
        et_name = (EditText) findViewById(R.id.et_name);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_person = (ImageView) findViewById(R.id.iv_person);
        et_search.addTextChangedListener(this);
        rl_pic.setOnClickListener(this);
        button_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        if (Network.isConnected(activity)) {
            progress.show();
            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
            JsonObject obj = new JsonObject();
            obj.addProperty("userId", userId);
            Network.requestForFriendList(activity, obj, this);
        } else {
            rl_no_data.setVisibility(View.VISIBLE);
            tv_try_again.setText("You Are No Connected. \n Please Try Again");
        }
        et_name.setTypeface(face);
        et_search.setTypeface(face);
        tv_try_again.setTypeface(face);
        tv_header.setTypeface(face);
        tv_add_photoes.setTypeface(face);
        button_save.setTypeface(face);

        String userPhoto = new SharedPreference(activity).getString(Constants.USER_PHOTO, "");
        if (userPhoto.contains("http://")) {
            Helper.setProfilePic(activity, userPhoto, iv_person);
        } else {
            String userPhotoo = "http://flupertech.com/Homecookie/usersProfile/" + userPhoto;
            Helper.setProfilePic(activity, userPhotoo, iv_person);
        }


        backgroundExecutor = MoreExecutors.listeningDecorator(MoreExecutors.getExitingScheduledExecutorService(new ScheduledThreadPoolExecutor(1)));
        uiExecutor = new UiThreadExecutor(getApplication());

        chatManager = new XmppChatManager();
        chatFragment = new ChatFragment();
        chatListener = this;


    }


    public void sendMessage(View view) {
        try {
            chatManager.sendMessage(chatFragment.getChatMessage());
            chatFragment.resetChatText();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processMessage(final com.app.homecookie.dto.Chat chat) {
        uiExecutor.execute(new Runnable() {
            @Override
            public void run() {
                chatFragment.addMessage(chat);
            }
        });
    }

    private void initView(ArrayList<FriendListBean> friendList) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AddFriendAdapter(activity, friendList);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(pos);
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        if (requestType == Network.REQUEST_TYPE_USER_FRIEND_LIST) {
            progress.dismiss();
            friendList = (ArrayList<FriendListBean>) data;
            initView(friendList);
        }
        if (requestType == Network.REQUEST_TYPE_CREATE_GROUP) {
            //ChatService.xmpp.connect("On_Create");
            String groupId;
            String groupName;
            GroupCreateDetailsBean groupCreateDetailsBean = (GroupCreateDetailsBean) data;
            if (groupCreateDetailsBean != null) {
                groupId = groupCreateDetailsBean.getGroupDetails().getGroupId();
                groupName = groupCreateDetailsBean.getGroupDetails().getGroupName();
                List<GroupCreateDetailsBean.MemerDetailsBean> memerDetailsBeanList = groupCreateDetailsBean.getMemerDetails();

                backgroundExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        chatManager.setConnectionParams(chatListener, com.app.homecookie.ChatHelper.Config.DOMAIN_URL, com.app.homecookie.ChatHelper.Config.GROUP_CHAT_SERVER, preference.getString(Constants.USER_USR_ID, ""), preference.getString(Constants.ACCESSTOKEN, ""));
                        if (chatManager.join(et_name.getText().toString())) {
                            CreateGroupActivity.getInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    openDialog();
//                                    Log.e("hduigasuid", "hfaofh");
                                    progress.dismiss();
                                    finish();

//                                CreateGroupActivity.getInstance().openChatFragment(group.getText().toString());
                                }
                            });
                        }
                    }
                });


               /* boolean isJoinedGroup = HomeCookieXMPP.createGroupChat(groupId, groupName);
                if (isJoinedGroup) {
                    int size = memerDetailsBeanList.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            String memberId = memerDetailsBeanList.get(i).getMemberId();
                            HomeCookieXMPP.addUserToRoom(groupId, memberId, "yes");
                        }
                    }
                    progress.dismiss();
                }*/

            }

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            new SharedPreference(activity).putInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN, Constants.GROUP_CHAT_FRAGMENT);
            /*Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();*/
        }
    }


    public static CreateGroupActivity getInstance() {
        return instance;
    }


    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    public void openDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view);

        final EditText chathost = (EditText) dialog.findViewById(R.id.chathost);
        Button button = (Button) dialog.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    chatManager.sendMessage(chathost.getText().toString());
                    chathost.setText("");
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }


            }
        });
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pic:
                Helper.hideSoftKeyBoard(activity);
                imagePicker.showImagePickerDialog();
                break;
            case R.id.button_save:
                if (Network.isConnected(activity)) {
                    if (isValid()) {
                        progress.show();
                        try {
                            String userId = new SharedPreference(activity).getString(Constants.USER_USR_ID, "0");
                            JSONObject obj = new JSONObject();
                            obj.put("groupName", groupName);
                            JSONArray userIdArray = new JSONArray();
                            for (int i = 0; i < friendList.size(); i++) {
                                if (friendList.get(i).isFriend()) {
                                    userIdArray.put(friendList.get(i).getId());
                                }
                            }
                            userIdArray.put(userId);

                            obj.put("userIds", userIdArray);
                            obj.put("CreatedBYUserId", userId);
                            JsonParser parser = new JsonParser();
                            JsonObject json = (JsonObject) parser.parse(obj.toString());
                            Network.requestForcreateGroup(activity, json, imgFile, this);

                        } catch (Exception e) {
                            Toast.makeText(CreateGroupActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(activity, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_back:
                new SharedPreference(activity).putInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN, Constants.GROUP_CHAT_FRAGMENT);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean isValid() {
        groupName = et_name.getText().toString();
        int totalMember = 0;
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).isFriend()) {
                totalMember++;
            }
        }
        if (groupName.isEmpty()) {
            Toast.makeText(activity, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (totalMember == 0) {
            Toast.makeText(activity, "Please Add Members to Group", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString();
        ArrayList<FriendListBean> filteredList = new ArrayList<FriendListBean>();
        if (s.length() >= 0) {
            for (int i = 0; i < friendList.size(); i++) {
                String name = friendList.get(i).getFirstName() + " " + friendList.get(i).getLastName();
                if (name.toLowerCase().contains(query)) {
                    filteredList.add(friendList.get(i));
                }
                initView(filteredList);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCameraClicked(Intent intent) {
        startActivityForResult(intent, Constants.CAMERA_REQUEST);
    }

    @Override
    public void onGalleryClicked(Intent intent) {
        startActivityForResult(intent, Constants.GALLERY_REQUEST);
    }


    class AddFriendAdapter extends RecyclerView.Adapter {
        int itemPosition;
        ArrayList<FriendListBean> friendList = new ArrayList<>();
        private LayoutInflater mInflater;
        private final ViewBinderHelper binderHelper = new ViewBinderHelper();
        Activity activity;
        String id;
        int pos;

        public AddFriendAdapter(Activity activity, ArrayList<FriendListBean> friendList) {
            this.activity = activity;
            mInflater = LayoutInflater.from(activity);
            this.friendList = friendList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.add_member_to_group, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder h, final int position) {
            final ViewHolder holder = (ViewHolder) h;
            pos = position;
            String name = friendList.get(position).getFirstName() + " " + friendList.get(position).getLastName();
            final String photo = friendList.get(position).getPhoto();
            holder.tv_name.setText(name);
            boolean isAdded = friendList.get(position).isFriend();
            if (isAdded) {
                holder.button_add.setVisibility(View.GONE);
                holder.tv_added.setVisibility(View.VISIBLE);
            } else {
                holder.button_add.setVisibility(View.VISIBLE);
                holder.tv_added.setVisibility(View.GONE);
            }
            Helper.setProfilePic(activity, photo, holder.iv_photo);
            if (holder.button_add.getVisibility() == View.VISIBLE) {
                holder.button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendList.get(position).setFriend(true);
                        boolean isAdded = friendList.get(position).isFriend();
                        friendAddedtoGroup.add(friendList.get(position));
                        if (isAdded) {
                            holder.button_add.setVisibility(View.GONE);
                            holder.tv_added.setVisibility(View.VISIBLE);
                        } else {
                            holder.button_add.setVisibility(View.VISIBLE);
                            holder.tv_added.setVisibility(View.GONE);
                        }

                    }
                });
            }

            holder.tv_added.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendList.get(position).setFriend(false);
                    boolean isAdded = friendList.get(position).isFriend();
                    friendAddedtoGroup.remove(friendList.get(position));
                    if (isAdded) {
                        holder.button_add.setVisibility(View.GONE);
                        holder.tv_added.setVisibility(View.VISIBLE);
                    } else {
                        holder.button_add.setVisibility(View.VISIBLE);
                        holder.tv_added.setVisibility(View.GONE);
                    }

                }
            });



           /* holder.bind(position);*/
        }

        @Override
        public int getItemCount() {
            return friendList.size();
        }


        public void saveStates(Bundle outState) {
            binderHelper.saveStates(outState);
        }

        public void restoreStates(Bundle inState) {
            binderHelper.restoreStates(inState);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_photo;
            TextView tv_name;
            TextView tv_added;
            Button button_add;
            //  FrameLayout deleteLayout;


            public ViewHolder(View itemView) {
                super(itemView);
                iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_added = (TextView) itemView.findViewById(R.id.tv_added);
                button_add = (Button) itemView.findViewById(R.id.button_add);
                tv_name.setTypeface(face);
                button_add.setTypeface(face);
                //      deleteLayout = (FrameLayout) itemView.findViewById(R.id.delete_layout);
            }


           /* public void bind(final int position) {
                deleteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                friendList.get(position).setFriend(false);
                                boolean isAdded = friendList.get(position).isFriend();
                                if (isAdded) {
                                    button_add.setVisibility(View.GONE);
                                    tv_added.setVisibility(View.VISIBLE);
                                } else {
                                    friendList.get(position).setFriend(false);
                                    button_add.setVisibility(View.VISIBLE);
                                    tv_added.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });*/

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    imgFile = ImagePicker.takenCameraPicture(activity);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap photo = BitmapFactory.decodeFile(imgFile.getPath(), options);
                    iv_group_photo.setVisibility(View.VISIBLE);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e == null) {
                                iv_group_photo.setImageBitmap(result);
                            } else {
                                iv_group_photo.setImageDrawable(null);
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
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    if (picturePath == null) {
                        picturePath = selectedImage.getPath();
                    }
                    imgFile = new File(picturePath);
                    iv_group_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    iv_group_photo.setVisibility(View.VISIBLE);
                    Ion.with(activity).load(imgFile).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            iv_group_photo.setImageBitmap(result);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void hitApi(JsonObject obj) {
        progress.show();
        Network.requestForAddFriend(activity, obj, this);
    }

    @Override
    public void onBackPressed() {
        new SharedPreference(activity).putInt(Constants.CHAT_FRAGMENT_TO_BE_SHOWN, Constants.GROUP_CHAT_FRAGMENT);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}


