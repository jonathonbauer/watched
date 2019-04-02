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
public class GoogleAPIHelper {
    private static GoogleAPIHelper instance;

    // API Constants
    private static final String API_KEY = "&key=AIzaSyDy7SPJ3HXAZ6keb25gLulUbG6FU5SfQsI";
    private static final String MAP_URL = "https://maps.googleapis.com/maps/api/place/search/json?location=";
    private static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private static final String PLACES_FIELDS = "&fields=name,geometry,formatted_phone_number,formatted_address,url,opening_hours,website";

    // https://maps.googleapis.com/maps/api/place/search/json?location=42.327628,-82.972314&radius=15000&sensor=true&key=AIzaSyCVE1f4uvNaB44As1ay_ycT1OflBEZdNls&types=movie_theater
    // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJDflW6xfRJIgRHUtzg4n1ElY&fields=name,geometry,formatted_phone_number,formatted_address,url,opening_hours,website&key=AIzaSyDy7SPJ3HXAZ6keb25gLulUbG6FU5SfQsI




    // Constructor
    private GoogleAPIHelper(){}

    /**
     * This function is used to either instantiate, or return the MovieAPIHelper that is used to manipulate the API
     * @return Either a new or the existing MovieAPIHelper
     */
    public static GoogleAPIHelper getInstance() {
        if(instance == null) {
            instance = new GoogleAPIHelper();
        }
        return instance;
    }

    public void getNearbyTheatres(Double lat, Double lng, Context context, final RequestListener requestListener) {
        // Build the query
        String url = MAP_URL + lat + "," + lng + "&radius=15000&sensor=true" + API_KEY + "&types=movie_theater";
        System.out.println("Get Nearby Theatres query: " + url);

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


    public void getTheatre(String placeID, Context context, final RequestListener requestListener) {

        // Build the query
        String url = PLACES_URL + placeID + PLACES_FIELDS + API_KEY;
        System.out.println("Theatre query: " + url);

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

    public Theatre parseTheatre(JSONObject response) {
        Theatre theatre = new Theatre();
        try {
            theatre.setName(response.getString("name"));
            theatre.setPlacesID(response.getString("place_id"));
            theatre.setAddress(response.getString("vicinity"));
            theatre.setLatitude(response.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            theatre.setLongitude(response.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return theatre;
    }


    public ArrayList<Theatre> parseTheatres(JSONObject response) {
        ArrayList<Theatre> theatres = new ArrayList<>();

        try {
            JSONArray results = response.getJSONArray("results");
            for(int i=0; i < results.length(); i++) {
                theatres.add(parseTheatre(results.getJSONObject(i)));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return theatres;
    }

    /**
     * This interface is to be used to notify the calling fragment of a response from the API.
     */
    public interface RequestListener{
        void onSuccess(JSONObject response);
    }


}
