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

/**
 * Created by balakrishnan on 6/3/18.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder>{
    private List<WeatherInfo> weatherList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tempView, humView, locnView,descView,latView,longView;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            tempView = (TextView) view.findViewById(R.id.tempView);
            humView = (TextView) view.findViewById(R.id.humView);
            locnView = (TextView) view.findViewById(R.id.locnView);
            descView = (TextView) view.findViewById(R.id.descView);
            latView = (TextView) view.findViewById(R.id.latView);
            longView =(TextView)view.findViewById(R.id.longView);
            imageView=(ImageView)view.findViewById(R.id.imageView);
        }
    }


    public WeatherAdapter(List<WeatherInfo> weatherList,Context context) {
        this.weatherList = weatherList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder1, int position) {
        final MyViewHolder holder=holder1;
        WeatherInfo w = weatherList.get(position);
        holder.tempView.setText(w.getTemperature());
        holder.humView.setText(w.getHumidity());
        holder.descView.setText(w.getDescription());
        holder.locnView.setText(w.getLocation());
        holder.latView.setText(w.getLattitude());
        holder.longView.setText(w.getLongitude());
        Picasso.with(context).load(w.getImgURL()).into(holder.imageView,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
