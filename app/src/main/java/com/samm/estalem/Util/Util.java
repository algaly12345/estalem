package com.samm.estalem.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;

public class Util {
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void language(Activity activity) {
        Locale locale = new Locale(SharedpreferencesData.getValuePreferences(activity, "language", ""));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getResources().updateConfiguration(config,
                activity.getResources().getDisplayMetrics());
    }


    public static void isNetworkAvailable(Activity baseContext) {
        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(baseContext).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
               // String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
             if(isNetworkAvailable)
             {
               //Toast.makeText(context, "av", Toast.LENGTH_SHORT).show();
                context.startService( new Intent(baseContext,ReceiveOrderService.class));
             }else {
             //    Toast.makeText(context, "diav", Toast.LENGTH_SHORT).show();
                 context.stopService( new Intent(baseContext,ReceiveOrderService.class));

             }




            }
        }, intentFilter);
    }

}
