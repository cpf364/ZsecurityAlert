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

public class CrashAdapter extends RecyclerView.Adapter<CrashAdapter.CrashViewHolder> {

    List<CrashInfo> crashInfoList;

    private LayoutInflater inflater;
    public CrashAdapter(Context context, List<CrashInfo> appManifestMap) {
        this.crashInfoList = appManifestMap;

        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CrashAdapter.CrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_application, parent, false);
        return new CrashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrashViewHolder holder, int position) {


        CrashInfo info = crashInfoList.get(position);
        // Bind the data to the view holder
        holder.tvTitle.setText("Application Name : "+info.getApplicationName());
        holder.tvDescription.setText("StackTrace : "+info.getStackTrace());

    }

    @Override
    public int getItemCount() {
        return crashInfoList.size();
    }

    public static class CrashViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;

        public CrashViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.appName);
            tvDescription = itemView.findViewById(R.id.methods);
        }
    }
}
