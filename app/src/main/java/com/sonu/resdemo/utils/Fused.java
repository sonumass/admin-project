package com.sonu.resdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Nitish on 7/28/2016.
 */

public class Fused implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Location location;
    Context context;
    public static String lat, lon;
    int tag;
    String TAG="Fused";
    protected Boolean mRequestingLocationUpdates=false;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public Fused(Context context, int tag) {
        this.context = context;
        this.tag=tag;
        lat = String.valueOf(28.5355);
        lon = String.valueOf(77.3910);

//            CropCuttingExperiment.update();
//
//
//            ClaimSurvey.update();
//
//
//            CropHealthSurvey.update();
//
//
//            LoneeFarmerActivity.update();
//
//
//            NonLoneeActivity.update();
        buildGoogleApiClient();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // Update location every second

        buildLocationSettingsRequest();

    }

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


        location = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (location != null) {
//            lat = String.valueOf(location.getLatitude());
//            lon = String.valueOf(location.getLongitude());

            lat = String.valueOf(28.5355);
            lon = String.valueOf(77.3910);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("jnk","lknljllk");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
//        lat = String.valueOf(location.getLatitude());
//        lon = String.valueOf(location.getLongitude());

        lat = String.valueOf(28.5355);
        lon = String.valueOf(77.3910);


//        if (CropCuttingExperiment.active)
//        CropCuttingExperiment.update();
//
//        if (ClaimSurvey.active)
//            ClaimSurvey.update();
//
//        if (CropHealthSurvey.active)
//            CropHealthSurvey.update();
//
//        if (LoneeFarmerActivity.active)
//            LoneeFarmerActivity.update();
//
//        if (NonLoneeActivity.active)
//            NonLoneeActivity.update();
    }

    void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context).
                addConnectionCallbacks(this).
                addApi(LocationServices.API).
                addOnConnectionFailedListener(this).build();

    }

   public void onStart() {
        googleApiClient.connect();
    }
    public void onResume() {
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (googleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }
    public void onPause()
    {
        if (googleApiClient.isConnected())
        stopLocationUpdates();
    }
  public  void onDestroy() {
        googleApiClient.disconnect();
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        mRequestingLocationUpdates = true;
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");

                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }
    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,   locationRequest,this);

    }
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
               // setButtonsEnabledState();
            }
        });
    }


    /**
     * Uses a {@link LocationSettingsRequest.Builder} to build
     * a {@link LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
        checkLocationSettings();
    }


    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }



}
