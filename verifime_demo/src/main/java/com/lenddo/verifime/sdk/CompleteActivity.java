package com.lenddo.verifime.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.core.LenddoCoreUtils;

import verifime.lenddo.com.verifimelib.VerifiMeManager;
import verifime.lenddo.com.verifimelib.sdk.utils.Utils;

public class CompleteActivity extends AppCompatActivity {

    private TextView successMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Toolbar toolbar = (Toolbar) findViewById(verifime.lenddo.com.verifimelib.R.id.toolbar);
        toolbar.setTitle("Document Upload");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, verifime.lenddo.com.verifimelib.R.color.white));
        toolbar.setBackgroundColor(ContextCompat.getColor(this,
                LenddoCoreInfo.getTheme().getColorPrimary()));

        Intent intent = getIntent();
        String applicationId = intent.getStringExtra("application_id");
        successMessage = (TextView) findViewById(R.id.success_message);
        successMessage.setText("Success! Application ID is " + applicationId);

        Button btn_success_continue = (Button) findViewById(R.id.btn_success_continue);
        btn_success_continue.setBackground(LenddoCoreUtils.makeButtonSelector(getApplicationContext(),
                LenddoCoreInfo.getTheme()));
        btn_success_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
