# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/joeyantonio/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
-dontoptimize
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-printconfiguration "build/outputs/mapping/configuration.txt"

# Add any project specific keep options here:
# Hidden Camera
-keep public class com.androidhiddencamera.HiddenCameraDialogFragment { public *; }
-keep public class com.androidhiddencamera.CameraConfig { public *; }
# CORE SDK
-keep public class com.lenddo.mobile.core.AuthV3ApiManager { public *; }
-keep public class com.lenddo.mobile.core.LenddoCoreInfo { public *; }
-keep public class com.lenddo.mobile.core.LenddoCoreUtils { public *; }
-keep public class com.lenddo.mobile.core.listeners.* { public *; }
-keep public class com.lenddo.mobile.core.Log { public *; }
-keep public class com.lenddo.mobile.core.models.* { public *; }
-keep public class com.lenddo.mobile.core.uiwidgets.* { public *; }
-keep public class com.lenddo.mobile.core.uiwidgets.TimedAutoCompleteTextView { public *; }
-keep public class com.lenddo.mobile.core.uiwidgets.TimedEditText { public *; }
-keep public class com.lenddo.mobile.core.VerifiMeApiManager { public *; }
# DATA SDK
-keep public class com.lenddo.mobile.datasdk.AndroidData { public *; }
-keep public class com.lenddo.mobile.datasdk.core.analytics.* { public *; }
-keep public class com.lenddo.mobile.datasdk.core.analytics.FormFillingAnalytics { public *; }
-keep public class com.lenddo.mobile.datasdk.DataManager {
     public com.lenddo.mobile.datasdk.models.ClientOptions getClientOptions();
     public static synchronized com.lenddo.mobile.datasdk.DataManager getInstance();
}
-keep public class com.lenddo.mobile.datasdk.listeners.OnDataSendingCompleteCallback { public *; }
-keep public class com.lenddo.mobile.datasdk.listeners.NetworkStatusListener { public *; }
-keep public class com.lenddo.mobile.datasdk.listeners.BrowserBatchQueryCallback { public *; }
-keep public class com.lenddo.mobile.datasdk.listeners.OnLocationFoundListener { public *; }
-keep public class com.lenddo.mobile.datasdk.models.* { *; }
-keep public class com.lenddo.mobile.datasdk.models.ClientOptions { public *; }
-keep public class com.lenddo.mobile.datasdk.utils.AndroidDataUtils { public *; }
# ONBOARDING SDK
-keep public class com.lenddo.mobile.onboardingsdk.client.LenddoConstants { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.client.LenddoEventListener { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.client.FieldPreProcessListener { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.dialogs.WebAuthorizeFragment { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.dialogs.AuthorizeCallbackCollector { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.listeners.* { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.models.* { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.utils.SignInHelper { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.utils.UIHelper { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.utils.Utils { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.widget.LenddoButton { public *; }
-keep public class com.lenddo.mobile.onboardingsdk.widget.OnlineAutoCompleteTextView { public *; }
# VERIFIME SDK
-keep class com.lenddo.mobile.verifime.models.* { public *; }
-keep class com.lenddo.mobile.verifime.listeners.OnVerifiMeQueryCompleteListener { public *; }

#Google
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

# Extends
-keep class * extends android.app.Activity
-keep class * extends android.app.Fragment

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable,InnerClasses

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
