package com.galpotechsolutions.news;

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

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils(){
    }

    /**
     * Return a list of {@link New} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<New> extractFeatureFromJson(String newJSON){

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newJSON)){
            return null;
        }

        // Create an empty ArrayLit that we can start adding earthquakes to
        List<New> news = new ArrayList<>();

        // Try to parse the newJSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try{
            //TODO: Aqui me quede.

            // build up a list of Earthquake objects with the corresponding data.

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newJSON);

            // Extract the JSONObject associated with the key called "response"
            JSONObject response = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the ket called "results"
            // which represents a list of results (or news).
            JSONArray newArray = response.getJSONArray("results");

            for (int i=0; i< newArray.length(); i++){
                // Get a single new at position i within the list of news
                JSONObject currentNew = newArray.getJSONObject(i);

                // For a given new, extract the JSONObject associated with the
                // key called "fields", which represents a list of all properties
                // for that new.
                JSONObject fields = currentNew.getJSONObject("fields");

                // Extract the value for the key called headline
                String headline = fields.getString("headline");
                // Extract the value for the key called sectionName;
                String sectionName = currentNew.getString("sectionName");
                // Extract the value for the key called webPublicationDate
                String webPublicationDate = currentNew.getString("webPublicationDate");
                // Extract the value for the key called byLine
                String byLine = fields.getString("byline");
                // Extract the value for the key called thumbnail
                String thumbnail = fields.getString("thumbnail");
                // Extract the value for the key called trailText
                String bodyText = fields.getString("trailText");
                // Extract the value for the key called webUrl
                String url = currentNew.getString("webUrl");

                // Create a new {@link New} object with the headline, sectionName, webPublicationDate,
                // byline, thumbnail, body and url from the JSON response
                New newResponse = new New(headline, sectionName, webPublicationDate, byLine, thumbnail, bodyText, url);
                news.add(newResponse);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
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

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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
     *  Query the Guardian database and return a list of {@link New} objects.
     */
    public static List<New> fetchNewData(String requestUrl){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem make the HTTP request", e );
        }

        // Extract relevant field from the JSON response and create a list of {@link New}s
        List<New> news = extractFeatureFromJson(jsonResponse);
        return news;
    }
}

