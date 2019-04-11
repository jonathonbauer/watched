package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviePopUp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviePopUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviePopUp extends Fragment {
    private static final String ARG_MOVIE = "movie";

    private Movie mMovie;
    private Movie watchListMovie;

    private DBHelper dbHelper;

    private TextView title;
    private TextView releaseDate;
    private SimpleRatingBar rating;
    private ImageView poster;
    private TextView credits;
    private TextView plot;
    private ImageView favourite;
    private ImageView watched;
    private ImageView add;


    private OnFragmentInteractionListener mListener;

    public MoviePopUp() {
        // Required empty public constructor
    }

    public static MoviePopUp newInstance(Movie movie) {
        MoviePopUp fragment = new MoviePopUp();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_pop_up, container, false);



        // Create the DB connection
        dbHelper = new DBHelper(getContext());

        // Get the XML elements
        title = view.findViewById(R.id.moviePopUpTitle);
        releaseDate = view.findViewById(R.id.moviePopUpReleaseDate);
        rating = view.findViewById(R.id.moviePopUpRating);
        poster = view.findViewById(R.id.moviePopUpPoster);
        credits = view.findViewById(R.id.moviePopUpTopBilling);
        plot = view.findViewById(R.id.moviePopUpPlot);
        favourite = view.findViewById(R.id.moviePopUpFavourite);
        watched = view.findViewById(R.id.moviePopUpWatched);
        add = view.findViewById(R.id.moviePopUpAdd);

        // Set the appropriate text values to the XML
        title.setText(mMovie.getTitle());
        double ratingValue = (mMovie.getRating() / 10.0) * 5.0;

        Calendar date = Calendar.getInstance();
        date.setTime(mMovie.getReleaseDate());
        String dateString = String.format("%1$tb %1$te, %1$tY", date);
        releaseDate.setText(dateString);


        rating.setRating((float) ratingValue);
        rating.setIndicator(true);
        credits.setText(mMovie.getTopBilling());
        plot.setText(mMovie.getPlot());
        Picasso.get().load(mMovie.getPosterPath()).placeholder(R.drawable.noimagefound).into(poster);

        changeButtons();

        // ImageView Button event handlers

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(watchListMovie == null) {
                    WatchListTable.getInstance().addMovie(mMovie, dbHelper);
                    add.setImageResource(R.drawable.ic_add_circle_black_24dp);
                } else {
                    if(watchListMovie.getDeleted() == 1) {
                        watchListMovie.setDeleted(0);
                        WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                        add.setImageResource(R.drawable.ic_add_circle_black_24dp);
                        if(watchListMovie.getFavourite() == 1) {
                            favourite.setImageResource(R.drawable.ic_star_black_24dp);
                        }
                        if(watchListMovie.getWatched() == 1) {
                            watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        }
                    } else {
                        watchListMovie.setDeleted(1);
                        WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                        add.setImageResource(R.drawable.ic_add_black_24dp);
                        favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                        watched.setImageResource(R.drawable.ic_check_black_24dp);
                    }

                }
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(watchListMovie == null) {
                    WatchListTable.getInstance().addMovie(mMovie, dbHelper);
                    add.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    favourite.setImageResource(R.drawable.ic_star_black_24dp);
                } else {
                    if(watchListMovie.getFavourite() == 0) {
                        watchListMovie.setFavourite(1);
                        WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                        favourite.setImageResource(R.drawable.ic_star_black_24dp);
                    } else {
                        watchListMovie.setFavourite(0);
                        WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                        favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                    }
                }
            }
        });

        watched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(watchListMovie == null) {
                    WatchListTable.getInstance().addMovie(mMovie, dbHelper);
                    add.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                } else {
                    if(watchListMovie.getWatched() == 0) {
                        watchListMovie.setWatched(1);
                        WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                        watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    } else {
                        watchListMovie.setWatched(0);
                        WatchListTable.getInstance().updateMovie(watchListMovie, dbHelper);
                        watched.setImageResource(R.drawable.ic_check_black_24dp);
                    }
                }
            }
        });
        return view;
    }


    public void changeButtons(){
        // Change the ImageView buttons depending on whether or not the movie has been watched, added, or favourited

        watchListMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, mMovie.getTmdbID());
        if(watchListMovie != null && watchListMovie.getDeleted() != 1) {
            add.setImageResource(R.drawable.ic_add_circle_black_24dp);
            if(watchListMovie.getFavourite() == 1) {
                favourite.setImageResource(R.drawable.ic_star_black_24dp);
            }
            if(watchListMovie.getWatched() == 1) {
                watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
            }
        } else {
            add.setImageResource(R.drawable.ic_add_black_24dp);
            favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
            watched.setImageResource(R.drawable.ic_check_black_24dp);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        changeButtons();
        getActivity().setTitle(watchListMovie.getTitle());

    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
