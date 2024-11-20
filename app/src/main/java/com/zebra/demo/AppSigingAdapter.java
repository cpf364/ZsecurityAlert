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

public class AppSigingAdapter extends RecyclerView.Adapter<AppSigingAdapter.AppSigingViewHolder> {

    private final Map<String, AppManifestScanData> appManifestMap;
    private final List<String> appNames;
    private LayoutInflater inflater;
    public AppSigingAdapter(Context context, Map<String, AppManifestScanData> appManifestMap) {
        this.appManifestMap = appManifestMap;
        this.appNames = new ArrayList<>(appManifestMap.keySet());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AppSigingAdapter.AppSigingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new AppSigingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppSigingViewHolder holder, int position) {

        String appName = appNames.get(position);
        AppManifestScanData appLogData = appManifestMap.get(appName);
        // Bind the data to the view holder
        holder.tvTitle.setText("Application Name : "+appLogData.getAppName());
        holder.tvDescription.setText("App Signing Version/Schema : "+appLogData.getSigningInfo()+"");

    }

    @Override
    public int getItemCount() {
        return appManifestMap.size();
    }

    public static class AppSigingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;

        public AppSigingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.appName);
            tvDescription = itemView.findViewById(R.id.methods);
        }
    }
}
