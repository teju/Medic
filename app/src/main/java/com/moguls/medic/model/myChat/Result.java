package com.moguls.medic.model.myChat;

public class Result {
    public ChatObj getChat() {
        return Chat;
    }

    public void setChat(ChatObj chat) {
        Chat = chat;
    }

    ChatObj Chat;

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String PhotoUrl;
    String Name;
    String ID;

    public String getUnreadCount() {
        return UnreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        UnreadCount = unreadCount;
    }

    String UnreadCount;

}
