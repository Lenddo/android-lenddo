Lenddo SDK for Android
======================

## Table of Contents

1.  [Introduction](#user-content-introduction)
2.  [An Overview of the Lenddo Process](#user-content-an-overview-of-the-lenddo-process)
3.  [Getting Started](#user-content-getting-started)
    1.  [Requirements](#user-content-requirements)
    2.  [Installation update with screenshot](#user-content-installation-update-with-screenshot)
4.  [Setting up the sample loan app](#user-content-setting-up-the-sample-loan-app)
5.  [Adding the Lenddo library to your existing project](#user-content-adding-the-lenddo-library-to-your-existing-project)
    1.  [Edit the App settings.gradle](#user-content-2-edit-the-app-settings-gradle)
    2.  [Edit the App build.gradle](#user-content-2-edit-the-app-build-gradle)
    3.  [Native Google Email Sign In Helper Class](#user-content-3-native-google-email-sign-in-helper-class)
    4.  [Permissions](#user-content-4-permissions)
    5.  [Enabling GMail API](#user-content-5-enabling-gmail-api)
6.  [Integration](#user-content-integration)
    1.  [Adding the Lenddo workflow to your app](#user-content-adding-the-lenddo-workflow-to-your-app)
    2.  [Configuring the Partner Script Id dynamically](#configuring-the-partner-script-id-dynamically)
    3.  [Adding Probe data for Verification](#adding-probe-data-for-verification)
    4.  [Add the Lenddo Button to your form](#user-content-add-the-lenddo-button-to-your-form)
    5.  [Customizing the Lenddo Button](#user-content-customizing-the-lenddo-button)
    6.  [Customizing the Popup Dialog when pressing the back key](#user-content-customizing-the-popup-dialog-when-pressing-the-back-key)
    7.  [Using the Lenddo AutoComplete View](#user-content-using-the-lenddo-autocomplete-view)
    8.  [Setting API Region](#user-content-setting-api-region)
    9.  [Adding Native Facebook Integration](#user-content-adding-native-facebook-integration)
7.  [Frequently Asked Questions](#user-content-frequently-asked-questions)

## Introduction

This is the Lenddo Onboarding SDK for Android based devices, if you are developing for other platforms like IOS and web, please refer to the [online documentation] (http://www.lenddo.com/documentation/lenddo_button.html)

The Lenddo SDK for Android allows you to integrate the Lenddo Verification and/or Scoring workflow seamlessly into your Android app.

## An Overview of the Lenddo Process

1.  User fills up a form on your app.
2.  User clicks on the **Lenddo Button** provided by the Lenddo SDK.
3.  The Lenddo Authorize site is shown.
4.  User completes the Lenddo Authorize process.
5.  A callback to your app is initiated and your app will consume the results.
6.  User is sent to the next phase in your app.

## Getting Started

1.  First make sure you have all the requirements. See the [Requirements](#user-content-requirements) section below.
2.  Successfully run the SDK in a simple loan app. See the [Installation update with screenshot](#user-content-installation-update-with-screenshot) section below.
3.  Add the Lenddo SDK libraries to your own application.
4.  Test and deploy

### Requirements

Before you start on integrating the Lenddo SDK, please make sure you have the following

1.  Properly installed latest version of **Android Studio**. You may refer to the Google Developer Docs on how to set this up [https://developer.android.com/sdk/index.html](https://developer.android.com/sdk/index.html)
2.  A valid Lenddo **Partner Script Id**
3.  Basic knowledge on setting up Android Libraries. (This document will explain the specific steps for the Lenddo SDK only).
4.  Download the LenddoSDK onto your hard drive
5.  For email onboarding, a **google-services.json** file is required in the app's root directory.
6.  Google **Web** OAuth2.0 Client Id

### Installation update with screenshot

Download the Lenddo SDK and extract the archive into your local drive. After extracting the archive, the Lenddo SDK folder structure should look like this:

![](https://cloud.githubusercontent.com/assets/481942/13133878/ec7f8ae8-d63a-11e5-9ede-ae5140099fc5.png)

The **LenddoSDK** folder contains the actual Lenddo SDK library project that you can include in your app. The **simple_loan** folder contains the sample app called **Simple Loan** which illustrates how to integrate the **Lenddo Button** into your existing app.

## Setting up the Sample Loan App

1.  Using Android Studio, click on **Select File -> Open** and **choose** the folder LenddoSDK-android which was created when you extracted the Lenddo SDK.zip. Android Studio will automatically set up the project for you.
2.  _(if you have an e-mail onboarding step)_ Create a **google-services.json** file by clicking on the "GET A CONFIGURATION FILE" [from this link](https://developers.google.com/identity/sign-in/android/start-integrating). Provide the package name: **lenddo.com.lenddoconnect**
3.  The sample app is already configured to use the LenddoSDK, all you need to do is to fill in your **partner-script-id**. Edit the **simple_loan/src/main/res/values/config.xml** and replace the words "PLACE YOUR PARTNER SCRIPT ID HERE" with the **partner_script_id** key provided to you. (See below).
    * _(if you have an e-mail onboarding step)_ Fill in the Google Web Client Id in the String as shown below. Get it from the [Google API Manager](https://console.developers.google.com/apis/credentials) site under the Credentials link.

```xml
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
          <!-- Lenddo Partner Script ID -->
          <string name="lenddo_app_id">PLACE_YOUR_PARTNER_SCRIPT_ID_HERE</string>
          <!-- Google Web OAuth2.0 Client ID -->
          <string name="google_client_id">PLACE_YOUR_GOOGLE_WEB_CLIENT_ID_HERE</string>
        </resources>
```

5.  Now, build and run the sample app (_make sure you have your emulator running or you have an Android device connected and configured for development. If you need more information on how to do this, please refer to the Android Studio documentation to learn more_).
6.  When the sample app successfully launches, you will see a sample form with a Lenddo Button at the bottom and an application ID field above it. This application ID field corresponds to an id that is created by your app (for testing purposes you can enter a sample ID).
7.  Click on the **Submit and Get Verified** button to complete the authorize process.

If you would like more information on how this works you can view The file **SampleActivity.java** in the simple_loan/src/main/java/lenddo.com.lenddoconnect folder

## Adding the Lenddo library to your existing project

Inside the extracted directory, copy the **LenddoSDK** subfolder and place it inside the root of your Application's Android Studio project folder. If you encounter an error copy the **LenddoSDK** subfolder in the Folder with the name of your Application in your computer.

### 1. Edit your **settings.gradle** file and add the following:
```java
include ':LenddoSDK'
```

### 2. Edit the App build.gradle
#### 2.1. Edit build.gradle
Open and edit the **build.gradle** file of your app (not the one in the project root). You should see a section for dependencies.:

##### 2.1. Example Gradle Dependencies
```java
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
}
```

#### 2.2. Add Lenddo Dependency
Add `compile project(':LenddoSDK')`

##### 2.2. Example Gradle Dependencies _after modification_
```java
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile project(':LenddoSDK')
}
```

---

### 2.3. Additional Steps Required if You Have E-mail Onboarding
> If you have the e-mail onboarding step included with your Authorize onboarding experience, follow the next two steps. Ignore these otherwise.
>
> #### 1. In your application module **build.gradle** file:
>```java
> dependencies {
>    compile fileTree(dir: 'libs', include: ['*.jar'])
>    compile project(':LenddoSDK')
>    compile 'com.google.android.gms:play-services-auth:9.8.0'
> }
>
> apply plugin: 'com.google.gms.google-services'
>```
>
> #### 2. In your project root build.gradle file:
>```java
> dependencies {
>        classpath 'com.android.tools.build:gradle:2.3.1'
>        classpath 'com.google.gms:google-services:3.0.0'
> }
>```

----

Android Studio should tell you to resync, the SDK classes should now be available after that.

### 3. Native Google Email Sign In Helper Class
_For applications that have an Email Onboarding process_, a native Google Email signin helper class is provided. Simply create a new package in your application: **com.lenddo.nativeonboarding** and copy the signin helper class. [GoogleSignInHelper](/simple_loan/src/main/java/com/lenddo/nativeonboarding/GoogleSignInHelper.java)

### 4. Permissions
In addition to the required permissions defined from within the SDK, _which are automatically incorporated into your app_, you must ensure the following permissions are also required:

```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 5. Enabling Gmail API
In order for the application to have login access and use Gmail onboarding for scoring, the Gmail API must be enabled in the [Google API Console](https://console.developers.google.com/apis/library). 

 - Choose the correct project
 - Under Library, search for Gmail and click Enable
 - Enabling Contacts API will also help improve score generation so enable that as well.

## Integration

### Adding the Lenddo workflow to your app

Edit your apps' AndroidManifest.xml (located in your src/main folder for gradle projects), and then add the following meta tag under the application tag:

```java
<meta-data android:name="partnerScriptId" android:value="partner_script_id" />
```

where **partner_script_id** is the partner script id provided to you by Lenddo.

If the application have an email onboarding process, a Google Web Client Id is required. Get or configure a the Google web client id from the [Google API  Manager](https://console.developers.google.com/apis/credentials) for your application and add the following meta-data to the AndroidManifest.xml file.

```java
<meta-data android:name="googleClientId" android:value="google_web_client_id" />
```

### Configuring the Partner Script Id dynamically

Normally, an application will only need a single partner script id. The Onboarding SDK allows changing of partner script id dynamically by setting it using the FormDataCollector object. Simply call the setPartnerScriptId() method before calling the UIHelper.showAuthorize() method or before clicking the Lenddo button.


### Adding Probe data for Verification
    
Probe data or user information are gathered from the application to be used for verification purposes. Probe may come from an application form that the user will need to fill up. Once the user fill's up the application form fields, the data is then passed to the Onboarding SDK as probe data. See code below on how to pass the probe data.

```java
// Get the user information from your application form's EditText widgets and pass them here
private FormDataCollector getProbeData(FormDataCollector formData) {
    // Place partner defined application id if not yet defined
    formData.setApplicationId("YOUR_APPLICATION_ID_STRING_HERE");
    formData.setLastName("LASTNAME STRING");
    formData.setFirstName("FIRSTNAME STRING");
    formData.setEmail("EMAIL STRING");
    formData.setDateOfBirth("01/01/2000"); // FORMAT: dd/MM/yyyy
    // Add additional fields such as EmployerName, MobilePhone, University, Address, etc
    
    // Configure the partner script dynamically if needed
    String partnerscript_id = "YOUR NEW PARTNER SCRIPT ID";
    formData.setPartnerScriptId(partnerscript_id);
    
    // Adding Government IDs
    governmentIds.add(new GovernmentId("DEMO-TYPE", "DEMO-VALUE"));
    governmentIds.add(new GovernmentId("passport", "PAS018218ASVR"));
    governmentIds.add(new GovernmentId("sss", "0-390128411-1274"));
    governmentIds.add(new GovernmentId("tin", "3023749103"));
    formData.setGovernmentIds(governmentIds);

    // send custom fields
    formData.putField("Loan_Amount", loanAmmount.getText().toString());

    formData.validate();
    return formData;
}
```

### Add the Lenddo Button to your form

The Lenddo button greatly simplifies integrating the Lenddo workflow to your app.

1.  Create your form (if you don't have an existing one already)

    The Lenddo verification process requires at the minimum, the following fields:

    *   Primary Address
    *   Email
    *   Last Name
    *   Middle Name
    *   First Name
    *   Date of Birth
    *   Home Phone Number
    *   Mobile Phone Number
    *   University
    *   Employer

    However the exact fields that is required for your App may be different depending on your requirements or use cases, please talk to your Lenddo Representative regarding this.

2.  Open up your Forms' layout xml and add the following to include the Lenddo Button onto your Layout:

    ```java
    <com.lenddo.sdk.widget.LenddoButton
       android:id="@+id/verifyButton"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_gravity="center_horizontal"
       android:gravity="center" />
    ```

    Note that you can also use your own custom button. (See section on customization for more information)

3.  Create an Instance of the UIHelper class inside the onCreate block of your activity. Note that the class constructor requires a **LenddoEventListener**. For the sample app, it implements the current activity as a **LenddoEventListener**.

    ```java
    private UIHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        ....
        helper = new UIHelper(this, this);
        helper.addGoogleSignIn(new GoogleSignInHelper());  // Add this line only if your application has an email onboarding process.
    }
    ```

4.  Setup your activity to implement the **LenddoEventListener** in your class or you can define your own class:

    ```java
    public class SampleActivity extends Activity implements LenddoEventListener {

        ....

        private UIHelper helper;

        protected void onCreate(Bundle savedInstanceState) {
            ....
            helper = new UIHelper(this, this);
            helper.addGoogleSignIn(new GoogleSignInHelper());  // Add this line only if your application has an email onboarding process.
            String applicationId = "your application id";
            LenddoCoreInfo.setCoreInfo(getApplicationContext(), LenddoCoreInfo.COREINFO_APPLICATION_ID, applicationId);
            LenddoCoreInfo.initCoreInfo(getApplicationContext());
        }

        @Override
        public boolean onButtonClicked(FormDataCollector collector) {
            return true;
        }

        @Override
        public void onAuthorizeComplete(FormDataCollector collector) {
        }

        @Override
        public void onAuthorizeCanceled(FormDataCollector collector) {
        }

        @Override
        public void onBackPressed() {
            if (helper.onBackPressed()) {
                super.onBackPressed();
            }
        }
        
        ....
    }
    ```

    Note: These methods allow you to hook into the Lenddo Authorize process.

5.  Still on the onCreate method, Link the button to the UIHelper:

    ```java
    LenddoButton button = (LenddoButton) findViewById(R.id.verifyButton);
      button.setUiHelper(helper);
    ```

6.  Pass the content of the form using the form collector. On the onButtonClicked method, you can set the required information using the formData object passed to you. You can also send additional custom fields (To be discussed with your Lenddo representative)

    ```java
    @Override
    public boolean onButtonClicked(FormDataCollector formData) {
        formData = getProbeData(formData);

        // Place partner defined application id (if not yet defined)
        formData.setApplicationId("YOUR_APPLICATION_ID_STRING_HERE");
    
        // Configure the partner script dynamically (if needed only)
        formData.setPartnerScriptId("YOUR NEW PARTNER SCRIPT ID");

        return true;
    }
    ```

    **Important Note:** It is important here that you must pass a unique identifier to formData.setApplicationId, this will be used if you want to match your transaction records later on.

7.  Clicking on the Lenddo Button should trigger the Lenddo Authorization/Verification process and your app will be notified via onAuthorizeComplete when the process is done.
8.  Depending on your requirements a score may be available, in this case this is available through our REST APIs. (_Please check here for details http://www.lenddo.com/documentation/rest_api.html_)

### Customizing the Lenddo Button

You may customize the Look and Feel of the Lenddo Button in a couple of ways:

1.  Style are available at the Lenddo SDK res/drawables where you can change various button attributes.
2.  You may create or use any of your existing Button. However you need to manually handle the onClick event with **UIHelper.showAuthorize** like this:

    ```java
    helper = new UIHelper(this, this);
    helper.addGoogleSignIn(new GoogleSignInHelper());  // Add this line only if your application has an email onboarding process.
    LenddoCoreInfo.initCoreInfo(getApplicationContext());

    Button sampleButton = (Button) findViewById(R.id.sample_button);

    sampleButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           UIHelper.showAuthorize(SampleActivity.this, helper);
       }
    });
    ```

### Customizing the Popup Dialog when pressing the back key

Pressing the back key during the onboarding process will return the user to the previous screen. While on the initial screen, pressing the back key will display a popup dialog that will confirm if the user wants to cancel the onboarding process.

To customize the popup dialog's strings, simply add the following method after initializing the **helper** object:

```java
    helper = new UIHelper(this, this);
    helper.addGoogleSignIn(new GoogleSignInHelper());  // Add this line only if your application has an email onboarding process.
    helper.customizeBackPopup("Custom Back Title", "Custom Back Popup Message", "CUSTOM YES", "CUSTOM NO");
```

Also, overwrite the onBackPressed method of the calling Activity:

```java
    @Override
    public void onBackPressed() {
        if (helper.onBackPressed()) {
            super.onBackPressed();
        }
    }
```

### Using the Lenddo AutoComplete View

To use Lenddo's AutoComplete view. Refer to this [link](autocomplete.md)

### Setting API Region

To configure the Lenddo Onboarding SDK to use a specific API region. Refer to this [link](apiregion.md)

### Adding Native Facebook Integration

To configure the Lenddo Onboarding SDK to add native Facebook Integration. Refer to this [link](nativefacebookintegration.md)


## Frequently Asked Questions

#### *How do I use the Lenddo button without form data?*

The application form data used as probe information are passed in the FormDataCollector object inside the onButtonClicked method. It is possible to not pass any other information aside from the application ID. See snippet below.

```java
@Override
public boolean onButtonClicked(FormDataCollector formData) {
    // Place partner defined application id if not yet defined
    formData.setApplicationId("123456789");
    return true;
}
```

#### *Why do we require the Google Web Client ID?*

While the google-services.json already contains the Google Android and Web OAuth2.0 Client IDs, it is still important to include the Google Web Client ID as a meta-data in the AndroidManifest.xml file. This data is then passed to the GoogleSignInHelper.java class and is part of the OAuth2.0 Native login process. The Web Client ID will be used by Lenddo's backend server to communicate callbacks for the login results. More information from [Google documentations](https://developers.google.com/identity/sign-in/android/start-integrating)

#### *Why do I get an INVALID_AUDIENCE error when using Native Google Integration?*

The INVALID_AUDIENCE error is caused by using the incorrect SHA1 signing certificate in the google-services.json file. Get your correct signing certificate hash for both debug and release using the gradle task "signingReport" and view the result in the Gradle console. Update your SHA1 certificate in the Firebase Console and download the latest google-services.json file.
