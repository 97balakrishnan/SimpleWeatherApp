package com.example.balakrishnan.simpleweather;


import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    double currentLatitude, currentLongitude;
    TextView tView,hView,dView,lView,latView,lonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tView =findViewById(R.id.tempView);
        hView =findViewById(R.id.humView);
        dView =findViewById(R.id.descView);
        lView=findViewById(R.id.locnView);
        latView=findViewById(R.id.latView);
        lonView=findViewById(R.id.longView);

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
    String imgURL;
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

                final String temp=main.get("temperature_string").toString();

                System.out.println("temperature"+temp);

                final String hum = main.get("relative_humidity").toString();

                final String weatherDesc = main.get("weather").toString();
                imgURL = main.get("icon_url").toString();
                System.out.println("Description"+weatherDesc);
                JSONObject display_location = (JSONObject)main.get("display_location");
                final String locn =display_location.get("full").toString();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        tView.setText(temp);
                        hView.setText(hum+"%");
                        lView.setText(locn);
                        dView.setText(weatherDesc);
                        latView.setText(String.valueOf(currentLatitude));
                        lonView.setText(String.valueOf(currentLongitude));
                        ImageView imageView=(ImageView)findViewById(R.id.imageView);
                        System.out.println(imgURL);
                        Picasso.with(getApplicationContext()).load(imgURL).into(imageView);
                    }
                };

                runOnUiThread(r);

                /*JSONObject channel = jsonObj.getJSONObject("channel");
                Iterator<?> keys = channel.keys();
                int i = 1;
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (key.startsWith("field")) {

                    }
                }*/

            } catch (Exception e) {

                e.printStackTrace();
            }
        }


    }

    private class BackgroundJSONCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            jsonfn();

            /*
            */return null;
        }
    }




}

