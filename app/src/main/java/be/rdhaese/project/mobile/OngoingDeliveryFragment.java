package be.rdhaese.project.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.OngoingDeliveryActivity;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.ParcelablePacketDTODecorator;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.DoNothingListener;
import be.rdhaese.project.mobile.location.LocationUpdateService;
import be.rdhaese.project.mobile.navigation.NavigationTool;
import be.rdhaese.project.mobile.task.AddRemarkTask;
import be.rdhaese.project.mobile.task.CannotDeliverTask;
import be.rdhaese.project.mobile.task.DeliverTask;
import be.rdhaese.project.mobile.task.EndRoundTask;
import be.rdhaese.project.mobile.task.GetRoundPacketsTask;
import be.rdhaese.project.mobile.task.UpdateStateRoundEndedTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class OngoingDeliveryFragment extends RoboFragment {

    @InjectView(R.id.txtPacketId)
    private TextView txtPacketId;
    @InjectView(R.id.txtCurrentPacketCount)
    private TextView txtCurrentPacketCount;
    @InjectView(R.id.txtPhoneNumber)
    private TextView txtPhoneNumber;
    @InjectView(R.id.txtStreet)
    private TextView txtStreet;
    @InjectView(R.id.txtNumber)
    private TextView txtNumber;
    @InjectView(R.id.txtMailbox)
    private TextView txtMailbox;
    @InjectView(R.id.txtPostalCode)
    private TextView txtPostalCode;
    @InjectView(R.id.txtCity)
    private TextView txtCity;

    @InjectView(R.id.btnAddRemark)
    private Button btnAddRemark;
    @InjectView(R.id.btnCannotDeliver)
    private Button btnCannotDeliver;
    @InjectView(R.id.btnDeliver)
    private Button btnDeliver;

    @InjectView(R.id.switchConfirmVisually)
    private Switch switchConfirmVisually;

    @InjectExtra(Constants.ROUND_ID_KEY)
    private Long roundId;
    @InjectExtra(value = Constants.PACKETS_KEY, optional = true)
    private ArrayList<ParcelablePacketDTODecorator> packets;
    @InjectExtra(value = Constants.ORIGINAL_AMOUNT_OF_PACKETS_KEY, optional = true)
    private Integer originalAmountOfPackets;
    @InjectExtra(value = Constants.NAVIGATION_STARTED_KEY, optional = true)
    private Boolean navigationStarted = false;

    private PacketDTO currentPacket;

    private DialogTool dialogTool;
    private ToastTool toastTool;
    private NavigationTool navigationTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean(Constants.DIALOG_TOOL_KEY);
        toastTool = context.getBean(Constants.TOAST_TOOL_KEY);
        navigationTool = context.getBean(Constants.NAVIGATION_TOOL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ongoing_delivery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            init();
        } catch (Exception e) {
            dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SCAN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //This runs after scanning for an id
                //Getting scannedId from result
                String scannedId = data.getStringExtra(Constants.SCAN_RESULT_KEY);

                if (scannedId.equals(currentPacket.getPacketId())) {
                    //Let back end send its mails and remove the packet from the system
                    try {
                        AsyncTaskResult<Boolean> deliveryResult = new DeliverTask().execute(roundId, currentPacket).get();
                        if (deliveryResult.hasException()) {
                            throw deliveryResult.getException();
                        }

                        //do nextPacket logic
                        nextPacket();
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
            AsyncTaskResult<List<PacketDTO>> roundPacketsResult = new GetRoundPacketsTask().execute(roundId).get();
            if (roundPacketsResult.hasException()) {
                throw roundPacketsResult.getException();
            }
            packets = new ArrayList<>(ParcelablePacketDTODecorator.mapCollectionToDecorator(roundPacketsResult.getResult()));
        }

        //Nothing injected, means first packet -> set originalAmountOfPackets
        if (originalAmountOfPackets == null) {
            originalAmountOfPackets = packets.size();
        }

        //Set current packet to first packet in list
        currentPacket = packets.get(0);

        //Initialize TextViews
        txtPacketId.setText(currentPacket.getPacketId());
        txtCurrentPacketCount.setText(String.format(
                "%s %s %s",
                originalAmountOfPackets - packets.size() + 1,
                getString(R.string.of),
                originalAmountOfPackets
        ));
        txtPhoneNumber.setText(currentPacket.getDeliveryPhone());
        txtStreet.setText(currentPacket.getDeliveryStreet());
        txtNumber.setText(currentPacket.getDeliveryNumber());
        if (currentPacket.getDeliveryMailbox() != null) {
            txtMailbox.setText(String.format(
                    "%s %s",
                    getString(R.string.box),
                    currentPacket.getDeliveryMailbox()));
        } else {
            txtMailbox.setText("");
        }
        txtPostalCode.setText(currentPacket.getDeliveryPostalCode());
        txtCity.setText(currentPacket.getDeliveryCity());

        //Set buttons OnClickListener
        btnAddRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Prepare dialog to ask for input
                String title = getString(R.string.add_remark);
                String message = getString(R.string.enter_your_remark);
                final EditText inputComponent = new EditText(getActivity());
                DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get input from dialog
                        CharSequence remark = inputComponent.getText();

                        //Check if there was something entered
                        if ((remark == null) && (remark.length() <= Constants.REMARK_MIN_LENGTH)) {
                            //Show toast that no remark was entered
                            String toastText = getString(R.string.no_remark_entered);
                            toastTool.createToast(getActivity(), toastText).show();
                        } else if (remark.length() > Constants.REMARK_MAX_LENGTH) {
                            //Show toast that remark is to long
                            String toastText = getString(R.string.remark_to_long);
                            toastTool.createToast(getActivity(), toastText).show();
                        } else {
                            //Add remark to back end
                            try {
                                AsyncTaskResult<Boolean> addRemarkResult = new AddRemarkTask().execute(roundId, remark.toString()).get();
                                if (addRemarkResult.hasException()) {
                                    throw addRemarkResult.getException();
                                }
                            } catch (Exception e) {
                                dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                            }
                        }
                        dialog.dismiss();
                    }
                };

                //Create and show dialog
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle(title)
                        .setMessage(message)
                        .setView(inputComponent)
                        .setPositiveButton(R.string.ok, okListener)
                        .setNegativeButton(R.string.cancel, new DoNothingListener())
                        .create();
                dialogTool.setDialogLocation(getActivity(), dialog);
                dialog.show();
            }
        });


        btnCannotDeliver.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Ask for reason
                        final StringBuilder reason = new StringBuilder();
                        //Prepare dialog to ask for input
                        String title = getString(R.string.cannot_deliver);
                        String message = getString(R.string.enter_a_reason);
                        final EditText inputComponent = new EditText(getActivity());
                        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Get input
                                reason.append(inputComponent.getText());

                                //Dismiss the dialog
                                dialog.dismiss();

                                //Check if there was something entered
                                if (reason.length() == Constants.REASON_MIN_LENGTH) {
                                    //Show toast that no remark was entered
                                    String toastText = getString(R.string.no_reason_entered);
                                    toastTool.createToast(getActivity(), toastText).show();
                                } else if (reason.length() > Constants.REASON_MAX_LENGTH) {
                                    //reason to long
                                    String toastText = getString(R.string.reason_to_long);
                                    toastTool.createToast(getActivity(), toastText).show();
                                } else {
                                    //Ask if really sure
                                    String title = getString(R.string.cannot_deliver);
                                    String message = String.format(
                                            "%s %s %s %s?",
                                            getString(R.string.cannot_deliver_part1),
                                            currentPacket.getPacketId(),
                                            getString(R.string.cannot_deliver_part2),
                                            reason
                                    );
                                    DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Let back end send its mails, do its priority logic and remove the packet from the round
                                            try {
                                                AsyncTaskResult<Boolean> cannotDeliverResult = new CannotDeliverTask().execute(roundId, currentPacket, reason).get();
                                                if (cannotDeliverResult.hasException()) {
                                                    throw cannotDeliverResult.getException();
                                                }

                                                //Do nextPacket logic
                                                nextPacket();
                                            } catch (Exception e) {
                                                dialogTool.fatalBackEndExceptionDialog(getActivity()).show();
                                            }

                                        }
                                    };
                                    DialogInterface.OnClickListener noListener = new DoNothingListener();
                                    dialogTool.yesNoDialog(
                                            getActivity(),
                                            title,
                                            message,
                                            yesListener,
                                            noListener
                                    ).show();
                                }
                            }
                        };
                        //Create and show dialog
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setTitle(title)
                                .setMessage(message)
                                .setView(inputComponent)
                                .setPositiveButton(R.string.ok, okListener)
                                .setNegativeButton(R.string.cancel, new DoNothingListener())
                                .create();
                        dialogTool.setDialogLocation(getActivity(), dialog);
                        dialog.show();
                    }
                }
        );

        btnDeliver.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!switchConfirmVisually.isChecked()) {
                            scan();
                        } else {
                            //Ask if sure:
                            //Prepare dialog
                            String title = getString(R.string.confirm_visually);
                            String message = getString(R.string.sure_that_packet_ids_match);
                            DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Let back end send its mails and remove the packet from the system
                                    try {
                                        AsyncTaskResult<Boolean> deliverResult = new DeliverTask().execute(roundId, currentPacket).get();
                                        if (deliverResult.hasException()) {
                                            throw deliverResult.getException();
                                        }

                                        //Do nextPacket logic
                                        nextPacket();
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
                    }
                }

        );

        if (!navigationStarted) {
            navigationStarted = true;
            //Navigate to packet address
            navigationTool.startNavigation(
                    getActivity(),
                    currentPacket.getDeliveryStreet(),
                    currentPacket.getDeliveryNumber(),
                    currentPacket.getDeliveryPostalCode(),
                    currentPacket.getDeliveryCity()
            );
        }
    }


    private void nextPacket() throws Exception {
        packets.remove(currentPacket);
        //check if packet is the last packet
        if (packets.size() == 0) {
            //Last packet:
            //Remove round from backend

            AsyncTaskResult<Boolean> roundEndedResult = new EndRoundTask().execute(roundId).get();
            if (roundEndedResult.hasException()) {
                throw roundEndedResult.getException();
            }

            if (roundEndedResult.getResult()) {
                Intent intent = new Intent(getActivity(), LocationUpdateService.class);
                getActivity().stopService(intent);
            }


            //Show HomeScreenActivity again with message
            AsyncTaskResult<Boolean> updateStateRoundEndedResult = new UpdateStateRoundEndedTask().execute(roundId).get();
            if (updateStateRoundEndedResult.hasException()) {
                throw updateStateRoundEndedResult.getException();
            }

            Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
            String message = getString(R.string.round_finished);
            intent.putExtra(Constants.MESSAGE_KEY, message);
            intent.putExtra(Constants.ROUND_FINISHED_KEY, true);
            startActivity(intent);
        } else {
            //Show this activity for next packet
            Intent intent = new Intent(getActivity(), OngoingDeliveryActivity.class);
            intent.putExtra(Constants.ROUND_ID_KEY, roundId);
            intent.putExtra(Constants.PACKETS_KEY, packets);
            intent.putExtra(Constants.ORIGINAL_AMOUNT_OF_PACKETS_KEY, originalAmountOfPackets);
            startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.NAVIGATION_STARTED_KEY, navigationStarted);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            navigationStarted = savedInstanceState.getBoolean(Constants.NAVIGATION_STARTED_KEY);
        }
    }

    private void scan() {
        //Need to scan qr-code:
        //Prepare Intent for scanner app
        Intent intent = new Intent(Constants.ACTION_SCAN);
        intent.setPackage(Constants.PACKAGE_SCAN);
        intent.putExtra(Constants.EXTRA_SCAN_MODE, Constants.EXTRA_QR_CODE_MODE);
        //Start scanner app activity
        startActivityForResult(intent, Constants.SCAN_REQUEST_CODE);
    }
}
