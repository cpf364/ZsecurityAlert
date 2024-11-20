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

public class AppBackupAdapter extends RecyclerView.Adapter<AppBackupAdapter.AppBackupViewHolder> {

    private final Map<String, AppManifestScanData> appManifestMap;
    private final List<String> appNames;
    private LayoutInflater inflater;
    public AppBackupAdapter(Context context, Map<String, AppManifestScanData> appManifestMap) {
        this.appManifestMap = appManifestMap;
        this.appNames = new ArrayList<>(appManifestMap.keySet());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AppBackupAdapter.AppBackupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new AppBackupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppBackupViewHolder holder, int position) {

        String appName = appNames.get(position);
        AppManifestScanData appLogData = appManifestMap.get(appName);

        if(Boolean.parseBoolean(appLogData.getIsBackup())) {
            holder.tvTitle.setText("Application Name : "+appLogData.getAppName());
            holder.tvDescription.setText("App Backup Enabled : "+ appLogData.getIsBackup() + "");
        }else{
            holder.tvTitle.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return appManifestMap.size();
    }

    public static class AppBackupViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;

        public AppBackupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.appName);
            tvDescription = itemView.findViewById(R.id.methods);
        }
    }
}
