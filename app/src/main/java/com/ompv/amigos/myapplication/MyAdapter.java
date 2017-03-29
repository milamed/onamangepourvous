package com.ompv.amigos.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import android.widget.ImageView;

import static android.R.attr.data;
import static com.ompv.amigos.myapplication.R.id.image;

/**
 * Created by delaroy on 2/13/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<DataClassAtt>  mDataset;
    private Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CardView mCardView;
        public TextView mTextView;
        public TextView mEmail;
        public  ImageView mImage;

        public MyViewHolder(View v){
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_text);

            mImage = (ImageView) v.findViewById(R.id.zoneimage);
        }

    }

    public MyAdapter(List<DataClassAtt> myDataset){
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        DataClassAtt p=mDataset.get(position);
        holder.mTextView.setText(p.getFirstName());
        
       // Uri imgUri=Uri.parse(p.getMimage());
       // holder.mImage.setImageURI(imgUri);

       //  String image_url = ""+p.getMimage().toString();
        // holder.mImage.setImageURI(Uri.parse(image_url));



        Log.v("Context","imagefill :"+p.getMimage());


    }

    @Override
    public int getItemCount() { return mDataset.size(); }

}
