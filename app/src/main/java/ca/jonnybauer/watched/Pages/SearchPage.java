package ca.jonnybauer.watched.Pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.jonnybauer.watched.Helpers.APIHelper;
import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Helpers.RequestHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

/**
 *
 * This Fragment is used to display and manipulate the search page.
 *
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class SearchPage extends Fragment {
    // Layout elements
    EditText searchField;
    ListView listView;
    ImageView resultPoster;
    TextView resultTitle;
    TextView resultActors;
    SimpleRatingBar resultRating;
    TextView resultPlot;
    ImageView resultFavourite;
    ImageView resultWatched;
    ImageView resultAdd;
    ArrayList<Movie> results;

    // Define an ArrayList for the credits
    ArrayList<String> movieCredits;
    ArrayList<ArrayList<String>> credits;

    // ListView Elements
    TextView title;
    TextView actors;
    TextView rating;

    // Database
    DBHelper dbHelper;

    // ListViewAdapter
    SearchResultAdapter adapter;

    // Selected result
    Movie selectedResult;

    private OnFragmentInteractionListener mListener;

    public SearchPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);

        // Create the DBHelper
        dbHelper = new DBHelper(getContext());

        // Layout elements
        searchField = view.findViewById(R.id.searchTextField);
        listView = view.findViewById(R.id.searchListView);

        resultPoster = view.findViewById(R.id.searchResultPoster);
        resultTitle = view.findViewById(R.id.searchResultTitle);
        resultRating = view.findViewById(R.id.searchResultRating);
        resultPlot = view.findViewById(R.id.searchResultPlot);
        resultFavourite = view.findViewById(R.id.searchResultFavourite);
        resultWatched = view.findViewById(R.id.searchResultWatched);
        resultAdd = view.findViewById(R.id.searchResultAdd);

        results = new ArrayList<>();

        // Search Field event handler - when the users taps the search icon
        searchField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Make sure the event was fired on the search icon
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= searchField.getRight() - searchField.getCompoundDrawables()[2].getBounds().width()){
                        processSearchQuery();
                        return true;
                    }
                }
                return false;
            }
        });

        // Search Field event listener - when the user presses enter
        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Make sure they key pressed was enter
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    processSearchQuery();
                    return true;
                } else {
                    return false;
                }

            }
        });


        // Create a SearchResultAdapter
        adapter = new SearchResultAdapter(getContext(), results);

        // Pair the listView with the adapter
        listView.setAdapter(adapter);



        // List view item event handler - Set the CardView items to the selected result
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected result
                selectedResult = adapter.getItem(position);
                setCardViewValues(selectedResult);
            }
        });

        // Card View event handlers - add, mark as watched or favourite

        resultAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add clicked");

                // Check if the movie is in the watch list and hasn't been deleted
                Movie watchListMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, selectedResult.getTmdbID());
                if(watchListMovie == null) {
                    System.out.println("Adding movie!");
                    // Change the add imageView to mark it as added
                    resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);

                    // Add it to the watchList
                    WatchListTable.getInstance().addMovie(selectedResult, dbHelper);

                } else if(watchListMovie.getDeleted() == 1) {
                    System.out.println("Re-Adding movie!");
                    watchListMovie.setDeleted(0);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);

                    // Change the add imageView to mark it as added
                    resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);
                } else {
                    System.out.println("Deleting movie!");
                    watchListMovie.setDeleted(1);
                    watchListMovie.setWatched(0);
                    watchListMovie.setFavourite(0);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                    resultAdd.setImageResource(R.drawable.ic_add_black_24dp);
                    resultFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                    resultWatched.setImageResource(R.drawable.ic_check_black_24dp);
                }
            }
        });

        resultFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add the selected result to the watch list, mark it as a favourite and change the imageviews
                System.out.println("Favourite clicked");
                // Check if the movie is in the watch list and hasn't been deleted
                Movie watchListMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, selectedResult.getTmdbID());
                if(watchListMovie == null) {
                    System.out.println("Adding movie and marking as favourite!");
                    // Change the add imageViews to mark it as added
                    resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    resultFavourite.setImageResource(R.drawable.ic_star_black_24dp);

                    selectedResult.setFavourite(1);

                    // Add it to the watchList
                    WatchListTable.getInstance().addMovie(selectedResult, dbHelper);

                } else if(watchListMovie.getDeleted() == 1) {
                    System.out.println("Re-adding movie and marking as favourite!");
                    watchListMovie.setDeleted(0);
                    watchListMovie.setFavourite(1);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);

                    // Change the add imageViews to mark it as added
                    resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    resultFavourite.setImageResource(R.drawable.ic_star_black_24dp);
                } else if(watchListMovie.getFavourite() == 1){
                    System.out.println("Un-favouriting movie!");
                    watchListMovie.setFavourite(0);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                    resultFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                } else {
                    System.out.println("Favouriting movie!");
                    watchListMovie.setFavourite(1);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                    resultFavourite.setImageResource(R.drawable.ic_star_black_24dp);
                }
            }
        });

        resultWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Watched clicked");
                // TODO: Add the selected result to the watch list, mark it as watched and change the imageviews
                // Check if the movie is in the watch list and hasn't been deleted
                Movie watchListMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, selectedResult.getTmdbID());
                if(watchListMovie == null) {

                    System.out.println("Adding Movie and marking as watched!");
                    // Change the add imageViews to mark it as added
                    resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    resultWatched.setImageResource(R.drawable.ic_check_circle_black_24dp);

                    selectedResult.setFavourite(1);

                    // Add it to the watchList
                    WatchListTable.getInstance().addMovie(selectedResult, dbHelper);

                } else if(watchListMovie.getDeleted() == 1) {
                    System.out.println("Re-adding and marking as watched!");
                    watchListMovie.setDeleted(0);
                    watchListMovie.setWatched(1);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);

                    // Change the add imageViews to mark it as added
                    resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    resultWatched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                } else if(watchListMovie.getWatched() == 1){
                    System.out.println("Unmarking as watched");
                    watchListMovie.setWatched(0);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                    resultWatched.setImageResource(R.drawable.ic_check_black_24dp);
                } else {
                    System.out.println("Marking as watched");
                    watchListMovie.setWatched(1);
                    watchListMovie.setLastUpdated(new Date());
                    WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                    resultWatched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });


        return view;
    }

    /**
     * This adapter is going to be used to load the ListView on the search page with all the results.
     * It is an extension of the ArrayAdapter class
     *
     * @author Jonathon Bauer
     * @version 1.0
     * @see android.widget.ArrayAdapter
     */
    public class SearchResultAdapter extends ArrayAdapter<Movie> {

        // Constructor
        public SearchResultAdapter(Context context, ArrayList<Movie> results) {
            super(context, 0, results);
        }

        // getView method
        public View getView(int position, View view, ViewGroup parent) {

            // Check if the view has been inflated, and if it hasn't inflate it
            if(view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.search_result, parent, false);
            }

            // Get the layout elements
            title = view.findViewById(R.id.resultViewTitle);
            actors = view.findViewById(R.id.resultViewActors);
            rating = view.findViewById(R.id.resultViewRating);


            // Set the layout elements to the item in the current position
            Movie result = getItem(position);
            Calendar release = Calendar.getInstance();
            release.setTime(result.getReleaseDate());
            String titleString = result.getTitle() + " (" + release.get(Calendar.YEAR) + ")";
            title.setText(titleString);
            String ratingString = result.getRating() + "";
            rating.setText(ratingString);
            if(result.getCredits() != null) {
                actors.setText(result.getTopBilling());
            }


//            if(actors.getText().toString().equals("")) {
//                APIHelper.getInstance().getCredits(result.getTmdbID(), getContext(), new APIHelper.RequestListener() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        movieCredits = APIHelper.getInstance().parseCredits(response);
//                        String creditsString = movieCredits.get(0) + ", " + movieCredits.get(1) + ", " + movieCredits.get(2);
//                        System.out.println("Changing actors to: " + creditsString);
//                        actors.setText(creditsString);
//                    }
//                });
//            }


            return view;
        }

    }

    /**
     * This function is used to set the values on the cardView to the passed in Movie object
     * @param selectedResult the Movie that we are setting the cardview to
     * @see Movie
     */
    public void setCardViewValues(Movie selectedResult){

        // Update the cardview to the selected results information
        Calendar date = Calendar.getInstance();
        date.setTime(selectedResult.getReleaseDate());
        String titleString = selectedResult.getTitle() + " (" + date.get(Calendar.YEAR) + ")";
        resultTitle.setText(titleString);
        double rating = (selectedResult.getRating() / 10.0) * 5.0;

        resultRating.setRating((float) rating);
        resultPlot.setText(selectedResult.getPlot());
        Picasso.get().load(selectedResult.getPosterPath()).into(resultPoster);


        // Check if the movie is in the watch list and hasn't been deleted
        Movie watchListMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, selectedResult.getTmdbID());
        if(watchListMovie != null && watchListMovie.getDeleted() != 1) {
            // Change the add imageView to mark it as added
            resultAdd.setImageResource(R.drawable.ic_add_circle_black_24dp);

            // Check if it's been marked as favourite, and if so change the imageview
            if(watchListMovie.getFavourite() == 1) {
                resultFavourite.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                resultFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
            }

            // Check if the movie has been watched and if so change the imageview
            if(watchListMovie.getWatched() == 1) {
                resultWatched.setImageResource(R.drawable.ic_check_circle_black_24dp);
            } else {
                resultWatched.setImageResource(R.drawable.ic_check_black_24dp);
            }
        } else {
            resultAdd.setImageResource(R.drawable.ic_add_black_24dp);
            resultWatched.setImageResource(R.drawable.ic_check_black_24dp);
            resultFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

    }

    /**
     * This function is used to hide the keyboard from the screen and process the users search query.
     * It makes the API call and updates the listview with the results
     */
    public void processSearchQuery(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        final String searchInput = searchField.getText().toString();



        if(validateInput(searchInput)) {
            System.out.println("Search is valid.");
            String cleanInput = searchInput.replace(" ", "+");
            movieCredits = new ArrayList<>();
            credits = new ArrayList<>();

            APIHelper.getInstance().searchMovie(cleanInput, getContext(), new APIHelper.RequestListener(){
                @Override
                public void onSuccess(JSONObject response) {
                    results = APIHelper.getInstance().parseMovies(response);
                    System.out.println("Found " + results.size() + " matching " + searchInput);

                    for(int i=0; i<results.size(); i++) {
                        final int index = i;
                         APIHelper.getInstance().getCredits(results.get(index).getTmdbID(), getContext(), new APIHelper.RequestListener() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                ArrayList<String> credits = APIHelper.getInstance().parseCredits(response);
                                results.get(index).setCredits(credits);
                                adapter.notifyDataSetChanged();
                                System.out.println("Setting credits for " + results.get(index).getTitle());
                            }
                        });
                    }



                    adapter = new SearchResultAdapter(getContext(), results);
                    listView.setAdapter(adapter);

                    selectedResult = adapter.getItem(0);
                    setCardViewValues(selectedResult);
                }
            });
        } else {
            System.out.println("Search is invalid.");
            Toast toast = Toast.makeText(getContext(), "Please only use A-Z, 0-9 and spaces in your search query.", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    public Boolean validateInput(String input){

        if(input.matches("[a-zA-Z0-9 ]*")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
