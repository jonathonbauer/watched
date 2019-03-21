package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.jonnybauer.watched.Adapters.WatchListAdapter;
import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.Models.WatchListStyle;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WatchListPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class WatchListPage extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private WatchListAdapter adapter;
    private ArrayList<Movie> watchList;
    private DBHelper dbHelper;

    public WatchListPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_list_page, container, false);

        // Get the Database
        dbHelper = new DBHelper(getContext());

        // Get the Recycler View
        recyclerView = view.findViewById(R.id.watchListRV);

        // Get the Watch List
        watchList = WatchListTable.getInstance().getAllMovies(dbHelper);

        // TODO: Figure out which layout the user has set as preferred

        // Create the adapter
        adapter = new WatchListAdapter(watchList, getContext(), WatchListStyle.POSTER);

        // Create the layout manager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // Pair the recyclerview with the layout manager and adapter
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
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
