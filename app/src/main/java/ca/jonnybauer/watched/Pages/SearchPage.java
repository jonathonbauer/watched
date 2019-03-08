package ca.jonnybauer.watched.Pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ca.jonnybauer.watched.Helpers.APIHelper;
import ca.jonnybauer.watched.Helpers.RequestHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.R;

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
    TextView resultRating;
    TextView resultPlot;
    ImageView resultFavourite;
    ImageView resultWatched;
    ImageView resultAdd;
    String output;
    ArrayList<Movie> results;

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

        // Search Field event handler - get the search results
        searchField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Make sure the event was fired on the search icon
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= searchField.getRight() - searchField.getCompoundDrawables()[2].getBounds().width()){
                        // TODO: get search results method call
                        System.out.println("Searching....");
                        String text = searchField.getText().toString();
                        APIHelper.getInstance().searchMovie(text, getContext(), new APIHelper.RequestListener(){
                            @Override
                            public void onSuccess(JSONObject response) {
                                System.out.println("Success: " + response.toString());
                                results = APIHelper.getInstance().parseMovies(response);
                                System.out.println("Parsed Results: " + results.size());
                                adapter = new SearchResultAdapter(getContext(), results);
                                listView.setAdapter(adapter);
                            }
                        });
                        return true;
                    }
                }
                return false;
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

                // Update the cardview to the selected results information
                resultTitle.setText(selectedResult.getTitle());
                String ratingString = selectedResult.getRating() + " / 10";
                resultRating.setText(ratingString);
                resultPlot.setText(selectedResult.getPlot());
                Picasso.get().load(selectedResult.getPosterPath()).into(resultPoster);
                System.out.println(selectedResult.getPosterPath());
                System.out.println(selectedResult.getReleaseDate().toString());

                // TODO: Check if the movie has been added, watched or add to favourites and change the imageviews accordingly

            }
        });

        // Card View event handlers - add, mark as watched or favourite

        resultAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add the selected result to the watch list and change the imageview
            }
        });

        resultFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add the selected result to the watch list, mark it as a favourite and change the imageviews
            }
        });

        resultWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add the selected result to the watch list, mark it as watched and change the imageviews
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
            TextView title = view.findViewById(R.id.resultViewTitle);
            TextView rating = view.findViewById(R.id.resultViewRating);


            // Set the layout elements to the item in the current position
            Movie result = getItem(position);


            title.setText(result.getTitle());
            String ratingString = result.getRating() + " / 10";
            rating.setText(ratingString);

            return view;
        }

        // getItem method



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
