package com.lenddo.mobile.mobiledatacollectiondemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.core.LenddoCoreUtils;
import com.lenddo.mobile.datasdk.AndroidData;
import com.lenddo.mobile.datasdk.DataManager;
import com.lenddo.mobile.datasdk.listeners.OnDataSendingCompleteCallback;
import com.lenddo.mobile.datasdk.utils.AndroidDataUtils;

public class MainActivity extends AppCompatActivity {

    EditText edt_application_id;
    TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configure a Data Sending Completion Callback for the Lenddo SDK
        DataManager.getInstance(getApplicationContext()).getClientOptions().registerDataSendingCompletionCallback(new OnDataSendingCompleteCallback() {
            @Override
            public void onDataSendingSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_status.setText("Thank you!\nMobile data collection has completed!\nYour Application ID is: "+edt_application_id.getText().toString());
                    }
                });
            }

            @Override
            public void onDataSendingError(final int statusCode, final String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_status.setText(errorMessage);
                    }
                });
            }

            @Override
            public void onDataSendingFailed(final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_status.setText(""+t.getMessage());
                    }
                });
            }

            @Override
            public void onDataSendingStart() {

            }
        });
        tv_status = (TextView) findViewById(R.id.tv_status);
        edt_application_id = (EditText) findViewById(R.id.edt_application_id);
        if (AndroidDataUtils.isInitialDataSent(getApplicationContext())) {
            edt_application_id.setText(LenddoCoreUtils.getApplicationId(getApplicationContext()));
            tv_status.setText("Thank you!\nMobile data collection has completed!\nYour Application ID is: "+edt_application_id.getText().toString());
        } else {
            edt_application_id.setText(LenddoCoreInfo.generateApplicationId("DEMO_", 7));
            launchLenddoMobileDataCollection();
        }
    }

    private void launchLenddoMobileDataCollection() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                String application_id = edt_application_id.getText().toString();
                if (null != application_id && !application_id.isEmpty()) {
                    edt_application_id.setEnabled(false);
                    tv_status.setText("Lenddo Data Collection started! This process is running in the background. Please wait for the results.");
                    AndroidData.startAndroidData(MainActivity.this, edt_application_id.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Application ID must not be empty!", Toast.LENGTH_LONG).show();
                }
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
