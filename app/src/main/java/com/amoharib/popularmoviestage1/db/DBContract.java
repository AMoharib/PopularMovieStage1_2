package com.amoharib.popularmoviestage1.db;

import android.provider.BaseColumns;

public class DBContract {
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_OVERVIEW = "movie_overview";
        public static final String COLUMN_POSTER = "movie_poster";
        public static final String COLUMN_VOTE_AVERAGE = "movie_vote_average";
        public static final String COLUMN_RELEASE_DATE = "movie_release_date";

        public static final String[] ALL_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_POSTER,
                COLUMN_OVERVIEW,
                COLUMN_VOTE_AVERAGE,
                COLUMN_RELEASE_DATE
        };
    }
}
