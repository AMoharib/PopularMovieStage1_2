package com.amoharib.popularmoviestage1.utils;

import android.net.Uri;

import com.amoharib.popularmoviestage1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie";
    private final static String API_KEY = BuildConfig.API_KEY;
    public final static String POPULAR_QUERY = "popular";
    public final static String TOP_RATED_QUERY = "top_rated";
    private final static String API_QUERY = "api_key";
    public final static String TRAILER_QUERY = "videos";
    public final static String REVIEW_QUERY = "reviews";


    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    public static URL buildUrl(String queryType) {
        Uri builtUri = null;

        switch (queryType) {
            case POPULAR_QUERY:
                builtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(POPULAR_QUERY)
                        .appendQueryParameter(API_QUERY, API_KEY)
                        .build();
                break;
            case TOP_RATED_QUERY:
                builtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(TOP_RATED_QUERY)
                        .appendQueryParameter(API_QUERY, API_KEY)
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerReviewUrl(String movieId, String trailerOrReview) {
        Uri builtUri = null;

        switch (trailerOrReview) {
            case TRAILER_QUERY:
                builtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(movieId)
                        .appendEncodedPath(TRAILER_QUERY)
                        .appendQueryParameter(API_QUERY, API_KEY)
                        .build();
                break;
            case REVIEW_QUERY:
                builtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(movieId)
                        .appendEncodedPath(REVIEW_QUERY)
                        .appendQueryParameter(API_QUERY, API_KEY)
                        .build();
                break;
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
