package com.colorcloud.p2pmesh;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by vyomkeshjha on 24/04/16.
 */
public class LocationManage {


    Context cont = null;

    LocationManage(Context context) {
        this.cont = context;
        getLocation();

    }
    public LatLng getLocation()
    {
        // Get the location manager
        LocationManager locationManager = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);
        Log.i("location",locationManager.toString());
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat,lon;
        try {
            lat = location.getLatitude ();
            lon = location.getLongitude ();
            Log.i("Location","lat ="+lat+"  lon="+lon);
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }

    }
    private static class LatLng{

        public static Double lat,lon;
        LatLng(Double lat,Double lon)
        {
            LatLng.lat=lat;
            LatLng.lon=lon;
        }
    }

}