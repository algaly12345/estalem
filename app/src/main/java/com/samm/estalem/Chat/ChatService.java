package com.samm.estalem.Chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.samm.estalem.R;
import com.samm.estalem.Util.SharedpreferencesData;

public class ChatService extends Service {
    public static HubConnection hubConnection;
    Activity activity;
    String userId;
    android.content.Context Context;

    public ChatService() {
    }

    public ChatService(Activity activity) {
        this.activity = activity;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanencea";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(R.drawable.circle)
                .setColor(Color.RED)
                .setSmallIcon(R.drawable.circle)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onCreate() {

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
//            startMyOwnForeground();
//        else
//            startForeground(1, new Notification());

        hubConnection = HubConnectionBuilder.create("http://astalem.com:8080/chatHubs").build();//http://192.168.43.174:80
        hubConnection.start();
        ReceiveMessage();

        if (SharedpreferencesData.getValuePreferences(ChatService.this, "clientPhone", "0").equals("0")) {
            userId =""+SharedpreferencesData.getValuePreferences(ChatService.this, "providerPhone", "");
        }else {
            userId =""+ SharedpreferencesData.getValuePreferences(ChatService.this, "clientPhone", "");
        }

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
                    SaveUserConnectionId(userId);
                }
            }
        }.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void SaveUserConnectionId(String userId) {
        hubConnection.send("SaveUserConnectionId", userId);
    }


//    public void ReceiveOrders(){
//        hubConnection.send("ReceiveOrder",5);
//    }


    @SuppressLint("LongLogTag")
    public void ReceiveMessage() {
        hubConnection.on("ReceiveMessage", (Message, otherUserId, DateTimeNow, MessageType) -> {
            try {
                if (MessageType.equals("text")) {
                    ChatActivity.receiveMessage(Message, DateTimeNow);
                }else {
                    ChatActivity.receiveImage(Message, DateTimeNow);
                }
            } catch (Exception e) {

            }
        }, String.class, String.class, String.class, String.class);
    }
}