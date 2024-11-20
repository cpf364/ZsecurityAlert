package com.zebra.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AllCategoriesActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SecurityFeatureAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        recyclerView = findViewById(R.id.securityFeaturesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Static data
        List<SecurityFeature> featureList = new ArrayList<>();
        featureList.add(new SecurityFeature("API Usage", "Deprecated & Hidden APIs", R.drawable.ic_checkmark));
        featureList.add(new SecurityFeature("Network Security", "Cleartext Traffic", R.drawable.ic_checkmark));
        featureList.add(new SecurityFeature("Wi-Fi Security", "Secure network", R.drawable.ic_checkmark));// Replace icons with your own
        featureList.add(new SecurityFeature("Certificate Management", "Detecting Third-Party Apps Trusting User-Installed Certificates", R.drawable.ic_checkmark));  // Replace icons with your own
        featureList.add(new SecurityFeature("Crash Reporting", "Application Crash Info", R.drawable.ic_checkmark));  // Replace icons with your own
        featureList.add(new SecurityFeature("Application Signing","Application signing scheme/version details",R.drawable.ic_checkmark));
        featureList.add(new SecurityFeature("Application Backup","Identifying Apps with Enabled Backup",R.drawable.ic_checkmark));
        featureList.add(new SecurityFeature("Application Debuggable or Test Mode","Detecting Apps Running in Debug or Test Mode",R.drawable.ic_checkmark));
        featureList.add(new SecurityFeature("Application Permissions ","Identifying Apps with Signature or Privileged Permissions ",R.drawable.ic_checkmark));
        featureList.add(new SecurityFeature("Application components","Identifying Exported Components with No Permissions ",R.drawable.ic_checkmark));


        // Set adapter
        adapter = new SecurityFeatureAdapter(AllCategoriesActivity.this,featureList);
        recyclerView.setAdapter(adapter);

    }
}