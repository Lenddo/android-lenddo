Native Facebook Integration
======================

The Lenddo Onboarding SDK supports verification through Facebook login. Seamless user login experience with Facebook can be achieved if the Facebook SDK is already being used in the parent application. The Lenddo Onboarding SDK can allow native login by following this guide. Doing so will easily let the user onboard with a Facebook account that is currently logged-in the device and will no longer ask for input of a username and password.

If your application is not using the Facebook SDK, it is possible to add the Facebook SDK by following the steps in this [link](https://developers.facebook.com/docs/android/getting-started). Take note that adding the Facebook SDK to your application will increase its APK size.


**Requirements:**

1. The Facebook SDK must already be integrated in your application. [Guide](https://developers.facebook.com/docs/android/getting-started)
2. Copy the FacebookSignInHelper.java to the package: **com.lenddo.nativeonboarding**

**Required Permissions**
The following Facebook permissions are required. Missing some of the permissions will make the verification process fail.

- email 
- public_profile
- user_friends
- user_birthday
- user\_work_history 
- user\_education_history 
- user_likes 
- user_location

**Code Integration**

To allow native Facebook login, simply copy the [FacebookSignInHelper.java](blob/master/simple_loan/src/main/java/com/lenddo/nativeonboarding/FacebookSignInHelper.java) to the package: **com.lenddo.nativeonboarding** and add call to the *addFacebookSignIn(new FacebookSignInHelper())* method after UIHelper object instantiation as shown below.

*Sample code:*

```java
// UIHelper object instantiation
helper = new UIHelper(this, this);

// Add support for Native Facebook login
helper.addFacebookSignIn(new FacebookSignInHelper());
```


