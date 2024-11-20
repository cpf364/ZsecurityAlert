package com.zebra.demo;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

public class JsonConverter {

    @Inject
    Map<String, AppLogData> appLogMap;


    @Inject
    Map<String, AppManifestScanData> appManifestMap;
    @Inject
    List<CrashInfo> crashInfoList;

    @Inject
    Set<String> uniqueSSIDs;
    Context mConText;

    public JsonConverter(Context applicationContext) {
        mConText = applicationContext;
        ((MyApplication) applicationContext).getAppComponent().inject(this);
    }

    public String generateRequestId() {
        long timestamp = System.currentTimeMillis(); // Current timestamp in milliseconds
        int randomNum = new Random().nextInt(9999); // Random number to add uniqueness
        return timestamp + "_" + randomNum;
    }

    public JSONObject convertToJson(Context context) {


        JSONObject jsonObject = new JSONObject();

        try {
            // Static data
            jsonObject.put("callerApp", context.getPackageName());
            jsonObject.put("requestId", generateRequestId());
            jsonObject.put("feature", "ZEBRA_SECURITY_MANAGER");
            jsonObject.put("action", "processSecurityData");

            // Create the data object
            JSONObject deprecatedAPIObj = new JSONObject();
            deprecatedAPIObj.put("category", "API Usage");

            JSONArray reportsArray = new JSONArray();

            // Create the Apps array
            JSONArray appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppLogData> entry : appLogMap.entrySet()) {
                String appName = entry.getKey();
                AppLogData appLogData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods
                for (String method : appLogData.getMethods()) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("method", method);
                    vulnerabilitiesArray.put(vulnerabilityObject);
                }
                for (String method : appLogData.getFields()) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("field", method);
                    vulnerabilitiesArray.put(vulnerabilityObject);
                }


                // Add the vulnerabilities array to the app object
                appObject.put("Vulnerabilities", vulnerabilitiesArray);

                // Add appObject to appsArray
                appsArray.put(appObject);
            }

            // Add the apps array to the data object
            deprecatedAPIObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            // jsonObject.put("data", deprecatedAPIObj);
            reportsArray.put(deprecatedAPIObj);

            /*------------------------------------------------------------------------------------------------*/

            JSONObject networkSecurityObj = new JSONObject();
            networkSecurityObj.put("category", "Network Security");


            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestScanData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods
                if (appManifestScanData.isCleartextTrafficPermitted()) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("Clear Text Traffic", true);
                    //vulnerabilityObject descrption we will try to add
                    vulnerabilitiesArray.put(vulnerabilityObject);
                    // Add the vulnerabilities array to the app object
                    appObject.put("Vulnerabilities", vulnerabilitiesArray);
                    // Add appObject to appsArray
                    appsArray.put(appObject);
                }

            }

            // Add the apps array to the data object
            networkSecurityObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            //jsonObject.put("data", networkSecurityObj);
            reportsArray.put(networkSecurityObj);

            /*------------------------------------------------------------------------------------------------*/
            JSONObject wifObj = new JSONObject();
            wifObj.put("category", "Wi-Fi  Security");
// Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (String info : uniqueSSIDs) {
                String appName = "WI-FI";

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods

                JSONObject vulnerabilityObject = new JSONObject();
                vulnerabilityObject.put("WI-FI Info ", info);
                vulnerabilitiesArray.put(vulnerabilityObject);


                // Add the vulnerabilities array to the app object
                appObject.put("Vulnerabilities", vulnerabilitiesArray);

                // Add appObject to appsArray
                appsArray.put(appObject);
            }

            // Add the apps array to the data object
            wifObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            //jsonObject.put("data", crashReportObj);
            reportsArray.put(wifObj);

            /*------------------------------------------------------------------------------------------------*/
            JSONObject certManagementObj = new JSONObject();
            certManagementObj.put("category", "Certificate Management");


            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestScanData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods
                if (Boolean.getBoolean(appManifestScanData.getIsUserCertificateTruest())) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("User Installed Certificate Trusting ", true);
                    //vulnerabilityObject descrption we will try to add
                    vulnerabilitiesArray.put(vulnerabilityObject);
                    // Add the vulnerabilities array to the app object
                    appObject.put("Vulnerabilities", vulnerabilitiesArray);

                    // Add appObject to appsArray
                    appsArray.put(appObject);
                }


            }

            // Add the apps array to the data object
            certManagementObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            // jsonObject.put("data", certManagementObj);
            reportsArray.put(certManagementObj);



            /*------------------------------------------------------------------------------------------------*/
            JSONObject appSigningObj = new JSONObject();
            appSigningObj.put("category", "Application Signing  ");

            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestScanData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods

                JSONObject vulnerabilityObject = new JSONObject();
                vulnerabilityObject.put("App Signing Version/Schema ", appManifestScanData.getSigningInfo());
                //vulnerabilityObject descrption we will try to add
                vulnerabilitiesArray.put(vulnerabilityObject);


                // Add the vulnerabilities array to the app object
                appObject.put("Vulnerabilities", vulnerabilitiesArray);

                // Add appObject to appsArray
                appsArray.put(appObject);
            }

            // Add the apps array to the data object
            appSigningObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            //jsonObject.put("data", appSigningObj);
            reportsArray.put(appSigningObj);


            /*------------------------------------------------------------------------------------------------*/
            JSONObject appBackupObj = new JSONObject();
            appBackupObj.put("category", "Application Backup  ");

            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestScanData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods

                JSONObject vulnerabilityObject = new JSONObject();
                vulnerabilityObject.put("App Backup Details ", appManifestScanData.getIsBackup());
                //vulnerabilityObject descrption we will try to add
                vulnerabilitiesArray.put(vulnerabilityObject);


                // Add the vulnerabilities array to the app object
                appObject.put("Vulnerabilities", vulnerabilitiesArray);

                // Add appObject to appsArray
                appsArray.put(appObject);
            }

            // Add the apps array to the data object
            appBackupObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            //jsonObject.put("data", appBackupObj);
            reportsArray.put(appBackupObj);

            /*------------------------------------------------------------------------------------------------*/
            JSONObject appDebugObj = new JSONObject();
            appDebugObj.put("category", " Application Debuggable or Test Mode  ");

            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestScanData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods
                if (Boolean.getBoolean(appManifestScanData.getIsdebuggable_testonly())) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("App Debuggable or Test mode  Running ", appManifestScanData.getIsdebuggable_testonly());
                    //vulnerabilityObject descrption we will try to add
                    vulnerabilitiesArray.put(vulnerabilityObject);
                    // Add the vulnerabilities array to the app object
                    appObject.put("Vulnerabilities", vulnerabilitiesArray);

                    // Add appObject to appsArray
                    appsArray.put(appObject);
                }


            }

            // Add the apps array to the data object
            appDebugObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            // jsonObject.put("data", appDebugObj);
            reportsArray.put(appDebugObj);

            /*------------------------------------------------------------------------------------------------*/
            JSONObject appPermissionObj = new JSONObject();
            appPermissionObj.put("category", " Application Permissions");

            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods
                for (String permission : appManifestData.getPermissionInfo()) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("permission", permission);
                    vulnerabilitiesArray.put(vulnerabilityObject);
                    // Add the vulnerabilities array to the app object
                    appObject.put("Vulnerabilities", vulnerabilitiesArray);

                    // Add appObject to appsArray
                    appsArray.put(appObject);
                }


            }

            // Add the apps array to the data object
            appPermissionObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            //jsonObject.put("data", appPermissionObj);
            reportsArray.put(appPermissionObj);

            /*------------------------------------------------------------------------------------------------*/
            JSONObject appComponentObj = new JSONObject();
            appComponentObj.put("category", " Application components");


            // Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (Map.Entry<String, AppManifestScanData> entry : appManifestMap.entrySet()) {
                String appName = entry.getKey();
                AppManifestScanData appManifestData = entry.getValue();

                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods
                for (String component : appManifestData.getComponents()) {
                    JSONObject vulnerabilityObject = new JSONObject();
                    vulnerabilityObject.put("component", component);
                    vulnerabilitiesArray.put(vulnerabilityObject);
                    // Add the vulnerabilities array to the app object
                    appObject.put("Vulnerabilities", vulnerabilitiesArray);

                    // Add appObject to appsArray
                    appsArray.put(appObject);
                }


            }

            // Add the apps array to the data object
            appComponentObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            // jsonObject.put("data", appComponentObj);
            reportsArray.put(appComponentObj);

            /*------------------------------------------------------------------------------------------------*/
            JSONObject crashReportObj = new JSONObject();
            crashReportObj.put("category", "Crash Report");

// Create the Apps array
            appsArray = new JSONArray();

            // Iterate through appLogMap
            for (CrashInfo info : crashInfoList) {
                String appName = info.getApplicationName();
                Log.d("AJAY", "Crash convertToJson: "+appName);
                // Create JSON object for each app
                JSONObject appObject = new JSONObject();
                appObject.put("Application", appName);

                // Create Vulnerabilities array
                JSONArray vulnerabilitiesArray = new JSONArray();

                // Add methods

                JSONObject vulnerabilityObject = new JSONObject();
                vulnerabilityObject.put("Crash Info ", info.getStackTrace());
                vulnerabilitiesArray.put(vulnerabilityObject);


                // Add the vulnerabilities array to the app object
                appObject.put("Vulnerabilities", vulnerabilitiesArray);

                // Add appObject to appsArray
                appsArray.put(appObject);
            }

            // Add the apps array to the data object
            crashReportObj.put("Apps", appsArray);

            // Add the data object to the main JSON object
            //jsonObject.put("data", crashReportObj);
            reportsArray.put(crashReportObj);


            /*------------------------------------------------------------------------------------------------*/

            jsonObject.put("data", reportsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
