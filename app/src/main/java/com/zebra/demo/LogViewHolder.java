package com.zebra.demo;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class LogViewHolder extends RecyclerView.ViewHolder {
    TextView appNameTextView;
    TextView accessTypeTextView;

    public LogViewHolder(View itemView) {
        super(itemView);
        appNameTextView = itemView.findViewById(R.id.appName);
        accessTypeTextView = itemView.findViewById(R.id.methods);
    }

    public void bind(AppLogData logEntry) {
        appNameTextView.setText("Application Name: " + logEntry.getAppName()+"\n");
        //accessTypeTextView.setText(logEntry.getAccessType() + ": " + logEntry.getAccessedItem() + logEntry.getAdditionalInfo());
        // Join all accessed items (methods/fields) into a single string
        // Concatenate methods and fields for display
        StringBuilder methodsBuilder = new StringBuilder();
        for (String method : logEntry.getMethods()) {
            methodsBuilder.append("Method :");
            methodsBuilder.append(method).append("\n\n");
        }

        for (String field : logEntry.getFields()) {
            methodsBuilder.append("Field :");
            methodsBuilder.append(field).append("\n\n");

        }

        accessTypeTextView.setText(methodsBuilder.toString().trim());

    }
}
