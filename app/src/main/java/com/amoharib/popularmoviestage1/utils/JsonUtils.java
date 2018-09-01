package com.amoharib.popularmoviestage1.utils;

import com.amoharib.popularmoviestage1.models.Movie;
import com.amoharib.popularmoviestage1.models.Review;
import com.amoharib.popularmoviestage1.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static ArrayList<Movie> getListOfMoviesFromJson(String json) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseObject = new JSONObject(json);
            JSONArray jsonArray = baseObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject newObject = jsonArray.getJSONObject(i);
                int id = newObject.getInt("id");
                String originalTitle = newObject.getString("original_title");
                String poster = newObject.getString("poster_path");
                String overview = newObject.getString("overview");
                Double voteAverage = newObject.getDouble("vote_average");
                String releaseDate = newObject.getString("release_date");
                Movie movie = new Movie(
                        id,
                        originalTitle,
                        poster,
                        overview,
                        voteAverage,
                        releaseDate
                );
                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static ArrayList<Trailer> getListOfTrailers(String json) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject baseObject = new JSONObject(json);
            JSONArray jsonArray = baseObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject newObject = jsonArray.getJSONObject(i);
                String id = newObject.getString("id");
                String key = newObject.getString("key");
                String name = newObject.getString("name");
                Trailer trailer = new Trailer(id, key, name);
                trailers.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<Review> getListOfReviews(String json) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject baseObject = new JSONObject(json);
            JSONArray jsonArray = baseObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject newObject = jsonArray.getJSONObject(i);
                String id = newObject.getString("id");
                String author = newObject.getString("author");
                String content = newObject.getString("content");
                String url = newObject.getString("url");
                Review review = new Review(id, author, content, url);
                reviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

}
