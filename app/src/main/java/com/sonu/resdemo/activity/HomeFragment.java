package com.sonu.resdemo.activity;

/**
 * Created by Sonu Sain 5/26/2017
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.sonu.resdemo.R;
import com.sonu.resdemo.model.MarkerData;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    ArrayList<MarkerData> markersArray = new ArrayList<MarkerData>();
    MapView mMapView;
    private GoogleMap googleMap;
    MarkerData merker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        merker=new MarkerData();
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        for (int i=0;i<=2;i++){
            if(i==0){
                merker.setLatitude(28.5936);
                merker.setLongitude(77.3188);
                merker.setTitile("titile1");

            }else if(i==1){
                merker.setLatitude(28.5703);
                merker.setLongitude(77.3218);
                merker.setTitile("Title2");
            }else if(i==2){
                merker.setLatitude(28.5849);
                merker.setLongitude(77.3118);
                merker.setTitile("Title3");
            }


        }
        markersArray.add(merker);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(28.5849,77.3118);

              //  googleMap.setInfoWindowAdapter(new BalloonAdapter(getActivity().getLayoutInflater()));
              //  googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                for(int i = 0 ; i < markersArray.size() ; i++ ) {

                    // createMarker(markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getTitle(), markersArray.get(i).getSnippet(), markersArray.get(i).getIconResID());
                    createMarker(markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getTitile());
                }
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override public void onInfoWindowClick(Marker marker) {
                        //do your thing here

                        Toast.makeText(getActivity(),marker.getTitle(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return rootView;
    }
    // protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
    protected Marker createMarker(double latitude, double longitude, String title) {
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                // .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}