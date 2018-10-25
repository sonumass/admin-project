package com.sonu.resdemo.activity;

import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sonu.resdemo.R;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sonu.resdemo.R;
import com.sonu.resdemo.maproot.DrawMarker;
import com.sonu.resdemo.maproot.DrawRouteMaps;
import com.sonu.resdemo.utils.DirectionsJSONParser;
public class RestaurantLocationActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

  private GoogleMap mMap;
  ArrayList<LatLng> mMarkerPoints;
  double mLatitude = 0;
  double mLongitude = 0;
  Location location;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurant_location);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
      .findFragmentById(R.id.map);
    mapFragment.getMapAsync(RestaurantLocationActivity.this);

    mMarkerPoints = new ArrayList<LatLng>();

    // Getting LocationManager object from System Service LOCATION_SERVICE


  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap = googleMap;
    LatLng origin = new LatLng(28.591150, 77.318970);
    LatLng destination = new LatLng(28.591207, 77.325276);
    DrawRouteMaps.getInstance(this)
      .draw(origin, destination, mMap);
    DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.blue_plus, "Origin Location");
    DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.minus, "Destination Location");

    LatLngBounds bounds = new LatLngBounds.Builder()
      .include(origin)
      .include(destination).build();
    Point displaySize = new Point();
    getWindowManager().getDefaultDisplay().getSize(displaySize);
    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));

  }

  @Override
  public void onLocationChanged(Location location) {

  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
}
