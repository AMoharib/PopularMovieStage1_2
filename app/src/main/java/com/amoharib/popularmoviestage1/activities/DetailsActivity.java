package com.amoharib.popularmoviestage1.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoharib.popularmoviestage1.adapters.ReviewAdapter;
import com.amoharib.popularmoviestage1.adapters.TrailerAdapter;
import com.amoharib.popularmoviestage1.db.DBContract;
import com.amoharib.popularmoviestage1.db.DBHelper;
import com.amoharib.popularmoviestage1.db.MovieProvider;
import com.amoharib.popularmoviestage1.models.Movie;
import com.amoharib.popularmoviestage1.models.Review;
import com.amoharib.popularmoviestage1.models.Trailer;
import com.amoharib.popularmoviestage1.R;
import com.amoharib.popularmoviestage1.utils.JsonUtils;
import com.amoharib.popularmoviestage1.utils.NetworkUtils;
import com.amoharib.popularmoviestage1.utils.Wrapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    public interface DataListener {
        public void onTrailersRetrieved(ArrayList<Trailer> trailers);

        public void onReviewsRetrieved(ArrayList<Review> reviews);
    }


    private ImageView imageView;
    private TextView title;
    private TextView dateReleased;
    private TextView userRate;
    private TextView overview;
    private SQLiteDatabase db;
    private ImageView favIcon;
    private RecyclerView trailersRecycler;
    private RecyclerView reviewsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        final Movie movie = (Movie) getIntent().getParcelableExtra("movie");

        populateView(movie);

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMovieInDatabase(movie.getId())) {
                    addMovieToDB(movie);
                    updateFavIcon(true);
                } else {
                    removeMovieFromDB(movie.getId());
                    updateFavIcon(false);
                }
            }
        });
    }

    private void removeMovieFromDB(int id) {
        getContentResolver().delete(MovieProvider.CONTENT_URI, DBContract.MovieEntry.COLUMN_MOVIE_ID + "=" + id, null);
    }

    private void addMovieToDB(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(DBContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
        cv.put(DBContract.MovieEntry.COLUMN_POSTER, movie.getBasePoster());
        cv.put(DBContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(DBContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(DBContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        getContentResolver().insert(MovieProvider.CONTENT_URI, cv);
    }

    private void populateView(Movie movie) {
        Picasso.with(this).load(movie.getPoster()).placeholder(R.drawable.addphoto).into(imageView);
        title.setText(movie.getOriginalTitle());
        dateReleased.setText(movie.getReleaseDate());
        userRate.setText(String.valueOf(movie.getVoteAverage()));
        overview.setText(movie.getOverview());
        boolean isMovieInDatabase = isMovieInDatabase(movie.getId());
        updateFavIcon(isMovieInDatabase);

        final ArrayList<Trailer> mTrailers = new ArrayList<>();
        final ArrayList<Review> mReviews = new ArrayList<>();

        trailersRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        trailersRecycler.setNestedScrollingEnabled(false);
        reviewsRecycler.setNestedScrollingEnabled(false);

        final TrailerAdapter trailerAdapter = new TrailerAdapter(mTrailers, this);
        final ReviewAdapter reviewAdapter = new ReviewAdapter(mReviews, this);

        trailersRecycler.setAdapter(trailerAdapter);
        reviewsRecycler.setAdapter(reviewAdapter);

        URL trailerUrl = NetworkUtils.buildTrailerReviewUrl(String.valueOf(movie.getId()), NetworkUtils.TRAILER_QUERY);
        URL reviewUrl = NetworkUtils.buildTrailerReviewUrl(String.valueOf(movie.getId()), NetworkUtils.REVIEW_QUERY);

        new MovieTask(new DataListener() {
            @Override
            public void onTrailersRetrieved(ArrayList<Trailer> trailers) {
                mTrailers.clear();
                mTrailers.addAll(trailers);
                trailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onReviewsRetrieved(ArrayList<Review> reviews) {
                mReviews.clear();
                mReviews.addAll(reviews);
                reviewAdapter.notifyDataSetChanged();
            }
        }).execute(trailerUrl, reviewUrl);

    }

    private void updateFavIcon(boolean isMovieInDatabase) {
        favIcon.setImageDrawable(getDrawable(isMovieInDatabase ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp));

    }

    private boolean isMovieInDatabase(int id) {
        //String query = String.format("select * from %s where %s = %s", DBContract.MovieEntry.TABLE_NAME, DBContract.MovieEntry.COLUMN_MOVIE_ID, id);
        Cursor cursor = getContentResolver().query(MovieProvider.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build(),
                null,
                DBContract.MovieEntry.COLUMN_MOVIE_ID,
                new String[]{
                        String.valueOf(id)
                },
                null
        );
        return cursor.getCount() > 0;
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        title = (TextView) findViewById(R.id.title);
        dateReleased = (TextView) findViewById(R.id.date_released);
        userRate = (TextView) findViewById(R.id.user_rate);
        overview = (TextView) findViewById(R.id.overview);
        favIcon = (ImageView) findViewById(R.id.fav_icon);
        trailersRecycler = (RecyclerView) findViewById(R.id.trailers_recycler);
        reviewsRecycler = (RecyclerView) findViewById(R.id.reviews_recycler);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }

    public class MovieTask extends AsyncTask<URL, Void, Wrapper> {

        DataListener dataListener;

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;
        }

        public boolean isInternetAvailable() {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                //You can replace it with your name
                return !ipAddr.equals("");

            } catch (Exception e) {
                return false;
            }
        }

        public MovieTask(DataListener dataListener) {
            this.dataListener = dataListener;
        }

        @Override
        protected Wrapper doInBackground(URL... urls) {
            Wrapper jsonResult = null;
            if (isNetworkConnected() && isInternetAvailable()) {
                URL url1 = urls[0];
                URL url2 = urls[1];
                try {
                    jsonResult = new Wrapper();
                    String s1 = NetworkUtils.getResponseFromHttpUrl(url1);
                    String s2 = NetworkUtils.getResponseFromHttpUrl(url2);
                    jsonResult.setS1(s1);
                    jsonResult.setS2(s2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(Wrapper s) {
            if (s != null) {
                dataListener.onReviewsRetrieved(JsonUtils.getListOfReviews(s.getS2()));
                dataListener.onTrailersRetrieved(JsonUtils.getListOfTrailers(s.getS1()));
            }
        }
    }

}
