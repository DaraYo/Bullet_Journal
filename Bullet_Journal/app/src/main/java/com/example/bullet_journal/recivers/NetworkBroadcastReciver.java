package com.example.bullet_journal.recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkBroadcastReciver extends BroadcastReceiver {

    private boolean wifiOn;
    private boolean dataOn;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            wifiOn = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            dataOn = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiOn = false;
            dataOn = false;
        }
    }

    public boolean isWifiOn() {
        return wifiOn;
    }

    public boolean isDataOn() {
        return dataOn;
    }
}