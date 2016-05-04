package be.rdhaese.project.mobile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.app_id.AppIdTool;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.DoNothingListener;
import be.rdhaese.project.mobile.task.GetNewRoundTask;
import be.rdhaese.project.mobile.task.UpdateStateRoundStartedTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;


public class NumberOfPacketsFragment extends RoboFragment {

    @InjectView(R.id.npNumberOfPackets)
    private NumberPicker npNumberOfPackets;
    @InjectView(R.id.btnStartRound)
    private Button btnStartRound;

    private DialogTool dialogTool;
    private AppIdTool appIdTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean(Constants.DIALOG_TOOL_KEY);
        appIdTool = context.getBean(Constants.APP_ID_TOOL_KEY);
    }

    private void init() {
        npNumberOfPackets.setMinValue(Constants.ROUND_MIN_AMOUNT_PACKETS);
        npNumberOfPackets.setMaxValue(Constants.ROUND_MAX_AMOUNT_PACKETS);

        btnStartRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startRound(v);
                } catch (Exception e) {
                    dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
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
            case Constants.PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        startRound();
                    } catch (Exception e) {
                        dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                    }
                }
                return;
            }
        }
    }

    private void startRound() throws Exception {
            Integer amountOfPackets = npNumberOfPackets.getValue();
            AsyncTaskResult<Long> roundIdResult = new GetNewRoundTask().execute(amountOfPackets
            ).get();
            if (roundIdResult.hasException()){
                throw roundIdResult.getException();
            }

            Long roundId = roundIdResult.getResult();
            if (Constants.INVALID_ROUND_ID.equals(roundId)) {
                //If returned roundId equals -1, it means there where no packets to start a round.

                //Show HomeScreenActiviy again with a message
                Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                String message = getString(R.string.no_packets_found);
                intent.putExtra(Constants.MESSAGE_KEY,message);
                startActivity(intent);
            } else {
                //A valid roundId is returned, we can go on to the next activity
                AsyncTaskResult<Boolean> updateStateRoundStartedResult = new UpdateStateRoundStartedTask().execute(appIdTool.getAppId(this.getContext()), roundId).get();
                if (updateStateRoundStartedResult.hasException()){
                    throw updateStateRoundStartedResult.getException();
                }
                Intent intent = new Intent(this.getActivity(), SearchingPacketsActivity.class);
                intent.putExtra(Constants.ROUND_ID_KEY, roundId);
                startActivity(intent);
            }
    }

    public void startRound(final View view) throws Exception {
        //Ask if the courier is sure, no turning back afterwards
        //Prepare the dialog
        String title = getString(R.string.no_turning_back);
        String message = getString(R.string.no_turning_back_are_you_sure);
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    askPermissionIfNecessaryAndStartRound(view);
                } catch (Exception e) {
                    dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                }
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



    private void askPermissionIfNecessaryAndStartRound(View view) throws Exception {
        if (ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            //TODO SDK23 related check
            //Figure out a way to force the user to pick yes
            //Will probably give troubles
            //If no, the application should maybe suspend and the round terminated...
        } else {
            new Thread(new Runnable() {
                final ProgressDialog pd = ProgressDialog.show(getActivity(), getString(R.string.creating_delivery_round), getString(R.string.can_take_a_few_seconds), true, false);
                @Override
                public void run() {
                    try {
                        startRound();
                        pd.dismiss();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
