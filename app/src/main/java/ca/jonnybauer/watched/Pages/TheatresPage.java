package ca.jonnybauer.watched.Pages;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import ca.jonnybauer.watched.Adapters.TheatreAdapter;
import ca.jonnybauer.watched.Helpers.GoogleAPIHelper;
import ca.jonnybauer.watched.Helpers.MovieAPIHelper;
import ca.jonnybauer.watched.Models.Theatre;
import ca.jonnybauer.watched.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TheatresPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TheatresPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TheatresPage extends Fragment{

    private OnFragmentInteractionListener mListener;

    private MapView mapView;
    private GoogleMap map;
    private ArrayList<Theatre> theatres;
    private ArrayList<Theatre> completeTheatres;
    private ArrayList<Marker> markers;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private LocationManager locationManager;
    private TheatreAdapter adapter;
    private Location location;
    private double userLat;
    private double userLng;
    private View view;
    private LayoutInflater inflater;
    private ViewGroup container;


    public TheatresPage() {
        // Required empty public constructor
    }

    public static TheatresPage newInstance() {
        TheatresPage fragment = new TheatresPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_theatres_page, container, false);
        this.inflater = inflater;
        this.container = container;


        theatres = new ArrayList<>();
        completeTheatres = new ArrayList<>();
        markers = new ArrayList<>();


        // Get the user location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Get the users location and nearby theatres

        mapView = view.findViewById(R.id.theatreMV);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

//        requestLocation();

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }





        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                findNearbyTheatres();
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.theatreFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                    map.setMyLocationEnabled(true);
                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                    findNearbyTheatres();
                } else {
                    requestLocation();
                }
            }
        });



        // Get the recyclerview and display the theatres on it

        recyclerView = view.findViewById(R.id.theatreRV);

        adapter = new TheatreAdapter(theatres, markers, map, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionsResult");
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
            findNearbyTheatres();
        }

    }

    public void requestLocation(){
        // Request permission to use the users location
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // If we have asked the user before, display a dialog explaining the reasoning then ask again
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle("Location Services");
                dialog.setMessage("Watched requires access to your location to display nearby theatres.");
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.out.println("Asking for permission again");
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                });
                dialog.show();
            } else {
                System.out.println("Asking for permission for the first time");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
//        System.out.println("Invalidating map");
//        mapView.invalidate();

    }

    public void findNearbyTheatres(){

        // Get the preferences file to find out if the user has asked to save their location
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if(preferences.getBoolean("save_location", true)) {

            System.out.println("User enabled save location");
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                if(preferences.getLong("user_lat", 0) == 0) {
                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                    preferences.edit().putLong("user_lat", Double.doubleToRawLongBits(location.getLatitude())).apply();
                    preferences.edit().putLong("user_lng", Double.doubleToRawLongBits(location.getLongitude())).apply();
                    System.out.println("New Location saved: " + preferences.getAll());
                    map.setMyLocationEnabled(true);
                    Toast toast = Toast.makeText(getContext(), "Getting location for the first time.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    location = new Location("");
                    location.setLatitude(Double.longBitsToDouble(preferences.getLong("user_lat",0)));
                    location.setLongitude(Double.longBitsToDouble(preferences.getLong("user_lng", 0)));
                    map.setMyLocationEnabled(false);
                }

            } else {
                requestLocation();
            }
        } else {
            System.out.println("User disabled save location");
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
            if(preferences.getLong("user_lat", 0) != 0) {
                preferences.edit().putLong("user_lat", 0).apply();
                preferences.edit().putLong("user_lng", 0).apply();
                map.setMyLocationEnabled(true);
            }
            preferences.getLong("user_lat", 0);

        }


        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(10).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            userLat = location.getLatitude();
            userLng = location.getLongitude();

            GoogleAPIHelper.getInstance().getNearbyTheatres(userLat, userLng, getContext(), new GoogleAPIHelper.RequestListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    theatres.clear();
                    completeTheatres.clear();
                    theatres = GoogleAPIHelper.getInstance().parseNearbyTheatres(response);
                    System.out.println("Found " + theatres.size() + " theatres nearby");

                    for (int i = 0; i < theatres.size(); i++) {
                        final Theatre currentTheatre = theatres.get(i);
                        GoogleAPIHelper.getInstance().getTheatre(currentTheatre.getPlacesID(), getContext(), new GoogleAPIHelper.RequestListener() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                completeTheatres.add(GoogleAPIHelper.getInstance().parseTheatre(response));
                                adapter.notifyDataSetChanged();
                            }
                        });


                    }
                    System.out.println(theatres.size() + " theatres found");
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;

                            markers.clear();
                            adapter.setMarkers(markers);
                            adapter.notifyDataSetChanged();

                            for (int i = 0; i < theatres.size(); i++) {
                                LatLng coordinates = new LatLng(theatres.get(i).getLatitude(), theatres.get(i).getLongitude());
                                markers.add(googleMap.addMarker(new MarkerOptions().position(coordinates).title(theatres.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));

                            }


                            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    System.out.println("Pin tapped");
                                    System.out.println("Number of markers: " + markers.size());
                                    for (int i = 0; i < markers.size(); i++) {
                                        if (marker.equals(markers.get(i))) {
                                            System.out.println("Tapped on: " + completeTheatres.get(i).getName());
                                            completeTheatres.get(i).setFavourite(1);
                                            markers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                            manager.scrollToPositionWithOffset(i, 5);
                                        } else {
                                            System.out.println("No match");
                                            completeTheatres.get(i).setFavourite(0);
                                            markers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    return false;
                                }
                            });


                        }
                    });

                    adapter = null;
                    adapter = new TheatreAdapter(completeTheatres, markers, map, getContext());
                    manager = new LinearLayoutManager(getContext());
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                }
            });

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this);
        transaction.attach(this);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.theatres_page_title));
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
