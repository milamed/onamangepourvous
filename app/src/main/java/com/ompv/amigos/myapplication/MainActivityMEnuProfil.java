package com.ompv.amigos.myapplication;

/**
 * Created by yessine on 13/03/17.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivityMEnuProfil extends Fragment {


    private SQLiteDatabase db;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView rv;
    Context context ;

    private static String DB_PATH = "/data/data/com.ompv.amigos.myapplication/databases/PersonDB";

    private static String DB_NAME = "PersonDB";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context= context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabprofil, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.user_profile_name);
        ImageView profile_photo = (ImageView) rootView.findViewById(R.id.user_profile_photo);
        textView.setText("Yessine Abid");
     //profile_photo.setImageURI("http://wallpaper-gallery.net/images/image/image-13.jpg");
        Glide.with(profile_photo.getContext()).load( "http://wallpaper-gallery.net/images/image/image-13.jpg");

        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);


        // affiche();



         new  AsyncLogin().execute();
        return rootView;
    }



    protected void affiche() {


        String query = "select * from user; ";


         db =  db.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery(query, null);
        List<DataClassAtt> l = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                l.add(new DataClassAtt(cursor.getString(4), cursor.getInt(0), cursor.getString(2),
                        cursor.getString(1), cursor.getString(3), cursor.getString(5)));

            } while (cursor.moveToNext());
        }

        for (DataClassAtt p : l) {
            Toast.makeText(context, l.size(), Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();

       // new  AsyncLogin().execute();
    }



    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        CatLoadingView mView= new CatLoadingView();;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
          /*  pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/

            mView.show(getFragmentManager(), "");



        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://onamangerpourvous.000webhostapp.com/listPost.php?action=getPost&idUserch=1");

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

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataAllPub> alldataPOST = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataAllPub postData = new DataAllPub();
                    postData.idPost = json_data.getInt("idPost");
                    postData.title = json_data.getString("title");
                    postData.description = json_data.getString("description");
                    postData.photo = json_data.getString("photo");
                    postData.price = json_data.getString("price");
                    postData.note = json_data.getInt("note");
                    postData.likes = json_data.getString("likes");
                    //  postData.idUser = json_data.getInt("idUser");
                    postData.nameUser = json_data.getString("nameUser");
                    //   postData.idRestaurant = json_data.getInt("idRestaurant");
                    postData.nameRestaurant = json_data.getString("nameRestaurant");
                    postData.datePub = json_data.getString("datePub");
                    postData.photoUser = json_data.getString("photoUser");

                    alldataPOST.add(postData);
                }

                rv.setHasFixedSize(true);

                MyAdapter adapter = new MyAdapter(context,alldataPOST);
                rv.setAdapter(adapter);

                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(llm);
                mView.dismiss();

                //affiche();

            } catch (JSONException e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }



}