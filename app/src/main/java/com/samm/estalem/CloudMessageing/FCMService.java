package com.samm.estalem.CloudMessageing;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.samm.estalem.Activities.Client.ClientMainActivity;
import com.samm.estalem.Activities.Provider.ProviderMainActivity;
import com.samm.estalem.Chat.ChatActivity;
import com.samm.estalem.Chat.ChatService;
import com.samm.estalem.Util.SharedpreferencesData;
import com.samm.estalem.Util.Util;

import java.util.Random;

public class FCMService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent;
        String phone = SharedpreferencesData.getValuePreferences(this, "providerPhone", "")+SharedpreferencesData.getValuePreferences(this, "clientPhone", "");

        if(phone.equals(""))
        {return;}
        if (remoteMessage.getData().get("type").equals("order")) {
            if (SharedpreferencesData.getValuePreferences(getApplicationContext(), "clientPhone", "").isEmpty()) {
                intent = new Intent(getApplicationContext(), ProviderMainActivity.class);

            } else {
                intent = new Intent(getApplicationContext(), ClientMainActivity.class);
            }
            NewMessageNotification newMessageNotification = new NewMessageNotification(getApplicationContext(), intent);
            int id = new Random().nextInt(100);
            newMessageNotification.message(id, remoteMessage.getData().get("title"), "", remoteMessage.getData().get("body"));
        }

        if (!Util.isMyServiceRunning(getApplicationContext(), ChatService.class)) {
            if (remoteMessage.getData().get("type").equals("chat")) {
                intent = new Intent(getApplicationContext(), ChatActivity.class);
                NewMessageNotification newMessageNotification = new NewMessageNotification(getApplicationContext(), intent);
                int id = new Random().nextInt(100);
                newMessageNotification.message(id, remoteMessage.getData().get("title"), remoteMessage.getData().get("phone"), remoteMessage.getData().get("body"));
            }
        }
    }
}
