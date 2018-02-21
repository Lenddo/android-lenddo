package com.lenddo.mobile.mobiledatacollectiondemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.datasdk.AndroidData;
import com.lenddo.mobile.datasdk.DataManager;
import com.lenddo.mobile.datasdk.listeners.OnDataSendingCompleteCallback;

public class MainActivity extends AppCompatActivity {

    EditText edt_application_id;
    Button btn_start;
    TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataManager.getInstance().getClientOptions().registerDataSendingCompletionCallback(new OnDataSendingCompleteCallback() {
            @Override
            public void onDataSendingSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_status.setText("Process is completed successfully!");
                    }
                });
            }

            @Override
            public void onDataSendingError(final int statusCode, final String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_status.setText("Error! "+statusCode+" " + errorMessage);
                    }
                });
            }

            @Override
            public void onDataSendingFailed(final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_status.setText("Failed! "+t.getMessage());
                    }
                });
            }
        });


        edt_application_id = (EditText) findViewById(R.id.edt_application_id);
        edt_application_id.setText(LenddoCoreInfo.generateApplicationId("DEMO_", 7));
        edt_application_id.setSelection(edt_application_id.getText().length());

        tv_status = (TextView) findViewById(R.id.tv_status);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String application_id = edt_application_id.getText().toString();
                if (null != application_id && !application_id.isEmpty()) {
                    btn_start.setEnabled(false);
                    edt_application_id.setEnabled(false);
                    tv_status.setText("Lenddo Data Collection started! This process is running in the background.");
                    AndroidData.startAndroidData(MainActivity.this, edt_application_id.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Application ID must not be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
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
