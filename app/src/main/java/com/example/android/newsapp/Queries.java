package com.example.android.newsapp;

import android.text.TextUtils;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Queries {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Queries.class.getSimpleName();

    private Queries() {
    }

    public static List<News> fetchNewsData(String requestUrl) {

        //Create URL
        URL url = createUrl(requestUrl);

        //Make web request and return results via JSON
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // gets relevant fields from the JSON response
        List<News> newsItems = extractFeatureFromJson(jsonResponse);

        // Return the news items
        return newsItems;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // error handling in the event the request fails
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // Handling to in the event the response is null
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsItems = new ArrayList<>();

        // Insure the JSON Response is valid and find the relevant data
        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");

            JSONArray newsArray = jsonResponse.getJSONArray("results");


            // for each loop to go through each item in the array and grab the specified information
            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNewsItem = newsArray.getJSONObject(i);
                //some of our data is in an array and not an object, this targets the array
                JSONArray tags = currentNewsItem.getJSONArray("tags");

                //Logic to determine if there is an author listed in the contributor field of the
                // the tags array and grab the value to set the author value later
                String authorName = null;
                if (tags.length() == 1) {
                    JSONObject contributorField = (JSONObject) tags.get(0);
                    authorName = contributorField.getString("webTitle");
                }

                // get the article title
                String articleTitle = currentNewsItem.getString("webTitle");

                // get the section name
                String articleSection = currentNewsItem.getString("sectionName");

                // Set the author name if it has a value
                String articleAuthor = authorName;

                //gets the article publication date
                String articleDate = currentNewsItem.getString("webPublicationDate");

                // gets the url related to the article
                String url = currentNewsItem.getString("webUrl");

                // Create a news object
                News newsItem = new News(articleTitle, articleSection, articleAuthor, articleDate, url);

                // Adds the news item
                newsItems.add(newsItem);
            }

        } catch (JSONException e) {
            //error handling in the event parsing fails
            Log.e("Queries", "Problem parsing the news JSON results", e);
        }

        // Return a list of news items
        return newsItems;
    }

}

