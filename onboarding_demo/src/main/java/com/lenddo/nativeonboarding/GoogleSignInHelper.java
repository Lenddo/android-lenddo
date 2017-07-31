package com.lenddo.nativeonboarding;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.gson.JsonObject;
import com.lenddo.mobile.core.http.AuthV3ApiClient;
import com.lenddo.mobile.core.http.OnLenddoQueryCompleteListener;
import com.lenddo.mobile.onboardingsdk.dialogs.WebAuthorizeFragment;
import com.lenddo.mobile.onboardingsdk.models.NetworkProfileSigninBody;
import com.lenddo.mobile.onboardingsdk.utils.SignInHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Joey Mar Antonio on 3/21/17.
 */

public class GoogleSignInHelper implements SignInHelper, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = GoogleSignInHelper.class.getName();
    public String client_id = null;
    private NetworkProfileSigninBody googleSigninBody = new NetworkProfileSigninBody();
    private String email;
    private GoogleApiClient mGoogleApiClient;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private WebAuthorizeFragment mFragment;
    private String mScopes;

    public GoogleSignInHelper() {}

    public ArrayList<String> getScopes(String scopeFromWebUrl) {
        mScopes = scopeFromWebUrl;
        ArrayList<String> scopes = new ArrayList<>();
        String[] words = scopeFromWebUrl.split("\\s+");
        Collections.addAll(scopes, words);
        return scopes;
    }

    public void signIn(WebAuthorizeFragment fragment, int RC_SIGN_IN, String clientId, ArrayList<String> scopes) {
        mFragment = fragment;
        Context context = fragment.getActivity().getApplicationContext();
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
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(fragment.getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private String generateProfileClientTokenGoogleRequestBody() {
        JsonObject object = new JsonObject();
        object.addProperty("oauth_version", "2.0");
        object.addProperty("client_id", googleSigninBody.client_id);
        object.addProperty("access_token", googleSigninBody.access_token);
        object.addProperty("id_token", googleSigninBody.id_token);
        object.addProperty("refresh_token", "refresh_token");
        object.addProperty("state", "{}");
        return object.toString();
    }

    private void onboardGoogleSignin(final Context context, String request, String servicetoken, final OnLenddoQueryCompleteListener listener) {
        AuthV3ApiClient.postProfilesClientToken("google", request, servicetoken, new OnLenddoQueryCompleteListener() {
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "Google Sign-in Connected.");

        AsyncTask<Void, Void, String > task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(
                            mFragment.getActivity().getApplicationContext(),
                            email,
                            "oauth2:" +mScopes);
                } catch (IOException | GoogleAuthException e) {
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                googleSigninBody.access_token = token;
                onboardGoogleSignin(mFragment.getActivity().getApplicationContext(),
                        generateProfileClientTokenGoogleRequestBody(),
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
            }
        };
        task.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Google Connection suspended. result: "+i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "Connection failed. result: "+result);
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(mFragment.getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(mFragment.getFragmentManager(), "errordialog");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == WebAuthorizeFragment.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "onActivityResult() SignIn Status: "+result.getStatus().toString());
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        result.getSignInAccount();
        if (result.isSuccess()) {
            Log.d(TAG, "Native Google Signed In");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            email = acct.getEmail();
            googleSigninBody.id_token = acct.getIdToken();
            googleSigninBody.client_id = client_id;
            mGoogleApiClient.connect();
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "Signed out, show unauthenticated UI");
            mFragment.loadURL(null, AuthV3ApiClient.getBaseUrl()+"/sync/cancel");
        }
    }
}
