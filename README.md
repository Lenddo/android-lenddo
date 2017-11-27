# Android Lenddo SDK


Table of Contents
=================

  * [Introduction](#introduction)
  * [Pre-requisites](#pre-requisites)
  * [Installing the Lenddo SDK](#installing-the-lenddo-sdk)
  * [Running the Demo Applications](#running-the-demo-applications)
  * [Lenddo Data Collection](#lenddo-data-collection)
  * [Connecting Social Networks to Lenddo](#connecting-social-networks-to-lenddo)
  * [Document capture using Verifi.Me](#document-capture-using-verifi_me)

## Introduction
The Lenddo SDK (lenddosdk module) allows you to collect information in order for Lenddo to verify the user's information and enhance its scoring capabilities. The Lenddo SDK connects to user's social networks and also collects information and mobile data in the background and can be activated as soon as the user has downloaded the app, granted permissions and logged into the app.

## Pre-requisites
Make sure you have Android Studio properly setup and installed, please refer to the Google Developer site for the instructions [Android Studio Download and Installation Instructions.](https://developer.android.com/sdk/index.html) Currently the supported IDE is up to version 2.3.3. The SDK is not compatible with the Beta release of Android Studio. Also, use API 25 Build tools. API 26 build tools are not yet compatible as of this moment.

Before incorporating the Data SDK into your app, you should be provided with the following information:

 * Partner Script ID
 * Lenddo Score Service API Secret

Please ask for the information above from your Lenddo representative. If you have a dashboard account, these values can also be found there.

There may be also other partner specific values that you are required to set.

## Installing the Lenddo SDK

1. Extract the contents of source.zip
2. Import Modules

 + **lenddosdk** - Lenddo SDK
 + **verifimelib** - Verifi.Me SDK library (optional)
 + **mobiledata_demo** - demo application that showcases the data collection and submission (optional)
 + **onboarding_demo** - demo application for the Lenddo onboarding process (optional)
 + **verifime_demo** - demo application for Verifi.Me SDK (optional)
       
       File > New > Import Module
![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/file_new_import-module.png)
       
 Import the modules to use:
![](https://github.com/Lenddo/android-lenddo/blob/master/wiki/import_selected_modules.png)

3. Sync Gradle

#### Adding the Lenddo Credentials

In your applications AndroidManifest.xml file, inside the application key, add the following metadata:

```gradle
<!-- partner script id is mandatory -->
<meta-data android:name="partnerScriptId" android:value="@string/partner_script_id" />
<!-- api secret can be optional -->
<meta-data android:name="partnerApiSecret" android:value="@string/api_secret" />
```

In your strings.xml put your partner script id and api secret resource string.

```xml
<string name="partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<string name="api_secret">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
```

#### Adding verifimelib Dependency

In your applications build.gradle file, under dependencies, add the following line

```gradle
compile project(':verifimelib')
```


## Running the Demo Applications

To see how to use the demo applications, click this link.

## Lenddo Data Collection

For mobile data collection guide, click this [link](wiki/datasdk.md).

## Connecting Social Networks to Lenddo

For Lenddo Onboarding guide, click this [link](wiki/onboardingsdk.md).

## Document capture using Verifi Me

To use Verifi.Me library to capture documents, click this [link](wiki/verifime.md).
