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
import java.util.Set;


public class WifiAdapter extends RecyclerView.Adapter<com.zebra.demo.WifiAdapter.WifiViewHolder> {

        List<String> uniqueSSIDsList;
        private LayoutInflater inflater;


        public WifiAdapter(DetailActivity context, Set<String> uniqueSSIDs) {

            uniqueSSIDsList = new ArrayList<>(uniqueSSIDs);
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public com.zebra.demo.WifiAdapter.WifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_application, parent, false);
            return new com.zebra.demo.WifiAdapter.WifiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.zebra.demo.WifiAdapter.WifiViewHolder holder, int position) {


            // Bind the data to the view holder
            String wifiname = uniqueSSIDsList.get(position);
            if( wifiname !=null && wifiname.length() !=0 ) {
                holder.tvDescription.setText("WI-FI Info  : " +wifiname);
                holder.tvTitle.setText("");
            }else{
                holder.tvTitle.setVisibility(View.GONE);
                holder.tvDescription.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return uniqueSSIDsList.size();
        }

        public static class WifiViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDescription;

            public WifiViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.appName);
                tvDescription = itemView.findViewById(R.id.methods);
            }
        }
    }