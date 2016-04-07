package be.rdhaese.project.mobile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.task.GetNewRoundTask;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;


public class NumberOfPacketsFragment extends RoboFragment {

    private static final int MY_PERMISSIONS_REQUEST = 1;

    @InjectView(R.id.npNumberOfPackets)
    private NumberPicker npNumberOfPackets;
    @InjectView(R.id.btnStartRound)
    private Button btnStartRound;

    private void init() {
        npNumberOfPackets.setMinValue(1);
        npNumberOfPackets.setMaxValue(10);

        btnStartRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startRound(v);
                    //TODO handle exceptions
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_number_of_packets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRound();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //TODO WHat here
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startRound() {
        System.setProperty("http.keepAlive", "false");
        try {
            Integer amountOfPackets = npNumberOfPackets.getValue();
            Long roundId = new GetNewRoundTask().execute(amountOfPackets
            ).get();
            Intent intent = new Intent(this.getActivity(), SearchingPacketsActivity.class);
            intent.putExtra("roundId", roundId);
            startActivity(intent);
        } catch (InterruptedException e) {
            //TODO handle this
            e.printStackTrace();
        } catch (ExecutionException e) {
            //TODO handle this
            e.printStackTrace();
        }
    }

    public void startRound(View view) throws ExecutionException, InterruptedException {
        //TODO ask if sure; no turning back
        askPermissionIfNecessaryAndStartRound(view);
    }

    private void askPermissionIfNecessaryAndStartRound(View view) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.INTERNET)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // no explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //TODO What happens here???
            startRound();
        }
    }
}
