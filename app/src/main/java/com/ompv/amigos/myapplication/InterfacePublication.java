package com.ompv.amigos.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.util.UUID;

public class InterfacePublication extends AppCompatActivity implements View.OnClickListener {

    private  ImageView mImage;
    private EditText mNomplat;
    private EditText mDescription;
    private EditText mPrix;
    private TextView mShare;
    private String filename;

    Bitmap bmp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_publication);
        initViews();

        filename = getIntent().getStringExtra("pic");
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
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.share:
            {

            }
        }
    }


}
