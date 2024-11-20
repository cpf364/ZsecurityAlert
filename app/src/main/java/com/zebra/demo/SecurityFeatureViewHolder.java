package com.zebra.demo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SecurityFeatureViewHolder extends RecyclerView.ViewHolder {
    ImageView featureIcon;
    TextView featureTitle;
    TextView featureDescription;

    public SecurityFeatureViewHolder(View itemView) {
        super(itemView);
        featureIcon = itemView.findViewById(R.id.featureIcon);
        featureTitle = itemView.findViewById(R.id.featureTitle);
        featureDescription = itemView.findViewById(R.id.featureDescription);
    }
}
