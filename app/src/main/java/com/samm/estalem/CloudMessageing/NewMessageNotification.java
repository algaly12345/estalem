package com.samm.estalem.CloudMessageing;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.samm.estalem.R;

import java.util.UUID;


/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p>
 * class to create notifications in a backward-compatible way.
 */
public class NewMessageNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "رسالة جديدة";
    private static final String CHANNEL_ID = "id";
    Context context;
    public Intent intent;

    public NewMessageNotification(Context context, Intent Intent) {
        this.context = context;
        this.intent = Intent;
    }

    public void message(int id,String title,String phone,String bigContain) {


        MediaPlayer mMediaPlayer = MediaPlayer.create(context, R.raw.piece);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.start();




     //   PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent pendingIntent;

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("phone",phone);
         pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, createNotificationChannel())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigContain))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setDefaults(Notification.DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


// notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }

    private String createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel chan1 = new NotificationChannel("default", "default", NotificationManager.IMPORTANCE_LOW);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            channel.setDescription("no sound");
            channel.setSound(null,null);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);


            notificationManager.createNotificationChannel(channel);
        }            return CHANNEL_ID;

    }
}


// Create an explicit intent for an Activity in your app




