package lenddo.com.lenddoonboarding;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lenddo.mobile.core.AuthV3ApiManager;
import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.core.Log;
import com.lenddo.mobile.core.http.BaseUrlConfig;
import com.lenddo.mobile.core.models.GovernmentId;
import com.lenddo.mobile.core.models.VerificationData;
import com.lenddo.mobile.onboardingsdk.client.LenddoConstants;
import com.lenddo.mobile.onboardingsdk.client.LenddoEventListener;
import com.lenddo.mobile.onboardingsdk.models.AuthorizationStatus;
import com.lenddo.mobile.onboardingsdk.models.AutoCompleteQuery;
import com.lenddo.mobile.onboardingsdk.models.FormDataCollector;
import com.lenddo.mobile.onboardingsdk.utils.OnboardingConfiguration;
import com.lenddo.mobile.onboardingsdk.utils.UIHelper;
import com.lenddo.mobile.onboardingsdk.widget.LenddoButton;
import com.lenddo.mobile.onboardingsdk.widget.OnlineAutoCompleteTextView;
import com.lenddo.nativeonboarding.FacebookSignInHelper;
import com.lenddo.nativeonboarding.GoogleSignInHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SampleActivity extends AppCompatActivity implements LenddoEventListener {

    private static final String TAG = SampleActivity.class.getName();
    private LenddoButton button;
    private EditText lastName;
    private EditText firstName;
    private EditText email;
    private EditText work_email;
    private TextView dateOfBirth;
    private EditText loanAmount;
    private Spinner gender;
    private Spinner sourceOfFunds;
    private UIHelper helper;
    private TextView customerId;
    private OnlineAutoCompleteTextView nameOfEmployer;
    private TextView mobilePhone;
    private Button dobButton;
    private EditText middleName;
    private TextView homePhone;
    private EditText motherLastName;
    private EditText motherFirstName;
    private EditText motherMiddleName;
    private Button employmentStartDateButton;
    private Button employmentEndDateButton;
    private TextView editTextEmploymentStart;
    private TextView editTextEmploymentEnd;
    private OnlineAutoCompleteTextView university;
    private EditText houseNumber;
    private EditText street;
    private EditText barangay;
    private EditText province;
    private EditText city;
    private EditText postalCode;
    private EditText apiRegion;
    private EditText ps_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_form);

        button = (LenddoButton) findViewById(R.id.verifyButton);

        lastName = (EditText) findViewById(R.id.editTextLastName);
        middleName = (EditText) findViewById(R.id.editTextMiddleName);
        firstName = (EditText) findViewById(R.id.editTextFirstName);
        university = (OnlineAutoCompleteTextView) findViewById(R.id.editTextUniversity);
        setUniversityHints();

        houseNumber = (EditText) findViewById(R.id.editTextHouseNumber);
        street = (EditText) findViewById(R.id.editTextStreetName);
        barangay = (EditText) findViewById(R.id.editTextBarangay);
        city = (EditText) findViewById(R.id.editTextMunicipality);
        province = (EditText) findViewById(R.id.editTextProvince);
        postalCode = (EditText)findViewById(R.id.editTextPostalCode);

        motherLastName = (EditText) findViewById(R.id.editTextMotherLastName);
        motherFirstName = (EditText) findViewById(R.id.editTextMotherFirstName);
        motherMiddleName = (EditText) findViewById(R.id.editTextMotherMiddleName);

        email = (EditText) findViewById(R.id.editTextEmail);
        work_email = (EditText) findViewById(R.id.editTextWorkEmail);
        dateOfBirth = (TextView) findViewById(R.id.editTextDateOfBirth);
        dobButton = (Button) findViewById(R.id.dobButton);

        editTextEmploymentStart = (TextView)findViewById(R.id.editTextEmploymentStartDate);
        editTextEmploymentEnd = (TextView)findViewById(R.id.editTextEmploymentEndDate);
        employmentStartDateButton = (Button)findViewById(R.id.employmentStartButton);
        employmentEndDateButton = (Button)findViewById(R.id.employmentEndButton);

        loanAmount = (EditText) findViewById(R.id.editTextLoanAmount);
        gender = (Spinner) findViewById(R.id.spinnerGender);
        sourceOfFunds = (Spinner) findViewById(R.id.spinnerSourceOfFunds);
        nameOfEmployer = (OnlineAutoCompleteTextView) findViewById(R.id.editTextNameOfEmployer);
        setEmployerHints();
        homePhone = (TextView)findViewById(R.id.editTextPrimaryNumber);
        mobilePhone = (TextView) findViewById(R.id.editTextMobileNumber);
        customerId = (TextView) findViewById(R.id.editTextCustomerId);
        customerId.setText(String.valueOf(LenddoCoreInfo.generateApplicationId("DEMO_", 5)));

        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                // Create a new instance of DatePickerDialog and return it
                final DatePickerDialog datePicker = new DatePickerDialog(SampleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        dateOfBirth.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        employmentStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                // Create a new instance of DatePickerDialog and return it
                final DatePickerDialog datePicker = new DatePickerDialog(SampleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        editTextEmploymentStart.setText(dateFormat.format(c.getTime()));
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        employmentEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                // Create a new instance of DatePickerDialog and return it
                final DatePickerDialog datePicker = new DatePickerDialog(SampleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        editTextEmploymentEnd.setText(dateFormat.format(c.getTime()));
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        helper = new UIHelper(this, this);
        helper.addGoogleSignIn(new GoogleSignInHelper());
//        helper.addFacebookSignIn(new FacebookSignInHelper());
        helper.customizeBackPopup("Custom Back Title", "Custom Back Popup Message", "Custom YES", "Custom NO");
        helper.enableKioskMode();

        String genderChoices[] = {"Male","Female"};
        gender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genderChoices));

        String sourceOfFundsChoices[] = {"Please Select", "Salary", "Commission", "Business",
                "Pension", "Remittance", "Allowance", "Self-Employed"};
        sourceOfFunds.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sourceOfFundsChoices));
        //setup the Lenddo Button
        button.setUiHelper(helper);

        Spinner spn_hostname = (Spinner) findViewById(R.id.spn_hostname);
        spn_hostname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                if (selected.equals("https://authorize-api.partner-service.link")) {
                    if (AuthV3ApiManager.getInstance().getApiRegion().isEmpty()) {
                        selected = "https://authorize-api%s.partner-service.link";
                    } else {
                        selected = AuthV3ApiManager.getInstance().getAuthorizeUrlWithRegion("https://authorize-api%s.partner-service.link");
                    }
                }
                LenddoConstants.AUTHORIZE_DATA_ENDPOINT = selected;
                Log.i(TAG, "Changed hostname to: "+selected);
                if (selected.contains("lendqa")) {
                    LenddoConstants.BINARY_BASEURL = BaseUrlConfig.BASEURL_QA_BINARY;
                } else {
                    LenddoConstants.BINARY_BASEURL = BaseUrlConfig.BASEURL_PROD_BINARY;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        apiRegion = (EditText) findViewById(R.id.editTextApiRegion);
        ps_id = (EditText) findViewById(R.id.editTextPSID);
    }

    private void setEmployerHints() {
        nameOfEmployer.region = AutoCompleteQuery.REGION_PH;
        nameOfEmployer.collection = AutoCompleteQuery.COLLECTION_EMPLOYERS;
        nameOfEmployer.version = "0";
    }

    private void setUniversityHints() {
        university.region = AutoCompleteQuery.REGION_PH;
        university.collection = AutoCompleteQuery.COLLECTION_UNIVERSITIES;
        university.version = "0";
    }

    @Override
    public boolean onButtonClicked(FormDataCollector formData) {
        button.setEnabled(false);
        //auto-collect (optional)
        if (ps_id.getText().length() == 24) {
            formData.setPartnerScriptId(ps_id.getText().toString());
        }
        formData.collect(SampleActivity.this, R.id.formContainer);

        VerificationData.Address primaryAddress = new VerificationData.Address();
        primaryAddress.line_1 = houseNumber.getText().toString() + " " + street.getText().toString();
        primaryAddress.line_2 = barangay.getText().toString() + " " + province.getText().toString();
        primaryAddress.city = city.getText().toString();
        primaryAddress.postal_code = postalCode.getText().toString();
        primaryAddress.country_code = "PH";

        //place partner defined user identifier
        formData.setApplicationId(customerId.getText().toString());
        formData.setLastName(lastName.getText().toString());
        formData.setMiddleName(middleName.getText().toString());
        formData.setHomePhone(homePhone.getText().toString());
        formData.setFirstName(firstName.getText().toString());
        formData.setEmail(email.getText().toString());
        formData.setWorkEmail(work_email.getText().toString());
        formData.setEmployerName(nameOfEmployer.getText().toString());
        formData.setMobilePhone(mobilePhone.getText().toString());
        formData.setDateOfBirth(dateOfBirth.getText().toString());
        formData.setStartEmploymentDate(editTextEmploymentStart.getText().toString());
        formData.setEndEmploymentDate(editTextEmploymentEnd.getText().toString());
        formData.setUniversityName(university.getText().toString());
        formData.setAddress(primaryAddress);
        List<GovernmentId> governmentIds = new ArrayList<>();
        governmentIds.add(new GovernmentId("DEMO-TYPE", "DEMO-VALUE"));
        governmentIds.add(new GovernmentId("passport", "PAS018218ASVR"));
        governmentIds.add(new GovernmentId("sss", "0-390128411-1274"));
        governmentIds.add(new GovernmentId("tin", "3023749103"));
        formData.setGovernmentIds(governmentIds);

        //send custom fields
        formData.putField("Loan_Amount", loanAmount.getText().toString());
        formData.validate();
        helper.setApiRegion(apiRegion.getText().toString());
        return true;
    }

    @Override
    public void onAuthorizeStarted(FormDataCollector collector) {
//        AndroidData.startAndroidData(SampleActivity.this, customerId.getText().toString());
    }

    @Override
    public void onAuthorizeComplete(FormDataCollector collector) {
        button.setEnabled(true);
        Intent finishIntent = new Intent(SampleActivity.this, CompleteActivity.class);
        AuthorizationStatus status = collector.getAuthorizationStatus();
        finishIntent.putExtra("client_id", status.getClientId());

        startActivity(finishIntent);
        finish();
    }

    @Override
    public void onAuthorizeCanceled(FormDataCollector collector) {
        button.setEnabled(true);
        Toast.makeText(SampleActivity.this, "cancelled!", Toast.LENGTH_LONG).show();
        Intent finishIntent = new Intent(SampleActivity.this, CanceledActivity.class);
        startActivity(finishIntent);
        finish();
    }

    @Override
    public void onAuthorizeError(int statusCode, String rawResponse) {
        button.setEnabled(true);
        Toast.makeText(SampleActivity.this, "Error! code: "+statusCode+" response:"+rawResponse, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizeFailure(Throwable throwable) {
        button.setEnabled(true);
        Toast.makeText(SampleActivity.this, "Failure: "+throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (helper.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called " + requestCode);
        helper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
