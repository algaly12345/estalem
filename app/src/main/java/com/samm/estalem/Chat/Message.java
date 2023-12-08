package com.samm.estalem.Chat;

import android.net.Uri;

class Message {
    public String text;
    //   private MemberData memberData;
    public String belongsToCurrentUser;
    Uri uri;
    public String dateTime;
    public Message(String text, String belongsToCurrentUser,Uri uri,String DateTime) {
        this.text = text;
        this.uri=uri;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.dateTime = DateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
