package com.zebra.demo;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.Arrays;
import java.util.HashSet;

public class ApplicationScanInfo {

    public static HashSet<String> getAppPermissionDetails(PackageManager pm, PackageInfo packageInfo){
            String[] permissions = packageInfo.requestedPermissions;
            HashSet<String>permissionSet = new HashSet<>();
            if (permissions != null) {
                for (String permission : permissions) {
                    try {
                        PermissionInfo permissionInfo = pm.getPermissionInfo(permission, 0);
                        if (permissionInfo.protectionLevel == PermissionInfo.PROTECTION_SIGNATURE ||
                                permissionInfo.protectionLevel == PermissionInfo.PROTECTION_FLAG_DEVELOPMENT ||
                                permissionInfo.protectionLevel == PermissionInfo.PROTECTION_FLAG_PRIVILEGED      ) {
                            // App has signature or privileged permission
                            permissionSet.add(permission +" is not Normal or Dangerous permission ");
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return permissionSet;
   }




    public static HashSet<String> getsharedLiblist(PackageInfo packageInfo) {
            String[] sharedLibraries = packageInfo.applicationInfo.sharedLibraryFiles;
            return new HashSet<>(Arrays.asList(sharedLibraries));
    }

    public static HashSet<String> getComponents(PackageManager pm, PackageInfo packageInfo) {

        HashSet<String>ComponentSet = new HashSet<>();

        // Check services
        checkExportedComponentsWithoutPermission(packageInfo.services, "Service",ComponentSet);

        // Check receivers
        checkExportedComponentsWithoutPermission(packageInfo.receivers, "Receiver",ComponentSet);

        // Check content providers
        checkExportedComponentsWithoutPermission(packageInfo.providers, "Provider",ComponentSet);

        return ComponentSet;
    }



    private static void checkExportedComponentsWithoutPermission(ServiceInfo[] services, String componentType, HashSet<String> componentSet) {
        if (services != null) {
            for (ServiceInfo service : services) {
                // Check if the service is exported and has no permission
                if (service.exported && service.permission == null) {
                    componentSet.add( componentType + " Name: " + service.name + " is exported without any permissions.");
                }
            }
        }
    }

    private static void checkExportedComponentsWithoutPermission(ActivityInfo[] receivers, String componentType,HashSet<String> componentSet) {
        if (receivers != null) {
            for (ActivityInfo receiver : receivers) {
                // Check if the receiver is exported and has no permission
                if (receiver.exported && receiver.permission == null) {
                    componentSet.add(componentType + " Name: " + receiver.name + " is exported without any permissions.");
                }
            }
        }
    }

    private static void checkExportedComponentsWithoutPermission(ProviderInfo[] providers, String componentType,HashSet<String> componentSet) {
        if (providers != null) {
            for (ProviderInfo provider : providers) {
                // Check if the provider is exported and has no read/write permissions
                if (provider.exported && provider.readPermission == null && provider.writePermission == null) {
                    componentSet.add( componentType + " Name: " + provider.name + " is exported without any permissions.");
                }
            }
        }
    }


    public  static boolean parseNetworkSecurityConfigForClearTextTraffic(XmlResourceParser parser) {
        try {

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if ("base-config".equals(tagName)) {
                            // Check if cleartextTrafficPermitted is true
                            String cleartextAttr = parser.getAttributeValue(null, "cleartextTrafficPermitted");
                            if ("true".equals(cleartextAttr)) {
                                return true;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean parseNetworkSecurityConfigForUserCertTrust(XmlResourceParser parser) {
        try {

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if ("certificates".equals(tagName)) {
                            // Check if certificates src is "user"
                            String certSrc = parser.getAttributeValue(null, "src");
                            if ("user".equals(certSrc)) {
                               return true;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
