package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import ca.jonnybauer.watched.Adapters.WatchListAdapter;
import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Helpers.MovieSort;
import ca.jonnybauer.watched.Models.Config;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.Models.WatchListStyle;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.ConfigTable;
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
    private Spinner spinner;
    private String watchListStyle;

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
        watchList = WatchListTable.getInstance().filterDeletedMovies(WatchListTable.getInstance().getAllMovies(dbHelper));


        // TODO: Figure out which layout the user has set as preferred
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        watchListStyle = preferences.getString("watch_list_style", "1");

        if(watchListStyle.equals("1")) {
            // Create the adapter
            adapter = new WatchListAdapter(watchList, getContext(), WatchListStyle.POSTER);

            // Create the layout manager
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            // Pair the recyclerview with the layout manager and adapter
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            // Create the adapter
            adapter = new WatchListAdapter(watchList, getContext(), WatchListStyle.LIST);

            // Create the layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Pair the recyclerview with the layout manager and adapter
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        // Sort By Spinner
        spinner = view.findViewById(R.id.watchListFilterSpinner);


        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_options, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = spinner.getSelectedItem().toString();

                if(selection.equals("Date Added (Ascending)")){
                    adapter.setWatchList(MovieSort.sortByDateAdded(watchList, 0));
                    adapter.notifyDataSetChanged();
                } else if(selection.equals("Date Added (Descending)")) {
                    adapter.setWatchList(MovieSort.sortByDateAdded(watchList, 1));
                    adapter.notifyDataSetChanged();
                } else if(selection.equals("Title (Ascending)")){
                    adapter.setWatchList(MovieSort.sortByTitle(watchList, 0));
                    adapter.notifyDataSetChanged();
                } else if(selection.equals("Title (Descending)")) {
                    adapter.setWatchList(MovieSort.sortByTitle(watchList, 1));
                    adapter.notifyDataSetChanged();
                } else if(selection.equals("Release Date (Ascending)")){
                    adapter.setWatchList(MovieSort.sortByDate(watchList, 0));
                    adapter.notifyDataSetChanged();
                } else if(selection.equals("Release Date (Descending)")){
                    adapter.setWatchList(MovieSort.sortByDate(watchList, 1));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void refreshItems(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        watchListStyle = preferences.getString("watch_list_style", "1");
        watchList = WatchListTable.getInstance().filterDeletedMovies(WatchListTable.getInstance().getAllMovies(dbHelper));
        if(watchListStyle.equals("1")) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new WatchListAdapter(watchList, getContext(), WatchListStyle.POSTER);
        } else {
            // Create the layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new WatchListAdapter(watchList, getContext(), WatchListStyle.LIST);
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
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
