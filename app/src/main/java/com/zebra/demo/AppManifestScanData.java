package com.zebra.demo;

import java.util.HashSet;

public class AppManifestScanData {

    private String appName;
    private HashSet<String> components;
    private String signingInfo;
    private String isdebuggable_testonly;

    private HashSet<String> permissionInfo;

    public String getIsUserCertificateTruest() {
        return isUserCertificateTruest;
    }

    public void setIsUserCertificateTruest(String isUserCertificateTruest) {
        this.isUserCertificateTruest = isUserCertificateTruest;
    }

    private String isUserCertificateTruest;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setComponents(HashSet<String> components) {
        this.components = components;
    }

    public void setSigningInfo(String signingInfo) {
        this.signingInfo = signingInfo;
    }

    public void setPermissionInfo(HashSet<String> permissionInfo) {
        this.permissionInfo = permissionInfo;
    }

    public void setSharedLibInfo(HashSet<String> sharedLibInfo) {
        this.sharedLibInfo = sharedLibInfo;
    }

    public String isBackup() {
        return isBackup;
    }

    public void setBackup(String backup) {
        isBackup = backup;
    }

    public void setCleartextTrafficPermitted(boolean cleartextTrafficPermitted) {
        isCleartextTrafficPermitted = cleartextTrafficPermitted;
    }

    private HashSet<String> sharedLibInfo;
    private String isBackup;
    private boolean isCleartextTrafficPermitted;

    public AppManifestScanData(String appName) {
        this.appName = appName;
        this.components = new HashSet<>();
        this.permissionInfo = new HashSet<>();
        this.sharedLibInfo = new HashSet<>();

    }

    public HashSet<String> getComponents() {
        return components;
    }

    public HashSet<String> getPermissionInfo() {
        return permissionInfo;
    }

    public HashSet<String> getSharedLibInfo() {
        return sharedLibInfo;
    }

    public String getSigningInfo() {
        return signingInfo;
    }

    public String getIsBackup() {
        return isBackup;
    }

    public boolean isCleartextTrafficPermitted() {
        return isCleartextTrafficPermitted;
    }

    public String getIsdebuggable_testonly() {
        return isdebuggable_testonly;
    }

    public void setIsdebuggable_testonly(String isdebuggable_testonly) {
        this.isdebuggable_testonly = isdebuggable_testonly;
    }


    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Application Name: ").append(appName).append("\n");
        result.append("isBackup: ").append(isBackup).append("\n");
        result.append("isDebuggableORTestonly").append(isdebuggable_testonly).append("\n");
        result.append("signingInfo: ").append(signingInfo).append("\n");
        result.append("isCleartextTrafficPermitted: ").append(isCleartextTrafficPermitted).append("\n");
        result.append("isUserCertificateTruested : ").append(isUserCertificateTruest).append("\n");
        // Append unique methods
        if (!components.isEmpty()) {
            for (String method : components) {
                result.append(method).append("\n");
            }
        }
        // Append unique fields
        if (!permissionInfo.isEmpty()) {
            for (String field : permissionInfo) {
                result.append("Permissions: ").append(field).append("\n");
            }
        }
        // Append unique fields
        if (!sharedLibInfo.isEmpty()) {
            for (String field : sharedLibInfo) {
                result.append("SharedLibs: ").append(field).append("\n");
            }
        }
        return result.toString();
    }
}
