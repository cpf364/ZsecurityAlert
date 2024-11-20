package com.zebra.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView detailRecyclerView;
    private ApplicationAdapter detailAdapter;
    private List<String> detailDataList;
    private static String TAG = "ZSecurityAlert";

    @Inject
    Map<String, AppLogData> appLogMap;
    @Inject
    Map<String, AppManifestScanData> appManifestMap;
    @Inject
    List<CrashInfo> crashInfoList;

    @Inject
    Set<String> uniqueSSIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView TitleText = (TextView) findViewById(R.id.titleText);
        ((MyApplication) getApplication()).getAppComponent().inject(this);

        detailRecyclerView = findViewById(R.id.recyclerView);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the data passed from the previous activity
        int pos = getIntent().getIntExtra("data",0);

        Log.d(TAG, "onCreate: postion :: "+pos);
        if(pos == 0) {
            detailAdapter = new ApplicationAdapter(this, appLogMap);
            detailRecyclerView.setAdapter(detailAdapter);
            TitleText.setText("API Usage ");
        }else if (pos ==1){
            TitleText.setText("Network Security ");
            NetworkAdapter networkAdpter = new NetworkAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(networkAdpter);
        }else if (pos ==2){
            TitleText.setText("Wi-Fi Security ");
            WifiAdapter wifiAdapter = new WifiAdapter(this, uniqueSSIDs);
            detailRecyclerView.setAdapter(wifiAdapter);
        }else if (pos ==3){
            TitleText.setText("Certificate Management");
            CertificateAdapter certAdapter = new CertificateAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(certAdapter);
        }else if (pos ==4){
            TitleText.setText("Crash Reporting");
            CrashAdapter crashAdapter = new CrashAdapter(this, crashInfoList);
            detailRecyclerView.setAdapter(crashAdapter);
        }else if (pos ==5){
            TitleText.setText(" Application Signing");
            AppSigingAdapter appSigAdapter = new AppSigingAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(appSigAdapter);
        }else if (pos ==6){
            TitleText.setText(" Application Backup");
            AppBackupAdapter appBackupAdapter = new AppBackupAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(appBackupAdapter);
        }else if (pos ==7){
            TitleText.setText("  Application Debuggable/Test Mode");
            AppDebugAdapter appdebugAdapter = new AppDebugAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(appdebugAdapter);
        }else if (pos ==8){
            TitleText.setText("   Application Permissions ");
            AppPermissionAdapter appPermissionAdapter = new AppPermissionAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(appPermissionAdapter);
        }else if (pos ==9){
            TitleText.setText("    Application components ");
            AppComponentAdapter appComponentAdapter = new AppComponentAdapter(this, appManifestMap);
            detailRecyclerView.setAdapter(appComponentAdapter);
        }
    }
}
