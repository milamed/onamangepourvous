package com.ompv.amigos.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.FileInputStream;

public class InterfacePublication extends AppCompatActivity {

    public static ImageView mImage;

    Bitmap bmp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_publication);
        initViews();

        String filename = getIntent().getStringExtra("pic");
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
    }
}
