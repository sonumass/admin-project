package com.sonu.resdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.sonu.resdemo.R;
import com.sonu.resdemo.scripts.Scripts;
import com.sonu.resdemo.utils.AppLocationService;
import com.sonu.resdemo.utils.CommonFunctions;
import com.sonu.resdemo.utils.LocationAddress;
import com.sonu.resdemo.utils.Preferences;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import static com.sonu.resdemo.utils.Fused.REQUEST_CHECK_SETTINGS;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    Preferences pref;
    String name = "", mobile = "", mobileverifiecode="",user_address="",imei="",email="",datetime="";
TextView txt_confirm;
EditText edt_mobile,edt_name,edt_adddress,edt_email;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 90000 * 10;
    private static final long FASTEST_INTERVAL = 90000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationAddress location;
    AppLocationService appLocationService;
   double longtitude;
   double latitude;
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
      //mLocationRequest.setInterval(INTERVAL);
        //  mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        builder.setNeedBle(false);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final com.google.android.gms.common.api.Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:


                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try
                        {
                            // Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                            status.startResolutionForResult(ConfirmOrderActivity.this,100);
                        } catch (IntentSender.SendIntentException e) {}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Confirm Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        init();
    }
    public  void api(){
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();
        updateUI();

    }
    public  void init(){
        pref=new Preferences(ConfirmOrderActivity.this);
     LinearLayout activity_main=(LinearLayout)findViewById(R.id.activity_main);
        txt_confirm=(TextView)findViewById(R.id.txt_confirm);
        txt_confirm.setOnClickListener(this);
        edt_name=(EditText)findViewById(R.id.edt_name);
        edt_mobile=(EditText)findViewById(R.id.edt_mobile_no);
        edt_adddress=(EditText)findViewById(R.id.edt_address);
        edt_email=(EditText)findViewById(R.id.edt_email);
        if(pref.getString("edit")=="1"){
            edt_name.setText(pref.getString("name"));
            edt_mobile.setText(pref.getString("mobile"));
            edt_adddress.setText(pref.getString("user_address"));
            edt_email.setText(pref.getString("email"));

        }

        appLocationService = new AppLocationService(ConfirmOrderActivity.this);
        location =new LocationAddress();
        activity_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftKeyboard(ConfirmOrderActivity.this);
                return true;
            }
        });
        api();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        updateUI();
                        break;
                    case RESULT_CANCELED:
Intent intent=new Intent(ConfirmOrderActivity.this,ConfirmOrderActivity.class);
startActivity(intent);
finish();
                        break;
                }
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }
    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            longtitude=mCurrentLocation.getLongitude();
            latitude=mCurrentLocation.getLatitude();

                location.getAddressFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
                        getApplicationContext(), new GeocoderHandler());



        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            edt_adddress.setText(locationAddress);
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 100).show();
            return false;
        }
    }
    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }
    @Override
    public void onBackPressed() {

        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Toast.makeText(UserDetails.this, "1", Toast.LENGTH_SHORT).show();

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_confirm:
                if(CommonFunctions.isEmpty(edt_name)){
                    edt_name.setError("Please enter name");
                    edt_name.requestFocus();
                }else if(CommonFunctions.isEmpty(edt_mobile)){
                    edt_mobile.setError("Please enter mobile no");
                    edt_mobile.requestFocus();
                }else if(CommonFunctions.isEmpty(edt_email)){
                    edt_email.setError("Please enter email");
                    edt_email.requestFocus();
                }else if(CommonFunctions.isEmpty(edt_adddress)){
                    edt_adddress.setError("Please enter address");
                    edt_adddress.requestFocus();
                }else if(edt_mobile.getText().toString().length()<10){
                    edt_mobile.setError("Please enter valid  mobile no");
                    edt_mobile.requestFocus();
                }else if(!CommonFunctions.emailValidator(edt_email.getText().toString().trim())){
                    edt_email.setError("Please enter valid  email");
                    edt_email.requestFocus();
                }else {
                    if(CommonFunctions.isConnected(ConfirmOrderActivity.this)){
                        name=edt_name.getText().toString();
                        mobile=edt_mobile.getText().toString();
                        email=edt_email.getText().toString();
                        user_address=edt_adddress.getText().toString();
                        imei= CommonFunctions.getDeviceIMEI(ConfirmOrderActivity.this);
                        datetime=CommonFunctions.getDateTime();
                        mobileverifiecode="123456";
                        pref.storeString("address",user_address);



new UsersignUpAsynTask().execute();
                    }else {
                        Toast.makeText(ConfirmOrderActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }
    class UsersignUpAsynTask extends AsyncTask<String, Void, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ConfirmOrderActivity.this);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Please wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            try{
                result= Scripts.UserSignup(name,mobile,mobileverifiecode,user_address,imei,email,datetime);
            }catch (Exception e){
                Log.e("error","errroe");
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("script rasponse", s);
            progress.cancel();
            try{

                JSONObject jsonObject=new JSONObject(s);


                String str=jsonObject.getString("success");
                switch (str){
                    case "1":
                        name=edt_name.getText().toString();
                        mobile=edt_mobile.getText().toString();
                        email=edt_email.getText().toString();
                        user_address=edt_adddress.getText().toString();
                        pref.storeString("name",edt_name.getText().toString());
                        pref.storeString("mobile",edt_mobile.getText().toString());
                        pref.storeString("email",edt_email.getText().toString());
                        pref.storeString("user_address",edt_adddress.getText().toString());
                       Intent intent =new Intent(ConfirmOrderActivity.this,MobileValidationActivity.class);
                       intent.putExtra("code",jsonObject.getString("verifiedcode"));
                        intent.putExtra("user_id",mobile);
                        pref.storeString("mobile",mobile);

                       startActivity(intent);
                        pref.storeString("edit","0");
                       finish();
                        break;
                    case "0":
                        Toast.makeText(ConfirmOrderActivity.this,"Somthing want wrong!", Toast.LENGTH_LONG).show();
                        break;
                }

            }catch (Exception e){
                Log.e("error","error in branch uploaded data");
            }


        }

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
