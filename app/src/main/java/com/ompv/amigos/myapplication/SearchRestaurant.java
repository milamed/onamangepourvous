package com.ompv.amigos.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

import static com.ompv.amigos.myapplication.R.styleable.View;

public class SearchRestaurant extends AppCompatActivity {



    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterRestaurant mAdapter;
    private EditText mEditTextSearch;
    URL url = null;
    String s = "";
    double lat;
    double lng;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GPSTracker GPS = new GPSTracker(this);

        lat = GPS.getLatitude();
        lng = GPS.getLongitude();

      //  Toast.makeText(getApplicationContext(), Double.toString(lat), Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), Double.toString(lng), Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_search_resto);
        //Make call to AsyncTask
        new AsyncFetch().execute();
        initViews();

        mEditTextSearch.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void initViews() {
        mEditTextSearch = (EditText) findViewById(R.id.editTextSearch);

      /*  mEditTextSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    Toast.makeText(getApplicationContext(), "unfocus", 2000).show();
            }
        });
        */
        mEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    s = mEditTextSearch.getText().toString();
                    new AsyncFetch().execute();
                    return true;
                }
                return false;
            }
        });
    }



    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SearchRestaurant.this);
        HttpURLConnection conn;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if("".equals(s)) {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                try {
                    url = new URL("https://onamangerpourvous.000webhostapp.com/listResto.php?action=getAllResto");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            System.out.println(url.toString());
            }else{
                try {
                    url = new URL("https://onamangerpourvous.000webhostapp.com/filter.php?action=searchByRestoName&name="+s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                System.out.println(url.toString());
            }

           // Toast.makeText(context,url.toString(), Toast.LENGTH_LONG).show();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
           /* try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://onamangerpourvous.000webhostapp.com/listResto.php?action=getAllResto");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            */
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
            System.out.println(url.toString());
            pdLoading.dismiss();
            List<DataRestaurant> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataRestaurant restaurantData = new DataRestaurant();
                    restaurantData.setmName(json_data.getString("name"));
                    restaurantData.setmNote(Float.parseFloat(json_data.getString("note")));
                    restaurantData.setmVille(json_data.getString("ville"));
                    restaurantData.setmPhotos(json_data.getString("photos"));
                    data.add(restaurantData);
                }


                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterRestaurant(SearchRestaurant.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(SearchRestaurant.this));

            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

}
