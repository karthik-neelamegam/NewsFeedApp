package me.karspider.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by karsp_000 on 16/10/2016.
 */
public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
    private final int layoutResourceId = R.layout.news_list_item;
    public NewsItemAdapter(Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsItem currentNewsItem = getItem(position);
        View currentView = convertView;
        if(convertView == null) {
            currentView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
        }
        //TODO: add stuff to the view
        TextView headlineTextView = (TextView) currentView.findViewById(R.id.news_item_headline);
        TextView textTextView = (TextView) currentView.findViewById(R.id.news_item_text);
        TextView authorTextView = (TextView) currentView.findViewById(R.id.news_item_author);
        TextView dateTextView = (TextView) currentView.findViewById(R.id.news_item_date);
        ImageView thumbnailImageView = (ImageView) currentView.findViewById(R.id.news_item_image);

        final String url = currentNewsItem.getUrl();
        String headline = currentNewsItem.getHeadline();
        String trailText = currentNewsItem.getText();
        String author = currentNewsItem.getAuthor();
        Bitmap thumbnail = currentNewsItem.getThumbnail();
        String date = formatDate(currentNewsItem.getDate());
        headlineTextView.setText(headline);
        textTextView.setText(trailText);
        authorTextView.setText(getContext().getString(R.string.by_author) + " " + author);
        dateTextView.setText(date);
        thumbnailImageView.setImageBitmap(thumbnail);

        return currentView;
    }

    private String formatDate(String dateString) {
        SimpleDateFormat sdfParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdfParser.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdfFormatter = new SimpleDateFormat("MMM d yyyy HH:mm");
        sdfFormatter.setTimeZone(TimeZone.getDefault());
        Date date;
        try {
            date = sdfParser.parse(dateString);
        } catch (ParseException e1) {
            date = null;
        }
        if(date != null) return sdfFormatter.format(date);
        return getContext().getString(R.string.date_unknown);
    }
}
