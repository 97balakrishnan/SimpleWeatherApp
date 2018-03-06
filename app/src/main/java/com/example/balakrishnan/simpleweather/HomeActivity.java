package com.example.balakrishnan.simpleweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private double currentLatitude, currentLongitude;
    private RecyclerView recyclerView;
    private WeatherAdapter wAdapter;
    private List<WeatherInfo> wList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        recyclerView = findViewById(R.id.recycler_view);
        wAdapter = new WeatherAdapter(wList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager((getApplicationContext()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wAdapter);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            GPSTracker gps = new GPSTracker(getApplicationContext());
            System.out.println(String.valueOf(gps.getLatitude()));
            System.out.println(String.valueOf(gps.getLongitude()));
            currentLongitude = gps.getLongitude();
            currentLatitude = gps.getLatitude();
            new BackgroundJSONCall().execute();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Location Permissions not Granted!",Toast.LENGTH_LONG).show();
        }
    }
    String imgURL,temp,hum,weatherDesc,locn;
    public void jsonfn() {

        HttpHandler sh = new HttpHandler();
        System.out.println("\n"+currentLatitude+"\n"+currentLongitude);
        String url = "http://api.wunderground.com/api/8a655346344b1471/conditions/q/"+currentLatitude+","+currentLongitude+".json";
        System.out.println(url);
        String jsonStr = sh.makeServiceCall(url);


        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONObject main = (JSONObject)jsonObj.get("current_observation");

                JSONObject display_location = (JSONObject)main.get("display_location");

                temp=main.get("temperature_string").toString();
                hum = main.get("relative_humidity").toString();
                weatherDesc = main.get("weather").toString();
                imgURL = main.get("icon_url").toString();
                locn =display_location.get("full").toString();

                WeatherInfo w = new WeatherInfo();
                w.setTemperature(temp);
                w.setHumidity(hum);
                w.setDescription(weatherDesc);
                w.setImgURL(imgURL);
                w.setLocation(locn);
                w.setLattitude(String.valueOf(currentLatitude));
                w.setLongitude(String.valueOf(currentLongitude));
                wList.add(w);
                wList.add(w);
                wList.add(w);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        wAdapter.notifyDataSetChanged();

                        /*final ImageView imageView=(ImageView)findViewById(R.id.imageView);
                        System.out.println(imgURL);

                        Picasso.with(getApplicationContext()).load(imgURL).into(imageView,new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                imageView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {

                            }
                        });*/
                    }
                };

                runOnUiThread(r);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }


    }

    private class BackgroundJSONCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            jsonfn();

            return null;
        }
    }



}
