package be.rdhaese.project.mobile.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.internal.ParcelableSparseArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.StartRoundFragment;
import be.rdhaese.project.mobile.activity.HomeScreenActivity;
import be.rdhaese.project.mobile.activity.LoadingInActivity;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.task.EndRoundTask;
import be.rdhaese.project.mobile.task.GetRoundPacketsTask;
import be.rdhaese.project.mobile.task.MarkAsLostTask;

/**
 * Created by RDEAX37 on 27/02/2016.
 */
public class SearchPacketsAdapter extends BaseAdapter {
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private static LayoutInflater inflater = null;

    private SearchingPacketsActivity activityContext;
    private List<SearchPacketsPacketDTO> packets;
    private Long roundId;

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    public SearchPacketsAdapter(SearchingPacketsActivity searchingPacketsActivity, List<SearchPacketsPacketDTO> packets, Long roundId) {
        activityContext = searchingPacketsActivity;
        this.packets = packets;
        this.roundId = roundId;
        inflater = (LayoutInflater) activityContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return packets.size();
    }

    @Override
    public Object getItem(int position) {
        return packets.get(position);
    }

    /**
     * NOT APPLICABLE<br />
     *
     * @param position
     * @return always -1;
     */
    @Override
    public long getItemId(int position) {
        return -1;
    }

    public class SearchPacketListItemHolder {
        private View rowView;
        private TextView txtPacketid;
        private TextView txtPacketIdText;
        private Switch switchConfirmVisually;
        private Button btnScan;

        public SearchPacketListItemHolder() {
            rowView = inflater.inflate(R.layout.search_packets_list_item, null);
            txtPacketid = (TextView) rowView.findViewById(R.id.txtPacketId);
            txtPacketIdText = (TextView) rowView.findViewById(R.id.txtPacketIdText);
            switchConfirmVisually = (Switch) rowView.findViewById(R.id.switchConfirmVisually);
            btnScan = (Button) rowView.findViewById(R.id.btnScan);
        }

        public void disableControls() {
            switchConfirmVisually.setEnabled(false);
            btnScan.setEnabled(false);
        }

        public void enableControls() {
            switchConfirmVisually.setEnabled(true);
            btnScan.setEnabled(true);
        }

        public void setTxtPacketIdText(String packetId) {
            txtPacketIdText.setText(packetId);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchPacketListItemHolder holder = new SearchPacketListItemHolder();
        final SearchPacketsPacketDTO currentPacket = packets.get(position);
        holder.setTxtPacketIdText(currentPacket.getPacketId());

        holder.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.switchConfirmVisually.isChecked()) {
                    //TODO test this flow on cellphone......
                    //Need to scan qr-code:
                    //Prepare Intent for scanner app
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    //Start scanner app activity from activityContext (ActivitySearchPackets)
                    activityContext.startActivityForResult(intent, 0);
                    //Get the scanned id from activityContext (ActivitySearchPackets)
                    String scannedId = activityContext.getPreviousScannedId();

                    //TODO what if something goes wrong?
                    if (scannedId.equals(currentPacket.getPacketId())) {
                        //Success:
                        //Construct toast text
                        foundPacket(scannedId);
                    } else {
                        String toastText = String.format("Scanned code did not match packet id %s", currentPacket.getPacketId());
                        Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //No scanning needed, just mark as found
                    foundPacket(currentPacket.getPacketId());
                }
            }

            private void foundPacket(String scannedId) {
                String toastText = String.format("Packet Found: %s", scannedId);
                //Show toast
                Toast.makeText(activityContext, toastText, Toast.LENGTH_SHORT).show();
                //Mark the packet as found
                currentPacket.setFound(true);
                //Set state of gui
                holder.rowView.setBackgroundResource(R.drawable.bg_found);
                holder.disableControls();
                //Check if this is the last packet and act accordingly
                doLastPacketCheck(currentPacket, holder);
            }
        });

        holder.rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isPacketTreated(currentPacket)) {
                    holder.rowView.setBackgroundResource(0);
                    currentPacket.setFound(false);
                    currentPacket.setLost(false);
                    holder.enableControls();
                } else {
                    holder.rowView.setBackgroundResource(R.drawable.bg_lost);
                    currentPacket.setLost(true);
                    holder.disableControls();
                }
                doLastPacketCheck(currentPacket, holder);
                return true;
            }
        });
        return holder.rowView;

    }

    private void doLastPacketCheck(SearchPacketsPacketDTO currentPacket, SearchPacketListItemHolder holder) {
        if (allPacketsAreTreated()) {
            showConfirmContinueToNextActivityDialog(currentPacket, holder);
        }
    }

    private void showConfirmContinueToNextActivityDialog(final SearchPacketsPacketDTO currentPacket, final SearchPacketListItemHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Go To Next Step")
                .setMessage("Are you sure the information in the list is correct?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //undo last action
                        holder.rowView.setBackgroundResource(0);
                        currentPacket.setFound(false);
                        currentPacket.setLost(false);
                        holder.enableControls();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ArrayList implementation needed to be able to put it on an intent
                        ArrayList<SearchPacketsPacketDTO> packetsThatAreLeft = new ArrayList<SearchPacketsPacketDTO>(packets);
                        for (SearchPacketsPacketDTO packet : packets) {
                            if (packet.getLost()) {
                                //Let the back end remove the packet
                                //from the round and mark it as lost
                                try {
                                    Boolean removed = new MarkAsLostTask().execute(roundId, packet).get();
                                    if (removed){
                                        packetsThatAreLeft.remove(packet);
                                    }
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

                            }
                        }

                        if (packetsThatAreLeft.size() == 0){
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
                        }else { //The round can go on to the next screen
                            //create intent with roundId and remaining packets
                            Intent intent = new Intent(activityContext, LoadingInActivity.class);
                            intent.putExtra("roundId", roundId);
                            intent.putParcelableArrayListExtra("packets", packetsThatAreLeft);
                            //show next activity
                            activityContext.startActivity(intent);
                        }
                    }
                });
        builder.create().show();
    }

    private boolean allPacketsAreTreated() {
        for (SearchPacketsPacketDTO packet : packets) {
            if (!isPacketTreated(packet)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPacketTreated(SearchPacketsPacketDTO packet) {
        return packet.getFound() || packet.getLost();
    }

}