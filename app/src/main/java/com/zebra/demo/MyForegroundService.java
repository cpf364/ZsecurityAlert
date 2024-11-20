package com.zebra.demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.SigningDetails;
import android.content.pm.SigningInfo;
import android.content.res.XmlResourceParser;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.zebra.zebradna.IZDNARequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class MyForegroundService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Inject
    Map<String, AppLogData> appLogMap;


    @Inject
    Map<String, AppManifestScanData> appManifestMap;
    @Inject
    List<CrashInfo> crashInfoList;
    private static String TAG = "ZSecurityAlert-MyForegroundService";

    private IZDNARequest mZDNARequest;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);

        ((MyApplication) getApplication()).getAppComponent().inject(this);

        bindZDNAClient();


    }
    private void bindZDNAClient() {
        Log.d(TAG, "bindZDNAClient: ");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.zebra.zebradna", "com.zebra.zebradna.app.core.android.services.ZDNARequestService"));
        bindService(intent, mZDNAConnection, Context.BIND_AUTO_CREATE);
    }

    public void sendDatatoZDNA(String jsonOutput) {
        String response = null;
        try {
            byte[] jsonBytes = jsonOutput.getBytes(StandardCharsets.UTF_8);
            Log.d(TAG, "generateJsonFromLogs: size::  "+jsonBytes);
            response = mZDNARequest.processRequest(jsonOutput);
            Log.d(TAG, "generateJsonFromLogs: response::  "+response);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }


    private ServiceConnection mZDNAConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, " IZDNARequest before connected");
            mZDNARequest = IZDNARequest.Stub.asInterface((IBinder) service);


            if (mZDNARequest != null) {
                Log.i(TAG, " Connected to IZDNARequest :: " + mZDNARequest);

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, " IZDNARequest disconnected");
            mZDNARequest = null;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        // Create a notification for the foreground service

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ZSecurityMonitor")
                .setContentText("Monitoring WiFi connections...")
                .setSmallIcon(R.drawable.ic_shield)
                .build();

        // Start the service in the foreground
        startForeground(1, notification);

        // Add your logic to monitor WiFi or any other tasks here



        /*JsonConverter jsonConvert = new JsonConverter(getApplicationContext());
        JSONObject obj = jsonConvert.convertToJson(getApplicationContext());
        Log.d(TAG, "readFileAndParseData: " + obj.toString());
        sendDatatoZDNA(obj.toString());*/


        return START_STICKY;  // Ensures the service restarts if the system kills it
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup if necessary when service stops
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Not using binding, return null
        return null;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


    private void doAppCrashSacn() {
        Log.d(TAG, "doAppCrashSacn: Start");
        File crashfile = new File(Environment.getExternalStorageDirectory(), "zalert_crash.txt");
        if (crashfile.exists()) {
            CrashLogParser crashLogParser = new CrashLogParser();
            crashLogParser.parseLog(crashfile,crashInfoList);
            Log.d(TAG, "doInBackground: " + crashInfoList);
        }
        Log.d(TAG, "doAppCrashSacn: End");
    }
    private void readFileAndParseData() {
        Log.d(TAG, "readFileAndParseData: Start");
        File file = new File(Environment.getExternalStorageDirectory(), "zalert.txt");
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                Pattern pattern = Pattern.compile("\\s([\\w\\.]+):\\sAccessing hidden (method|field)\\s([\\w\\/;<>\\(\\)]+)(.*)");
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String appName = matcher.group(1);  // Application name
                        String type = matcher.group(2);     // Access type (method/field)
                        String accessData = matcher.group(3);  // Method/field data
                        String additionalInfo = matcher.group(4);  // Additional information in parentheses
                        //  Log.d("AJAY", "onClick: LableNam appName :: "+appName + " type  :: "+type +" accessData :: " +accessData +" additionalInfo :: "+additionalInfo);

                        AppInfo appInfo = getAppInfo(appName);
                        if (appInfo == null) {
                            Log.d(TAG, "readFileAndParseData: appName :: " + appName);
                            continue;
                        }
                        if(!appInfo.isThirdParty()){
                            Log.d(TAG, "readFileAndParseData: ");
                            continue;
                        }
                        // Check if the app is already in the map
                        AppLogData appLogData = appLogMap.getOrDefault(appInfo.getAppName(), new AppLogData(appInfo.getAppName()));

                        // Determine whether it's a method or a field and add to the respective set
                        if (type.contains("method")) {
                            String methodData = accessData + additionalInfo;
                            appLogData.addMethod(methodData);
                        } else if (type.contains("field")) {
                            String fieldData = accessData + additionalInfo;
                            appLogData.addField(fieldData);
                        }

                        // Update the map
                        appLogMap.put(appInfo.getAppName(), appLogData);
                    }
                }

                for (String name : appLogMap.keySet()) {
                    String key = name.toString();
                    String value = appLogMap.get(name).toString();
                    Log.d(TAG, "onClick: key :: " + key + "  value :: " + value);
                }

                // adapter = new ApplicationAdapter(getApplicationContext(), appLogMap);
                // recyclerView.setAdapter(adapter);


                Log.d(TAG, "readFileAndParseData: End");
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Log.d(TAG, "File not found! ");
        }


    }


    public void doAppManfiestSacn() {
        Log.d(TAG, "doAppManfiestSacn: Start");
        PackageManager pm = getPackageManager();

        List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_SIGNING_CERTIFICATES
                | PackageManager.GET_PERMISSIONS | PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES | PackageManager.GET_PROVIDERS);
        for (PackageInfo packageInfo : packages) {


            try {


                ApplicationInfo appInfo = packageInfo.applicationInfo;

                boolean isThirdParty = (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
                if(!isThirdParty){
                    continue;
                }
                String appLabel = pm.getApplicationLabel(appInfo).toString();
                AppManifestScanData appManifestScanData = appManifestMap.getOrDefault(appLabel, new AppManifestScanData(appLabel));
                int res = appInfo.networkSecurityConfigRes;
                XmlResourceParser parser = pm.getXml(appInfo.packageName, res, appInfo);

                boolean isUserCertifcate = ApplicationScanInfo.parseNetworkSecurityConfigForUserCertTrust(parser);
                boolean isClearTextTraffic_networkconfig = ApplicationScanInfo.parseNetworkSecurityConfigForClearTextTraffic(parser);

                boolean isClearTextTraffic = (appInfo.flags & ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0;

                appManifestScanData.setCleartextTrafficPermitted(isClearTextTraffic || isClearTextTraffic_networkconfig);
                appManifestScanData.setIsUserCertificateTruest(String.valueOf(isUserCertifcate));

                boolean allowBackup = (appInfo.flags & ApplicationInfo.FLAG_ALLOW_BACKUP) != 0;
                boolean isDebuggable = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
                boolean isTest = (appInfo.flags & ApplicationInfo.FLAG_TEST_ONLY) != 0;

                //apk signing alrd agiry weak
                //user CErtifcation

                appManifestScanData.setBackup(String.valueOf(allowBackup));
                appManifestScanData.setIsdebuggable_testonly(String.valueOf(isDebuggable || isTest));


                SigningInfo sInfo = packageInfo.signingInfo;
                SigningDetails sd = sInfo.getApkSignerDetails();
                appManifestScanData.setSigningInfo(String.valueOf(sd.getSignatureSchemeVersion()));
                appManifestScanData.setPermissionInfo(ApplicationScanInfo.getAppPermissionDetails(pm, packageInfo));
                //appManifestScanData.setSharedLibInfo(ApplicationScanInfo.getsharedLiblist(packageInfo));
                appManifestScanData.setComponents(ApplicationScanInfo.getComponents(pm, packageInfo));


                Log.d(TAG, "doAppManfiestSacn: " + appManifestScanData.toString());
                appManifestMap.put(appLabel, appManifestScanData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "doAppManfiestSacn: END");
    }


    private AppInfo getAppInfo(String appName) {
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packages) {
            String packageName = packageInfo.packageName;

            // Check if the package name matches the appName
            if (packageName.contains(appName)) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                String versionName = packageInfo.versionName != null ? packageInfo.versionName : "N/A";
                String appLabel = pm.getApplicationLabel(appInfo).toString();
                boolean isThirdParty = (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;  // Check if it's a third-party app

                // Return the AppInfo object
                return new AppInfo(packageName, appLabel, versionName, isThirdParty);
            }
        }
        return null;  // App not found
    }

}
