package me.karspider.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by karsp_000 on 16/10/2016.
 */
public class NewsItemLoader extends AsyncTaskLoader<ArrayList<NewsItem>> {
    String requestUrl;

    public NewsItemLoader(Context context, String requestUrl) {
        super(context);
        this.requestUrl = requestUrl;
    }

    @Override
    public ArrayList<NewsItem> loadInBackground() {
        return QueryUtils.fetchNewsItems(requestUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
