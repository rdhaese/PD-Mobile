package be.rdhaese.project.mobile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.app_id.AppIdTool;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.DoNothingListener;
import be.rdhaese.project.mobile.task.GetNewRoundTask;
import be.rdhaese.project.mobile.task.UpdateStateRoundStartedTask;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;


public class NumberOfPacketsFragment extends RoboFragment {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final Long INVALID_ROUND = -1L;

    @InjectView(R.id.npNumberOfPackets)
    private NumberPicker npNumberOfPackets;
    @InjectView(R.id.btnStartRound)
    private Button btnStartRound;

    private DialogTool dialogTool;
    private ToastTool toastTool;
    private AppIdTool appIdTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean("dialogTool");
        toastTool = context.getBean("toastTool");
        appIdTool = context.getBean("appIdTool");
    }

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
                    //TODO ask if sure
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

            if (INVALID_ROUND.equals(roundId)) {
                //If returned roundId equals -1, it means there where no packets to start a round.

                //Show HomeScreenActiviy again with a message
                Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                String message = "No packets found to start a round. Please contact management.";
                intent.putExtra("message",message);
                startActivity(intent);
            } else {
                //A valid roundId is returned, we can go on to the next activity
                new UpdateStateRoundStartedTask().execute(appIdTool.getAppId(this.getContext()), roundId);
                Intent intent = new Intent(this.getActivity(), SearchingPacketsActivity.class);
                intent.putExtra("roundId", roundId);
                startActivity(intent);
            }
        } catch (InterruptedException e) {
            //TODO handle this
            e.printStackTrace();
        } catch (ExecutionException e) {
            //TODO handle this
            e.printStackTrace();
        }
    }

    public void startRound(final View view) throws ExecutionException, InterruptedException {
        //Ask if the courier is sure, no turning back afterwards
        //Prepare the dialog
        String title = "No Turning Back";
        String message = "Are you sure you want to start the round? There is no turning back after! You'll have to finish the round.";
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                askPermissionIfNecessaryAndStartRound(view);
            }
        };
        DialogInterface.OnClickListener noListener = new DoNothingListener();
        //Show dialog
        dialogTool.yesNoDialog(
                getActivity(),
                title,
                message,
                yesListener,
                noListener)
                .show();
    }

    private void askPermissionIfNecessaryAndStartRound(View view) {
        if (ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            //TODO
            //SDK23 related check
            //Figure out a way to force the user to pick yes
            //Will probably give troubles
            //If no, the application should maybe suspend and the round terminated...
        } else {
            startRound();
        }
    }
}
