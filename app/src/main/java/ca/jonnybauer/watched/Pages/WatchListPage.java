package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    private Boolean showWatchedMovies;
    private View view;
    private int spanCount;

    public WatchListPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_watch_list_page, container, false);

        // Get the Database
        dbHelper = new DBHelper(getContext());

        // Get the Recycler View
        recyclerView = view.findViewById(R.id.watchListRV);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        watchListStyle = preferences.getString("watch_list_style", "1");
        showWatchedMovies = preferences.getBoolean("show_watched", true);


        // Get the Watch List

        if(!showWatchedMovies) {
            watchList = null;
            watchList = WatchListTable.getInstance().filterWatchedAndDeletedMovies(WatchListTable.getInstance().getAllMovies(dbHelper));
        } else {
            watchList = null;
            watchList = WatchListTable.getInstance().filterDeletedMovies(WatchListTable.getInstance().getAllMovies(dbHelper));
        }

        if(watchListStyle.equals("1")) {
            // Create the adapter
            adapter = new WatchListAdapter(watchList, getContext(), WatchListStyle.POSTER);

            setGridSpan();

            StaggeredGridLayoutManager layoutManager;


            layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);

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
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedItem = (TextView) parent.getChildAt(0);
                if(selectedItem != null) {
                    selectedItem.setTextColor(getResources().getColor(R.color.mainFontColor));
                }

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

    public void setGridSpan(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        float dpiX = screenSize.x / getResources().getDisplayMetrics().density;

        System.out.println(dpiX);
        if(dpiX > 450 && dpiX < 680) {
            System.out.println("Span count is " + spanCount);
            spanCount = 3;
        } else if(dpiX >= 680 && dpiX < 1000){
            System.out.println("Span count is " + spanCount);
            spanCount = 4;
        } else if(dpiX > 1000) {
            System.out.println("Span count is " + spanCount);
            spanCount = 7;
        } else {
            System.out.println("Span count is " + spanCount);
            spanCount = 2;
        }

//        if(screenSize.x > 1440) {
////            System.out.println("Screen size is larger: " + screenSize.x);
//            spanCount = 3;
//
//        } else {
////            System.out.println("Screen size is smaller: " + screenSize.x);
//            spanCount = 2;
//        }
    }

    public void refreshItems(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        watchListStyle = preferences.getString("watch_list_style", "1");
        showWatchedMovies = preferences.getBoolean("show_watched", true);


        // Get the Watch List

        if(!showWatchedMovies) {
            watchList = null;
            watchList = WatchListTable.getInstance().filterWatchedAndDeletedMovies(WatchListTable.getInstance().getAllMovies(dbHelper));
        } else {
            watchList = null;
            watchList = WatchListTable.getInstance().filterDeletedMovies(WatchListTable.getInstance().getAllMovies(dbHelper));
        }

        if(watchListStyle.equals("1")) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        float dpiY = screenSize.y / getResources().getDisplayMetrics().density;

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

//        getActivity().getActionBar().hide();
//        System.out.println("Config changed, " )
        if(dpiY < 500) {
            System.out.println("hiding bar");
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        } else if(!((AppCompatActivity)getActivity()).getSupportActionBar().isShowing()){
            System.out.println("showing bar");
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }

        setGridSpan();
        refreshItems();

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.watch_list_page_title));
        refreshItems();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        float dpiY = screenSize.y / getResources().getDisplayMetrics().density;

        if(dpiY < 500) {
            System.out.println("hiding bar");
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        } else if(!((AppCompatActivity)getActivity()).getSupportActionBar().isShowing()){
            System.out.println("showing bar");
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }

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
    public void onStop() {
        super.onStop();
        System.out.println("Stopping");
        if(!((AppCompatActivity)getActivity()).getSupportActionBar().isShowing()){
            System.out.println("showing bar");
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
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
