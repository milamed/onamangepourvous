package com.ompv.amigos.myapplication;

/**
 * Created by evaneos on 26/05/17.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;

public class AdapterRestaurant extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataRestaurant> data= Collections.emptyList();
    DataRestaurant current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRestaurant (Context context, List<DataRestaurant > data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_restaurant , parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataRestaurant  current=data.get(position);

        myHolder.mtextRestaurantName.setText(current.getmName());
        myHolder.mtextRestaurantNote.setText(current.getmNote()+"");
        myHolder.mtextRestaurantVille.setText(current.getmVille());
        Glide.with(context).load("https://onamangerpourvous.000webhostapp.com/" + current.getmPhotos())

                .into(myHolder.mRestaurantPhoto);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{


        TextView mtextRestaurantName;
        TextView mtextRestaurantNote;
        TextView mtextRestaurantVille;
        ImageView mRestaurantPhoto;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            mtextRestaurantName= (TextView) itemView.findViewById(R.id.restaurantNameContainer);
            mtextRestaurantNote= (TextView) itemView.findViewById(R.id.noteContainer);
            mtextRestaurantVille= (TextView) itemView.findViewById(R.id.villeContainer);
            mRestaurantPhoto= (ImageView) itemView.findViewById(R.id.restoImage);
            }

    }

}
