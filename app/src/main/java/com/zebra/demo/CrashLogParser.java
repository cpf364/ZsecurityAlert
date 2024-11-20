package com.zebra.demo;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrashLogParser {


    public List<CrashInfo> parseLog(File logFilePath,List<CrashInfo> list) {

        CrashInfo currentReport = null;

        // Patterns to detect process line and stack trace lines.
        Pattern processPattern = Pattern.compile("Process:\\s+([^,]+),");
        Pattern stackTracePattern = Pattern.compile("(java\\..+|\\sat\\s.+)");

        // Scanner scanner = new Scanner(logData);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(logFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                //String line = scanner.nextLine();

                // If a new crash starts, create a new CrashReport object.
                if (line.contains("FATAL EXCEPTION")) {
                    if (currentReport != null) {
                        list.add(currentReport);  // Save previous crash report.
                    }
                    currentReport = new CrashInfo();  // Start a new crash report.
                }

                // Extract application name.
                Matcher processMatcher = processPattern.matcher(line);
                if (processMatcher.find() && currentReport != null) {
                    currentReport.setApplicationName(processMatcher.group(1));
                }

                // Collect stack trace lines.
                Matcher stackMatcher = stackTracePattern.matcher(line);
                if (stackMatcher.find() && currentReport != null) {
                    currentReport.addStackTrace(line);
                }
            }

            // Add the last crash report if not already added.
            if (currentReport != null) {
                list.add(currentReport);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

         return list;
    }


}
