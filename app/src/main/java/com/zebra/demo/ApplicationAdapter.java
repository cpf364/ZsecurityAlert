package com.zebra.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zebra.demo.AppLogData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApplicationAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private final Map<String, AppLogData> appLogMap;
    private final List<String> appNames;
    private LayoutInflater inflater;

    public ApplicationAdapter(Context context,Map<String, AppLogData> appLogMap) {
        this.appLogMap = appLogMap;
        this.appNames = new ArrayList<>(appLogMap.keySet());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        // Get the log entry at the given position
        String appName = appNames.get(position);
        AppLogData appLogData = appLogMap.get(appName);
        // Bind the data to the view holder
        holder.bind(appLogData);
    }


    @Override
    public int getItemCount() {
        return appNames.size();
    }

}
