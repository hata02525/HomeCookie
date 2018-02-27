package com.app.homecookie.ChatHelper.model;

/**
 * Created by ${Devendra} on 4/5/2016.
 */
public class MessageModel {
    /**
     * name : dhiman
     * lastMessage : {"id":"27A6466B-CBF0-4589-843E-FF7D46912752","messageBody":"Hi"}
     * displayPic : http://67.209.121.170/homecookie/usersProfile/1496902165_1496902163.jpeg
     * id : 60
     */

    private String name;
    private LastMessageBean lastMessage;
    private String displayPic;
    private String id;
    private String receiver;
    private int msgIdl;
    private boolean isMine;
    private String type;
    private String sender;
    private String msg;

    public MessageModel() {

    }

    public MessageModel(String name, LastMessageBean lastMessage,
                        String displayPic, String id, String receiver, int msgIdl, boolean isMine, String type, String sender, String msg) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.displayPic = displayPic;
        this.id = id;
        this.receiver = receiver;
        this.msgIdl = msgIdl;
        this.isMine = isMine;
        this.type = type;
        this.sender = sender;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LastMessageBean getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessageBean lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(String displayPic) {
        this.displayPic = displayPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getMsgIdl() {
        return msgIdl;
    }

    public void setMsgIdl(int msgIdl) {
        this.msgIdl = msgIdl;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean mine) {
        isMine = mine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class LastMessageBean {
        private String id;
        private String messageBody;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessageBody() {
            return messageBody;
        }

        public void setMessageBody(String messageBody) {
            this.messageBody = messageBody;
        }
    }
   /* String sender;
    String receiver;
    String receiverName;
    String msg;
    String type;
    String receiverPic;
    public boolean isMine;
    public int getMsgIdl() {
        return msgIdl;
    }

    public void setMsgIdl(int msgIdl) {
        this.msgIdl = msgIdl;
    }

    int msgIdl;
    public MessageModel(){

    }
    public MessageModel(String sen, String rec, String msgBody, String msgtype, boolean whoIsSender,String receiverName,String receiverPic, int Id){
        this.msgIdl=Id;
        this.sender=sen;
        this.receiver=rec;
        this.msg=msgBody;
        this.type=msgtype;
        this.isMine=whoIsSender;
        this.receiverPic = receiverPic;
        this.receiverName = receiverName;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public String getReceiverPic() {
        return receiverPic;
    }

    public void setReceiverPic(String receiverPic) {
        this.receiverPic = receiverPic;
    }*/



}
