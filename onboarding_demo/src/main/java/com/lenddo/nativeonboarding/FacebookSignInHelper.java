package com.lenddo.nativeonboarding;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.lenddo.mobile.core.http.AuthV3ApiClient;
import com.lenddo.mobile.core.http.OnLenddoQueryCompleteListener;
import com.lenddo.mobile.onboardingsdk.dialogs.WebAuthorizeFragment;
import com.lenddo.mobile.onboardingsdk.models.NetworkProfileSigninBody;
import com.lenddo.mobile.onboardingsdk.utils.SignInHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Joey Mar Antonio on 4/18/17.
 */

public class FacebookSignInHelper implements SignInHelper {

    public static final String TAG = FacebookSignInHelper.class.getName();
    private NetworkProfileSigninBody profileSigninBody = new NetworkProfileSigninBody();
    private WebAuthorizeFragment mFragment;
    private CallbackManager callbackManager;
    private String originalFBAppId;

    public FacebookSignInHelper() {}

    public ArrayList<String> getScopes(String scopeFromWebUrl) {
        ArrayList<String> scopes = new ArrayList<>();
        scopeFromWebUrl=scopeFromWebUrl.replaceAll(","," ");
        String[] words = scopeFromWebUrl.split("\\s+");
        Collections.addAll(scopes, words);

        return scopes;
    }

    // Do Native Login
    public void signIn(WebAuthorizeFragment fragment, int RC_SIGN_IN, String clientId, ArrayList<String> scopes) {
        originalFBAppId = FacebookSdk.getApplicationId();

        mFragment = fragment;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "FB Login Success!");
                profileSigninBody.oauth_version = "2.0";
                profileSigninBody.refresh_token = "refresh_token";
                profileSigninBody.access_token = loginResult.getAccessToken().getToken();
                profileSigninBody.client_id = loginResult.getAccessToken().getApplicationId();
                profileSigninBody.id_token = loginResult.getAccessToken().getUserId();

                onboardFacebookSignin(mFragment.getActivity().getApplicationContext(),
                        generateProfileClientTokenFacebookRequestBody(),
                        AuthV3ApiClient.getOnboardingServiceToken(), new OnLenddoQueryCompleteListener() {
                            @Override
                            public void onComplete(String rawResponse) {
                                Log.d(TAG, "onPostExecute(): "+rawResponse);
                                mFragment.loadURL(null, AuthV3ApiClient.getBaseUrl()+"/sync/success");
                            }

                            @Override
                            public void onError(int statusCode, String rawResponse) {
                                Log.e(TAG, "onPostExecute() Error: "+rawResponse);
                                mFragment.loadURL(null, AuthV3ApiClient.getBaseUrl()+"/sync/error");
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Log.e(TAG, "onPostExecute() Failure: "+throwable.getMessage());
                                mFragment.loadURL(null, AuthV3ApiClient.getBaseUrl()+"/sync/error");
                            }
                        });
                FacebookSdk.setApplicationId(originalFBAppId);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "FB Login Cancel!");
                mFragment.loadURL(null, AuthV3ApiClient.getBaseUrl()+"/sync/cancel");
                FacebookSdk.setApplicationId(originalFBAppId);
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "FB Login Error! :"+error.toString());
                mFragment.loadURL(null, AuthV3ApiClient.getBaseUrl()+"/sync/error");
                FacebookSdk.setApplicationId(originalFBAppId);
            }
        });

        if (isLoggedIn()) {
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
        }
        FacebookSdk.setApplicationId(clientId);
        LoginManager.getInstance().logInWithReadPermissions(fragment, scopes);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private String generateProfileClientTokenFacebookRequestBody() {
        JsonObject object = new JsonObject();
        object.addProperty("oauth_version", profileSigninBody.oauth_version);
        object.addProperty("client_id", profileSigninBody.client_id);
        object.addProperty("access_token", profileSigninBody.access_token);
        object.addProperty("id_token", profileSigninBody.id_token);
        object.addProperty("refresh_token", profileSigninBody.refresh_token);
        object.addProperty("state", "{}");
        return object.toString();
    }

    private void onboardFacebookSignin(final Context context, String request, String servicetoken, final OnLenddoQueryCompleteListener listener) {
        AuthV3ApiClient.postProfilesClientToken("facebook", request, servicetoken, new OnLenddoQueryCompleteListener() {
            @Override
            public void onComplete(String rawResponse) {
                Log.i(TAG, "onboardFacebookSignin(): "+rawResponse);
                listener.onComplete(rawResponse);
            }

            @Override
            public void onError(int statusCode, String rawResponse) {
                Log.e(TAG, "onboardFacebookSignin() onError statusCode:"+statusCode+" rawResponse:"+rawResponse);
                listener.onError(statusCode, rawResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "onboardFacebookSignin() onFailure:"+throwable.getMessage());
                listener.onFailure(throwable);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult() requestcode:"+requestCode+" resultcode:"+resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
