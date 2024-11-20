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

public class AppComponentAdapter extends RecyclerView.Adapter<AppComponentAdapter.AppComponentViewHolder> {

    private final Map<String, AppManifestScanData> appManifestMap;
    private final List<String> appNames;
    private LayoutInflater inflater;
    public AppComponentAdapter(Context context, Map<String, AppManifestScanData> appManifestMap) {
        this.appManifestMap = appManifestMap;
        this.appNames = new ArrayList<>(appManifestMap.keySet());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AppComponentAdapter.AppComponentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new AppComponentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppComponentViewHolder holder, int position) {

        String appName = appNames.get(position);
        AppManifestScanData appLogData = appManifestMap.get(appName);
        // Bind the data to the view holder

        if(!appLogData.getComponents().isEmpty()) {
            holder.tvTitle.setText("Application Name : "+appLogData.getAppName());

            StringBuilder methodsBuilder = new StringBuilder();
            for (String method : appLogData.getComponents()) {
                methodsBuilder.append("App Components : \n");
                methodsBuilder.append(method).append("\n\n");
            }

            holder.tvDescription.setText(methodsBuilder);

        }else{
            holder.tvTitle.setVisibility(View.GONE);
            holder.tvDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appManifestMap.size();
    }

    public static class AppComponentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;

        public AppComponentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.appName);
            tvDescription = itemView.findViewById(R.id.methods);
        }
    }
}
