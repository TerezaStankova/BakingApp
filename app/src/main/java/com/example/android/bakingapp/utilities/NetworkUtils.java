package com.example.android.bakingapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import timber.log.Timber;

public class NetworkUtils {
    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static URL buildUrl() {
        Uri movieQueryUri = Uri.parse(RECIPES_URL).buildUpon()
                .build();

        try {
            URL movieQueryURL = new URL(movieQueryUri.toString());
            Timber.v("URL: " + movieQueryURL);
            Log.v("URL","URL: " + movieQueryURL);
            return movieQueryURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            /*You should checkout Volley library
             *(https://developer.android.com/training/volley/) for making network requests.
             * *They really make networking so much easy.*/

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
