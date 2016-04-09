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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.LoadingInActivity;
import be.rdhaese.project.mobile.activity.OngoingDeliveryActivity;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.ParcelablePacketDTODecorator;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.DoNothingListener;
import be.rdhaese.project.mobile.task.AddRemarkTask;
import be.rdhaese.project.mobile.task.CannotDeliverTask;
import be.rdhaese.project.mobile.task.DeliverTask;
import be.rdhaese.project.mobile.task.EndRoundTask;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class OngoingDeliveryFragment extends RoboFragment {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

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

    @InjectExtra("roundId")
    private Long roundId;
    @InjectExtra("packets")
    private ArrayList<ParcelablePacketDTODecorator> packets;
    @InjectExtra(value = "originalAmountOfPackets", optional = true)
    private Integer originalAmountOfPackets;

    private PacketDTO currentPacket;

    private DialogTool dialogTool;
    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        dialogTool = context.getBean("dialogTool");
        toastTool = context.getBean("toastTool");
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
        init();
    }

    private void init() {
        //Nothing injected, means first packet -> set originalAmountOfPackets
        if (originalAmountOfPackets == null) {
            originalAmountOfPackets = packets.size();
        }

        //Set current packet to first packet in list
        currentPacket = packets.get(0);

        //Initialize TextViews
        txtPacketId.setText(currentPacket.getPacketId());
        txtCurrentPacketCount.setText(String.format(
                "%s of %s",
                originalAmountOfPackets - packets.size() + 1,
                originalAmountOfPackets
        ));
        txtPhoneNumber.setText(currentPacket.getClientPhone());
        txtStreet.setText(currentPacket.getClientStreet());
        txtNumber.setText(currentPacket.getClientNumber());
        if (currentPacket.getClientMailbox() != null) {
            txtMailbox.setText(String.format(
                    "Box: %s", currentPacket.getClientMailbox()));
        } else {
            txtMailbox.setText("");
        }
        txtPostalCode.setText(currentPacket.getClientPostalCode());
        txtCity.setText(currentPacket.getClientCity());

        //Set buttons OnClickListener
        btnAddRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Prepare dialog to ask for input
                String title = "Add Remark";
                String message = "Enter your remark and push 'Ok'.";
                final EditText inputComponent = new EditText(getActivity());
                DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get input from dialog
                        CharSequence remark = inputComponent.getText();

                        //Check if there was something entered
                        if ((remark != null) && (remark.length() > 0)) {
                            //Add remark to back end
                            try {
                                new AddRemarkTask().execute(roundId, remark.toString()).get();
                            } catch (InterruptedException e) {
                                //TODO handle exception
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                //TODO handle exception
                                e.printStackTrace();
                            }
                        } else {
                            //Show toast that no remark was entered
                            String toastText = "No remark entered";
                            toastTool.createToast(getActivity(), toastText).show();
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
                        .setPositiveButton("Ok", okListener)
                        .setNegativeButton("Cancel", new DoNothingListener())
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
                        String title = "Cannot Deliver";
                        String message = "Enter a reason to mail to stakeholders and push 'Ok'.";
                        final EditText inputComponent = new EditText(getActivity());
                        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Get input
                                reason.append(inputComponent.getText());

                                //Dismiss the dialog
                                dialog.dismiss();

                                //Check if there was something entered
                                if (reason.length() == 0) {
                                    //Show toast that no remark was entered
                                    String toastText = "No reason entered";
                                    toastTool.createToast(getActivity(), toastText).show();
                                } else {
                                    //Ask if really sure
                                    String title = "Cannot Deliver";
                                    String message = String.format(
                                            "Are you sure packet %s cannot be delivered with reason %s?",
                                            currentPacket.getPacketId(),
                                            reason
                                    );
                                    DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Let back end send its mails, do its priority logic and remove the packet from the round
                                            try {
                                                new CannotDeliverTask().execute(roundId, currentPacket, reason).get();
                                            } catch (InterruptedException e) {
                                                //TODO handle exception
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                //TODO handle exception
                                                e.printStackTrace();
                                            }

                                            //Do nextPacket logic
                                            nextPacket();
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
                                .setPositiveButton("Ok", okListener)
                                .setNegativeButton("Cancel", new DoNothingListener())
                                .create();
                        dialogTool.setDialogLocation(getActivity(), dialog);
                        dialog.show();
                    }
                }
        );

        btnDeliver.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              if (!switchConfirmVisually.isChecked()) {
                                                  //TODO test this flow on cellphone......
                                                  //Need to scan qr-code:
                                                  //Prepare Intent for scanner app
                                                  Intent intent = new Intent(ACTION_SCAN);
                                                  intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                                                  //Start scanner app activity from parent activity (LoadingInActivity)
                                                  getActivity().startActivityForResult(intent, 0);
                                                  //Get the scanned id from LoadingInActivity
                                                  String scannedId = ((OngoingDeliveryActivity) getActivity()).getPreviousScannedId();

                                                  //TODO what if something goes wrong?
                                                  if (scannedId.equals(currentPacket.getPacketId())) {
                                                      //Let back end send its mails and remove the packet from the system
                                                      try {
                                                          new DeliverTask().execute(roundId, currentPacket).get();
                                                      } catch (InterruptedException e) {
                                                          //TODO handle exception
                                                          e.printStackTrace();
                                                      } catch (ExecutionException e) {
                                                          //TODO handle exception
                                                          e.printStackTrace();
                                                      }
                                                  }

                                                  //do nextPacket logic
                                                  nextPacket();
                                              } else {
                                                  //Ask if sure:
                                                  //Prepare dialog
                                                  String title = "Confirm Visually";
                                                  String message = "Are you sure packet IDs match? This is your responsibility!";
                                                  DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                                                      @Override
                                                      public void onClick(DialogInterface dialog, int which) {
                                                          //Let back end send its mails and remove the packet from the system
                                                          try {
                                                              new DeliverTask().execute(roundId, currentPacket).get();
                                                          } catch (InterruptedException e) {
                                                              //TODO handle exception
                                                              e.printStackTrace();
                                                          } catch (ExecutionException e) {
                                                              //TODO handle exception
                                                              e.printStackTrace();
                                                          }
                                                          //Do nextPacket logic
                                                          nextPacket();
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

        //TODO start navigation to currentPacket address
    }

    private void nextPacket() {
        packets.remove(currentPacket);
        //check if packet is the last packet
        if (packets.size() == 0) {
            //Last packet:
            //Remove round from backend
            try {
                new EndRoundTask().execute(roundId).get();
            } catch (InterruptedException e) {
                //TODO handle exception
                e.printStackTrace();
            } catch (ExecutionException e) {
                //TODO handle exception
                e.printStackTrace();
            }

            //TODO start navigation to PD address

            //Show HomeScreenActivity again with message
            Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
            String message = "Round Finished!";
            intent.putExtra("message", message);
            startActivity(intent);
        } else {
            //Show this activity for next packet
            Intent intent = new Intent(getActivity(), OngoingDeliveryActivity.class);
            intent.putExtra("roundId", roundId);
            intent.putExtra("packets", packets);
            intent.putExtra("originalAmountOfPackets", originalAmountOfPackets);
            startActivity(intent);
        }

    }

}
