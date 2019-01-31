# Android Lenddo SDK


Table of Contents
=================

- [Introduction](#introduction)
- [Pre-requisites](#pre-requisites)
- [Installing the Lenddo SDK](#installing-the-lenddo-sdk)
   - [Adding the Lenddo Credentials](#adding-the-lenddo-credentials)
   - [Adding verifimelib Dependency](#adding-verifimelib-dependency)
- [Initializing Lenddo SDK](#initializing-lenddo-sdk)
- [Migrating from the old SDK](#migrating-from-the-old-sdk)
- [Running the Demo Applications](#running-the-demo-applications)
- [Lenddo Mobile Data Collection](#lenddo-mobile-data-collection)
- [Connecting Social Networks to Lenddo](#connecting-social-networks-to-lenddo)
- [Document capture using Verifi Me](#document-capture-using-verifi-me)

## Introduction
The Lenddo SDK (lenddosdk module) allows you to collect information in order for LenddoEFL to verify the user's information and enhance its scoring capabilities. The Lenddo SDK connects to user's social networks and also collects information and mobile data in the background and can be activated as soon as the user has downloaded the app, granted permissions and logged into the app.

## Pre-requisites
Make sure you have Android Studio properly setup and installed, please refer to the Google Developer site for the instructions [Android Studio Download and Installation Instructions.](https://developer.android.com/sdk/index.html) Use API28 Build tools as minimum.

Before incorporating the Lenddo SDK into your app, you should be provided with the following information:

 * Partner Script ID

Please ask for the information above from your LenddoEFL representative. If you have a dashboard account, these values can also be found there.

There may be also other partner specific values that you are required to set.

## Installing the Lenddo SDK

1. Add the JitPack repository to your build file

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add the dependency

```gradle
dependencies {
	        implementation 'com.github.lenddo.android-lenddo:lenddosdk:v2.0.0'
	}
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

In your strings.xml put your partnerscript_id resource string.

```xml
<string name="partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<!--individual partner script id-->
<string name="data_partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<string name="onboarding_partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
<string name="verifi_me_partner_script_id">ASK_YOUR_LENDDO_REPRESENTATIVE_FOR_THIS_VALUE</string>
```


## Initializing Lenddo SDK
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

## Running the Demo Applications

To see how to use the demo applications, click this [link](wiki/demo.md).

## Lenddo Mobile Data Collection

For mobile data collection guide, click this [link](wiki/datasdk.md).

## Connecting Social Networks and eKYC

For onboarding your Social Networks accounts with LenddoEFL and using the eKYC feature, click this [link](wiki/onboardingsdk.md).

