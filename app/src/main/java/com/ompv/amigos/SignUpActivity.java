package com.ompv.amigos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ompv.amigos.myapplication.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private TextView mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordAgain = (EditText) findViewById(R.id.passwordagain);
        mEmail = (EditText) findViewById(R.id.mail);
        mSignUp = (TextView) findViewById(R.id.signUp);

        mSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.signUp: {
            }
            break;
        }

    }
}
