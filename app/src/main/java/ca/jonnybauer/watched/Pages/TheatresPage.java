package ca.jonnybauer.watched.Pages;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    LinearLayoutManager manager;
    TheatreAdapter adapter;
    private double userLat;
    private double userLng;


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
        View view = inflater.inflate(R.layout.fragment_theatres_page, container, false);

        theatres = new ArrayList<>();
        completeTheatres = new ArrayList<>();
        markers = new ArrayList<>();


        // Get the user location
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1 );

        // Get the users location and nearby theatres

        mapView = view.findViewById(R.id.theatreMV);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();


        try {
            MapsInitializer.initialize(getContext());
            System.out.println("Map Created");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }

                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                if (location != null)
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(10).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    userLat = location.getLatitude();
                    userLng = location.getLongitude();

                    System.out.println("Location changed: " + userLat + userLng);

                    GoogleAPIHelper.getInstance().getNearbyTheatres(userLat, userLng, getContext(), new GoogleAPIHelper.RequestListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            theatres = GoogleAPIHelper.getInstance().parseNearbyTheatres(response);
                            System.out.println("Found " + theatres.size() + " theatres nearby");

                            for(int i=0; i< theatres.size(); i++) {
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
                                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        map.setMyLocationEnabled(true);
                                    }
                                    for(int i=0; i < theatres.size(); i++) {
                                        LatLng coordinates = new LatLng(theatres.get(i).getLatitude(), theatres.get(i).getLongitude());
                                        markers.add(googleMap.addMarker(new MarkerOptions().position(coordinates).title(theatres.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));

                                    }

                                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            System.out.println("Pin tapped");
                                            System.out.println("Number of markers: " + markers.size());
                                            for(int i=0; i< markers.size(); i++) {
                                                if(marker.equals(markers.get(i))) {
                                                    System.out.println("Tapped on: " + completeTheatres.get(i).getName());
                                                    completeTheatres.get(i).setFavourite(1);
                                                    markers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                                    manager.scrollToPositionWithOffset(i, 5);
                                                } else {
                                                    System.out.println("No match");
                                                    completeTheatres.get(i).setFavourite(0);
                                                    markers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
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
