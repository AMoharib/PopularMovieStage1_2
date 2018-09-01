package com.amoharib.popularmoviestage1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL)",
                DBContract.MovieEntry.TABLE_NAME,
                DBContract.MovieEntry.COLUMN_MOVIE_ID,
                DBContract.MovieEntry.COLUMN_MOVIE_TITLE,
                DBContract.MovieEntry.COLUMN_POSTER,
                DBContract.MovieEntry.COLUMN_OVERVIEW,
                DBContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                DBContract.MovieEntry.COLUMN_RELEASE_DATE
        );
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
