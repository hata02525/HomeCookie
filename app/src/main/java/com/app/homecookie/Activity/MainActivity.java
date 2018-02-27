package com.app.homecookie.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.homecookie.Fragments.CreatIdFragment;
import com.app.homecookie.Fragments.CreateProfileFragment;
import com.app.homecookie.Fragments.ForgotPasswordFragment;
import com.app.homecookie.Fragments.ForgotPasswordVerifyCodeFragment;
import com.app.homecookie.Fragments.LoginFragment;
import com.app.homecookie.Fragments.SetNewPasswordFragment;
import com.app.homecookie.Fragments.SignUpFragment;
import com.app.homecookie.Fragments.TermsFragment;
import com.app.homecookie.Helper.SharedPreference;
import com.app.homecookie.Network.Network;
import com.app.homecookie.Network.OnNetworkCallBack;
import com.app.homecookie.R;
import com.app.homecookie.Util.Constants;
import com.app.homecookie.Util.Progress;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonObject;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import static android.content.pm.PackageManager.GET_SIGNATURES;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnNetworkCallBack {

    private long mBackPressed = 0;
    private long TIME_INTERVAL = 2000;
    private static final int RC_SIGN_IN = 007;
    Context context;
    CallbackManager callbackManager;
    public GoogleApiClient mGoogleApiClient;
    public Progress progress;
    JsonObject jsonObject;
    public String name = "";
    public String emailId = "";
    public String dob = "";
    public String gender = "";
    public String facebookId = "";
    public String googleId = "";
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        sharedPreference = new SharedPreference(MainActivity.this);
        progress = new Progress(MainActivity.this);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();

        String hashKey = getDebugKeyHash(this);
        Log.e("HASH_KEY :", "" + hashKey);


        /****************************---------------Calling Google API Client-----------------******************/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            // Make the call to GoogleApiClient
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        replaceLoginFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_CMERA_REQUEST_CODE);
                return;
            }
        }


        Log.e("AccessToken", new SharedPreference(this).getString(Constants.ACCESSTOKEN, ""));

    }

    public void replaceLoginFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new LoginFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceSignUpFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SignUpFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceCreateIdFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CreatIdFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceCreateProfileFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CreateProfileFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceTermsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new TermsFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceForgotPasswordFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ForgotPasswordFragment()).addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceVerifyCodeFrament() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ForgotPasswordVerifyCodeFragment()).addToBackStack(null).commitAllowingStateLoss();
    }


    public void replaceSetNewPasswordFragment(String verificationKey) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, SetNewPasswordFragment.newInstance(verificationKey)).addToBackStack(null).commitAllowingStateLoss();
    }


    /* -----------------------------Method to get KeyHash for Facebook-------------***********/
    private String getDebugKeyHash(Context ctx) {
        // Add code to print out the key hash
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "SHA-1 generation: the key count not be generated: NameNotFoundException thrown";
        } catch (NoSuchAlgorithmException e) {
            return "SHA-1 generation: the key count not be generated: NoSuchAlgorithmException thrown";
        }

        return "SHA-1 generation: epic failed";

    }
    /*****************-----------------------------End of Method to get KeyHash for Facebook------------------***********/


    /*******
     * -Facebook Login code Start-
     *****/

    private void facebookLogin() {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity Response ", response.toString());
                                /*{Response:  responseCode: 200, graphObject: {"id":"688943871275382","first_name":"sandeep","last_name":"Kushwah","email":"sandeep.kus022@gmail.com",
                                "picture":{"data":{"is_silhouette":false,"url":"https:\/\/scontent.xx.fbcdn.net\/v\/t1.0-1\/p200x200\/14495345_630162317153538_6320408807838912776_n.jpg?oh=905eb63f8559c63bf041c2154846b7e0&oe=5923B434"}}}, error: null}*/
                                try {
                                    String id = object.optString("id");
                                    String fName = object.optString("first_name");
                                    String lName = object.optString("last_name");
                                    String email = object.optString("email");
                                    String birthday = object.optString("birthday");
                                    JSONObject picture = object.getJSONObject("picture").getJSONObject("data");
                                    String pictureUrl = picture.getString("url");
                                    facebookId = id;
                                    emailId = email;
                                    name = fName + " " + lName;
                                    dob = birthday;

                                    if (Network.isConnected(MainActivity.this)) {
                                        progress.show();
                                        String accessToken = new SharedPreference(MainActivity.this).getString(Constants.ACCESSTOKEN, "");
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("accessToken", accessToken);
                                        jsonObject.addProperty("email", email);
                                        jsonObject.addProperty("password", "");
                                        jsonObject.addProperty("socialtype", "1");
                                        jsonObject.addProperty("socialId", id);

                                        callToSocialLogin(jsonObject);
                                    } else {
                                        Toast.makeText(MainActivity.this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                                    }
                                    // Toast.makeText(MainActivity.this, "Welcome :" +fName+" "+lName+", Email :"+email+" Birthday :"+birthday+","+" image-url :"+pictureUrl, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,picture.type(large),birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Problem connecting to Facebook", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void loginFacebook() {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
    }

    /*******
     * -Facebook Login code End-
     *****/
    public void googleSignIn() {
        progress.show();
        googleSignOut();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("TAG", "display name: " + acct.getDisplayName());
            String id = "";
            String fName = "";
            String lName = "";
            String email = "";
            String pictureUrl = "";
            if (acct.getId() != null) {
                id = acct.getId();

            }
            if (acct.getDisplayName() != null) {
                String name = acct.getDisplayName();
                String fullName[] = name.split(" ");
                fName = fullName[0];
                lName = fullName[1];
            }
            if (acct.getEmail() != null) {
                email = acct.getEmail();
            }
            if (acct.getPhotoUrl() != null) {
                pictureUrl = String.valueOf(acct.getPhotoUrl());
            }

            googleId = id;
            emailId = email;
            name = fName + " " + lName;
            progress.show();
            if (Network.isConnected(MainActivity.this)) {
                progress.show();
                String accessToken = new SharedPreference(MainActivity.this).getString(Constants.ACCESSTOKEN, "");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("accessToken", accessToken);
                jsonObject.addProperty("email", email);
                jsonObject.addProperty("password", "");
                jsonObject.addProperty("socialtype", "2");
                jsonObject.addProperty("socialId", id);


                Network.requestForLogin(MainActivity.this, jsonObject, this);
                // calltoSocialLogin(jsonObject);
            } else {
                Toast.makeText(MainActivity.this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }

            // Toast.makeText(MainActivity.this, "Welcome :" +fName+" "+lName+", Email :"+email+" image-url :"+pictureUrl, Toast.LENGTH_SHORT).show();
            progress.dismiss();

        } else {
            progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (visibleFragment instanceof LoginFragment) {
            closeActivityWithDoubleClick();
        } else if (visibleFragment instanceof SignUpFragment || visibleFragment instanceof ForgotPasswordFragment || visibleFragment instanceof TermsFragment) {
            replaceLoginFragment();
        } else if (visibleFragment instanceof CreatIdFragment) {
            closeActivityWithDoubleClick();
        } else if (visibleFragment instanceof CreateProfileFragment) {
            closeActivityWithDoubleClick();
        } else {
            super.onBackPressed();
        }
    }


    private void closeActivityWithDoubleClick() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 005: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("CONNECTIOL_FAILED", "Connection failed");
    }

    @Override
    public void onSuccess(int requestType, boolean isSuccess, String msg, Object data) {
        progress.dismiss();
        progress.dismiss();
        if (isSuccess) {
            int profileStatus = sharedPreference.getInt(Constants.USER_PROFILE_STATUS, 0);
            if (profileStatus == 1) {
                replaceCreateIdFragment();
            }
            if (profileStatus == 2) {
                replaceCreateProfileFragment();
            }
            if (profileStatus == 3) {
                sharedPreference.putBoolean(Constants.SESSION, true);
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finishAffinity();
            }
        }
    }

    @Override
    public void onError(String msg) {
        progress.dismiss();
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void callToSocialLogin(JsonObject jsonObject) {

        Network.requestForLogin(MainActivity.this, jsonObject, this);
    }

}


