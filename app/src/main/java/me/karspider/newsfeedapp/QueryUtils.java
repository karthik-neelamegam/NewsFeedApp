package me.karspider.newsfeedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by karsp_000 on 16/10/2016.
 */
public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final String GUARDIAN_API_KEY = "138611c9-b6de-4390-ade5-4a77e63353fc";

    private QueryUtils() {

    }

    private static URL createURL(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem converting URL string to URL object");
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    private static String getJSONResponse(URL url) throws IOException {
        String jsonResponse = null;
        InputStream inputStream = null;
        if (url == null) return jsonResponse;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error, response code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String capitaliseFirstLetter(String name) {
        if (name != null && name.length() > 0)
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        else return name;
    }

    private static String formatName(String firstName, String lastName) {
        firstName = capitaliseFirstLetter(firstName);
        lastName = capitaliseFirstLetter(lastName);
        return (firstName + " " + lastName).trim();
    }

    private static ArrayList<NewsItem> extractNewsItemsFromJSONResponse(String jsonResponse) {
        ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
        try {
            JSONObject mainJsonObject = new JSONObject(jsonResponse);
            JSONObject response = mainJsonObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNewsItem = (JSONObject) results.get(i);
                String url = currentNewsItem.getString("webUrl");
                String date = currentNewsItem.getString("webPublicationDate");
                JSONObject fields = currentNewsItem.getJSONObject("fields");
                String thumbnailUrl = fields.getString("thumbnail");
                Bitmap thumbnail = getThumbnailImage(thumbnailUrl);
                String trailText = fields.getString("trailText");
                String headline = fields.getString("headline");
                JSONArray tags = currentNewsItem.getJSONArray("tags");
                String author = null;
                if (tags.length() > 0) {
                    author = "";
                    for (int j = 0; j < tags.length(); j++) {
                        if (j > 0) author += " and ";
                        JSONObject contributor = (JSONObject) tags.get(j);
                        author += formatName(contributor.getString("firstName"), contributor.getString("lastName"));
                    }
                }
                newsItems.add(new NewsItem(headline, trailText, author, date, url, thumbnail));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItems;
    }

    public static Bitmap getThumbnailImage(String urlString) {
        InputStream inputStream = null;
        Bitmap thumbnail = null;
        HttpURLConnection conn = null;
        URL url = createURL(urlString);
        if (url == null) return null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                thumbnail = BitmapFactory.decodeStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error, response code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return thumbnail;
    }

    public static ArrayList<NewsItem> fetchNewsItems(String requestUrl) {
        ArrayList<NewsItem> newsItems = null;
        URL url = createURL(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = getJSONResponse(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            newsItems = extractNewsItemsFromJSONResponse(jsonResponse);
            Log.e(LOG_TAG, "hello" + jsonResponse);
            Log.e(LOG_TAG, newsItems.toString());
        }
        return newsItems;
    }


}
