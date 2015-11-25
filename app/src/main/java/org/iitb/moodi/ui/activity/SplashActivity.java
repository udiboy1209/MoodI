package org.iitb.moodi.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;


import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.AddUserResponse;
import org.iitb.moodi.api.CheckUserResponse;
import org.iitb.moodi.api.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    CallbackManager callbackManager;
    public static final int REGISTRATION_ACTIVITY=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_splash);
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);

        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Check whether user exists!
                checkUserExistence();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, exception.getMessage());
            }
        });
        if (AccessToken.getCurrentAccessToken()!=null) {
            Log.d(TAG,AccessToken.getCurrentAccessToken().getToken());
            loginButton.setVisibility(View.GONE);
            Log.d(TAG,me.getJSON());
            Log.d(TAG,prefs.getString("user_json",""));
            checkUserExistence();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LoginDebug", "ActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REGISTRATION_ACTIVITY) {
            Log.d(TAG,me.getJSON());
            Log.d(TAG,prefs.getString("user_json", ""));
            me = (new Gson()).fromJson(prefs.getString("user_json",""),User.class);
            Log.d(TAG, me.getJSON());
            // Adding user to the database
            addUser();
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addUser() {
        // Make sure that User object me has all details required for adding User.

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                AddUserResponse c = (AddUserResponse) o;
                Log.d(TAG+", reg",c.getStatus()+"");
                Log.d(TAG+", reg",c.getMINumber());
                me.mi_no = c.getMINumber();

                SharedPreferences.Editor spe = prefs.edit();
                spe.putString("user_json", me.getJSON());
                Log.d(TAG, me.getJSON());
                spe.commit();
                Log.d(TAG + ", pref", prefs.getString("user_json", ""));
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG, error);
            }
        };
        Log.d(TAG,AccessToken.getCurrentAccessToken().getToken());
        Log.d(TAG,me.getJSON());
        methods.addUser(me.fbid, me.city_id, me.clg_id,
                me.name, me.email, me.phone,
                me.dob, me.gender, me.year_study,
                AccessToken.getCurrentAccessToken().getToken(), callback);
    }

    public void checkUserExistence() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(m_API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                CheckUserResponse c = (CheckUserResponse) o;
                Log.d(TAG,c.getStatus()+"");
                Log.d(TAG,c.getUser());
                if (!c.getStatus() && c.getUser().equalsIgnoreCase("Not registered")) {
                    // User hasn't registered for Mood Indigo!
                    // First get his data
                    getBiodata();
                }
                else if (c.getStatus()) {
                    // User entry exists
                    me = (new Gson()).fromJson(c.getUser(),User.class);
                    Log.d(TAG,me.getJSON());
                    SharedPreferences.Editor spe = prefs.edit();
                    spe.putString("user_json", me.getJSON());
                    Log.d(TAG, me.getJSON());
                    spe.commit();
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG, error);
            }
        };
            methods.checkUser(Profile.getCurrentProfile().getId(),
                              AccessToken.getCurrentAccessToken().getToken(),
                              callback);
    }
    public void getBiodata() {
        Log.d(TAG,AccessToken.getCurrentAccessToken().getToken());
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        JSONObject userDetails = response.getJSONObject();
                        Log.d(TAG, userDetails.toString());
                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), RegistrationActivity.class);
                        try {
                            i.putExtra("name", userDetails.getString("name"));
                            i.putExtra("fbid", userDetails.getString("id"));
                            i.putExtra("email", userDetails.getString("email"));
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        startActivityForResult(i, REGISTRATION_ACTIVITY);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
