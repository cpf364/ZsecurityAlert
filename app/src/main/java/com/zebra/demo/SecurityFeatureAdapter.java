package com.zebra.demo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecurityFeatureAdapter extends RecyclerView.Adapter<SecurityFeatureViewHolder> {
    private List<SecurityFeature> featureList;
    private Context context;

    public SecurityFeatureAdapter(Context context,List<SecurityFeature> featureList) {
        this.featureList = featureList;
        this.context = context;
    }
    @Override
    public SecurityFeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_security_feature, parent, false);
        return new SecurityFeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SecurityFeatureViewHolder holder, int position) {
        SecurityFeature feature = featureList.get(position);
        holder.featureTitle.setText(feature.getTitle());
        holder.featureDescription.setText(feature.getDescription());
        holder.featureIcon.setImageResource(feature.getIconResId());

        // Set an OnClickListener on the RecyclerView item to launch the activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ZSecurityAlert", "onClick: ");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("data", position);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }
}
