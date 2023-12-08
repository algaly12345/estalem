package com.samm.estalem.Util;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import io.branch.referral.Branch;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class CheckInternetApp  extends Application {
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";

    @Override
    public void onCreate() {
        super.onCreate();
        registerForNetworkChangeEvents(this);
        Branch.getAutoInstance(this);
    }

    public static void registerForNetworkChangeEvents(final Context context) {
        NetworkStateChangeReceiver networkStateChangeReceiver = new NetworkStateChangeReceiver();
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
    }


}
