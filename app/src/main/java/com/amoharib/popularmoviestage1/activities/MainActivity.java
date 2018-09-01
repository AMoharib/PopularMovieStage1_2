package com.amoharib.popularmoviestage1.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amoharib.popularmoviestage1.adapters.MoviesAdapter;
import com.amoharib.popularmoviestage1.db.DBHelper;
import com.amoharib.popularmoviestage1.db.MovieProvider;
import com.amoharib.popularmoviestage1.models.Movie;
import com.amoharib.popularmoviestage1.R;
import com.amoharib.popularmoviestage1.utils.JsonUtils;
import com.amoharib.popularmoviestage1.utils.NetworkUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recycler;
    private MoviesAdapter adapter;
    private ArrayList<Movie> movies;
    private SQLiteDatabase db;
    private Cursor cursor = null;

    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        //cursor = getAllMovies();

        movies = new ArrayList<>();
        adapter = new MoviesAdapter(movies, this, cursor);

        int posterSize = 500;
        mGridLayoutManager = new GridLayoutManager(this, calculateBestSpanCount(posterSize));
        recycler.setLayoutManager(mGridLayoutManager);
        recycler.setAdapter(adapter);


        if (savedInstanceState != null) {
            adapter.setMovies(savedInstanceState.<Movie>getParcelableArrayList("movies"));
            ((GridLayoutManager) recycler.getLayoutManager()).scrollToPosition(savedInstanceState.getInt("position"));
        } else {
            URL url = NetworkUtils.buildUrl(NetworkUtils.TOP_RATED_QUERY);
            new MovieTask().execute(url);
        }

    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", ((GridLayoutManager) recycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putParcelableArrayList("movies", adapter.getMovies());
        super.onSaveInstanceState(outState);
    }


    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        URL url = null;
        switch (id) {
            case R.id.top_rated_choice:
                url = NetworkUtils.buildUrl(NetworkUtils.TOP_RATED_QUERY);
                break;
            case R.id.popular_choice:
                url = NetworkUtils.buildUrl(NetworkUtils.POPULAR_QUERY);
                break;
            case R.id.fav_items:
                getSupportLoaderManager().initLoader(0, null, this);
                return true;
        }
        new MovieTask().execute(url);
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Movie> getAllMovieFromDB() {
        ArrayList<Movie> movies = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                movies.add(new Movie(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Double.parseDouble(cursor.getString(4)),
                        cursor.getString(5)
                ));
                cursor.moveToNext();
            }
        }
        return movies;
    }

    @Override
    protected void onResume() {
        getSupportLoaderManager().restartLoader(0, null, this);
        super.onResume();
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {

            Cursor mCursor = null;

            @Override
            public void deliverResult(@Nullable Cursor data) {
                mCursor = data;
                cursor = mCursor;
                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if (mCursor != null) {
                    deliverResult(mCursor);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieProvider.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        movies.clear();
        movies.addAll(getAllMovieFromDB());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        movies.clear();
        movies.addAll(getAllMovieFromDB());
        adapter.notifyDataSetChanged();
    }

    public class MovieTask extends AsyncTask<URL, Void, String> {

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

        @Override
        protected String doInBackground(URL... urls) {
            String jsonResult = null;
            if (isNetworkConnected() && isInternetAvailable()) {
                URL url = urls[0];
                try {
                    jsonResult = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String s) {
            movies.clear();
            if (s != null) {
                movies.addAll(JsonUtils.getListOfMoviesFromJson(s));
                adapter.setMovies(movies);
                ((GridLayoutManager) recycler.getLayoutManager()).scrollToPosition(0);
                System.out.println(movies.size());
            } else {
                Toast.makeText(MainActivity.this, "No internet connection, Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
