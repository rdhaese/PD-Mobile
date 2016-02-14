package be.rdhaese.project.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.NumberPicker;

import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.task.GetNewRoundTask;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_number_of_packets)
public class NumberOfPacketsActivity extends RoboActivity {

    private static final int MY_PERMISSIONS_REQUEST = 1;

    @InjectView(R.id.npNumberOfPackets)
    private NumberPicker npNumberOfPackets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        npNumberOfPackets.setMinValue(1);
        npNumberOfPackets.setMaxValue(10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    blabla();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    System.out.println("NO PERMISSION GRANTED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void blabla() {
        System.setProperty("http.keepAlive", "false");
        System.out.println("BEFORE EXECUTING REMOTE CALL");
        try {
            System.out.println(new GetNewRoundTask().execute(1).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void startRound(View view) throws ExecutionException, InterruptedException {
        //askPermissionIfNecessaryAndStartRound();
        Intent intent = new Intent(this, SearchingPacketsActivity.class);
        startActivity(intent);
    }

    private void askPermissionIfNecessaryAndStartRound() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // no explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            blabla();
        }
    }
}
