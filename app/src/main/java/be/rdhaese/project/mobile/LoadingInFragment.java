package be.rdhaese.project.mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.LoadingInActivity;
import be.rdhaese.project.mobile.activity.OngoingDeliveryActivity;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.ParcelablePacketDTODecorator;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.DoNothingListener;
import be.rdhaese.project.mobile.location.LocationUpdateService;
import be.rdhaese.project.mobile.task.EndRoundTask;
import be.rdhaese.project.mobile.task.GetRoundPacketsTask;
import be.rdhaese.project.mobile.task.MarkAsLostTask;
import be.rdhaese.project.mobile.task.StartRoundTask;
import be.rdhaese.project.mobile.task.UpdateStateNextPacketTask;
import be.rdhaese.project.mobile.task.UpdateStateOngoingDeliveryTask;
import be.rdhaese.project.mobile.task.UpdateStateRoundEndedTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class LoadingInFragment extends RoboFragment {


    @InjectView(R.id.btnScan)
    private Button btnScan;
    @InjectView(R.id.btnConfirmVisually)
    private Button btnConfirmVisually;
    @InjectView(R.id.btnLost)
    private Button btnLost;

    @InjectView(R.id.txtPacketId)
    private TextView txtPacketId;
    @InjectView(R.id.txtAmountOfPacketsLeft)
    private TextView txtAmountOfPacketsLeft;

    @InjectExtra(Constants.ROUND_ID_KEY)
    private Long roundId;
    @InjectExtra(value = Constants.PACKETS_KEY, optional = true)
    private ArrayList<ParcelablePacketDTODecorator> packets;
    @InjectExtra(value = Constants.CURRENT_PACKET_INDEX_KEY, optional = true)
    private Integer currentPacketIndex;

    private PacketDTO currentPacket;

    private DialogTool dialogTool;
    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean(Constants.DIALOG_TOOL_KEY);
        toastTool = context.getBean(Constants.TOAST_TOOL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_in, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();//TODO remove
            dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                //This runs after scanning for an id
                //Getting scannedId from result
                String scannedId = data.getStringExtra(Constants.SCAN_RESULT_KEY);

                if (scannedId.equals(currentPacket.getPacketId())) {
                    //Success:
                    String toastText = getString(R.string.scan_successful);
                    toastTool.createToast(getActivity(), toastText).show();
                    //Do nextPacket logic
                    try {
                        nextPacket(true);
                    } catch (Exception e) {
                        dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                    }
                } else {
                    //No success:
                    //Show toast:
                    String toastText = String.format("%s %s", getString(R.string.scanned_code_did_not_match), currentPacket.getPacketId());
                    toastTool.createToast(getActivity(), toastText).show();
                }
            }
        }
    }

    private void init() throws Exception {
        //No packets injected, means the app got shutdown and started during an ongoing round
        if (packets == null) {
            //Get the packets from the back end
            AsyncTaskResult<List<PacketDTO>> roundPacketResult = new GetRoundPacketsTask().execute(roundId).get();
            if (roundPacketResult.hasException()) {
                throw roundPacketResult.getException();
            }
            packets = new ArrayList<>(ParcelablePacketDTODecorator.mapCollectionToDecorator(roundPacketResult.getResult()));
        }

        //Nothing injected, means first packet -> index 0
        if (currentPacketIndex == null) {
            currentPacketIndex = 0;
        }

        //Set currentPacket
        currentPacket = packets.get(currentPacketIndex);

        //Initialize TextViews
        txtPacketId.setText(currentPacket.getPacketId());
        int packetsSize = packets.size();
        String amountOfPacketsLeftText = String.format(
                "%s %s %s",
                packetsSize - currentPacketIndex,
                getString(R.string.of),
                packetsSize
        );
        txtAmountOfPacketsLeft.setText(amountOfPacketsLeftText);

        //Set buttons onClickListener
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        btnConfirmVisually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Prepare dialog
                String title = getString(R.string.confirm_visually);
                String message = getString(R.string.sure_that_packet_ids_match);
                DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            confirmVisually();
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
                        noListener
                ).show();
            }
        });

        btnLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Prepare dialog
                String title = getString(R.string.mark_as_lost);
                String message = getString(R.string.sure_that_packet_is_lost);
                DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            markAsLost();
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
                        noListener
                ).show();
            }
        });
    }

    private void scan() {
        //Need to scan qr-code:
        //Prepare Intent for scanner app
        Intent intent = new Intent(Constants.ACTION_SCAN);
        intent.setPackage(Constants.PACKAGE_SCAN);
        intent.putExtra(Constants.EXTRA_SCAN_MODE, Constants.EXTRA_QR_CODE_MODE);
        //Start scanner app activity
        startActivityForResult(intent, 0);
    }

    private void confirmVisually() throws Exception {
        //Do nextPacket logic
        nextPacket(true);
    }

    private void nextPacket(boolean notLost) throws Exception {
        if (notLost) {
            currentPacketIndex++;
        }
        if (currentPacketIndex >= packets.size()) {
            //The last packet was handled
            if (packets.isEmpty()) {
                //All packets are lost
                try {
                    //Let backend mark the round as ended
                    AsyncTaskResult<Boolean> roundEndedResult = new EndRoundTask().execute(roundId).get();
                    if (roundEndedResult.hasException()){
                        throw roundEndedResult.getException();
                    }
                    AsyncTaskResult<Boolean> updateStateRoundEndedResult = new UpdateStateRoundEndedTask().execute(roundId).get();
                    if (updateStateRoundEndedResult.hasException()){
                        throw updateStateRoundEndedResult.getException();
                    }
                } catch (Exception e) {
                    dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                }

                //Go back to the start screen
                Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                startActivity(intent);

                //Show toast
                toastTool.createToast(getActivity(), getString(R.string.all_packets_lost)).show();
            } else {
                //Still some packets left -> the round can start
                String toastText = getString(R.string.all_packets_loaded_in);
                toastTool.createToast(getActivity(), toastText).show();

                //Ask if the courier is ready to start
                //Prepare dialog
                String title = getString(R.string.start_round);
                String message = getString(R.string.push_yes_to_start);
                DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startRound();
                        } catch (Exception e) {
                            dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                        }
                    }
                };

                //Show dialog
                dialogTool.yesDialog(
                        getActivity(),
                        title,
                        message,
                        yesListener
                ).show();
            }
        } else {
            if(notLost) {
                AsyncTaskResult<Boolean> updateStateNextPacketResult = new UpdateStateNextPacketTask().execute(roundId).get();
                if (updateStateNextPacketResult.hasException()) {
                    throw updateStateNextPacketResult.getException();
                }
            }
            //Start this activity again, the currentPacketIndex was incremented,
            //so the next packet will be shown.
            Intent intent = new Intent(this.getActivity(), LoadingInActivity.class);
            intent.putExtra(Constants.ROUND_ID_KEY, roundId);
            intent.putExtra(Constants.CURRENT_PACKET_INDEX_KEY, currentPacketIndex);
            intent.putParcelableArrayListExtra(Constants.PACKETS_KEY, packets);
            startActivity(intent);
        }

    }

    private void startRound() throws Exception {
        AsyncTaskResult<Boolean> roundStartedResult = new StartRoundTask().execute(roundId).get();
        if (roundStartedResult.hasException()) {
            throw roundStartedResult.getException();
        }
        if (roundStartedResult.getResult()) {
            AsyncTaskResult<Boolean> updateStateOngoingDeliveryResult = new UpdateStateOngoingDeliveryTask().execute(roundId).get();
            if (updateStateOngoingDeliveryResult.hasException()) {
                throw updateStateOngoingDeliveryResult.getException();
            }
            Intent intent = new Intent(getActivity(), LocationUpdateService.class);
            intent.putExtra(Constants.ROUND_ID_KEY, roundId);
            getActivity().startService(intent);
        }

        //Go to ongoing round activity (which will also start the GPS navigation)
        Intent intent = new Intent(this.getActivity(), OngoingDeliveryActivity.class);
        intent.putExtra(Constants.ROUND_ID_KEY, roundId);
        intent.putParcelableArrayListExtra(Constants.PACKETS_KEY, packets);
        startActivity(intent);
    }

    private void markAsLost() throws Exception {
        //Let the back end remove the packet
        //from the round and mark it as lost
        AsyncTaskResult<Boolean> removedResult = new MarkAsLostTask().execute(roundId, currentPacket).get();
        if (removedResult.hasException()) {
            throw removedResult.getException();
        }

        packets.remove(currentPacketIndex.intValue());

        //Do nextPacket logic
        nextPacket(false);
    }
}
