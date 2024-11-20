package com.zebra.demo;

import android.util.Log;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private static final String METHOD_REGEX = "Accessing hidden method (L.+?);";
    private static final String FIELD_REGEX = "Accessing hidden field (L.+?);";
    private static final String APP_REGEX = "^(.+?):";
    private static String TAG = "ZSecurityAlert";
    public static Map<String, List<String>> parseLog(String logContent) {
        Map<String, List<String>> appVulnerabilities = new HashMap<>();
        Pattern pattern = Pattern.compile("\\s([\\w\\.]+):\\s(Accessing hidden (method|field)\\s(L[^;]+;->[^\\s]+))");
        String[] lines = logContent.split("\n");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String appName = matcher.group(1);  // Application name
                String type = matcher.group(2);     // Access type (method/field)
                String accessData = matcher.group(3);  // Method/field data
                Log.d(TAG, "onClick: appName :: " + appName + " type  :: " + type + " accessData :: " + accessData);

            }
        }
        /*for (String line : lines) {
            if (line.contains("Accessing hidden")) {
                // Extract application name
                String appName = line.split(" ")[5];  // The application name in the log

                // Extract method or field
                String vulnerability = line.substring(line.indexOf("Accessing hidden "));

                // Add to map
                if (!appVulnerabilities.containsKey(appName)) {
                    appVulnerabilities.put(appName, new ArrayList<>());
                }
                if (!appVulnerabilities.get(appName).contains(vulnerability)) {
                    appVulnerabilities.get(appName).add(vulnerability);
                }
            }*/
        // }
        return appVulnerabilities;

    }
}
