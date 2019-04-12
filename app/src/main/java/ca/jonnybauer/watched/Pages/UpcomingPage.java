package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;

import ca.jonnybauer.watched.Adapters.UpcomingAdapter;
import ca.jonnybauer.watched.Adapters.WatchListAdapter;
import ca.jonnybauer.watched.Helpers.MovieAPIHelper;
import ca.jonnybauer.watched.Helpers.MovieSort;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.Models.WatchListStyle;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpcomingPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpcomingPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingPage extends Fragment {
    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    UpcomingAdapter adapter;
    ProgressBar progressBar;
    ArrayList<Movie> upcomingMovies;
    ArrayList<Movie> sortedMovies;

    public UpcomingPage() {
        // Required empty public constructor
    }

    public static UpcomingPage newInstance() {
        UpcomingPage fragment = new UpcomingPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_page, container, false);

        recyclerView = view.findViewById(R.id.upcomingRV);
        progressBar = view.findViewById(R.id.upcomingProgress);
        getUpcomingMovies();
        return view;
    }

    public void getUpcomingMovies(){
        MovieAPIHelper.getInstance().getUpcoming(getContext(), new MovieAPIHelper.RequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                upcomingMovies = MovieAPIHelper.getInstance().parseMovies(response);
                sortedMovies = MovieSort.sortByDate(upcomingMovies, 1);
                for(int i=0; i< sortedMovies.size(); i++) {
                    final int index = i;
                    MovieAPIHelper.getInstance().getCredits(sortedMovies.get(index).getTmdbID(), getContext(), new MovieAPIHelper.RequestListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            ArrayList<String> credits = MovieAPIHelper.getInstance().parseCredits(response);
                            sortedMovies.get(index).setCredits(credits);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                adapter = new UpcomingAdapter(sortedMovies, getContext());
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.upcoming_page_title));
//        getUpcomingMovies();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
