package com.ompv.amigos.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ompv.amigos.myapplication.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private TextView mSignUp;
    private String name;
    private String email;
    private String password;
    private String password2;
    SignUpActivity.BackgroundTask mBackgroundTask;

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
                verifyInfo();

                //signUp();
            }
            break;
        }

    }

    private void verifyInfo() {
        boolean test = true;
       name = mUsername.getText().toString();
       email = mEmail.getText().toString();
       password = mPassword.getText().toString();
        password2 = mPasswordAgain.getText().toString();
        if(!password2.equals(password))
        {
            test=false;
            Toast.makeText(this, "erreur mot de passe", Toast.LENGTH_SHORT).show();
        }
        else{test=true;}
        if("".equals(name)||"".equals(email)||"".equals(password)||"".equals(password2))
        {
            test=false;
            Toast.makeText(this, "erreur champ vide !", Toast.LENGTH_SHORT).show();
        }
        else{test=true;}
        if(test)
        {
            signUp();
        }
    }

    private void signUp() {
        Toast.makeText(this, name+" "+email+" "+password, Toast.LENGTH_SHORT).show();
        mBackgroundTask= new BackgroundTask();
        mBackgroundTask.execute(name,email,password);

    }
    class BackgroundTask extends AsyncTask<String,Void,String> {

        String addUser_url;



        @Override
        protected void onPreExecute() {

            addUser_url = "https://onamangerpourvous.000webhostapp.com/addUser.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String nom, mail, pass;
            URL url = null;
            nom = args[0];
            mail = args[1];
            pass = args[2];

            try {

                addUser_url="https://onamangerpourvous.000webhostapp.com/addUser.php?name="+nom+"&mail="+mail+"&password="+pass;
                url = new URL(addUser_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoOutput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

              /*  String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(nom, "UTF-8") + "&" +
                        URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                */
                InputStream inputStream = httpsURLConnection.getInputStream();
                inputStream.close();
                httpsURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "One row of data Inserted ...";
        }
            @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(SignUpActivity.this, result, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
