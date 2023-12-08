package com.samm.estalem.Util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.samm.estalem.Activities.Provider.ReceiveProviderOrderActivity;
import com.samm.estalem.Classes.HttpsTrustManager;
import com.samm.estalem.Classes.ViewModel.TrackingViewModel;
import com.samm.estalem.R;


public class ReceiveOrderService extends Service {

    public static HubConnection hubConnection = HubConnectionBuilder.create("http://astalem.com:5000/orderHubs").build();

    Context Context;

    public ReceiveOrderService() {
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanencea";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder  notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(R.drawable.abot_as)
                .setColor(Color.RED)
                .setSmallIcon(R.drawable.logo)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

             //   hubConnection//http://192.168.43.174:80



        HttpsTrustManager.allowAllSSL();
            hubConnection.start();



        new CountDownTimer(5000, 1000) {
        public void onTick(long millisUntilFinished) {
        }
        public void onFinish() {
            if(hubConnection.getConnectionState()== HubConnectionState.CONNECTED){
               SaveProviderConnectionId(SharedpreferencesData.getValuePreferences(getBaseContext(),"providerPhone",""));
               ReceiveOrder();
            }
        }
           }.start();

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  if(hubConnection.getConnectionState()== HubConnectionState.CONNECTED)
         //   hubConnection.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void SaveProviderConnectionId(String id){
     if(hubConnection.getConnectionState()== HubConnectionState.CONNECTED)
        hubConnection.send("SaveProviderConnectionId",id);
    }
    public static void SendLocation(TrackingViewModel trackingViewModel){

        if(hubConnection.getConnectionState()== HubConnectionState.CONNECTED){
            hubConnection.send("SendLocation",trackingViewModel);
        }else {
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    try {
                        hubConnection.start();
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                try {
                                    if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED)
                                        hubConnection.send("SendLocation", trackingViewModel);
                                } catch (Exception e) {
                                }
                            }
                        }.start();
                    } catch (Exception e) {
                    }
                }
            }.start();
        }


    }

    @SuppressLint("LongLogTag")
    public void ReceiveOrder() {
        try {
            hubConnection.on("ReceiveOrder", (orderId) -> {
                Intent intent = new Intent(ReceiveOrderService.this, ReceiveProviderOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("orderId", orderId);
                startActivity(intent);
            }, Integer.class);
        } catch (Exception e) {
        }
    }


}
