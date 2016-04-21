package be.rdhaese.project.mobile.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.dto.LongLatDTO;
import be.rdhaese.project.mobile.task.AddLocationUpdateTask;

/**
 * Created by RDEAX37 on 10/04/2016.
 */
public class LocationUpdateService extends Service {

    private static final Long INTERVAL = new Long(5 * 60 * 1000); //5 minutes in milliseconds
    //private static final Long INTERVAL = 1000L;
    private Long roundId;

    private LocationManager locMan;
    private Boolean locationChanged;
    private Handler handler = new Handler();

    public static Location curLocation;
    public static boolean isService = true;

    LocationListener gpsListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (curLocation == null) {
                curLocation = location;
                locationChanged = true;
            } else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()) {
                locationChanged = false;
                return;
            } else
                locationChanged = true;

            curLocation = location;

            if (locationChanged)
                if (ActivityCompat.checkSelfPermission(LocationUpdateService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationUpdateService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Do nothing, permission is checked in HomeScreenActivity
                    //TODO permission checks in HomeScreenActivity
                }
            locMan.removeUpdates(gpsListener);

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == 0)// UnAvailable
            {
            } else if (status == 1)// Trying to Connect
            {
            } else if (status == 2) {// Available
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();

        curLocation = getBestLocation();

        if (curLocation == null) {
            Toast.makeText(getBaseContext(), "Unable to get your location", Toast.LENGTH_SHORT).show();
        }

        isService = true;
    }

    final String TAG = getClass().getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        roundId = intent.getLongExtra("roundId", -1L);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onStart(Intent i, int startId) {
        handler.postDelayed(GpsFinder, INTERVAL);
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(GpsFinder);
        handler = null;
        isService = false;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public Runnable GpsFinder = new Runnable() {
        public void run() {
            Location tempLoc = getBestLocation();
            if (tempLoc != null) {
                curLocation = tempLoc;
                LongLatDTO longLatDTO = new LongLatDTO();
                longLatDTO.setLongitude(curLocation.getLongitude());
                longLatDTO.setLatitude(curLocation.getLatitude());

                try {
                    new AddLocationUpdateTask().execute(roundId, longLatDTO).get();
                } catch (InterruptedException e) {
                    //TODO handle exception
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    //TODO handle exception
                    e.printStackTrace();
                }
            }
            tempLoc = null;
            handler.postDelayed(GpsFinder, INTERVAL);// register again to start after 1 seconds...
        }
    };

    private Location getBestLocation() {
        Location gpslocation = null;
        Location networkLocation = null;

        if (locMan == null) {
            locMan = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Do nothing, permission is checked in HomeScreenActivity
                    //TODO permission checks in HomeScreenActivity
                }
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, 1, gpsListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
                gpslocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
            if (locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, 1, gpsListener);
                networkLocation = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (IllegalArgumentException e) {
            //Log.e(ErrorCode.ILLEGALARGUMENTERROR, e.toString());
            Log.e("error", e.toString());
        }
        if (gpslocation == null && networkLocation == null)
            return null;

        if (gpslocation != null && networkLocation != null) {
            if (gpslocation.getTime() < networkLocation.getTime()) {
                gpslocation = null;
                return networkLocation;
            } else {
                networkLocation = null;
                return gpslocation;
            }
        }
        if (gpslocation == null) {
            return networkLocation;
        }
        if (networkLocation == null) {
            return gpslocation;
        }
        return null;
    }
}