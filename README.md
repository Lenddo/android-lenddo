# Android Lenddo SDK


Table of Contents
=================

- [Introduction](#introduction)
- [Pre-requisites](#pre-requisites)
- [Installing the Lenddo SDK](#installing-the-lenddo-sdk)
   - [Adding the Lenddo Credentials](#adding-the-lenddo-credentials)
   - [Adding verifimelib Dependency](#adding-verifimelib-dependency)
- [Initilizing Lenddo SDK](#initilizing-lenddo-sdk)
- [Migrating from the old SDK](#migrating-from-the-old-sdk)
- [Running the Demo Applications](#running-the-demo-applications)
- [Lenddo Mobile Data Collection](#lenddo-mobile-data-collection)
- [Connecting Social Networks to Lenddo](#connecting-social-networks-to-lenddo)
- [Document capture using Verifi Me](#document-capture-using-verifi-me)

## Introduction
The Lenddo SDK (lenddosdk module) allows you to collect information in order for Lenddo to verify the user's information and enhance its scoring capabilities. The Lenddo SDK connects to user's social networks and also collects information and mobile data in the background and can be activated as soon as the user has downloaded the app, granted permissions and logged into the app.

## Pre-requisites
Make sure you have Android Studio properly setup and installed, please refer to the Google Developer site for the instructions [Android Studio Download and Installation Instructions.](https://developer.android.com/sdk/index.html) Use API 26 Build tools as minimum.

Before incorporating the Data SDK into your app, you should be provided with the following information:

 * Partner Script ID

Please ask for the information above from your Lenddo representative. If you have a dashboard account, these values can also be found there.

There may be also other partner specific values that you are required to set.

## Installing the Lenddo SDK

1. Extract the contents of source.zip
2. Import Modules

 + **lenddosdk** - Lenddo Mobile SDK
 + **verifimelib** - Verifi.Me SDK library (optional)
 + **mobiledata_demo** - demo application that showcases the data collection and submission (optional)
 + **onboarding_demo** - demo application for the Lenddo onboarding process (optional)
 + **verifime_demo** - demo application for Verifi.Me SDK (optional)
       
       File > New > Import Module
![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/file_new_import-module.png)
       
 Import the modules to use:
![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/import_selected_modules.png)


In your applications build.gradle file, under dependencies, add the following line

```gradle
compile 'com.android.support:multidex:1.0.3'
compile project(':lenddosdk')
```

3. Sync Gradle

#### Adding the Lenddo Credentials

In your applications AndroidManifest.xml file, inside the application key, add the following metadata:

```xml
<!-- partner script id is mandatory -->
<meta-data android:name="partnerScriptId" android:value="@string/partner_script_id" />
```

Or if you wish to have separate partner script id on each module (Data, Onboarding, and VerifiMe), you would have to declare a partnerScriptId tag for each.

```xml
<!-- partner script id for Mobile Data collection  -->
<meta-data android:name="dataPartnerScriptId" android:value="@string/data_partner_script_id" />

<!-- partner script id for Social Network onboarding -->
<meta-data android:name="onboardingPartnerScriptId" android:value="@string/onboarding_partner_script_id" />

<!-- partner script id for Document Verification with Verifi.Me -->
<meta-data android:name="verifiMePartnerScriptId" android:value="@string/verifi_me_partner_script_id" />

```

In your strings.xml put your partner script id and api secret resource string.

```xml
<string name="partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<string name="api_secret">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<!--individual partner script id-->
<string name="data_partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<string name="onboarding_partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<string name="verifi_me_partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
```

#### Adding verifimelib Dependency (optional)

If you are going to use the Document Verification module (Verifi.Me), in your applications build.gradle file, under dependencies, add the following line

```gradle
compile project(':verifimelib')
```

## Initilizing Lenddo SDK
In your Application class initialize Lenddo core info as shown below 

```java
package com.sample.app;

import android.support.multidex.MultiDexApplication;

import com.lenddo.mobile.core.LenddoCoreInfo;

public class  SampleApp extends MultiDexApplication {
   @Override
   public void onCreate() {
       super.onCreate();
       LenddoCoreInfo.initCoreInfo(getApplicationContext());

       // do other Lenddo stuff after init
   }
}
```

## Migrating from the old SDK

To migrate from the old sdk (released last 2017) to the new sdk (released 2018), click this [link](wiki/migration.md).

To update the LenddoSDK from a version update, simply follow the following steps:

 1. Remove the old module (:lenddosdk) from your project workspace
 2. Delete the old SDK directory
 3. Extract the latest version and Import as module in your project
 4. Ensure you have the required credentials in your Manifest file

## Running the Demo Applications

To see how to use the demo applications, click this [link](wiki/demo.md).

## Lenddo Mobile Data Collection

For mobile data collection guide, click this [link](wiki/datasdk.md).

## Connecting Social Networks to Lenddo

For onboarding your Social Networks accounts with LenddoEFL, click this [link](wiki/onboardingsdk.md).

## Document capture using Verifi Me

To use Verifi.Me library to capture documents, click this [link](wiki/verifime.md).
