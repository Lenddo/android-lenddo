package com.lenddo.verifime.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.core.Log;

import verifime.lenddo.com.verifimelib.Identification;
import verifime.lenddo.com.verifimelib.VerifiMeManager;
import verifime.lenddo.com.verifimelib.kyc.callbacks.VerifiMeCallback;
import verifime.lenddo.com.verifimelib.listeners.OnDocumentUploadCompleteListener;
import verifime.lenddo.com.verifimelib.models.DocumentConfig;
import verifime.lenddo.com.verifimelib.models.Theme;
import verifime.lenddo.com.verifimelib.sdk.models.FormData;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnDocumentUploadCompleteListener {

    private View uploadDocumentsButton;
    private VerifiMeManager verifiMeManager;
    private TextInputEditText applicationIdEdt;
    private TextInputEditText firstNameEdt;
    private TextInputEditText lastNameEdt;
    private TextInputEditText emailEdt;
    private TextInputEditText birthdayEdt;
    private TextInputEditText employerEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LenddoCoreInfo.initCoreInfo(getApplicationContext());

        uploadDocumentsButton = findViewById(R.id.upload_documents_button);
        uploadDocumentsButton.setOnClickListener(this);

        applicationIdEdt = (TextInputEditText) findViewById(R.id.applicationId);
        firstNameEdt = (TextInputEditText) findViewById(R.id.first_name);
        lastNameEdt = (TextInputEditText) findViewById(R.id.last_name);
        emailEdt = (TextInputEditText) findViewById(R.id.email_address);
        birthdayEdt = (TextInputEditText) findViewById(R.id.birthday);
        employerEdt = (TextInputEditText) findViewById(R.id.employer);

        verifiMeManager = VerifiMeManager.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            int timestamp = (int) System.currentTimeMillis()/1000;
            String appID = "DEMO_" + timestamp;
            applicationIdEdt.setText(appID);
            firstNameEdt.setText("Juan");
            lastNameEdt.setText("Dela Cruz");
            emailEdt.setText("juandelacruz@email.com");
            birthdayEdt.setText("22-03-1981");
            employerEdt.setText("Lenddo Pte Ltd");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        boolean isExit = false;
        if (applicationIdEdt.getText().toString().trim().length() == 0) {
            applicationIdEdt.setError("Mandatory");
            isExit = true;
        }
        if (firstNameEdt.getText().toString().trim().length() == 0) {
            firstNameEdt.setError("Mandatory");
            isExit = true;
        }
        if (lastNameEdt.getText().toString().trim().length() == 0) {
            lastNameEdt.setError("Mandatory");
            isExit = true;
        }
        if (!isExit && view == uploadDocumentsButton) {

            String applicationId = applicationIdEdt.getText().toString();
            //Setup the Document Config Object
            DocumentConfig documentConfig = new DocumentConfig();

            //Add the IDs that are to be captured
            documentConfig.addDocumentUploadPage()
                    .setHeaderText("Required IDs")
                    .setMinimumIDs(2)
                    .setMaximumIDs(3)
                    .addProfilePhoto("Profile Photo")
                    .addPhotoDocument(Identification.DRIVERS_LICENSE, "Driver's License")
                    .addSignature("Signature")
                    .addPhotoDocument(Identification.PASSPORT, "Passport");

            //Add a summary page
            documentConfig.addSummaryPage("Sample Corp.");

            FormData formData = new FormData();
            formData.setFirstName(firstNameEdt.getText().toString());
            formData.setLastName(lastNameEdt.getText().toString());
            formData.setEmployerName(employerEdt.getText().toString());
            formData.setEmail(emailEdt.getText().toString());
            formData.setDateOfBirth(birthdayEdt.getText().toString());

            // Add Applicant information for reference
            documentConfig.addForm(formData);

            //Reset all documents (Optional)
//            verifiMeManager.clearDocuments(this);

            //Start the process
            verifiMeManager.setTheme(new Theme(R.color.OTO_green_primarydark, R.color.OTO_green_primary, R.color.OTO_green_accent,
                    R.color.gmail_red, R.color.white));
            verifiMeManager.showDocumentUploader(this, applicationId, documentConfig, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        VerifiMeManager.onActivityResult(requestCode, resultCode, data, new VerifiMeCallback() {

            @Override
            public void onSuccess(String applicationId) {
                Intent intent = new Intent(HomeActivity.this, CompleteActivity.class);
                intent.putExtra("application_id", applicationId);
                startActivity(intent);
            }

            @Override
            public void onCanceled() {
                Toast.makeText(HomeActivity.this, "VerifiMe canceled", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onError(int statusCode, String rawResponse) {
        Log.e(HomeActivity.class.getSimpleName(), "onError() code:"+statusCode+" response:"+rawResponse);
    }

}
