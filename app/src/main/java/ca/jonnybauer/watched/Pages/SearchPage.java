package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

    private OnFragmentInteractionListener mListener;

    public SearchPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_page, container, false);
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
            rating.setText(result.getRating());

            return view;
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
