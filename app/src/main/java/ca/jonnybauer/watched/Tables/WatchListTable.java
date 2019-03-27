package ca.jonnybauer.watched.Tables;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Movie;

/**
 * This class represents the watch_list table, and will be used to manipulate it.
 *
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class WatchListTable {

    // Instance to be used to access the database
    public static WatchListTable instance = null;

    // Constructor
    private WatchListTable() {}

    // Get Instance Method

    public static WatchListTable getInstance(){
        if(instance == null) {
            return new WatchListTable();
        } else {
            return instance;
        }
    }

    // Table Contents
    private static final String TABLE_NAME = "watch_list";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TMDB_ID = "tmdb_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_POSTER_PATH = "poster_path";
    private static final String COLUMN_CREDITS = "credits";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_PLOT = "plot";
    private static final String COLUMN_FAVOURITE = "favourite";
    private static final String COLUMN_WATCHED = "watched";
    private static final String COLUMN_DELETED = "deleted";
    private static final String COLUMN_DATE_ADDED = "date_added";
    private static final String COLUMN_LAST_UPDATED = "last_updated";

    // Create Table Query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TMDB_ID + " INTEGER,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_POSTER_PATH + " TEXT,"
            + COLUMN_CREDITS + " TEXT,"
            + COLUMN_RELEASE_DATE + " INTEGER,"
            + COLUMN_RATING + " INTEGER,"
            + COLUMN_PLOT + " TEXT,"
            + COLUMN_FAVOURITE + " INTEGER,"
            + COLUMN_WATCHED + " INTEGER,"
            + COLUMN_DELETED + " INTEGER,"
            + COLUMN_DATE_ADDED + " INTEGER,"
            + COLUMN_LAST_UPDATED + " INTEGER)";

    // Delete Table Query
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Create Record Query
    public void addMovie(Movie movie, DBHelper dbHelper){
        System.out.println("Saving movie to database!");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TMDB_ID, movie.getTmdbID());
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath());

        // Convert the Credits arraylist to a string using Gson
        Gson gson = new Gson();
        String creditsString= gson.toJson(movie.getCredits());

        values.put(COLUMN_CREDITS, creditsString);
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());
        values.put(COLUMN_RATING, movie.getRating());
        values.put(COLUMN_PLOT, movie.getPlot());
        values.put(COLUMN_FAVOURITE, movie.getFavourite());
        values.put(COLUMN_WATCHED, movie.getWatched());
        values.put(COLUMN_DELETED, movie.getDeleted());
        values.put(COLUMN_DATE_ADDED, movie.getDateAdded().getTime());
        values.put(COLUMN_LAST_UPDATED, movie.getLastUpdated().getTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get Record Query
    public Movie getMovie(DBHelper dbHelper, int movieID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] args = { movieID + "" };

        Cursor cursor = db.query(TABLE_NAME, null, selection, args, null, null, null);

        // Set the type to retrieve the credits array
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // Declare a new Gson to retrieve the credits array
        Gson gson = new Gson();

        if(cursor.moveToNext()) {
            // Get the credits arraylist
            ArrayList<String> credits = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_CREDITS)), type);


            Movie movie = new Movie(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_TMDB_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)),
                    credits,
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_RELEASE_DATE))),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLOT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_WATCHED)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED)),
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_DATE_ADDED))),
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_LAST_UPDATED))));
            cursor.close();
            db.close();
            return movie;
        } else {
            return null;
        }
    }

    // Get Record via TMDB_ID
    public Movie getMovieWithTmdbID(DBHelper dbHelper, int tmdbID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_TMDB_ID + " = ?";
        String[] args = { tmdbID + "" };

        Cursor cursor = db.query(TABLE_NAME, null, selection, args, null, null, null);

        // Set the type to retrieve the credits array
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // Declare a new Gson to retrieve the credits array
        Gson gson = new Gson();

        if(cursor.moveToNext()) {
            // Get the credits arraylist
            ArrayList<String> credits = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_CREDITS)), type);

            Movie movie = new Movie(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_TMDB_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)),
                    credits,
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_RELEASE_DATE))),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLOT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_WATCHED)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED)),
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_DATE_ADDED))),
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_LAST_UPDATED))));
            cursor.close();
            db.close();
            return movie;
        } else {
            return null;
        }
    }

    // Get All Records Query
    public ArrayList<Movie> getAllMovies(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY  ?", null);
        // Set the type to retrieve the credits array
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // Declare a new Gson to retrieve the credits array
        Gson gson = new Gson();

        while(cursor.moveToNext()) {
            // Get the credits arraylist
            ArrayList<String> credits = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_CREDITS)), type);
            Movie movie = new Movie(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_TMDB_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)),
                    credits,
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_RELEASE_DATE))),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PLOT)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_WATCHED)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED)),
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_DATE_ADDED))),
                    new Date(cursor.getInt(cursor.getColumnIndex(COLUMN_LAST_UPDATED))));
            movies.add(movie);
        }
        cursor.close();
        db.close();
        return movies;
    }

    public ArrayList<Movie> filterDeletedMovies(ArrayList<Movie> movies) {
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        for(int i=0; i<movies.size(); i++) {
            if(movies.get(i).getDeleted() == 0) {
                filteredMovies.add(movies.get(i));
            }
        }
        return filteredMovies;
    }


    // Update Record Query
    public void updateMovie(Movie movie, DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TMDB_ID, movie.getTmdbID());
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath());

        // Convert the Credits arraylist to a string using Gson
        Gson gson = new Gson();
        String creditsString= gson.toJson(movie.getCredits());

        values.put(COLUMN_CREDITS, creditsString);
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());
        values.put(COLUMN_RATING, movie.getRating());
        values.put(COLUMN_PLOT, movie.getPlot());
        values.put(COLUMN_FAVOURITE, movie.getFavourite());
        values.put(COLUMN_WATCHED, movie.getWatched());
        values.put(COLUMN_DELETED, movie.getDeleted());
        values.put(COLUMN_DATE_ADDED, movie.getDateAdded().getTime());
        values.put(COLUMN_LAST_UPDATED, movie.getLastUpdated().getTime());
        String selection = COLUMN_ID + " LIKE ?";
        String[] args = { movie.getId() + "" };
        db.update(TABLE_NAME, values, selection, args);
    }


    // Delete Record Query
    public void deleteMovie(DBHelper dbHelper, int movieID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = COLUMN_ID + "LIKE ?";
        String[] args = { movieID + "" };
        db.delete(TABLE_NAME, selection, args);
    }
    



}
