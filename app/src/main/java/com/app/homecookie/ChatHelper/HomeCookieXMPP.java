package com.app.homecookie.ChatHelper;

/**
 * Created by ${Devendra} on 4/5/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.homecookie.Activity.HomeActivity;
import com.app.homecookie.ChatHelper.model.MessageModel;
import com.app.homecookie.ChatHelper.model.RecentChatModel;
import com.app.homecookie.Fragments.OnetoOneChatFragrment;
import com.app.homecookie.Fragments.RecentChatFragment;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.GroupChatInvitation;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.app.homecookie.ChatHelper.XMPPUtils.getMultiUserChat;

public class HomeCookieXMPP extends Stanza implements PingFailedListener, RosterListener {
    private static MultiUserChat mchat;
    private Set<Integer> onlineUsers;

    private static byte[] dataReceived;
    public static boolean connected = false;
    public boolean loggedin = false;
    public static boolean isconnecting = false;
    public static boolean isToasted = true;
    private boolean chat_created = false;
    private String serverAddress;
    private boolean isGroupCreated;
    public static XMPPTCPConnection connection;
    public static String loginUser;
    public static String passwordUser;
    Gson gson;
    ChatService context;
    public static HomeCookieXMPP instance = null;
    public static boolean instanceCreated = false;
    private FileTransferManager manager;
    RecentDb recentDb;
    private static final String TAG = "HomeCookieXMPP";



    public HomeCookieXMPP(ChatService context, String serverAdress, String logiUser,
                          String passwordser) {
        this.serverAddress = serverAdress;
        this.loginUser = logiUser;
        this.passwordUser = passwordser;
        this.context = context;
        init();
    }

    public HomeCookieXMPP() {

    }

    public XMPPTCPConnection getConnection() {
        return connection;
    }

    public static HomeCookieXMPP getInstance(ChatService context, String server,
                                             String user, String pass) {

        if (instance == null) {
            instance = new HomeCookieXMPP(context, server, user, pass);
            instanceCreated = true;

        }
        return instance;

    }

    public org.jivesoftware.smack.chat.Chat Mychat;
    public MultiUserChatManager muc;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    String text = "";
    String mMessage = "", mReceiver = "";

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    public void init() {
        onlineUsers = new HashSet<>();
        gson = new Gson();
        mMessageListener = new MMessageListener(context);
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection();
        Log.e("XMPP", "initCalled");

    }

    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAddress);
        config.setHost(serverAddress);
        config.setPort(5222);
        config.setDebuggerEnabled(true);
        config.setConnectTimeout(1000);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        connection = new XMPPTCPConnection(config.build());
        try {
            connection.connect();
            connection.login(loginUser, passwordUser);
        } catch (SmackException e) {
            Log.e("LOGIN", "Ohho! Smack Exception!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("LOGIN", "Ohho! IO Exception!");
            e.printStackTrace();
        } catch (XMPPException e) {
            Log.e("LOGIN", "Ohho! XMPP Exception!");
            e.printStackTrace();
        }

        Presence presence = new Presence(Presence.Type.available);
        presence.setMode(Presence.Mode.available);
        try {
            connection.sendPacket(presence);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
        PingManager pingManager = PingManager.getInstanceFor(connection);
        pingManager.registerPingFailedListener(this);
        manager = FileTransferManager.getInstanceFor(connection);
        //      manager.addFileTransferListener(new FileTransferIMPL());
        FileTransferNegotiator.getInstanceFor(connection);
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    /*public static boolean join(String group){
        try {
            MultiUserChatManager mchatManager = MultiUserChatManager.getInstanceFor(connection);
            mchat = mchatManager.getMultiUserChat(group + "@"+ GROUP_CHAT_SERVER);
                Log.d("CONNECT", "Joining room !! "+ group + " and username " + memberId);
                boolean createNow = false;
                try{
                    String jId = HomeCookieUtils.getUserId(memberId);
                    mchat.createOrJoin(jId);
                    createNow = true;
                }
                catch (Exception e){
                    Log.d("CONNECT", "Error while creating the room "+group + e.getMessage());
                }

                if(createNow){
                    mchat.sendConfigurationForm(new Form(DataForm.Type.submit)); //this is to create the room immediately after join.
                    Log.d("CONNECT", "Room created!!");
                    return true;
                }
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        }
        return false;
    }*/


    public static boolean createGroupChat(@Nullable String groupId, @NonNull String groupTitle){


//        PacketFilter filter = MessageTypeFilter.GROUPCHAT;
        StanzaFilter filter = MessageTypeFilter.GROUPCHAT;




        groupId = (groupId == null) ? UUID.randomUUID().toString() : groupId;
        mchat = XMPPUtils.getMultiUserChat(connection, groupId);
        mchat.addParticipantListener(new PresenceListener() {
            @Override
            public void processPresence(Presence presence) {

            }
        });

        try
        {
            //mchat.create(loginUser);
            mchat.join(loginUser);

            //mchat.sendConfigurationForm(new Form(DataForm.Type.submit)); //this is to create the room immediately after join.
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    /*public static boolean setGroupTitle(String groupId, String groupTitle){
        Form form = new Form(DataForm.Type.submit);
        form.setAnswer("muc#roomconfig_roomname", groupTitle.trim());
        mchat = XMPPUtils.getMultiUserChat(connection, groupId);
        try{
            mchat.sendConfigurationForm(form);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

    /*public static MultiUserChat joinGroupChat(@NonNull String groupId){

        mchat = XMPPUtils.getMultiUserChat(connection, groupId);

        if(!mchat.isJoined()){
            String username = loginUser;
            try
            {
                mchat.join(username);
                return multiUserChat;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }*/


    public static boolean addUserToRoom(@NonNull String groupId, @NonNull String userId, @Nullable String reason){
        mchat = getMultiUserChat(connection, groupId);
        reason = (reason == null) ? XMPPUtils.DEFAULT_USER_RESOURCE : reason;
        String userJID = XMPPUtils.getUserJID(userId);
        try
        {
            mchat.invite(userJID, reason);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMucStanza(String groupId, MessageModel chatMessage) throws SmackException.NotConnectedException
    {
        String body = gson.toJson(chatMessage);
        Message message = new Message();
        message.setBody(body);
        MultiUserChat multiUserChat = getMultiUserChat(connection, groupId);
        multiUserChat.sendMessage((Message) message);
        multiUserChat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {
                Log.d("Group message outcomes:", message.getBody());
            }
        });
        //todo handle text message


        return true;
    }



    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (connection.isConnected())
                    return false;
                isconnecting = true;
                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            Log.e(TAG, caller + "=>connecting....");
                        }
                    });
                Log.d("Connect() Function", caller + "=>connecting....");

                try {
                    connection.connect();
                    DeliveryReceiptManager dm = DeliveryReceiptManager
                            .getInstanceFor(connection);
                    dm.setAutoReceiptMode(AutoReceiptMode.always);
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                        @Override
                        public void onReceiptReceived(final String fromid,
                                                      final String toid, final String msgid,
                                                      final Stanza packet) {
                            try {
                                connection.sendPacket(packet);
                            } catch (NotConnectedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    connected = true;

                } catch (IOException e) {
                    if (isToasted)
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Log.e(TAG, "(" + caller + ")" + "IOException: ");
                                        //Toast.makeText(context,"(" + caller + ")" + "IOException: ",Toast.LENGTH_SHORT).show();
                                    }
                                });

                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (SmackException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            Log.e(TAG, "(" + caller + ")" + "SMACKException");
                            //Toast.makeText(context,"(" + caller + ")" + "SMACKException: ",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (XMPPException e) {
                    if (isToasted)

                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Log.e(TAG, "(" + caller + ")" + "XMPPException: ");
                                        //Toast.makeText(context,"(" + caller + ")" + "XMPPException: ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    Log.e("connect(" + caller + ")",
                            "XMPPException: " + e.getMessage());

                }
                return isconnecting = false;
            }
        };
        connectionThread.execute();
    }

    public void login() {
        try {
            Mychat = null;
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            Log.e("LOGIN", "Ohho! We're still not connected to the Xmpp server!");
            connection.login(loginUser, passwordUser);
            Log.e("LOGIN", "Yey! We're connected to the Xmpp server!");
        } catch (XMPPException | SmackException | IOException e) {
            Log.e("LOGIN", "Ohho! Smack Exception!");
            initialiseConnection();
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("LOGIN", "Ohho! Base 64 Exception!");
            e.printStackTrace();
        }

    }

    @Override
    public void pingFailed() {

    }

    @Override
    public CharSequence toXML() {
        return null;
    }


    @Override
    public void entriesAdded(Collection<String> addresses) {
        Log.e(TAG, " :: entriesAdded" + addresses);
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        Log.e(TAG, " :: entriesUpdated" + addresses);

    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        Log.e(TAG, " :: entriesDeleted" + addresses);

    }

    @Override
    public void presenceChanged(Presence presence) {
        int userId = getIdFromAddress(presence.getFrom());
        boolean online = presence.isAvailable();
        Log.e(TAG, " :: presenceChanged" + userId + "  --->   " + online);

        if (online && onlineUsers.contains(userId))
            return;

        if (!online && !onlineUsers.contains(userId))
            return;

        if (online) {
            onlineUsers.add(userId);
        } else {
            onlineUsers.remove(userId);
        }
    }

    private int getIdFromAddress(String address) {
        if (address.contains("@")) {
            try {
                return Integer.parseInt(address.substring(0, address.indexOf("@")));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);
        }
    }

    public void sendMessage(MessageModel chatMessage) {
        String body = gson.toJson(chatMessage);

        if (!chat_created) {
            Mychat = ChatManager.getInstanceFor(connection).createChat(chatMessage.getReceiver() + "@67.209.121.170", mMessageListener);
            chat_created = true;
            try {
                Roster.getInstanceFor(connection).createEntry(chatMessage.getReceiver() + "@" + serverAddress, chatMessage.getReceiver() + "", null);
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(String.valueOf(chatMessage.getMsgIdl()));
        message.setType(Message.Type.chat);

        try {
            if (connection.isAuthenticated()) {
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(message.getBody());
                String messageText = object.get("msg").getAsString();
                Mychat.sendMessage(message.getBody());
            } else {
                login();
            }
        } catch (NotConnectedException e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!-Not Connected!");

        } catch (Exception e) {
            Log.e("SEND_MESSAGE_ERROR", e.getMessage());
        }

    }


    public void sendGroupMessage(String groupId, MessageModel chatMessage) {
        String body = gson.toJson(chatMessage);

        if (!chat_created) {
            Mychat = ChatManager.getInstanceFor(connection).createChat(groupId + "@67.209.121.170", mMessageListener);
            chat_created = true;
            try {
                Roster.getInstanceFor(connection).createEntry(groupId + "@" + serverAddress, groupId + "", null);
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(String.valueOf(chatMessage.getMsgIdl()));
        message.setType(Message.Type.groupchat);

        try {
            if (connection.isAuthenticated()) {
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(message.getBody());
                String messageText = object.get("msg").getAsString();
                Mychat.sendMessage(message.getBody());
            } else {
                login();
            }
        } catch (NotConnectedException e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!-Not Connected!");

        } catch (Exception e) {
            Log.e("SEND_MESSAGE_ERROR", e.getMessage());
        }

    }


    /*public void joinGroup(String groupId) {
        muc = MultiUserChatManager.getInstanceFor(connection);
        muc.getMultiUserChat(groupId + "@67.209.121.170");
        isGroupCreated = true;
    }*/


    /*public void sendGroupMessage(String groupName, String message) throws SmackException.NotConnectedException, XMPPException {
        Roster roster = Roster.getInstanceFor(connection);
        RosterGroup rosterGroup = roster.getGroup(groupName);
        Collection<RosterEntry> entries = rosterGroup.getEntries();
        for (RosterEntry entry : entries) {
            String user = entry.getName();
            System.out.println(String.format("Sending message " + message + " to user " + user));
            Chat chat = ChatManager.getInstanceFor(connection).createChat(user , mMessageListener);
            chat.sendMessage(message);
        }
    }*/



    public void sendMessagetoGroup(String groupId, MessageModel chatMessage) {
        String body = gson.toJson(chatMessage);
        muc = MultiUserChatManager.getInstanceFor(connection);
        mchat = muc.getMultiUserChat(groupId + "@67.209.121.170");
        isGroupCreated = true;
        try {
            mchat.join(chatMessage.getReceiver() + serverAddress);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

        mchat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {
                System.out.println("Message listener Received message in send message: "
                        + (message != null ? message.getBody() : "NULL") + "  , Message sender :" + message.getFrom());
            }
        });

    }

    public void sendMessageToGroupMembers(String groupId) {

        GroupChatInvitation groupChatInvitation = new GroupChatInvitation(groupId + "@67.209.121.170");

        Message msg = new Message("", Message.Type.groupchat);
        msg.setBody("This is nagarjuna friednds. Please join this room and let us have fun.");
        try {
            connection.sendPacket(msg);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.d("xmpp", "Connected!");
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        // Toast.makeText(context, "ConnectionCLosed!", Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            isGroupCreated = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        Log.e(TAG, "ConnectionClosedOn Error!!");
                        // Toast.makeText(context, "ConnectionClosedOn Error!!",Toast.LENGTH_SHORT).show();

                    }
                });
            connected = false;
            chat_created = false;
            isGroupCreated = false;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int arg0) {

            Log.e("xmpp", "Reconnectingin " + arg0);

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        // Toast.makeText(context, "ReconnectionFailed!",Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;

            chat_created = false;
            isGroupCreated = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        //   Toast.makeText(context, "REConnected!",Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;

            chat_created = false;
            isGroupCreated = false;
            loggedin = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;

            ChatManager.getInstanceFor(connection).addChatListener(
                    mChatManagerListener);

            chat_created = false;
            isGroupCreated = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Log.e(TAG, "Connected!!");
//                        Toast.makeText(context, "Connected!",Toast.LENGTH_SHORT).show();

                    }
                });
        }
    }

    private class MMessageListener implements ChatMessageListener, ChatStateListener {
        Context context;

        public MMessageListener(Context contxt) {
            this.context = contxt;
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.e("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '"
                    + message);

            if (message.getType() == Message.Type.chat
                    && message.getBody() != null) {
                final MessageModel chatMessage = gson.fromJson(
                        message.getBody(), MessageModel.class);

                recentDb = new RecentDb(context);
                processMessage(message);
            }

            if (message.getType() == Message.Type.groupchat
                    && message.getBody() != null) {
                Log.d("Group message outcomes:", message.toString());
            }
        }

        private void processMessage(final Message message) {

            String sender1 = message.getFrom();
            String receiver = message.getTo();
            final Random random = new Random();
            final String delimiter = "\\@";
            String[] temp = sender1.split(delimiter);
            String[] temp1 = receiver.split(delimiter);
            final String sender = temp[0];
            Log.d("USERS" + sender, temp1[0]);
            SharedPreference sharedPreference = new SharedPreference(context);
            final MessageModel messageModel = gson.fromJson(
                    message.getBody(), MessageModel.class);
            messageModel.setIsMine(false);
            messageModel.setMsgIdl(random.nextInt(1000));
            messageModel.setType("TEXT");
            messageModel.setReceiver(temp1[0]);
            messageModel.setSender(sender);

            OnetoOneChatFragrment.chatlist.add(messageModel);


            CommonMethods commonMethods = new CommonMethods(context);
            commonMethods.createTable(temp1[0]);
            //  String tablename, String s, String r, String m, String w,String datatype
            //commonMethods.insertIntoTable(temp1[0], sender, temp1[0], message.getBody(), "r", "TEXT");
            String tableId;
            int myId = Integer.parseInt(messageModel.getReceiver());
            int otherId = Integer.parseInt(messageModel.getSender());
            if (myId > otherId) {
                tableId = myId + "" + otherId;
            } else {
                tableId = otherId + "" + myId;
            }

            RecentDb recentDb = new RecentDb(context);
            if (recentDb != null) {
                int receverId = Integer.parseInt(messageModel.getSender());
                String photo = messageModel.getDisplayPic();
                String lastMessage = messageModel.getMsg();
                RecentChatModel model = new RecentChatModel(Integer.parseInt(tableId), String.valueOf(receverId),
                        messageModel.getName(), photo, lastMessage);


                boolean isInserted = recentDb.insertNewChatToDb(model);
                if (!isInserted) {
                    recentDb.updateChat(model);
                    ArrayList<RecentChatModel> recievedChat = new ArrayList<>();
                    recievedChat.add(model);
                    RecentChatFragment.recentChatList = recievedChat;
                    RecentChatFragment.adapter.notifyDataSetChanged();
                    Log.e("updated", recentDb.getAllRecentchat().toString());
                }
                ArrayList<RecentChatModel> recievedChat = new ArrayList<>();
                recievedChat.add(model);
                if (RecentChatFragment.recentChatList != null)
                    RecentChatFragment.recentChatList = recievedChat;
                if (RecentChatFragment.adapter != null)
                    RecentChatFragment.adapter.notifyDataSetChanged();

            }

            commonMethods.insertIntoTable(tableId, messageModel.getSender(), messageModel.getReceiver(), messageModel.getMsg(), "r", "TEXT");
            String recieveId = sharedPreference.getString(Constants.RECEIVER_ID, "0");
            final String recieverId = messageModel.getSender();
            if (recieveId.equalsIgnoreCase(recieverId)) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        OnetoOneChatFragrment.chatAdapter.notifyDataSetChanged();
                    }
                });
            }
            final String notificationReciver = messageModel.getReceiver();
            final String userId = sharedPreference.getString(Constants.USER_USR_ID, "0");
            if (notificationReciver.equalsIgnoreCase(userId)) {
                generateChatPush(messageModel.getName(), messageModel.getSender(), messageModel.getDisplayPic(), messageModel.getMsg(), context);
            }

        }


        @Override
        public void stateChanged(Chat chat, ChatState state) {
            switch (state) {
                case active:
                    Log.d("state", "active");
                    break;
                case composing:
                    Log.d("state", "composing");
                    break;
                case paused:
                    Log.d("state", "paused");
                    break;
                case inactive:
                    Log.d("state", "inactive");
                    break;
                case gone:
                    Log.d("state", "gone");
                    break;
            }
        }

        private void createDirectoryAndSaveFile(byte[] imageToSave, String fileName) {
            File direct = new File(Environment.getExternalStorageDirectory() + "/LocShopie/Received/");
            if (!direct.exists()) {
                File wallpaperDirectory = new File("/sdcard/LocShopie/Received/");
                wallpaperDirectory.mkdirs();
            }
            File file = new File(new File("/sdcard/LocShopie/Received/"), fileName);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(imageToSave);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void processMessage(final FileTransferRequest request) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Log.e("MSG RECE", "LOOPER");
                    Random random = new Random();
                    CommonMethods commonMethods = new CommonMethods(context);
                    int iend = request.getRequestor().lastIndexOf("@");
                    String requester = request.getRequestor().substring(0, 10);
                    commonMethods.createTable(requester);
                    Log.e("MSG RECE", requester);

                    SharedPreferences pref = context.getSharedPreferences("Login", context.MODE_PRIVATE);
                    String rec = pref.getString("user", null);

                    // String tablename, String s, String r, String m, String w,String datatype
                    commonMethods.insertIntoTable(requester, requester, rec, request.getFileName(), "r", "IMAGE");
                    final MessageModel chatMessage = new MessageModel();
                    chatMessage.setSender(requester);
                    chatMessage.setType("IMAGE");
                    chatMessage.setReceiver(rec);
                    chatMessage.setMsgIdl(random.nextInt(1000));
                    chatMessage.setIsMine(false);
                    chatMessage.setMsg(request.getFileName());
                    OnetoOneChatFragrment.chatlist.add(chatMessage);
                    OnetoOneChatFragrment.chatAdapter.notifyDataSetChanged();
                    Log.e("MSG RECE", request.getRequestor());

                }
            });
        }


        public boolean createNewAccount(String username, String newpassword) {
            boolean status = false;
            if (connection == null) {
                try {
                    connection.connect();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }

            try {
                String newusername = username + connection.getServiceName();
                Log.e("service", connection.getServiceName());
                AccountManager accountManager = AccountManager.getInstance(connection);
                accountManager.createAccount(username, newpassword);
                status = true;
            } catch (SmackException.NoResponseException e) {
                status = false;
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
                status = false;
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                status = false;
            }
            connection.disconnect();
            return status;

        }


        private void generateChatPush(String toUserName, String toUserId, String toUserPhoto, String message, Context context) {
            Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(Constants.NEW_MESSAGE)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setContentText(toUserName + " :" + message);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

            // Customizing the push notification
            Notification notification = mBuilder.build();

            long[] vibrate = {
                    0, 300, 200, 300, 200
            };
            notification.vibrate = vibrate;
            notification.ledARGB = 0xff00ff00;
            notification.ledOnMS = 500;
            notification.ledOffMS = 500;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                notification.priority = Notification.PRIORITY_HIGH;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                notification.visibility = Notification.VISIBILITY_PUBLIC;
            Intent notificationIntent = new Intent(context, HomeActivity.class);
            notificationIntent.putExtra(Constants.TO_USER_ID, toUserId);
            notificationIntent.putExtra(Constants.TO_USER_NAME, toUserName);
            notificationIntent.putExtra(Constants.TO_USER_PHOTO, toUserPhoto);
            notificationIntent.putExtra(Constants.START_ONE_TO_ONE_CHAT, true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notification.contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationManager.notify(0, notification);
        }
    }

    public Set<Integer> getOnlineUsers() {
        return onlineUsers;
    }


    public static ArrayList<HashMap<String, String>> getOnline() {
        ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();


        Presence presence = new Presence(Presence.Type.available);

        try {
            connection.sendPacket(presence);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }


        final Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();

        for (RosterEntry entry : entries) {

            HashMap<String, String> map = new HashMap<String, String>();
            Presence entryPresence = roster.getPresence(entry.getUser());

            Presence.Type type = entryPresence.getType();
            if (entry != null) {
                if (entry.getName() != null) {
                    map.put("USER", entry.getName().toString());
                    map.put("STATUS", type.toString());
                    usersList.add(map);
                }
            }

        }
        return usersList;

    }
}