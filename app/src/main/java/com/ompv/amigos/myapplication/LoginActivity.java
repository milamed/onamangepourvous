package com.ompv.amigos.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import javax.net.ssl.HttpsURLConnection;

import static java.security.AccessController.getContext;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mSignIn;
    private LoginButton mSignInWithFacebook;
    private SignInButton mSignInWithGoogle;
    private TextView mHelp;
    private TextView mCreateAccount;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private BackgroundTask mBackgroundTask;

    public CoordinatorLayout mcoordinatorlayout;

    private SQLiteDatabase db;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    public DataClassAtt mADataClassAtt;
    private Context context;

    protected void createDatabase() {
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS user(idUser INTEGER PRIMARY KEY , password VARCHAR," +
                "mail VARCHAR,phone VARCHAR,name VARCHAR,photo VARCHAR);");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDatabase();
        facebookSDKInitialize();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
        getLoginDetails(mSignInWithFacebook);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private void initViews() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mSignIn = (TextView) findViewById(R.id.signIn);
        mSignInWithFacebook = (LoginButton) findViewById(R.id.signInFacebook);
        mSignInWithGoogle = (SignInButton) findViewById(R.id.signInGoogle);
        mHelp = (TextView) findViewById(R.id.help);
        mCreateAccount = (TextView) findViewById(R.id.createAccount);

        mcoordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mCreateAccount.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
        mSignInWithFacebook.setOnClickListener(this);
        mSignInWithGoogle.setOnClickListener(this);
        mHelp.setOnClickListener(this);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            //btnSignIn.setVisibility(View.GONE);
            //btnSignOut.setVisibility(View.VISIBLE);
            //btnRevokeAccess.setVisibility(View.VISIBLE);
            //llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            //btnSignIn.setVisibility(View.VISIBLE);
            //btnSignOut.setVisibility(View.GONE);
            //btnRevokeAccess.setVisibility(View.GONE);
            //llProfileLayout.setVisibility(View.GONE);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

/*
            String personName = acct.getDisplayName().toString();
            String personGivenName = acct.getGivenName().toString();
            String personFamilyName = acct.getFamilyName().toString();
            String personEmail = acct.getEmail().toString();
            String personId = acct.getId();
            String personPhoto = acct.getPhotoUrl().toString();

            Log.e(TAG, "Name: " + personName + ", email: " + personEmail
                    + ", Image: " + personPhoto);
            mBackgroundTask= new BackgroundTask();
            mBackgroundTask.execute(personId,personName,personEmail,personPhoto);
            Intent mInfoProfil = new Intent(LoginActivity.this,MainActivityMEnu.class);

            startActivity(mInfoProfil);
*/
            //  txtName.setText(personName);
            //txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);

            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        // handleSignInResult( result);
        GoogleSignInAccount acct = result.getSignInAccount();
        if (acct == null) {
            Log.v("med", "pffff");
        }
        String personName = "";
        String personPhoto = "";
        String personEmail = "";
        if (acct.getDisplayName() != null) {
            personName = acct.getDisplayName();
        }


        if (acct.getEmail() != null) {
            personEmail = acct.getEmail();
        }

        if (acct.getPhotoUrl() != null) {
            personPhoto = acct.getPhotoUrl().toString();
        }

        // String personName = acct.getDisplayName();
        String personGivenName = acct.getGivenName();
        String personFamilyName = acct.getFamilyName();
        // String personEmail = acct.getEmail();
        String personId = acct.getId();
        // String personPhoto = acct.getPhotoUrl().toString();

        // mBackgroundTask= new BackgroundTask();
        //mBackgroundTask.execute(personId,personName,personEmail,personPhoto);
        Toast.makeText(this, personName + " " + personEmail, Toast.LENGTH_SHORT).show();

        saveUser(personName, personEmail, personPhoto);


    }

    private void saveUser(String name, String email, String photo) {
        BackgroundSaveTask mBackgroundSaveTask = new BackgroundSaveTask();
        mBackgroundSaveTask.execute(name, email, photo);
        Intent mInfoProfil = new Intent(LoginActivity.this, MainActivityMEnu.class);
        startActivity(mInfoProfil);
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


                signInService();

            }
            break;
            // case R.id.signInFacebook: {
            //}
            //break;
            case R.id.signInGoogle: {
                signIn();
            }
            break;
            case R.id.help: {

                Intent intent = new Intent(getApplicationContext(), SearchRestaurant.class);
                startActivity(intent);
            }
            break;
            case R.id.createAccount: {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    private void signInService() {
        new AsyncLogin().execute();


    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;
        CatLoadingView mView = new CatLoadingView();
        ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tAuthentification...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://onamangerpourvous.000webhostapp.com/afficheliste.php?action=get_allliste");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        protected void affiche() {


            String query = "select * from user ;";


            Cursor cursor = db.rawQuery(query, null);
            List<DataClassAtt> l = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    l.add(new DataClassAtt(cursor.getString(4), cursor.getInt(0), cursor.getString(2),
                            cursor.getString(1), cursor.getString(3), cursor.getString(5)));

                } while (cursor.moveToNext());
            }

            for (DataClassAtt p : l) {
                Toast.makeText(getApplicationContext(), l.toString(), Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataClassAtt> alldataPOST = new ArrayList<>();
            boolean test = false;
            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);


                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataClassAtt postData = new DataClassAtt();
                    if ((json_data.getString("mail").toString().equals(mUsername.getText().toString())) &&
                            (json_data.getString("password").toString().equals(mPassword.getText().toString()))) {
                        test = true;

                         /* postData.setFirstName(json_data.getString("name"));
                            postData.setEmail(json_data.getString("mail"));
                            postData.setpassword(json_data.getString("password"));
                           `idUser`, `password`, `mail`, `phone`, `name`, `photo`
                            */
                        String userName = json_data.getString("name").toString();
                        String userMail = json_data.getString("mail").toString();
                        String userPassword = json_data.getString("password").toString();
                        String userPhone = json_data.getString("phone").toString();
                        String userPhoto = json_data.getString("photo").toString();
                        int idUser = json_data.getInt("idUser");

                        db.execSQL("delete from user");

                        String query = "INSERT INTO user(name, password, mail, phone,  photo,idUser)  " +
                                "VALUES('" + userName + "', '" + userPassword + "', '" + userMail + "', '" + userPhone + "', '" + userPhoto + "', " + idUser + ");";
                        db.execSQL(query);


                        pdLoading.hide();

                    }





                    alldataPOST.add(postData);
                }
                pdLoading.hide();
                if (test == true) {


                    //  Log.v("Context", "userconnect :" + mUsername.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivityMEnu.class);
                    startActivity(intent);
                } else {

                    pdLoading.hide();
                    //Snackbar.make(mcoordinatorlayout,"hello  from snackbar",Snackbar.LENGTH_LONG).setAction("action",null).show();


                    Snackbar snackbar = Snackbar
                            .make(mcoordinatorlayout, "Erreur d'Authentification !", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.RED);

                    snackbar.show();

                }


            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


    class BackgroundTask extends AsyncTask<String, Void, String> {

        String addUser_url;

        @Override
        protected void onPreExecute() {

            addUser_url = "https://onamangerpourvous.000webhostapp.com/addUser.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String nom, mail, image, id;
            URL url = null;
            id = args[0];
            nom = args[1];
            mail = args[2];
            image = args[3];


            try {
                url = new URL(addUser_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpsURLConnection httpsURLConnection = null;
            try {
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpsURLConnection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            httpsURLConnection.setDoOutput(true);
            OutputStream outputStream = null;
            try {
                outputStream = httpsURLConnection.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String data_string = null;
            try {
                data_string = URLEncoder.encode("idUser", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(nom, "UTF-8") + "&" +
                        URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8") + "&" +
                        URLEncoder.encode("photo", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.write(data_string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = httpsURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpsURLConnection.disconnect();

            return "One row of data Inserted ...";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    /************************************************************************/
    /************************************************************************/

    class BackgroundSaveTask extends AsyncTask<String, Void, String> {

        String maddUser_url;


        @Override
        protected void onPreExecute() {

            maddUser_url = "https://onamangerpourvous.000webhostapp.com/addUser2.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String nom = "", mail = "", photo = "";
            URL murl = null;
            nom = args[0];
            mail = args[1];
            photo = args[2];

            try {

                maddUser_url = "https://onamangerpourvous.000webhostapp.com/addUser2.php?name=" + nom + "&mail=" + mail + "&photo=" + photo;
                murl = new URL(maddUser_url);
                HttpsURLConnection mhttpsURLConnection = (HttpsURLConnection) murl.openConnection();
                mhttpsURLConnection.setRequestMethod("GET");
                mhttpsURLConnection.setDoOutput(true);
                OutputStream outputStream = mhttpsURLConnection.getOutputStream();
                // BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

              /*  String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(nom, "UTF-8") + "&" +
                        URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                */
                InputStream inputStream = mhttpsURLConnection.getInputStream();
                inputStream.close();
                mhttpsURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "One row of data Inserted ...";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
