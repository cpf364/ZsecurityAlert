package com.zebra.demo;

import java.util.ArrayList;
import java.util.List;

public class CrashInfo {
    private String applicationName;
    private StringBuilder stackTrace;

    public CrashInfo() {
        this.stackTrace = new StringBuilder();
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void addStackTrace(String line) {
        stackTrace.append(line).append("\n");
    }

    public String getStackTrace() {
        return stackTrace.toString();
    }

}
