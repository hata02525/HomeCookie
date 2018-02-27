package com.app.homecookie.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.UiThreadExecutor;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.app.homecookie.XmppChatManager;
import com.app.homecookie.adaptor.ChatAdaptor;
import com.app.homecookie.chat.ChatListener;
import com.app.homecookie.dto.Chat;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneToManyChatFragment extends Fragment implements ChatListener {

    String groupId = "0";
    String groupName = "";
    String groupPhoto = "";
    String userId = "0";

    private View view;
    private EditText et_message;
    private ImageView iv_send;
    private SharedPreference sharedPreference;
    private Activity activity;
    private TextView tv_group_name;
    private ImageView iv_group_photo;
    private Random random = new Random();
    ListView recyclerView;
    private Progress progress;

    ListeningScheduledExecutorService backgroundExecutor;

    ListeningExecutorService uiExecutor;
    private XmppChatManager chatManager;

    private ChatListener chatListener;
    SharedPreference preference;
    private ArrayList<Chat> chats;
    String name = "";
    ChatAdaptor adaptor;

    public static OneToManyChatFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        OneToManyChatFragment fragment = new OneToManyChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            groupId = bundle.getString(Constants.GROUP_ID);
            groupName = bundle.getString(Constants.GROUP_NAME);
            groupPhoto = bundle.getString(Constants.GROUP_PHOTO);

            chats = new ArrayList<Chat>();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one_to_many_chat, container, false);
        activity = getActivity();
        sharedPreference = new SharedPreference(activity);
        et_message = (EditText) view.findViewById(R.id.et_message);
        iv_send = (ImageView) view.findViewById(R.id.iv_send);
        tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
        iv_group_photo = (ImageView) view.findViewById(R.id.iv_group_photo);
        recyclerView = (ListView) view.findViewById(R.id.recyclerView);
        tv_group_name.setText(groupName);
        name = sharedPreference.getString(Constants.USER_F_NAME, "") + " " + sharedPreference.getString(Constants.USER_L_NAME, "");
        progress = new Progress(activity);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        backgroundExecutor = MoreExecutors.listeningDecorator(MoreExecutors.getExitingScheduledExecutorService(new ScheduledThreadPoolExecutor(1)));
        uiExecutor = new UiThreadExecutor(getActivity().getApplication());

        chatManager = new XmppChatManager();
        chatListener = this;
        progress.show();

        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                chatManager.setConnectionParams(chatListener, com.app.homecookie.ChatHelper.Config.DOMAIN_URL, com.app.homecookie.ChatHelper.Config.GROUP_CHAT_SERVER, sharedPreference.getString(Constants.USER_USR_ID, ""), sharedPreference.getString(Constants.ACCESSTOKEN, ""));
                if (chatManager.join(groupName)) {
                    HomeActivity.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Log.e("success", "welcome to chat");

//                            progress.dismiss();
//                                CreateGroupActivity.getInstance().openChatFragment(group.getText().toString());
                        }
                    });
                }
            }
        });


        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    chatManager.sendMessage(et_message.getText().toString());
                    et_message.setText("");
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
               /* userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
//
                String userPhoto = sharedPreference.getString(Constants.USER_PHOTO, "");

                String message = et_message.getText().toString();
                if (!message.equalsIgnoreCase("")) {
                    int messageId = random.nextInt(1000);
                    MessageModel.LastMessageBean lastMessageBean = new MessageModel.LastMessageBean();
                    lastMessageBean.setId(String.valueOf(messageId));
                    lastMessageBean.setMessageBody(message);

                    try {
                        ChatService.xmpp.sendMucStanza(groupId, new MessageModel(name, lastMessageBean, userPhoto, groupId, userId, messageId, true, "TEXT", "", message));
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }*/

                // ChatService.xmpp.sendMessagetoGroup(groupId, new MessageModel(name, lastMessageBean, userPhoto, groupId, userId, messageId, true, "TEXT", "", message));
//                }
            }
        });

        setAdaptor(chats);

        return view;
    }

    private void setAdaptor(List<Chat> chats) {
        adaptor = new ChatAdaptor(getActivity().getApplicationContext(), chats);
        recyclerView.setAdapter(adaptor);
    }

    public void addMessage(Chat chat) {
        chats.add(chat);
        if (adaptor != null) {
            adaptor.notifyDataSetChanged();
            recyclerView.invalidate();
        }
    }

    @Override
    public void processMessage(final Chat chat) {
        uiExecutor.execute(new Runnable() {
            @Override
            public void run() {
                addMessage(chat);
            }
        });
    }
}
