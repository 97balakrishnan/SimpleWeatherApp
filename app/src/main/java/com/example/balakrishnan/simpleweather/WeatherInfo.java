package com.example.balakrishnan.simpleweather;

/**
 * Created by balakrishnan on 6/3/18.
 */

public class WeatherInfo {
    private String title,description;
    private String imgURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
