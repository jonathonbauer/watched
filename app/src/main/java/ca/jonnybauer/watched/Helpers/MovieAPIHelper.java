package ca.jonnybauer.watched.Helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.Models.Theatre;

/**
 * This class is used to make API calls and parse their results.
 * @author Jonathon Bauer
 * @version 1.0
 */
public class MovieAPIHelper {
    private static MovieAPIHelper instance;

    // API Constants
    private static final String API_KEY = "?api_key=28cc34f7d78c1be918490d1e22896393";
    private static final String MAP_KEY = "&key=AIzaSyDy7SPJ3HXAZ6keb25gLulUbG6FU5SfQsI";

    // Map API Queries
    private static final String MAP_URL = "https://maps.googleapis.com/maps/api/place/search/json?location=";

    // https://maps.googleapis.com/maps/api/place/search/json?location=42.327628,-82.972314&radius=15000&sensor=true&key=AIzaSyCVE1f4uvNaB44As1ay_ycT1OflBEZdNls&types=movie_theater
    // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJDflW6xfRJIgRHUtzg4n1ElY&fields=name,geometry,formatted_phone_number,formatted_address,url,opening_hours,website&key=AIzaSyDy7SPJ3HXAZ6keb25gLulUbG6FU5SfQsI

    // Movie API Queries
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie" + API_KEY + "&language=en-US&query=";
    private static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing" + API_KEY + "&language=en-US&page=1";
    private static final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular" + API_KEY + "&language=en-US&page=1";
    private static final String UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming" + API_KEY + "&language=en-US&page=1";
    private static final String TRENDING_URL = "https://api.themoviedb.org/3/trending/movie/week" + API_KEY;
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";



    // Constructor
    private MovieAPIHelper(){}

    /**
     * This function is used to either instantiate, or return the MovieAPIHelper that is used to manipulate the API
     * @return Either a new or the existing MovieAPIHelper
     */
    public static MovieAPIHelper getInstance() {
        if(instance == null) {
            instance = new MovieAPIHelper();
        }
        return instance;
    }

    /**
     * This function is used to make an API Call to get a Movie based on the provided movieID
     * @param movieID The unique 'TheMovieDB' id of the movie we are looking for.
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API

     */
    public void getMovie(int movieID, Context context, final RequestListener requestListener) {
        // Build the query
        String url = MOVIE_URL + movieID + API_KEY;

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });

        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     * This function is used to make an API Call to get a movie object based on the provided search query
     * @param query The search query that we will search the database for
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API

     */
    public void searchMovie(String query, Context context, final RequestListener requestListener) {
        // Build the query
        String url = SEARCH_URL + query + "&page=1&include_adult=false";
        System.out.println("Search query: " + url);

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Trigger the response listener when a response is received
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });
        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     * This function is used to make an API Call to get  similar movies based on the provided movieID
     * @param movieID The unique 'TheMovieDB' id of the movie we are looking for similarities for.
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API
     */
    public void getSimilarMovies(int movieID, Context context, final RequestListener requestListener) {
        // Build the query
        String url = MOVIE_URL + movieID + "/similar" + API_KEY + "&language=en-US&page=1";

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });

        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     * This function is used to make an API Call to get suggested movies based on the provided movieID
     * @param movieID The unique 'TheMovieDB' id of the movie we are looking for suggestions for.
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API
     */
    public void getSuggestions(int movieID, Context context, final RequestListener requestListener) {
        // Build the query
        String url = MOVIE_URL + movieID + "/recommendations" + API_KEY + "&language=en-US&page=1";

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });

        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     This function is used to make an API Call to get current playing movies
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API
     */
    public void getNowPlaying(Context context, final RequestListener requestListener) {

        // Build the query
        String url = NOW_PLAYING_URL;

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Trigger the response listener when a response is received
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });
        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     * This function is used to make an API Call to get current popular movies
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API
     */
    public void getPopular(Context context, final RequestListener requestListener) {

        // Build the query
        String url = POPULAR_URL;

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Trigger the response listener when a response is received
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });
        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     * This function is used to make an API Call to get upcoming movies
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API
     */
    public void getUpcoming(Context context, final RequestListener requestListener) {

        // Build the query
        String url = UPCOMING_URL;
        System.out.println(url);
        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Trigger the response listener when a response is received
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });
        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    /**
     * This function is used to make an API Call to get movies that have been trending for the last week
     * @param context Application context, used to add the request to the queue
     * @param requestListener A response listener used to notify the calling class of a response from the API
     */
    public void getTrending(Context context, final RequestListener requestListener) {

        // Build the query
        String url = TRENDING_URL;

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Trigger the response listener when a response is received
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });
        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }

    public void getCredits(int movieID, Context context, final RequestListener requestListener) {
        // Build the query
        String url = MOVIE_URL + movieID + "/credits" + API_KEY;

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Trigger the response listener when a response is received
                requestListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // TODO: Handle errors properly
                System.out.println(error);
            }
        });
        // Add the request to the request queue
        RequestHelper.getInstance(context).addToRequestQueue(request, context);
    }


    /**
     * This function is used to instantiate a Movie object based on the JSONObject response from the API.
     * @param response The API response that is to be parsed into a Movie object
     * @return An instantiated Movie object using the provided API response
     * @see Movie
     */
    public Movie parseMovie(JSONObject response) {
        try {
            // Get the needed values from the JSONObject
            int tmdbID = response.getInt("id");
            String title = response.getString("original_title");
            String posterPath = IMAGE_URL + response.getString("poster_path");
            String dateString = response.getString("release_date");

            Date releaseDate;
            if(dateString.length() != 0) {
                int year = Integer.parseInt(dateString.substring(0, 4));
                int month = Integer.parseInt(dateString.substring(5, 7));
                int day = Integer.parseInt(dateString.substring(8, 10));
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                // TODO: Find a more appropriate fix for the release date issue

                calendar.add(Calendar.MONTH, -1);
                releaseDate = new Date(calendar.getTimeInMillis());
                System.out.println(releaseDate);
            } else {
                releaseDate = new Date(0);
            }




            String plot = response.getString("overview");
            double rating = response.getDouble("vote_average");

            // Return the movie
            return new Movie(tmdbID, title, posterPath, releaseDate, rating, plot);

        } catch(JSONException e){
            System.out.println(e);
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This function is used to instantiate an array of Movie objects based on the JSONObject response from the API.
     * @param response The API response that is to be parsed into a Movie object
     * @return An array of Movie objects using the provided API response
     * @see Movie
     */
    public ArrayList<Movie> parseMovies(JSONObject response) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            // Get the array titled results from the response
            JSONArray results = response.getJSONArray("results");

            // Populate an ArrayList of movies with the parsed movie results
            for(int i=0; i < results.length(); i++) {
                Movie movie = parseMovie(results.getJSONObject(i));
                movies.add(movie);
            }

            System.out.println("Returning " + results.length() + " result(s)");
            // return the array of movies
            return movies;

        } catch(JSONException e){
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This function is used to instantiate an array of Strings representing the credits of a movie based on the JSONObject response from the API.
     * @param response The API response that is to be parsed into an array of Strings
     * @return An array of Strings using the provided API response
     */
    public ArrayList<String> parseCredits(JSONObject response) {
        ArrayList<String> credits = new ArrayList<>();

        try {
            // Get the array titled results from the response
            JSONArray results = response.getJSONArray("cast");

            // Populate an ArrayList of movies with the parsed movie results
            for(int i=0; i < results.length(); i++) {
                credits.add(results.getJSONObject(i).getString("name"));
            }

            // return the array of movies
            return credits;

        } catch(JSONException e){
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * This interface is to be used to notify the calling fragment of a response from the API.
     */
    public interface RequestListener{
        void onSuccess(JSONObject response);
    }


}
