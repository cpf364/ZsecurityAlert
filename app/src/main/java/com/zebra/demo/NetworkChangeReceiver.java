package com.zebra.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Inject
    Set<String> uniqueSSIDs;

    private static String TAG = "ZSecurityAlert";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ((MyApplication) context.getApplicationContext()).getAppComponent().inject(this);
        switch (Objects.requireNonNull(intent.getAction())){
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                Log.d(TAG, "onReceive");
                checkWifiSecurity(context);
                break;

        }
    }

    public  void checkWifiSecurity(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String ssid = wifiInfo.getSSID();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    int securityType = 0;
                    securityType = wifiInfo.getCurrentSecurityType();
                    String securityTypeString = getSecurityTypeString(securityType);
                    uniqueSSIDs.add("SSID: " + ssid + ", Security Type: " + securityTypeString);
                    Log.d(TAG, "SSID: " + ssid + ", Security Type: " + securityTypeString);
                } else
                    Log.d(TAG, "SSID: " + ssid + ", Security Type: Not available on this API level");
            }
        }
    }

    private  String getSecurityTypeString(int securityType) {
        switch (securityType) {
            case WifiInfo.SECURITY_TYPE_OPEN:
                return "Open";
            case WifiInfo.SECURITY_TYPE_WEP:
                return "WEP";
            case WifiInfo.SECURITY_TYPE_PSK:
                return "WPA/WPA2";
            case WifiInfo.SECURITY_TYPE_EAP:
                return "EAP";
            case WifiInfo.SECURITY_TYPE_SAE:
                return "WPA3 SAE";
            case WifiInfo.SECURITY_TYPE_OWE:
                return "OWE";
            default:
                return "Unknown";
        }
    }
}