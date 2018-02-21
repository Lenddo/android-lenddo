Lenddo Data Collection
=======================

## Table of Contents

1.  [Introduction](#user-content-introduction)
2.  [Prerequisites](#user-content-prerequisites)
3.  [Data Collection Mechanism and Required Permissions](#user-content-data-collection-mechanism-and-required-permissions)
4.  [Data SDK Package](#user-content-data-sdk-package)
5.  [Installation Instructions](#user-content-installation-instructions)
    1.  [Initialize Data Collection](#user-content-initialize-data-collection)
    2.  [Starting Data Collection](#user-content-starting-data-collection)
    3.  [Passing the Provider Access Token](#user-content-passing-the-provider-access-token)
    4.  [Passing the Application ID](#user-content-passing-the-application-id)
    5.  [Sending Application Data](#user-content-sending-application-data)
6.  [Using Form Filling Analytics](#user-content-using-form-filling-analytics)
    1.  [Using the TimedWidget Views](#user-content-using-the-timedwidget-views)
    2.  [Collecting Form Analytics from your TimedWidget](#user-content-collecting-form-analytics-from-your-timedwidget) 
    3.  [Submitting the Data Collected](#user-content-submitting-the-data-collected)
    4.  [Clearing the Data Collected](#user-content-clearing-the-data-collected)
7.  [Configuring the Data SDK](#user-content-configuring-the-data-sdk)
    1.  [Using WIFI internet connectivity instead of data plan ](#user-content-using-wifi-internet-connectivity-instead-of-data-plan)
    2.  [Set Accent color for Material Theme dialog](#user-content-set-accent-color-for-material-theme-dialog) 
    3.  [Registering a Data Sending Completion Callback](#user-content-registering-a-data-sending-completion-callback)
    4.  [Enabling Log Messages](#user-content-enabling-log-messages)

    
## Introduction 

The Lenddo SDK (lenddosdk) allows you to collect information in order for Lenddo to verify the user's information and enhance its scoring capabilities. The Lenddo SDK collects information in the background and can be activated as soon as the user has downloaded the app, given permissions are granted to the app.

## Prerequisites
Make sure you have Android Studio properly setup and installed, please refer to the Google Developer site for the instructions [Android Studio Download and Installation Instructions.](https://developer.android.com/sdk/index.html) The SDK is not compatible with the Beta release of Android Studio.

Before incorporating the Lenddo SDK into your app, you should be provided with the following information:

 * Partner Script ID
 * Lenddo Score Service API Secret

Please ask for the information above from your Lenddo representative. If you have a dashboard account, these values can also be found there.

There may be also other partner specific values that you are required to set.

## Data Collection Mechanism and Required Permissions

The LenddoDataSDK captures the following data stored on the phone consistent with the permissions defined (see section on adding permissions):

*   Contacts
*   SMS (Performed Periodically)
*   Call History (Performed Periodically)
*   User's Location (Performed Periodically)
*   User's Browsing history (Performed Periodically)
*   User’s Installed Apps
*   Calendar Events
*   Phone Number, Brand and Model

Lenddo SDK will use information stored on the users' phone. It is advisable for all permissions to be added to your app to enable LenddoData to extract the necessary information for verification and scoring. The optimal permissions are already defined for you in the Libraries’ AndroidManifest.xml and are automatically added to your app using gradle when you rebuild the app after adding our SDK.

Below is the list of required permissions.

```java
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_SMS" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.READ_CALENDAR" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="com.android.browser.permission.READ_HISTORY_BOOKMARKS" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

If you do not want the all default permissions added, you manually have to remove permissions by editing the **lenddosdk/src/main/AndroidManifest.xml** and comment out permissions you do not wish to grant, however please note that the following permissions at the minimum are required for the operation of the SDK and should NOT be removed:

```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

It is also important that these permissions are consistent with the privacy policy of your app.

### Initialize Data Collection 

You need to add an Application class to your app (If it does not already have one). See below for an example:

```java
package com.sample.app;

import android.app.Application;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.datasdk.AndroidData;
import com.lenddo.mobile.datasdk.models.ClientOptions;

public class  SampleApp extends Application {
   @Override
   public void onCreate() {
       super.onCreate();
       LenddoCoreInfo.initCoreInfo(getApplicationContext());

       ClientOptions clientOptions = new ClientOptions();
       clientOptions.enableLogDisplay(BuildConfig.DEBUG);

       // Uncomment the next line when you want data to be uploaded only when wifi is available
       // clientOptions.setWifiOnly(true);

       AndroidData.setup(getApplicationContext(), clientOptions);
   }
}
```

If you already have an Application class, then simply add the AndroidData.setup() call to the onCreate method of your Application class as shown above.

Note that you need to set the secret key and api key provided to you by Lenddo here:

```java
PARTNER_SCRIPT_ID
API_SECRET
```

Your Lenddo contact will inform you if this is something you need to change.

If you did not have an application class before, you need to add a android:name attribute to the application tag in to your main application’s AndroidManifest.xml (below is an example):

```xml
<?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="lenddo.com.lenddoconnect">

        <application
            android:name=".SampleApp"
            android:allowBackup="true"
            android:icon="@drawable/ic_launcher"
            android:label="@string/app_name"
            android:theme="@style/AppTheme">
        ......
```

### Starting Data Collection

You may start data collection at any time, though ideally it is done after a user has successfully logged in to your app and before the Lenddo Onboarding SDK is started. You are required to pass a string that identifies the user (e.g. user id) as the second parameter. This allows you and our data science team to associate acquired data with the specific user at a later point in time. Below is the sample code to trigger data collection:

```java
AndroidData.startAndroidData(YourActivity.this, “USER_IDENTIFIER_OR_APPLICATION_ID”);
```

Please note that you only need to do this once for the current user. Data collection will automatically start even on the next session of your app unless your app was uninstalled or had its data cleared.

Once integration has been completed and you have started Data Collection during testing, notify your Lenddo representative to check on the data that have been collected and if changes are necessary.


### Passing the Provider Access Token

To enhance the amount of data collected, the Lenddo Data SDK accepts provider access tokens from different providers. Below is an example on how it is done for Facebook:

```java               
String provider = AndroidData.PROVIDER_FACEBOOK;
// This is the ID that uniquely identifies the user on the selected Provider. For Facebook this would be the Facebook ID.
String providerId = "Your Network ID";
// This is the Access token you receive after finishing the oAuth 2.0 Sequence.
String accessToken = "Your Network Access Token";
String extraData = "Optional extra data. refreshtoken for KakaoStory";
long expiration;  // Network Expiration timestamp

// The extradata variable passed to setProviderAccessToken must be an instance of JsonObject
JsonObject extra_data_json = new JsonObject();
extra_data_json.addProperty("refresh_token", extraData);

AndroidData.setProviderAccessToken(context, provider, providerId, accessToken, extra_data_json, expiration);
```

Supported provider strings are as follows:

```java
AndroidData.PROVIDER_FACEBOOK;
AndroidData.PROVIDER_LINKEDIN;
AndroidData.PROVIDER_YAHOO;
AndroidData.PROVIDER_WINDOWSLIVE;
AndroidData.PROVIDER_GOOGLE;
AndroidData.PROVIDER_TWITTER;
AndroidData.PROVIDER_KAKAOTALK;
```


### Sending Application Data

LenddoSDK let partner send application data which contains verification data that is use for verification purposes and partner data that might also be helpful for verification but unlike verification data, partner data is a free form data. Below is an example on how to send application data.

```java
    // Set up application data
    ApplicationPartnerData data = new ApplicationPartnerData();

    // Use Application Id as reference number
    data.reference_number = "APPLICATION_ID";
    
    // Initialize partner data (you can add any data (key-values pair))
    data.application = new JsonObject();

    // Initialize verification data
    data.verification_data = new ApplicationPartnerData.verification_data();
    data.verification_data.address = new ApplicationPartnerData.verification_data.address();
    data.verification_data.employment_period = new ApplicationPartnerData.verification_data.employment_period();
    data.verification_data.mothers_maiden_name = new ApplicationPartnerData.verification_data.mothers_maiden_name();
    data.verification_data.name = new ApplicationPartnerData.verification_data.name();
    data.verification_data.phone = new ApplicationPartnerData.verification_data.phone();

    // Set Values for verification data
    data.verification_data.name.first = "string";
    data.verification_data.name.middle = "string";
    data.verification_data.name.last = "string";
    data.verification_data.date_of_birth = "1995-03-08";
    data.verification_data.employer = "string";
    data.verification_data.phone.mobile = "string";
    data.verification_data.phone.home = "string";
    data.verification_data.mothers_maiden_name.first = "string";
    data.verification_data.mothers_maiden_name.middle = "string";
    data.verification_data.mothers_maiden_name.last = "string";
    data.verification_data.university = "string";
    data.verification_data.email = "partner@email.com";
    data.verification_data.address.line_1 = "string";
    data.verification_data.address.line_2 = "string";
    data.verification_data.address.administrative_division = "string";
    data.verification_data.address.city = "string";
    data.verification_data.address.country_code = "string";
    data.verification_data.address.postal_code = "string";
    data.verification_data.address.latitude = 0;
    data.verification_data.address.longitude = 0;

    String payload = new Gson().toJson(data).toString()
    AndroidData.sendPartnerApplicationData(context, payload);
```

## Using Form Filling Analytics

Using Lenddo's custom TimedWidgets (TimedEditText, TimedSeekBar, TimedRadioButton) it is possible to get form filling analytics with every user's input in the application form. To do this, your application must make use of the custom views that implements the TimedWidget interface.

### Using the TimedWidget Views

To add form filling analytics feature to your application form, replace all android views with custom views that implements the TimedWidget interface. For example, replace all EditText views in your application with the TimedEditText view.


In your root layout container add the following xml namespace

```xml
        xmlns:lenddo="http://schemas.android.com/apk/res-auto"
```

```xml
    <com.lenddo.core.uiwidgets.TimedEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/tedt_firstname"
        lenddo:enableFormFillingAnalytics="true"
        lenddo:analyticsKeyTitle="firstname" />

```

Add the following properties, enableFormFillingAnalytics and analyticsKeyTitle  to enable the form filling analytics for the TimedEditText and add a key title that will identify this form field.

```xml
        lenddo:enableFormFillingAnalytics="true"
        lenddo:analyticsKeyTitle="firstname"
```


### Collecting Form Analytics from your TimedWidget

To collect the form filling analytics data from your TimedWidget, instantiate and use the TimedWidget object similar to its extended view. Example, for an EditText view use the TimedEditText class.

In your Java code:

```Java
        TimedEditText tedt_firstname = (TimedEditText) findViewById(R.id.tedt_firstname);
``` 

To start collecting analytics data with your TimedEditText, simply add the TimedEditText object to the FormFillingAnalytics class

```Java
        FormFillingAnalytics.getInstance(getApplicationContext()).add(tedt_firstname.getFormFillingAnalyticsDataValue());
```


### Submitting the Data Collected

Finally, call the submitFormFillingAnalytics() method to submit the data online.

```Java
        AndroidData.submitFormFillingAnalytics(getApplicationContext());
```

### Clearing the Data Collected

After submitting the collected data, the stored analytics data are automatically cleared. You may clear the stored analytics data without submitting the collected data online by calling the reset() method. This will clear all collected form filling analytics data without sending it online.

```Java
        FormFillingAnalytics.getInstance(getApplicationContext()).reset();
```


## Configuring the Data SDK

The Lenddo Data SDK has a configuration that can be set programmatically. By setting the _ClientOptions_ object it is possible to configure the Lenddo Data SDK.

### Using WIFI internet connectivity instead of data plan 

```Java
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.setWifiOnly(true);
        AndroidData.setup(getApplicationContext(), clientOptions);
```

### Set Accent color for Material Theme dialog  

```Java
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.setThemeColor("#aa2255");  // PINK Accent color
        AndroidData.setup(getApplicationContext(), clientOptions);
```


### Registering a Data Sending Completion Callback

The Lenddo Data SDK can be configured to have a callback that will let the calling application run its own code upon data sending completion. The callback has an interface for both a  successful data sending and error on data submission.

```Java
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.registerDataSendingCompletionCallback(new OnDataSendingCompleteCallback() {
            @Override
            public void onDataSendingSuccess() {
                // call your routines here (response status code is 20x ~ 30x)
                Log.d("Callback", "Data sending completed successfully!");
            }

            @Override
            public void onDataSendingError(int statusCode, String errorMessage) {
                // call your routines here
                Log.d("Callback", "Data sending failed! statuscode:"+statusCode+" error:"+errorMessage);
            }
            
            @Override
            public void onDataSendingFailed(Throwable t) {
                // call your routines here
                Log.e("Callback", "Network connection failure! " + t.getMEssage());
            }

        });

        AndroidData.setup(getApplicationContext(), clientOptions);
```

If a data sending have failed, the Lenddo Data SDK will try to resend the gathered data the next time the call to *AndroidData.startAndroidData(activity, applicationId)* is encountered. Eventually, the *onDataSendingSuccess()* method will be called. 
To remove a callback, pass a null object to registerDataSendingCompletionCallback() method.

```Java
        clientOptions.registerDataSendingCompletionCallback(null);
```

### Enabling Log Messages

The Lenddo Data SDK log messages are disabled by default and can be enabled manually by setting the following configuration.

```Java
        clientOptions.enableLogDisplay(true);
```

