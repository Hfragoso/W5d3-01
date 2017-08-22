package com.example.admin.w5d3_01;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Admin on 8/16/2017.
 */

public class FeedReaderContract {

    public static final String CONTENT_AUTHORITY = "com.example.admin.w5d3_01";
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_GENRE = "genre";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

//    private FeedReaderContract() {
//    }

    public static class Genre_FeedEntry implements BaseColumns {

        // content://com.example.w5d3-01/genre
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_GENRE;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_GENRE;

        public static final Uri buildGenreUri(long id){
            Uri returningString = ContentUris.withAppendedId(CONTENT_URI, id);
            return returningString;
        }

        public static final String TABLE_NAME = "genre";
        public static final String COLUMN_NAME = "name";
    }

    public static class Movie_FeedEntry implements BaseColumns {

        // content://com.example.w5d3-01/movie
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_GENRE_ID = "genre_id";
    }
}
