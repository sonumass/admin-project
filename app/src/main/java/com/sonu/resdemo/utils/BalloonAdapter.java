package com.sonu.resdemo.utils;

/**
 * Created by Sonu Saini on 5/26/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class BalloonAdapter implements InfoWindowAdapter {
    LayoutInflater inflater = null;
    private TextView textViewTitle;
Context contex;
    public BalloonAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View v = inflater.inflate(R.layout.infowindow, null);
        if (marker != null) {
            textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            textViewTitle.setText(marker.getTitle()+"sonu");
            textViewTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("type","text");
                    //Toast.makeText(contex,marker.get,Toast.LENGTH_LONG).show();
                }
            });
        }
        return (v);
    }



    @Override
    public View getInfoContents(Marker marker) {
        return (null);
    }
}

