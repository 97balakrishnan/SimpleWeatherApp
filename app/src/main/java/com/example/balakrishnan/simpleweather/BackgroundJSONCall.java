package com.example.balakrishnan.simpleweather;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    BackgroundJSONCall(View v, Activity act) {
        this.v = v;
        this.act=act;
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
        System.out.println("\n" + currentLatitude + "\n" + currentLongitude);
        String url = "http://api.wunderground.com/api/8a655346344b1471/conditions/q/" + currentLatitude + "," + currentLongitude + ".json";
        System.out.println(url);
        String jsonStr = sh.makeServiceCall(url);
        String fURL = url.replace("conditions", "forecast");
        String fJSONStr = sh.makeServiceCall(fURL);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject fJSONObject = new JSONObject(fJSONStr);
                JSONObject forecast = (JSONObject) fJSONObject.get("forecast");
                JSONObject SimpleForecast = (JSONObject) forecast.get("simpleforecast");
                JSONArray ForecastDay = (JSONArray) SimpleForecast.get("forecastday");

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
                        humView.setText(hum+" humid");
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

                /*WeatherInfo w = new WeatherInfo();
                w.setTemperature(temp);
                w.setHumidity(hum);
                w.setDescription(weatherDesc);
                w.setImgURL(imgURL);
                w.setLocation(locn);
                w.setLattitude(String.valueOf(currentLatitude));
                w.setLongitude(String.valueOf(currentLongitude));
                wList.add(w);
                for(int i=0;i<ForecastDay.length();i++)
                {
                    JSONObject j = (JSONObject)ForecastDay.get(i);
                    JSONObject high =(JSONObject)j.get("high");
                    WeatherInfo w1=new WeatherInfo();
                    w1.setTemperature(high.get("celsius").toString());
                    w1.setImgURL(j.get("icon_url").toString());
                    w1.setHumidity(j.get("avehumidity").toString());
                    w1.setLocation(locn);
                    w1.setLattitude(String.valueOf(currentLatitude));
                    w1.setLongitude(String.valueOf(currentLongitude));
                    w1.setDescription(weatherDesc);
                    wList.add(w1);
                }
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        wAdapter.notifyDataSetChanged();

                    }
                };

                runOnUiThread(r);*/

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            act.runOnUiThread(r);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}


