# Verifi.Me SDK Library

Table of Contents
=================
- [Introduction](#introduction)
- [Pre-requisites](#pre-requisites)
- [Installing the Verifi.Me SDK](#installing-the-verifime-sdk)
        - [Adding verifimelib Dependency](#adding-verifimelib-dependency)
- [Running the Verifi.Me Demo Application](#running-the-verifime-demo-application)
- [Configuring the Verifi.Me SDK library](#configuring-the-verifime-sdk-library)
        - [Document Types](#document-types)
        - [Document Upload Page](#document-upload-page)
        - [Document Summary Page](#document-summary-page)
        - [Application Form Data for Verification](#application-form-data-for-verification)
        - [Setting up a Callback to handle results](#setting-up-a-callback-to-handle-results)
        - [Starting the Document Capture](#starting-the-document-capture)

## Introduction
The Verifi.Me SDK library (verifimelib module) allows you to capture documents using the Android device's camera. This library can be configured to list a set of required documents to be submitted. It allows document photo capture, Selfie image capture, and signature capture. The images are then uploaded to Lenddo's servers and can be viewed in the Lenddo Dashboard.

## Pre-requisites
Make sure you have the latest version of Android Studio properly setup and installed, please refer to the Google Developer site for the instructions [Android Studio Download and Installation Instructions](https://developer.android.com/studio/index.html).

Your Android application should have imported the Lenddo SDK as a module. If not, follow the installation guide below.

By incorporating the Verifi.Me SDK library into your app, you will need to grant the camera permission to proceed.

## Installing the Verifi.Me SDK

1. Download and extract the contents of source.zip
2. Import Modules

 + **lenddosdk** - Lenddo SDK
 + **verifimelib** - Verifi.Me SDK library
 + **verifime_demo** - demo application for Verifi.Me SDK _(optional module)_

       File > New > Import Module (Browse, points to the extracted source code directory)
![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/file_new_import-module.png)
       
 Import the modules to use:
![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/import_selected_modules.png)

3. Sync Gradle



#### Adding the Lenddo Partner Script Id

In your applications AndroidManifest.xml file, inside the application key, add the following metadata:

```gradle
<meta-data android:name="partnerScriptId" android:value="@string/partner_script_id" />
```

In your strings.xml put your partner script id resource string.

```xml
<string name="partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
```

#### Adding verifimelib Dependency

In your applications build.gradle file, under dependencies, add the following line

```gradle
compile project(':verifimelib')
```

## Running the Verifi.Me Demo Application

1. Import the Verifi.Me Demo application (verifime_demo module)
2. Run the verifime_demo module

![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/run_demo_app.png)

## Configuring the Verifi.Me SDK library

Setting up the library
1. Add the imports (this can be done automatically in the Android Studio IDE)
2. Calling Activity should implement OnDocumentUploadCompleteListener
3. Declare and initialise a VerifiMeManager member variable
4. Initialise Lenddo credentials using the `LenddoCoreInfo.initCoreInfo()` method
5. Initialise the document config object and configure an upload page and a summary page
6. Add the applicants form data
7. Create a callback handler using `onActivityResult()`
8. Call `showDocumentUploader()` to begin document capture using Verifi.Me


```java
import verifime.lenddo.com.verifimelib.VerifiMeManager;
import verifime.lenddo.com.verifimelib.kyc.callbacks.VerifiMeCallback;
import verifime.lenddo.com.verifimelib.models.Theme;
import verifime.lenddo.com.verifimelib.listeners.OnDocumentUploadCompleteListener;

    // Create the VerifiMeManager member variable
    private VerifiMeManager verifiMeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialise the VerifiMeManager member variable
        verifiMeManager = VerifiMeManager.getInstance();

        // Configure the color theme (this is an optional step)
        verifiMeManager.setTheme(new Theme(R.color.primarydark, R.color.primary, R.color.accent));
    }
```

#### Document Types
1. Document Photo - This can be an identification card or a paper document captured by the camera.
2. Selfie Image - Self photo to be captured by the front facing camera.
3. Signature - A signature to be drawn on the Android device screen, can support touch pen if device allows it.

#### Document Upload Page
The Document Upload Page contains the configured list of documents to be capture.
```java
private DocumentConfig configureVerifiMe() {
    // Setup a Document Config Object
    DocumentConfig documentConfig = new DocumentConfig();

    //Add the IDs that are to be captured
    documentConfig.addDocumentUploadPage()
        .setHeaderText("Required IDs") // Set's the upload page title
        .setMinimumIDs(2) // Set the minimum required documents in order to proceed (optional)
        .setMaximumIDs(3) // Set the maximum required documents in order to proceed (optional)
        .addProfilePhoto("Selfie") // This will capture a selfie image using the front camera
        .addPhotoDocument(Identification.DRIVERS_LICENSE, "Driver's License") // Capture an identification card
        .addPhotoDocument(Identification.PASSPORT, "Passport") // Capture a paper document
        .addSignature("Signature") // Capture a signature
        .setButtonText("NEXT");  // Set the caption for the button below the page

    // Add a Document Summary Page
    documentConfig.addSummaryPage("Sample Corp."); // Add a summary page with the given title

    // Attaching user information, optional
    FormData formData = new FormData();
    formData.setFirstName("FIRSTNAME");
    formData.setLastName("LASTNAME");
    formData.setEmployerName("EMPLOYER NAME");
    formData.setEmail("EMAIL ADDRESS");
    formData.setDateOfBirth("DD/MM/YYYY"); // Please follow this date format

    // Add Applicant information for reference
    documentConfig.addForm(formData);

    return documentConfig;
}
```

#### Document Summary Page
This page will show a thumbnail of captured documents to be uploaded to Lenddo server.
```java
    // Add a Document Summary Page
    documentConfig.addSummaryPage("Sample Corp."); // Add a summary page with the given title
```

#### Application Form Data for Verification
```java
    FormData formData = new FormData();
    formData.setFirstName("FIRSTNAME");
    formData.setLastName("LASTNAME");
    formData.setEmployerName("EMPLOYER NAME");
    formData.setEmail("EMAIL ADDRESS");
    formData.setDateOfBirth("DD/MM/YYYY"); // Please follow this date format

    // Add Applicant information for reference
    documentConfig.addForm(formData);
```

#### Setting up a Callback to handle results
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        VerifiMeManager.onActivityResult(requestCode, resultCode, data, new VerifiMeCallback() {

            @Override
            public void onSuccess(String applicationId) {
                // Load a success page
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
```

#### Starting the Document Capture

To trigger the document capture, call the `showDocumentUploader()` method. 

```java
    // Start Document Capture
    verifiMeManager.showDocumentUploader(this, applicationId, documentConfig, this);
```

