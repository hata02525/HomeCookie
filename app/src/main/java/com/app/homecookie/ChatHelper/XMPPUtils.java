package com.app.homecookie.ChatHelper;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import static com.app.homecookie.ChatHelper.Config.DOMAIN_URL;
import static com.app.homecookie.ChatHelper.Config.GROUP_CHAT_SERVER;

/**
 * Created by ankur on 29/7/17.
 */

class XMPPUtils {
    public static MultiUserChat getMultiUserChat(XMPPTCPConnection xmpptcpConnection, String groupId){
        String roomJID = XMPPUtils.getRoomJID(groupId);
        MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(xmpptcpConnection);
        return multiUserChatManager.getMultiUserChat(roomJID);
    }
    public static String getRoomJID(String roomName) {
        return roomName + "@" + GROUP_CHAT_SERVER;
    }

    public static String getUserJID(String username) {
        return username + "@" + DOMAIN_URL;
    }

    public static final String DEFAULT_USER_RESOURCE = "Smack";
}

