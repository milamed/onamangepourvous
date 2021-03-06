package com.ompv.amigos.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class InterfacePublication extends AppCompatActivity implements View.OnClickListener {

    private  ImageView mImage;
    private EditText mNomplat;
    private EditText mDescription;
    private EditText mPrix;
    private TextView mShare;
    private String filename;
    private String picpath;
    private PostTask mPostTask;
    long  totalSize=0;
ProgressBar progressBar;
    Bitmap bmp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_publication);
        progressBar=(ProgressBar)findViewById(R.id.progressBar) ;
        initViews();

        filename = getIntent().getStringExtra("pic");
        picpath = getIntent().getStringExtra("path");
        Toast.makeText(this, picpath, Toast.LENGTH_SHORT).show();
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            mImage.setImageBitmap(bmp);
//            Picasso.with(this)
//                    .load(String.valueOf(bmp))
//                    .resize(3000, 5000)
//                    .centerCrop()
//                    .into(mImage);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        mImage = (ImageView) findViewById(R.id.img);
        mNomplat = (EditText) findViewById(R.id.nomPlat);
        mDescription = (EditText) findViewById(R.id.descriptionPlat);
        mPrix = (EditText) findViewById(R.id.prixPlat);
        mShare = (TextView) findViewById(R.id.share);
        mShare.setOnClickListener(this);
        mPostTask = new PostTask();
    }

    public void uploadMultipart() {
        //getting name for the image
        //String name = editText.getText().toString().trim();

        //getting the actual path of the image
        //String path = getPath(filePath);
        String path = picpath;

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, "https://onamangerpourvous.000webhostapp.com/upload2.php")
                    .addFileToUpload(picpath, "image") //Adding file
                    .addParameter("name", filename) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.share:
            {
                //uploadMultipart();
             //   new UploadFileToServer().execute();
                mPostTask.execute(mNomplat.getText().toString(),mDescription.getText().toString(),mPrix.getText().toString());
            }
        }
    }



    /************************************************************************/
    /************************************************************************/
    class PostTask extends AsyncTask<String,Void,String> {

        String maddUser_url;



        @Override
        protected void onPreExecute() {

            maddUser_url = "https://onamangerpourvous.000webhostapp.com/listPost.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String title="", desc="",prix="";
            URL murl = null;
            title = args[0];
            desc = args[1];
            prix = args[2];

            try {

                maddUser_url="https://onamangerpourvous.000webhostapp.com/listPost.php?action=addPost&idRestaurant=1&title="
                        +title+"&description="+ desc+"&price="+prix+"&idUser=63&photo=crepe.jpg&note=0&likes=0";
                murl = new URL(maddUser_url);
                HttpsURLConnection mhttpsURLConnection = (HttpsURLConnection) murl.openConnection();
                mhttpsURLConnection.setRequestMethod("GET");
                mhttpsURLConnection.setDoOutput(true);
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
        protected void onPostExecute(String result)
        {
            Toast.makeText(InterfacePublication.this, result, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://geekdev.000webhostapp.com/fileUpload.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                String chaine=("ahouuuu"+picpath);

                // 004
                File sourceFile = new File(picpath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            //showAlert("Upload is Done");
            super.onPostExecute(result);


        }

    }
}
