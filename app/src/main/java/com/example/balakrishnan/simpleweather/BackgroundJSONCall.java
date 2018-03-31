package com.example.balakrishnan.simpleweather;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by balakrishnan on 6/3/18.
 */

public class BackgroundJSONCall extends AsyncTask<Double, Void, Void> {

    private Double currentLatitude, currentLongitude;
    private View v;
    private Activity act;
    private String city;
    private boolean isCitySearch=false;
    BackgroundJSONCall(){}
    BackgroundJSONCall(View v, Activity act) {
        this.v = v;
        this.act=act;
        isCitySearch=false;
    }
    public void AssignCity(String city){
        isCitySearch=true;
        this.city=city;
    }


    @Override
    protected Void doInBackground(Double... arg0) {


            currentLatitude = arg0[0];
            currentLongitude = arg0[1];
            jsonfn();

        return null;
    }

    String imgURL, temp, hum, weatherDesc, locn;
    Runnable r;
    private void jsonfn() {

        HttpHandler sh = new HttpHandler();
        String url="";
        if(!isCitySearch) {
            System.out.println("\n" + currentLatitude + "\n" + currentLongitude);
            url = "http://api.wunderground.com/api/8a655346344b1471/conditions/q/" + currentLatitude + "," + currentLongitude + ".json";
        }
        else
        {
            url = "http://api.wunderground.com/api/8a655346344b1471/conditions/q/"+city+".json";
        }
        System.out.println(url);
        String jsonStr = sh.makeServiceCall(url);
        String fURL = url.replace("conditions", "forecast");
        String fJSONStr = sh.makeServiceCall(fURL);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
 //               JSONObject fJSONObject = new JSONObject(fJSONStr);
//                JSONObject forecast = (JSONObject) fJSONObject.get("forecast");
//                JSONObject SimpleForecast = (JSONObject) forecast.get("simpleforecast");
 //               JSONArray ForecastDay = (JSONArray) SimpleForecast.get("forecastday");

                JSONObject main = (JSONObject) jsonObj.get("current_observation");

                JSONObject display_location = (JSONObject) main.get("display_location");

                temp = main.get("temperature_string").toString();
                hum = main.get("relative_humidity").toString();
                weatherDesc = main.get("weather").toString();
                imgURL = main.get("icon_url").toString();
                locn = display_location.get("full").toString();

                final TextView tempView = (TextView) v.findViewById(R.id.tempView);
                final TextView humView = (TextView) v.findViewById(R.id.humView);
                final TextView descView = (TextView) v.findViewById(R.id.descView);
                final TextView latView = (TextView) v.findViewById(R.id.latView);
                final TextView longView = (TextView) v.findViewById(R.id.longView);
                final TextView locnView = (TextView) v.findViewById(R.id.locnView);
                final ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
                final GifImageView gifImageView = (GifImageView)v.findViewById(R.id.gifImageView);
                r = new Runnable() {
                    @Override
                    public void run() {
                        tempView.setText(temp);
                        descView.setText(weatherDesc);
                        humView.setText(hum+" Humid");
                        locnView.setText(locn);
                        latView.setText(String.valueOf(currentLatitude).substring(0,5)+" (lat)");
                        longView.setText(String.valueOf(currentLongitude).substring(0,5)+" (lon)");


                        Picasso.with(act.getApplicationContext()).load(imgURL).into(imageView,new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                imageView.setVisibility(View.VISIBLE);
                                gifImageView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

                    }
                };



            } catch (Exception e) {

                if(act!=null)
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(act.getApplicationContext(),"No weather data available",Toast.LENGTH_LONG).show();
                        }
                    });
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Couldnt get json response");
        }

    }
    @Override
    protected void onPostExecute(Void aVoid) {

        try {
            if(r!=null)
                act.runOnUiThread(r);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        super.onPostExecute(aVoid);

    }

}


