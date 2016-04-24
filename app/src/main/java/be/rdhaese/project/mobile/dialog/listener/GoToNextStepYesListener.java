package be.rdhaese.project.mobile.dialog.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.LoadingInActivity;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.task.EndRoundTask;
import be.rdhaese.project.mobile.task.MarkAsLostTask;
import be.rdhaese.project.mobile.task.UpdateStateLoadingInTask;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class GoToNextStepYesListener implements DialogInterface.OnClickListener {

    private Activity activityContext;
    private Long roundId;
    private List<SearchPacketsPacketDTO> packets;

    public GoToNextStepYesListener(Activity activityContext, Long roundId, List<SearchPacketsPacketDTO> packets) {
        this.activityContext = activityContext;
        this.roundId = roundId;
        this.packets = packets;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //ArrayList implementation needed to be able to put it on an intent
        ArrayList<SearchPacketsPacketDTO> packetsThatAreLeft = new ArrayList<>();
        for (SearchPacketsPacketDTO packet : packets) {
            if (packet.getLost()) {
                //Let the back end remove the packet
                //from the round and mark it as lost
                try {
                    Boolean removed = new MarkAsLostTask().execute(roundId, packet).get();
                    String message = String.format(
                            "Result of backend trying to mark packet [%s] for round [%s] as lost: [%s]",
                            packet.getPacketId(), roundId, removed);
                    Log.d(getClass().getSimpleName(), message);
                } catch (InterruptedException | ExecutionException e) {
                    String message = String.format(
                            "Could not mark packet [%s] for round [%s] as lost",
                            packet.getPacketId(), roundId);
                    Log.e(getClass().getSimpleName(), message, e);
                }

            } else {
                packetsThatAreLeft.add(packet);
            }
        }

        if (packetsThatAreLeft.size() == 0) {
            //If no packets left -> the round ends
            try {
                //Let backend mark the round as ended
                Boolean roundEnded = new EndRoundTask().execute(roundId).get();
                String message = String.format(
                        "Result of backend trying to end round [%s]",
                        roundId);
                Log.d(getClass().getSimpleName(), message);
            } catch (InterruptedException | ExecutionException e) {
                String message = String.format(
                        "Could not end round [%s]",
                        roundId);
                Log.e(getClass().getSimpleName(), message, e);
            }

            //Go back to the start screen
            Intent intent = new Intent(activityContext, HomeScreenActivity.class);
            activityContext.startActivity(intent);
        } else { //The round can go on to the next screen
            new UpdateStateLoadingInTask().execute(roundId);
            //create intent with roundId and remaining packets
            Intent intent = new Intent(activityContext, LoadingInActivity.class);
            intent.putExtra("roundId", roundId);
            intent.putParcelableArrayListExtra("packets",
                    new ArrayList<>(
                            SearchPacketsPacketDTO.mapCollectionSearchPacketsToParcelableDTO(packetsThatAreLeft)));
            //show next activity
            activityContext.startActivity(intent);
        }
    }
}

