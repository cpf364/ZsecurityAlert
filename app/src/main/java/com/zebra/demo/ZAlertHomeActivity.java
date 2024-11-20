package com.zebra.demo;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ZAlertHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        TextView agreementTextView = findViewById(R.id.agreement_tv);
        setSpannableText(agreementTextView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);


        Button proceedButton = findViewById(R.id.getStartedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ZAlertHomeActivity.this, DataProcessActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSpannableText(TextView agreementTextView) {
        String agreementText = getString(R.string.agreement_policy_message);

        SpannableString spannableString = new SpannableString(agreementText);

        // Underline and make "Agreement" clickable
        ClickableSpan agreementClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Open Agreement page
            }
        };

        int agreementStartIndex = agreementText.indexOf("Agreement");
        int agreementEndIndex = agreementStartIndex + "Agreement".length();

        spannableString.setSpan(agreementClickableSpan, agreementStartIndex, agreementEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), agreementStartIndex, agreementEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Underline and make "Privacy policy" clickable
        ClickableSpan privacyPolicyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Open Privacy Policy page
            }
        };

        int privacyStartIndex = agreementText.indexOf("Privacy policy");
        int privacyEndIndex = privacyStartIndex + "Privacy policy".length();

        spannableString.setSpan(privacyPolicyClickableSpan, privacyStartIndex, privacyEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), privacyStartIndex, privacyEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        agreementTextView.setText(spannableString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent serviceIntent = new Intent(ZAlertHomeActivity.this, MyForegroundService.class);
        startForegroundService(serviceIntent);
    }
}
