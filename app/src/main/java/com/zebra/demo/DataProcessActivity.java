package com.zebra.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.SigningDetails;
import android.content.pm.SigningInfo;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.zebra.zebradna.IZDNARequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import javax.inject.Inject;


public class DataProcessActivity extends AppCompatActivity {

    //  private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    @Inject
    Map<String, AppLogData> appLogMap;


    @Inject
    Map<String, AppManifestScanData> appManifestMap;
    @Inject
    List<CrashInfo> crashInfoList;


    private IZDNARequest mZDNARequest;
    private static String TAG = "ZSecurityAlert";
    ImageView windowsLogo;
    private ImageView progressImageView;
    private ObjectAnimator rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_screen);

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        //   Button GetData = (Button) findViewById(R.id.GetData);

        // Set up the RecyclerView
        //  recyclerView = findViewById(R.id.recyclerView);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(this));


      /*  GetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
*/

        // windowsLogo = findViewById(R.id.windows_logo);

        // Load the rotation animation
        //Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);

        // Start the animation
        // windowsLogo.startAnimation(rotateAnimation);

        progressImageView = findViewById(R.id.progressImage);

        startImageAnimation();

        // Call AsyncTask to read file and parse the data

        new ReadFileAndParseDataTask().execute("/path/to/your/file/zalert.txt");


        bindZDNAClient();

    }


    private void startImageAnimation() {
        rotateAnimation = ObjectAnimator.ofFloat(progressImageView, "rotation", 0f, 360f);
        rotateAnimation.setDuration(2000); // 2 seconds for a full rotation
        rotateAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimation.setRepeatMode(ObjectAnimator.RESTART);

        rotateAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // When the rotation completes, trigger a scale animation
                ScaleAnimation scaleDown = new ScaleAnimation(
                        1.0f, 0.5f, // From original size to half size
                        1.0f, 0.5f, // From original size to half size
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                );
                scaleDown.setDuration(200); // Scale down duration
                scaleDown.setFillAfter(true);

                // Scale up animation to return to normal size
                ScaleAnimation scaleUp = new ScaleAnimation(
                        0.5f, 1.0f, // From half size to original size
                        0.5f, 1.0f, // From half size to original size
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                );
                scaleUp.setDuration(200); // Scale up duration
                scaleUp.setFillAfter(true);
                scaleUp.setStartOffset(200); // Delay before starting scale up

                // Combine the two animations and apply to the ImageView
                progressImageView.startAnimation(scaleDown);
                progressImageView.postDelayed(() -> progressImageView.startAnimation(scaleUp), 200); // Start scale-up animation after scale-down
            }
        });

        // Start the rotation animation
        rotateAnimation.start();

     /*   // Stop animation after 10 seconds and open a new activity
        progressImageView.postDelayed(() -> {
            rotateAnimation.cancel();
            // Open new activity here (ScanResultActivity or similar)
            startActivity(new Intent(ZAlertProgressActivity.this, ZAlertResultActivity.class));
            finish();
        }, 10000);*/
    }

    // AsyncTask for reading the file and parsing data in background
    private class ReadFileAndParseDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show loading indicator if necessary

        }

        @Override
        protected String doInBackground(String... filePaths) {
            StringBuilder parsedData = new StringBuilder("");


                readFileAndParseData();



                doAppCrashSacn();


                doAppManfiestSacn();


            JsonConverter jsonConvert = new JsonConverter(getApplicationContext());
            JSONObject obj = jsonConvert.convertToJson(getApplicationContext());
            Log.d(TAG, "readFileAndParseData: " + obj.toString());
            sendDatatoZDNA(obj.toString());

            return parsedData.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Update the UI with the parsed data
            //
            rotateAnimation.cancel();

            // Delay for 3 seconds and then launch the MainActivity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(DataProcessActivity.this, AllCategoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000); // 3000 milliseconds = 3 seconds
        }
    }



    private void bindZDNAClient() {
        Log.d(TAG, "bindZDNAClient: ");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.zebra.zebradna", "com.zebra.zebradna.app.core.android.services.ZDNARequestService"));
        bindService(intent, mZDNAConnection, Context.BIND_AUTO_CREATE);
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
            Log.i(TAG, "IEthernetSettings disconnected");
            mZDNARequest = null;
        }
    };

    public void sendDatatoZDNA(String jsonOutput) {


        writeToFile("/sdcard/ZDNA.json",jsonOutput);
        String response = null;
        try {
            byte[] jsonBytes = jsonOutput.getBytes(StandardCharsets.UTF_8);
            Log.d(TAG, "generateJsonFromLogs: size::  "+jsonBytes);
            response = mZDNARequest.processRequest(jsonOutput);
            Log.d(TAG, "generateJsonFromLogs: response::  "+response);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }catch (Exception e){

              e.printStackTrace();

        }

    }
    public  void writeToFile(String fileName, String data ) {
        File file = new File(fileName);
        FileOutputStream fileOutputStream = null;
        try {
            // Write the data to the file
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
