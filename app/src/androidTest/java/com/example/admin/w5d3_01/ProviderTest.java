package com.example.admin.w5d3_01;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

public class ProviderTest extends AndroidTestCase {
    private static final String TEST_GENRE_NAME = "Family";
    private static final String TEST_UPDATE_GENRE_NAME = "SciFi";
    private static final String TEST_MOVIE_NAME = "Back to the future";
    private static final String TEST_UPDATE_MOVIE_NAME = "Back to the future II";
    private static final String TEST_MOVIE_RELEASE_DATE = "1985-09-15";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDeleteAllRecords();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testDeleteAllRecords();
    }

    public void testDeleteAllRecords() {
        mContext.getContentResolver().delete(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                FeedReaderContract.Genre_FeedEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(0, cursor.getCount());

        cursor = mContext.getContentResolver().query(
                FeedReaderContract.Genre_FeedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(0, cursor.getCount());

        cursor.close();
    }

    public void testGetType(){
        String type = mContext.getContentResolver().getType(FeedReaderContract.Genre_FeedEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.../genre
        assertEquals(FeedReaderContract.Genre_FeedEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(FeedReaderContract.Genre_FeedEntry.buildGenreUri(0));

        assertEquals(FeedReaderContract.Genre_FeedEntry.CONTENT_ITEM_TYPE, type);


        type = mContext.getContentResolver().getType(FeedReaderContract.Movie_FeedEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.../genre
        assertEquals(FeedReaderContract.Movie_FeedEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(FeedReaderContract.Movie_FeedEntry.buildMovieUri(0));

        assertEquals(FeedReaderContract.Movie_FeedEntry.CONTENT_ITEM_TYPE,type);
    }

    public void testInsertAndReadGenre(){
        ContentValues values = getGenreContentValues();
        Uri genreInsertUri = mContext.getContentResolver().insert(
                FeedReaderContract.Genre_FeedEntry.CONTENT_URI,
                values
        );

        long genreId = ContentUris.parseId(genreInsertUri);

        assertTrue(genreId > 0);

        Cursor cursor = mContext.getContentResolver().query(
                FeedReaderContract.Genre_FeedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor(cursor, values);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                FeedReaderContract.Genre_FeedEntry.buildGenreUri(genreId),
                null,
                null,
                null,
                null
        );

        validateCursor(cursor, values);
        cursor.close();
    }

    public void testInsertReadMovie(){
        ContentValues values = getGenreContentValues();
        Uri genreInsertUri = mContext.getContentResolver().insert(
                FeedReaderContract.Genre_FeedEntry.CONTENT_URI,
                values
        );
        long genreId = ContentUris.parseId(genreInsertUri);

        ContentValues movieValues = getMovieContentValues(genreId);

        Uri movieInsertUri = mContext.getContentResolver().insert(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                movieValues
        );

        long movieId = ContentUris.parseId(movieInsertUri);

        assertTrue(movieId > 0);

        Cursor movieCursor = mContext.getContentResolver().query(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        validateCursor(movieCursor, movieValues);
        movieCursor.close();

        movieCursor = mContext.getContentResolver().query(
                FeedReaderContract.Movie_FeedEntry.buildMovieUri(movieId),
                null,
                null,
                null,
                null
        );

        validateCursor(movieCursor, movieValues);
        movieCursor.close();
    }

    public void testUpdateMovie(){
        ContentValues genreValues = getGenreContentValues();
        Uri genreInsertUri = mContext.getContentResolver().insert(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                genreValues
        );
        long genreId = ContentUris.parseId(genreInsertUri);

        ContentValues movieValues = getMovieContentValues(genreId);
        Uri movieInsertUri = mContext.getContentResolver().insert(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                movieValues
        );

        long movieId = ContentUris.parseId(movieInsertUri);

        ContentValues updateMovie = new ContentValues(movieValues);
        updateMovie.put(FeedReaderContract.Movie_FeedEntry._ID, movieId);
        updateMovie.put(FeedReaderContract.Movie_FeedEntry.COLUMN_NAME, TEST_UPDATE_MOVIE_NAME);

        int updateId = mContext.getContentResolver().update(
                FeedReaderContract.Movie_FeedEntry.CONTENT_URI,
                updateMovie,
                FeedReaderContract.Movie_FeedEntry._ID + " =?",
                new String[]{String.valueOf(movieId)}
        );

        assertTrue(updateId > 0);


        Cursor movieCursor = mContext.getContentResolver().query(
                FeedReaderContract.Movie_FeedEntry.buildMovieUri(movieId),
                null,
                null,
                null,
                null
        );

        validateCursor(movieCursor, updateMovie);
        movieCursor.close();
    }

    private ContentValues getGenreContentValues(){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.Genre_FeedEntry.COLUMN_NAME, TEST_GENRE_NAME);
        return values;
    }
    private ContentValues getMovieContentValues(long genreId){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.Movie_FeedEntry.COLUMN_NAME, TEST_MOVIE_NAME);
        values.put(FeedReaderContract.Movie_FeedEntry.COLUMN_DATE, TEST_MOVIE_RELEASE_DATE);
        values.put(FeedReaderContract.Movie_FeedEntry.COLUMN_GENRE_ID, genreId);
        return values;
    }

    private void validateCursor(Cursor valueCursor, ContentValues expectedValues){
        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valuesSet = expectedValues.valueSet();

        for(Map.Entry<String, Object> entry: valuesSet){
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);

            assertFalse(idx == -1);

            switch (valueCursor.getType(idx)){
                case Cursor.FIELD_TYPE_FLOAT:
                    assertEquals(entry.getValue(), valueCursor.getDouble(idx));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    assertEquals(Integer.parseInt(entry.getValue().toString()), valueCursor.getInt(idx));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
                default:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
            }
        }
        valueCursor.close();
    }


    public void testUpdateGenre(){
        ContentValues values = getGenreContentValues();
        Uri genreUri = mContext.getContentResolver().insert(
                FeedReaderContract.Genre_FeedEntry.CONTENT_URI,
                values
        );

        long genreId = ContentUris.parseId(genreUri);

        ContentValues updateValues = new ContentValues();
        updateValues.put(FeedReaderContract.Genre_FeedEntry._ID, genreId);
        updateValues.put(FeedReaderContract.Genre_FeedEntry.COLUMN_NAME, TEST_UPDATE_GENRE_NAME);

    }
}
