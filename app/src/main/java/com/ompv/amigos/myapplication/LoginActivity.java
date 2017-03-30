package com.ompv.amigos.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mPassword;
    private TextView mSignIn;
    private LoginButton mSignInWithFacebook;
    private TextView mSignInWithTwitter;
    private TextView mHelp;
    private TextView mCreateAccount;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
        getLoginDetails(mSignInWithFacebook);
    }

    private void initViews() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mSignIn = (TextView) findViewById(R.id.signIn);
        mSignInWithFacebook = (LoginButton) findViewById(R.id.signInFacebook);
        mSignInWithTwitter = (TextView) findViewById(R.id.signInTwitter);
        mHelp = (TextView) findViewById(R.id.help);
        mCreateAccount = (TextView) findViewById(R.id.createAccount);

        mCreateAccount.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
        mSignInWithFacebook.setOnClickListener(this);
        mSignInWithTwitter.setOnClickListener(this);
    }

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("data", data.toString());
    }

    protected void getLoginDetails(LoginButton login_button) {
        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                Intent intent = new Intent(getApplicationContext(), MainActivityMEnu.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                // code for cancellation
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.<br />
        AppEventsLogger.activateApp(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.<br />
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn: {
                Intent intent = new Intent(getApplicationContext(), MainActivityMEnu.class);
                startActivity(intent);

            }
            break;
            // case R.id.signInFacebook: {
            //}
            //break;
            case R.id.signInTwitter: {
            }
            break;
            case R.id.help: {
            }
            break;
            case R.id.createAccount: {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
            break;
        }

    }
}
