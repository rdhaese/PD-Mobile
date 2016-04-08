package be.rdhaese.project.mobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.LoadingInActivity;
import be.rdhaese.project.mobile.activity.OngoingDeliveryActivity;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.ParcelablePacketDTODecorator;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.task.MarkAsLostTask;
import be.rdhaese.project.mobile.task.StartRoundTask;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class LoadingInFragment extends RoboFragment {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

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

    @InjectExtra(value = "roundId", optional = false)
    private Long roundId;
    @InjectExtra(value = "currentPacketIndex", optional = true)
    private Integer currentPacketIndex;
    private ArrayList<ParcelablePacketDTODecorator> packets;

    private PacketDTO currentPacket;

    private DialogTool dialogTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean("dialogTool");
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
        init();
    }

    private void init() {
        //Load packets from intent
        Intent intent = getActivity().getIntent();
        packets = intent.getParcelableArrayListExtra("packets");

        //Nothing injected, means first packet -> index 0
        if (currentPacketIndex == null){
            currentPacketIndex = 0;
        }

        //Set currentPacket
        currentPacket = packets.get(currentPacketIndex);

        //Initialize textfields
        txtPacketId.setText(currentPacket.getPacketId());
        int packetsSize = packets.size();
        String amountOfPacketsLeftText = String.format(
                "%s of %s",
                packetsSize - currentPacketIndex,
                packetsSize
        );
        txtAmountOfPacketsLeft.setText(amountOfPacketsLeftText);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        btnConfirmVisually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                //Prepare dialog
                String title = "Confirm Visually";
                String message = "Are you sure packet IDs match? This is your responsibility!";
                DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmVisually();
                    }
                };
                DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                };

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
                String title= "Mark As Lost";
                String message = "Are you sure you want the packet as lost? This is your responsibility!";
                DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        markAsLost();
                    }
                };
                DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                };

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
        //TODO test this flow on cellphone......
        //Need to scan qr-code:
        //Prepare Intent for scanner app
        Intent intent = new Intent(ACTION_SCAN);
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        //Start scanner app activity from parent activity (LoadingInActivity)
        getActivity().startActivityForResult(intent, 0);
        //Get the scanned id from LoadingInActivity
        String scannedId = ((LoadingInActivity)getActivity()).getPreviousScannedId();

        //TODO what if something goes wrong?
        if (scannedId.equals(currentPacket.getPacketId())) {
            //Success:
            String toastText = "Scan successful!";
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
            //Do nextPacket logic
            nextPacket();
        } else {
            //No success:
            //Show toast:
            String toastText = String.format("Scanned code did not match packet id %s", currentPacket.getPacketId());
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
        }

    }

    private void confirmVisually(){
        //Do nextPacket logic
       nextPacket();
    }

    private void nextPacket() {
        currentPacketIndex++;
        if (currentPacketIndex >= packets.size()){
            //The last packet was handled -> the round can start
            String toastText = "All packets are loaded in.";
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();

            //Ask if the courier is ready to start
            //Prepare dialog
            String title = "Start Round";
            String message = "Push 'Yes' to start the round.";
            DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startRound();
                }
            };

            //Show dialog
            dialogTool.yesDialog(
                    getActivity(),
                    title,
                    message,
                    yesListener
            ).show();
        }else {
            //Start this activity again, the currentPacketIndex was incremented,
            //so the next packet will be shown.
            Intent intent = new Intent(this.getActivity(), LoadingInActivity.class);
            intent.putExtra("roundId", roundId);
            intent.putExtra("currentPacketIndex", currentPacketIndex);
            intent.putParcelableArrayListExtra("packets", packets);
            startActivity(intent);
        }
    }

    private void startRound() {
        try {
            Boolean roundStarted = new StartRoundTask().execute(roundId).get();
            String message = String.format(
                    "Result of backend starting round [%s]: [%s]",
                    roundId, roundStarted);
            Log.d(getClass().getSimpleName(), message);
        } catch (InterruptedException | ExecutionException e) {
            String message = String.format(
                    "Could not start round [%s]",
                    roundId);
            Log.e(getClass().getSimpleName(), message, e);
        }
        //Go to ongoing round activity (which will also start the GPS navigation)
        Intent intent = new Intent(this.getActivity(), OngoingDeliveryActivity.class);
        intent.putExtra("roundId", roundId);
        intent.putParcelableArrayListExtra("packets", packets);
        startActivity(intent);
    }

    private void markAsLost(){
        //Let the back end remove the packet
        //from the round and mark it as lost
        try {
            Boolean removed = new MarkAsLostTask().execute(roundId, currentPacket).get();
            if (removed){
                packets.remove(currentPacketIndex);
            }
            String message = String.format(
                    "Result of backend trying to mark packet [%s] for round [%s] as lost: [%s]",
                    currentPacket.getPacketId(), roundId, removed);
            Log.d(getClass().getSimpleName(), message);
        } catch (InterruptedException | ExecutionException e) {
            String message = String.format(
                    "Could not mark packet [%s] for round [%s] as lost",
                    currentPacket.getPacketId(), roundId);
            Log.e(getClass().getSimpleName(), message, e);
        }

        //Do nextPacket logic
        nextPacket();
    }
}
