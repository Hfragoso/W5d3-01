package com.example.admin.w5d3_01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.admin.w5d3_01.FeedReaderContract.Genre_FeedEntry;
import com.example.admin.w5d3_01.FeedReaderContract.Movie_FeedEntry;
/**
 * Created by Admin on 8/16/2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydatabase.db";

    //GENRE
    public static final String SQL_CREATE_GENRE =
            "CREATE TABLE " + Genre_FeedEntry.TABLE_NAME + " (" +
                    Genre_FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    Genre_FeedEntry.COLUMN_NAME + " TEXT)";

    public static final String SQL_DELETE_GENRE =
            "DROP TABLE IF EXISTS " + Genre_FeedEntry.TABLE_NAME;
    //GENRE

    //MOVIE
    public static final String SQL_CREATE_MOVIE =
            "CREATE TABLE " + Movie_FeedEntry.TABLE_NAME + " (" +
                    Movie_FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    Movie_FeedEntry.COLUMN_NAME + " TEXT," +
                    Movie_FeedEntry.COLUMN_DATE + " TEXT," +
                    Movie_FeedEntry.COLUMN_GENRE_ID + " INT," +
                    "FOREIGN KEY(" + Movie_FeedEntry.COLUMN_GENRE_ID +
                    ") REFERENCES "+ Genre_FeedEntry.TABLE_NAME + "(" +Genre_FeedEntry._ID + "))";

    public static final String SQL_DELETE_MOVIE = "DROP TABLE IF EXISTS " + Movie_FeedEntry.TABLE_NAME;
    //MOVIE

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_GENRE);
        sqLiteDatabase.execSQL(SQL_DELETE_MOVIE);
        onCreate(sqLiteDatabase);
    }
}
