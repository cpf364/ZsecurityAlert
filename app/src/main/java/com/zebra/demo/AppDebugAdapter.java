package com.zebra.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppDebugAdapter extends RecyclerView.Adapter<AppDebugAdapter.AppDebugViewHolder> {

    private final Map<String, AppManifestScanData> appManifestMap;
    private final List<String> appNames;
    private LayoutInflater inflater;
    public AppDebugAdapter(Context context, Map<String, AppManifestScanData> appManifestMap) {
        this.appManifestMap = appManifestMap;
        this.appNames = new ArrayList<>(appManifestMap.keySet());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AppDebugAdapter.AppDebugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new AppDebugViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppDebugViewHolder holder, int position) {

        String appName = appNames.get(position);
        AppManifestScanData appLogData = appManifestMap.get(appName);
        // Bind the data to the view holder

        if(Boolean.parseBoolean(appLogData.getIsdebuggable_testonly())) {
            holder.tvTitle.setText("Application Name : "+appLogData.getAppName());
            holder.tvDescription.setText( "App Running Debuggable/Test mode  : "+appLogData.getIsdebuggable_testonly()+"");

        }else{
            holder.tvTitle.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return appManifestMap.size();
    }

    public static class AppDebugViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;

        public AppDebugViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.appName);
            tvDescription = itemView.findViewById(R.id.methods);
        }
    }
}
