package me.karspider.newsfeedapp;

import android.graphics.Bitmap;

/**
 * Created by karsp_000 on 16/10/2016.
 */
public class NewsItem {
    private String headline;
    private String text;
    private String author;
    private String url;
    private String date;
    private Bitmap thumbnail;

    public NewsItem(String headline, String text, String author, String date, String url, Bitmap thumbnail) {
        this.headline = headline;
        this.text = text;
        this.author = author;
        this.url = url;
        this.date = date;
        this.thumbnail = thumbnail;
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
