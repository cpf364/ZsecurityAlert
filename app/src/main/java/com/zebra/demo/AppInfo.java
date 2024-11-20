package com.zebra.demo;

public class AppInfo {
    private String packageName;
    private String appName;
    private String versionName;
    private boolean isThirdParty;

    // Constructor
    public AppInfo(String packageName, String appName, String versionName, boolean isThirdParty) {
        this.packageName = packageName;
        this.appName = appName;
        this.versionName = versionName;
        this.isThirdParty = isThirdParty;
    }

    // Getters
    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public boolean isThirdParty() {
        return isThirdParty;
    }

    @Override
    public String toString() {
        return "App Name: " + appName + "\n" +
                "Package Name: " + packageName + "\n" +
                "Version: " + versionName + "\n" +
                "Third-Party App: " + (isThirdParty ? "Yes" : "No") + "\n";
    }
}
