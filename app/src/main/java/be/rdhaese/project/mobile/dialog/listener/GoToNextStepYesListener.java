package be.rdhaese.project.mobile.dialog.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.LoadingInActivity;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.task.EndRoundTask;
import be.rdhaese.project.mobile.task.MarkAsLostTask;
import be.rdhaese.project.mobile.task.UpdateStateLoadingInTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import be.rdhaese.project.mobile.toast.ToastTool;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class GoToNextStepYesListener implements DialogInterface.OnClickListener {

    private Activity activityContext;
    private Long roundId;
    private List<SearchPacketsPacketDTO> packets;

    private DialogTool dialogTool;
    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean(Constants.DIALOG_TOOL_KEY);
        toastTool = context.getBean(Constants.TOAST_TOOL_KEY);
    }

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
                    AsyncTaskResult<Boolean> removedResult = new MarkAsLostTask().execute(roundId, packet).get();
                    if (removedResult.hasException()){
                        throw removedResult.getException();
                    }
                } catch (Exception e) {
                    dialogTool.fatalBackEndExceptionDialog(activityContext).show();
                }

            } else {
                packetsThatAreLeft.add(packet);
            }
        }

        if (packetsThatAreLeft.size() == 0) {
            //If no packets left -> the round ends
            try {
                //Let backend mark the round as ended
                AsyncTaskResult<Boolean> roundEndedResult = new EndRoundTask().execute(roundId).get();
                if (roundEndedResult.hasException()){
                    throw roundEndedResult.getException();
                }
            } catch (Exception e) {
                dialogTool.fatalBackEndExceptionDialog(activityContext).show();
            }

            //Go back to the start screen
            Intent intent = new Intent(activityContext, HomeScreenActivity.class);
            activityContext.startActivity(intent);

            //Show toast
            toastTool.createToast(activityContext, activityContext.getString(R.string.all_packets_marked_as_lost)).show();
        } else { //The round can go on to the next screen
            try {
                AsyncTaskResult<Boolean> updateStateLoadingInResult = new UpdateStateLoadingInTask().execute(roundId).get();
                if (updateStateLoadingInResult.hasException()){
                    throw updateStateLoadingInResult.getException();
                }
            } catch (Exception e){
                dialogTool.fatalBackEndExceptionDialog(activityContext).show();
            }
            //create intent with roundId and remaining packets
            Intent intent = new Intent(activityContext, LoadingInActivity.class);
            intent.putExtra(Constants.ROUND_ID_KEY, roundId);
            intent.putParcelableArrayListExtra(Constants.PACKETS_LIST_KEY,
                    new ArrayList<>(
                            SearchPacketsPacketDTO.mapCollectionSearchPacketsToParcelableDTO(packetsThatAreLeft)));
            //show next activity
            activityContext.startActivity(intent);
        }
    }
}

