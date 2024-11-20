package com.zebra.demo;

import java.util.HashSet;
import java.util.Set;

public class AppLogData {
    private String appName;
    private HashSet<String> methods;
    private HashSet<String> fields;



    public AppLogData(String appName) {
        this.appName = appName;
        this.methods = new HashSet<>();
        this.fields = new HashSet<>();


    }

    public String getAppName() {
        return appName;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public Set<String> getFields() {
        return fields;
    }

    // Add a method if it's not a duplicate
    public void addMethod(String method) {
        methods.add(method);
    }

    // Add a field if it's not a duplicate
    public void addField(String field) {
        fields.add(field);
    }



    // Generate output for this app's data
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Application Name: ").append(appName).append("\n");

        // Append unique methods
        if (!methods.isEmpty()) {
            for (String method : methods) {
                result.append("Method: ").append(method).append("\n");
            }
        }

        // Append unique fields
        if (!fields.isEmpty()) {
            for (String field : fields) {
                result.append("Field: ").append(field).append("\n");
            }
        }

        return result.toString();
    }
}
