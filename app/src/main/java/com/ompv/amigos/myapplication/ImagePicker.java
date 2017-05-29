package com.ompv.amigos.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePicker extends AppCompatActivity implements View.OnClickListener {
    private TextView mCamera;
    private TextView mGallery;
    private TextView mTitle;
    private Typeface mTypeFace;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int IMAGE_GALLERY_REQUEST = 2;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image_picker);
        initViews();
    }

    private void initViews() {
        mCamera = (TextView) findViewById(R.id.camera);
        mGallery = (TextView) findViewById(R.id.gallery);
        mTitle = (TextView) findViewById(R.id.title);
        mTypeFace = Typeface.createFromAsset(getAssets(),"Windsong.ttf");
        mTitle.setTypeface(mTypeFace);
        mCamera.setOnClickListener(this);
        mGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.camera :
            {
                dispatchTakePictureIntent();
            }break;
            case R.id.gallery :
            {
                getPhoto();
            }break;

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0);
            }
        }}

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void getPhoto() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String photoPath = storageDir.getPath();
        Uri data = Uri.parse(photoPath);
        photoPicker.setDataAndType(data,"image/*");
        startActivityForResult(photoPicker, IMAGE_GALLERY_REQUEST);

    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //mImage.setImageBitmap(imageBitmap);
            //Write file
            String filename = "bitmap.png";
            FileOutputStream stream = null;
            try {
                stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageBitmap.recycle();

            //Pop intent
            Intent in1 = new Intent(this, InterfacePublication.class);
            in1.putExtra("pic", filename);
            startActivity(in1);

        }
        else if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK) {
            //the address of the image on the sd card
            Uri imageURI = data.getData();
            //getting an input Stream , based on the URI of the image
            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(imageURI);
                //get a bitmap of the image
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                //ImageView mImage = ;
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                image.recycle();

                //Pop intent
                Intent in1 = new Intent(this, InterfacePublication.class);
                in1.putExtra("pic", filename);
                in1.putExtra("path",getPath(imageURI) );
                startActivity(in1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "unable de open image", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
