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
import com.lenddo.mobile.core.models.Theme;

import verifime.lenddo.com.verifimelib.Identification;
import verifime.lenddo.com.verifimelib.VerifiMeManager;
import verifime.lenddo.com.verifimelib.kyc.callbacks.VerifiMeCallback;
import verifime.lenddo.com.verifimelib.listeners.OnDocumentUploadCompleteListener;
import verifime.lenddo.com.verifimelib.models.DocumentConfig;
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
            long timestamp = System.currentTimeMillis()/1000;
            String appID = "DEMO_" + timestamp;
            applicationIdEdt.setText(appID);
            firstNameEdt.setText("Juan");
            lastNameEdt.setText("Dela Cruz");
            emailEdt.setText("juandelacruz@email.com");
            birthdayEdt.setText("22/03/1981");
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
            documentConfig.addDocumentUploadPage("required_ids")
                    .setHeaderText("Required IDs")
                    .setMinimumIDs(2)
                    .setMaximumIDs(3)
                    .setSubtitle("Add your document", R.color.green)
                    .addProfilePhoto("Profile Photo")
                    .addPhotoDocument(Identification.DRIVERS_LICENSE, "Driver's License")
                    .addSignature("Signature")
                    .addPhotoDocument(Identification.PASSPORT, "Passport")
                    .setButtonText("NEXT");

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
//            verifiMeManager.setTheme(new Theme(R.color.OTO_green_primarydark, R.color.OTO_green_primary, R.color.OTO_green_accent,
//                    R.color.gmail_red, R.color.white));
            verifiMeManager.showDocumentUploader(this, applicationId, configureVerifiMe(), this);
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

    private DocumentConfig configureVerifiMe() {
        // Setup a Document Config Object
        DocumentConfig documentConfig = new DocumentConfig();

        //Add the Proof of ID
        documentConfig.addDocumentUploadPage("proof_of_id")
                .setHeaderText("Proof of ID") // Set's the upload page title
                .setMinimumIDs(1) // Set the minimum required documents in order to proceed (optional)
                .setMaximumIDs(1) // Set the maximum required documents in order to proceed (optional)
                .setSubtitle("Choose one")
                .addPhotoDocument("passport", "Passport")
                .addPhotoDocument("drivers_license", "Driver\'s License")
                .addPhotoDocument("prc_id", "PRC ID")
                .addPhotoDocument("postal_id", "Postal ID")
                .addPhotoDocument("voter_id", "Voter ID")
                .addPhotoDocument("gsis_ecard", "GSIS ECard")
                .addPhotoDocument("sss_umid", "SSS/UMID")
                .addPhotoDocument("senior_citizen_card", "Senior Citizen Card")
                .addPhotoDocument("government_office_id", "Government Office ID")
                .addPhotoDocument("company_id", "Company ID")
                .setButtonText(getString(R.string.continue_allcaps));  // Set the caption for the button below the page

        //Add the Proof of Income
        documentConfig.addDocumentUploadPage("proof_of_income")
                .setHeaderText("Proof of Income") // Set's the upload page title
                .setMinimumIDs(1) // Set the minimum required documents in order to proceed (optional)
                .setMaximumIDs(1) // Set the maximum required documents in order to proceed (optional)
                .setSubtitle("Choose one")
                .addPhotoDocument("payslip", "Payslip")
                .addPhotoDocument("payslip1", "Payslip1 (Bi-Monthly)")
                .addPhotoDocument("payslip2", "Payslip2 (Bi-Monthly)")
                .addPhotoDocument("income_tax_return", "Income Tax Return")
                .addPhotoDocument("coe_with_income_declaration", "COE with Income Declaration")
                .addPhotoDocument("audited_financial_statement", "Audited Financial Statement")
                .addPhotoDocument("branch_referral_form", "Branch Referral Form for Depositor")
                .addPhotoDocument("sec_certificate", "SEC Certificate")
                .addPhotoDocument("business_registration_certificate", "Business Rgistration Certificate")
                .setButtonText(getString(R.string.continue_allcaps));  // Set the caption for the button below the page

        //Add the Additional Requirements
        documentConfig.addDocumentUploadPage("additional_requirements")
                .setHeaderText("Additional Requirements") // Set's the upload page title
                .setMinimumIDs(2) // Set the minimum required documents in order to proceed (optional)
                .setMaximumIDs(2) // Set the maximum required documents in order to proceed (optional)
                .addProfilePhoto("Selfie") // This will capture a selfie image using the front camera
                .addSignature("Signature") // Capture a signature
                .setButtonText(getString(R.string.continue_allcaps));  // Set the caption for the button below the page

        //Add the Proof of Address
        documentConfig.addDocumentUploadPage("proof_of_address")
                .setHeaderText("Proof of Address") // Set's the upload page title
                .setMinimumIDs(1) // Set the minimum required documents in order to proceed (optional)
                .setMaximumIDs(1) // Set the maximum required documents in order to proceed (optional)
                .setSubtitle("Choose one")
                .addPhotoDocument("cable_bill", "Cable Bill")
                .addPhotoDocument("electricity_bill", "Electricity Bill")
                .addPhotoDocument("telephone_bill", "Telephone Bill")
                .addPhotoDocument("water_bill", "Water Bill")
                .setButtonText(getString(R.string.continue_allcaps));  // Set the caption for the button below the page

        // Add a Document Summary Page
        documentConfig.addSummaryPage("Review and Upload"); // Add a summary page with the given title

        return documentConfig;
    }

}
