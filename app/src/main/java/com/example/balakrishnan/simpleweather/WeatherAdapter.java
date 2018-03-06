package com.example.balakrishnan.simpleweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by balakrishnan on 6/3/18.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder>{
    private List<WeatherInfo> weatherList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, descView;
        public ImageView imageView;
        public GifImageView gifImageView;

        public MyViewHolder(View view) {
            super(view);
            descView = (TextView) view.findViewById(R.id.descView);
            titleView = (TextView) view.findViewById(R.id.titleView);
            imageView=(ImageView)view.findViewById(R.id.imageView);
            gifImageView=(GifImageView)view.findViewById(R.id.gifImageView);

        }
    }


    public WeatherAdapter(List<WeatherInfo> weatherList,Context context) {
        this.weatherList = weatherList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder1, int position) {
        final MyViewHolder holder=holder1;
        WeatherInfo w = weatherList.get(position);
        holder.titleView.setText(w.getTitle());
        holder.descView.setText(w.getDescription());
        Picasso.with(context).load(w.getImgURL()).into(holder.imageView,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.gifImageView.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        System.out.println("BindViewHolder called");

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
