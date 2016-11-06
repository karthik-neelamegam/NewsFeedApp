package me.karspider.newsfeedapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsItem>> {
    private static String requestUrl = "http://content.guardianapis.com/search?api-key="+QueryUtils.GUARDIAN_API_KEY+"&show-tags=contributor&show-fields=thumbnail,headline,trailText&page-size=50";
    private NewsItemAdapter newsItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsItemLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsItem>> loader, ArrayList<NewsItem> data) {
        ListView newsListView = (ListView) findViewById(R.id.news_list_view);
        newsItemAdapter = new NewsItemAdapter(this, data);
        newsListView.setAdapter(newsItemAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem clickedNewsItem = (NewsItem) parent.getItemAtPosition(position);
                Intent visitPageIntent = new Intent(Intent.ACTION_VIEW);
                visitPageIntent.setData(Uri.parse(clickedNewsItem.getUrl()));
                parent.getContext().startActivity(visitPageIntent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsItem>> loader) {

    }
}
