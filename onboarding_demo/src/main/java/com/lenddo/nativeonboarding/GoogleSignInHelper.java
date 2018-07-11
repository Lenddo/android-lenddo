package com.lenddo.nativeonboarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonObject;
import com.lenddo.mobile.core.AuthV3ApiManager;
import com.lenddo.mobile.core.Log;
import com.lenddo.mobile.core.listeners.OnLenddoQueryCompleteListener;
import com.lenddo.mobile.onboardingsdk.dialogs.WebAuthorizeFragment;
import com.lenddo.mobile.onboardingsdk.models.NetworkProfileSigninBody;
import com.lenddo.mobile.onboardingsdk.utils.SignInHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
* Created by Joey Mar Antonio on 3/21/17.
*/

public class GoogleSignInHelper implements SignInHelper {

    private static final String TAG = GoogleSignInHelper.class.getName();
    private String client_id = null;
    private NetworkProfileSigninBody googleSigninBody = new NetworkProfileSigninBody();
    private WebAuthorizeFragment mFragment;
    private GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInHelper() {}

    public ArrayList<String> getScopes(String scopeFromWebUrl) {
        ArrayList<String> scopes = new ArrayList<>();
        String[] words = scopeFromWebUrl.split("\\s+");
        Collections.addAll(scopes, words);
        return scopes;
    }

    public void signIn(WebAuthorizeFragment fragment, final int RC_SIGN_IN, String clientId, ArrayList<String> scopes) {
        mFragment = fragment;
        Context context = mFragment.getActivity().getApplicationContext();
        if (clientId==null || client_id==null) {
            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                clientId = bundle.getString("googleClientId");
                if (clientId != null) {
                    client_id = clientId;
                } else {
                    Log.e(TAG, "Google Client ID not set in Manifest META-DATA! Exiting");
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions.Builder gsob = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(client_id)
                .requestServerAuthCode(client_id)
                .requestId();

        for (String scope: scopes) {
            gsob.requestScopes(new Scope(scope));
        }

        GoogleSignInOptions gso = gsob.build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            Log.d(TAG, "account.getServerAuthCode: " + account.getServerAuthCode());
            Log.d(TAG, "account.getIdToken: " + account.getIdToken());

            // AuthCode is only used once if you try and use it again you will get an error message.
            // So for simplicity, sign out existing signed in account
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(mFragment.getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "signOut onComplete()");
                            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                            mFragment.startActivityForResult(signInIntent, WebAuthorizeFragment.RC_SIGN_IN);
                        }
                    });
        }
        else {
            Log.d(TAG, "account is null");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            mFragment.startActivityForResult(signInIntent, WebAuthorizeFragment.RC_SIGN_IN);
        }
    }

    private String generateProfileClientTokenGoogleRequestBody() {
        JsonObject object = new JsonObject();
        object.addProperty("oauth_version", "2.0");
        object.addProperty("client_id", googleSigninBody.client_id);
        object.addProperty("access_token", googleSigninBody.access_token);
        object.addProperty("id_token", googleSigninBody.id_token);
        if (googleSigninBody.refresh_token != null && !googleSigninBody.refresh_token.isEmpty()) {
            object.addProperty("refresh_token", googleSigninBody.refresh_token);
        }
        return object.toString();
    }

    private void onboardGoogleSignin(final Context context, String request, String servicetoken, final OnLenddoQueryCompleteListener listener) {
        AuthV3ApiManager.getInstance().postProfilesClientToken("google", request, servicetoken, new OnLenddoQueryCompleteListener() {
            @Override
            public void onComplete(String rawResponse) {
                Log.i(TAG, "onboardGoogleSignin(): "+rawResponse);
                listener.onComplete(rawResponse);
            }

            @Override
            public void onError(int statusCode, String rawResponse) {
                Log.e(TAG, "onboardGoogleSignin() onError statusCode:"+statusCode+" rawResponse:"+rawResponse);
                listener.onError(statusCode, rawResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "onboardGoogleSignin() onFailure:"+throwable.getMessage());
                listener.onFailure(throwable);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void handleSignInAccount(@Nullable final GoogleSignInAccount account) {
        if (account != null) {
            googleSigninBody.id_token = account.getIdToken();
            googleSigninBody.client_id = client_id;

            AsyncTask<Void, Void, GoogleTokenResponse > task = new AsyncTask<Void, Void, GoogleTokenResponse>() {
                @Override
                protected GoogleTokenResponse doInBackground(Void... params) {
                    String authCode = account.getServerAuthCode();

                    String fileName = "credentials.json";
                    String contents;
                    try {
                        InputStream stream = mFragment.getActivity().getAssets().open(fileName);

                        int size = stream.available();
                        byte[] buffer = new byte[size];
                        stream.read(buffer);
                        stream.close();
                        contents = new String(buffer);
                    } catch (IOException e) {
                        Log.e(TAG, "An error occurred on reading contents from " + fileName + "\n" + e.toString());
                        return null;
                    }

                    JSONObject googleConfig;
                    try {
                        googleConfig = new JSONObject(contents);
                        Log.d(TAG, "googleConfig: " + googleConfig.toString());
                    } catch (JSONException ex) {
                        Log.e(TAG, "An error occurred converting contents " + fileName + " to json object.\n" + ex.toString());
                        return null;
                    }

                    String clientId = client_id;
                    String clientSecret;
                    String redirectUris = "";

                    try {
                        clientSecret = googleConfig.getJSONObject("web").getString("client_secret");
                    } catch (Exception e) {
                        Log.e(TAG, "Unable to parse client_secret from credentials.json.");
                        return null;
                    }

                    try {
                        redirectUris = googleConfig.getJSONObject("web").getJSONArray("redirect_uris").get(0).toString();
                    } catch (JSONException e) {
                        Log.e(TAG, "redirect_uris will be empty. ");
                    } catch (NullPointerException e) {
                        Log.e(TAG, "Unable to parse redirect_uris from credentials.json.");
                        return null;
                    }

                    // Exchange auth code for access token
                    GoogleTokenResponse tokenResponse;
                    try {
                        tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                                new NetHttpTransport(),
                                JacksonFactory.getDefaultInstance(),
                                "https://www.googleapis.com/oauth2/v4/token",
                                clientId,
                                clientSecret,
                                authCode,
                                redirectUris)  // Specify the same redirect URI that you use with your web
                                // app. If you don't have a web version of your app, you can
                                // specify an empty string.
                                .execute();
                    } catch (Exception e) {
                        Log.e(TAG, "Unable to getAccessToken. \n" + e.toString());
                        return null;
                    }

                    try {
                        String accessToken = tokenResponse.getAccessToken();
                        Log.d(TAG, "accessToken: " + accessToken);

                    }
                    catch (NullPointerException npe) {
                        return null;
                    }
                    return tokenResponse;
                }

                @Override
                protected void onPostExecute(GoogleTokenResponse tokenResponse) {
                    if (tokenResponse != null) {
                        String accessToken = tokenResponse.getAccessToken();
                        String refreshToken = tokenResponse.getRefreshToken();
                        Log.d(TAG, "accessToken: " + accessToken);
                        Log.d(TAG, "refreshToken: " + refreshToken);
                        googleSigninBody.access_token = accessToken;
                        googleSigninBody.refresh_token = refreshToken;
                        onboardGoogleSignin(mFragment.getActivity().getApplicationContext(),
                                generateProfileClientTokenGoogleRequestBody(),
                                AuthV3ApiManager.getInstance().getServiceToken().token, new OnLenddoQueryCompleteListener() {
                                    @Override
                                    public void onComplete(String rawResponse) {
                                        Log.d(TAG, "onPostExecute(): "+rawResponse);
                                        mFragment.loadURL(null, AuthV3ApiManager.getInstance().getBaseUrl()+"/sync/success");
                                    }

                                    @Override
                                    public void onError(int statusCode, String rawResponse) {
                                        Log.e(TAG, "onPostExecute() Error: "+rawResponse);
                                        mFragment.loadURL(null, AuthV3ApiManager.getInstance().getBaseUrl()+"/sync/error");
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Log.e(TAG, "onPostExecute() Failure: "+throwable.getMessage());
                                        mFragment.loadURL(null, AuthV3ApiManager.getInstance().getBaseUrl()+"/sync/error");
                                    }
                                });
                    } else {
                        Log.e(TAG, "Encountered context == null while onPostExecute()");
                        mFragment.loadURL(null, AuthV3ApiManager.getInstance().getBaseUrl()+"/sync/error");
                    }
                }
            };
            task.execute();
        }
        else {
            mFragment.loadURL(null, AuthV3ApiManager.getInstance().getBaseUrl() + "/sync/error");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.d(TAG, "onActivityResult(" +requestCode + ", " + resultCode + ")");
        if (requestCode == WebAuthorizeFragment.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            handleSignInAccount(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            handleSignInAccount(null);
        }
    }
}
