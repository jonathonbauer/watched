package ca.jonnybauer.watched.Helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.jonnybauer.watched.Models.Movie;

/**
 * This class is used to make API calls and parse their results.
 * @author Jonathon Bauer
 * @version 1.0
 */
public class APIHelper {
    private static APIHelper instance;

    // API Constants
    private static final String API_KEY = "?api_key=28cc34f7d78c1be918490d1e22896393";

    // API Queries
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie" + API_KEY + "&language=en-US&query=";
    private static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing" + API_KEY + "&language=en-US&page=1";
    private static final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular" + API_KEY + "&language=en-US&page=1";
    private static final String UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming" + API_KEY + "&language=en-US&page=1";
    private static final String TRENDING_URL = "https://api.themoviedb.org/3/trending/movie/week" + API_KEY;
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185/";


    // Constructor
    private APIHelper(){}

    /**
     * This function is used to either instantiate, or return the APIHelper that is used to manipulate the API
     * @return Either a new or the existing APIHelper
     */
    public static APIHelper getInstance() {
        if(instance == null) {
            instance = new APIHelper();
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
//            2018-12-18
            int year = Integer.parseInt(dateString.substring(0,4));
            int month = Integer.parseInt(dateString.substring(5,7));
            int day = Integer.parseInt(dateString.substring(8,10));

            System.out.println(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            Date releaseDate = new Date(calendar.getTimeInMillis());
            System.out.println("title " + title);
            System.out.println("year " + calendar.get(Calendar.YEAR));
            System.out.println("month " + calendar.get(Calendar.MONTH));
            System.out.println("day " + calendar.get(Calendar.DATE));
            System.out.println("full date " + releaseDate.getMonth());
            String plot = response.getString("overview");
            int rating = response.getInt("vote_average");

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
     * @return An instantiated array of Movie objects using the provided API response
     * @see Movie
     */
    public ArrayList<Movie> parseMovies(JSONObject response) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            // Get the array titled results from the response
            JSONArray results = response.getJSONArray("results");

            // Populate an ArrayList of movies with the parsed movie results
            for(int i=0; i < response.getInt("total_pages"); i++) {
                Movie movie = parseMovie(results.getJSONObject(i));
                movies.add(movie);
            }

            // return the array of movies
            return movies;

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
