package com.ompv.amigos.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ompv.amigos.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mPassword;
    private TextView mSignIn;
    private TextView mSignInWithFacebook;
    private TextView mSignInWithTwitter;
    private TextView mHelp;
    private TextView mCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mSignIn = (TextView) findViewById(R.id.signIn);
        mSignInWithFacebook = (TextView) findViewById(R.id.signInFacebook);
        mSignInWithTwitter = (TextView) findViewById(R.id.signInTwitter);
        mHelp = (TextView) findViewById(R.id.help);
        mCreateAccount = (TextView) findViewById(R.id.createAccount);

        mCreateAccount.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
        mSignInWithFacebook.setOnClickListener(this);
        mSignInWithTwitter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.signIn:{}break;
            case R.id.signInFacebook:{}break;
            case R.id.signInTwitter:{}break;
            case R.id.help:{}break;
            case R.id.createAccount:{
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }break;
        }

    }
}
