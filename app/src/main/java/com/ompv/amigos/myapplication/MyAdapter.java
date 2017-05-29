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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import static android.R.attr.data;
import static com.ompv.amigos.myapplication.R.id.image;

/**
 * Created by delaroy on 2/13/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<DataAllPub>  mDataset;
    private Context context;

    private LayoutInflater inflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CardView mCardView;

        public TextView midUser;
        public TextView mtitle;
        public TextView mdescription;
        public ImageView mphoto;
        public TextView mprice;
        public TextView mnote;
        public  TextView midRestaurant;
        public  TextView mdate;
        public  ImageView mphotoUser;
        public  TextView mzonenblike;



        public MyViewHolder(View v){
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            midUser = (TextView) v.findViewById(R.id.username);
            midRestaurant = (TextView) v.findViewById(R.id.location);
            mphoto= (ImageView) itemView.findViewById(R.id.zoneimage);
            mphotoUser= (ImageView) itemView.findViewById(R.id.imgProfil);
            mzonenblike = (TextView) v.findViewById(R.id.zonenblike);

            mdate= (TextView) itemView.findViewById(R.id.date);
        }

    }

    public MyAdapter(Context context,List<DataAllPub> myDataset){

        this.context=context;
        this.mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);



        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        DataAllPub p=mDataset.get(position);
        holder.midUser.setText(p.nameUser);
        holder.midRestaurant.setText(p.nameRestaurant);
        holder.mdate.setText(p.datePub);
        holder.mzonenblike.setText( p.likes);


        Glide.with(holder.mphoto.getContext()).load("https://onamangerpourvous.000webhostapp.com/" + p.photo).into(holder.mphoto);
        Glide.with(holder.mphotoUser.getContext()).load( p.photoUser).into(holder.mphotoUser);

     //   Log.v("Context","imagefill :"+p.getMimage());


    }

    @Override
    public int getItemCount() { return mDataset.size(); }

}
