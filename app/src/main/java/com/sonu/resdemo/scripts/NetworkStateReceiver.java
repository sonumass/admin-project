package com.sonu.resdemo.scripts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sonu.resdemo.utils.CommonFunctions;

/**
 * Created by sonu saini on 6/6/17.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    // commonFunctions;
    public void onReceive(Context context, Intent intent) {
        Log.d("app", "Network connectivity change");
        if(intent.getExtras()!=null) {
            NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni!=null && ni.getState()== NetworkInfo.State.CONNECTED) {
                if (CommonFunctions.isConnected(context))
                {
                Log.i("app","Network "+ni.getTypeName()+" connected");
                //Intent service1 = new Intent(context, OfflineService.class);
              //  context.startService(service1);
                }
            }
        }
        if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Log.d("app","There's no network connectivity");

        }
    }
}