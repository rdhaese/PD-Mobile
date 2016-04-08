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
import be.rdhaese.project.mobile.decorator.ParcelablePacketDTODecorator;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepNoListener;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepYesListener;
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
    private DialogTool dialogTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
        dialogTool = context.getBean("dialogTool");
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

        public View getRowView() {
            return this.rowView;
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
        String title = "Go To Next Step";
        String message = "Are you sure the information in the list is correct?";
        DialogInterface.OnClickListener yesListener = new GoToNextStepYesListener(
                activityContext, roundId, packets);
        DialogInterface.OnClickListener noListener = new GoToNextStepNoListener(
                currentPacket, holder
        );

        dialogTool.yesNoDialog(
                activityContext,
                title,
                message,
                yesListener,
                noListener)
                .show();
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