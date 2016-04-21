package be.rdhaese.project.mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.adapter.SearchPacketsAdapter;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepNoListener;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepYesListener;
import be.rdhaese.project.mobile.task.GetRoundPacketsTask;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class SearchingPacketsFragment extends RoboFragment {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private ArrayList<SearchPacketsPacketDTO> searchPacketsPacketDTOs;

    private DeliveryRoundWebService roundService;
    private DialogTool dialogTool;
    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
        dialogTool = context.getBean("dialogTool");
        toastTool = context.getBean("toastTool");
    }

    @InjectView(R.id.lvwPackets)
    private ListView lvwPackets;

    @InjectExtra("roundId")
    private Long roundId;

    private SearchPacketsAdapter searchPacketsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searching_packets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                //This runs after scanning for an id
                //Getting scannedId from result
                String scannedId = data.getStringExtra("SCAN_RESULT");
                SearchPacketListItemHolder scannedHolder = ApplicationContext.getInstance().getBean("scannedHolder");
                if (scannedId.equals(scannedHolder.getPacketId())){
                    String toastText = String.format("Packet Found: %s", scannedId);
                    //Show toast
                    toastTool.createToast(getActivity(), toastText).show();
                    //Mark the packet as found
                    SearchPacketsPacketDTO packet = null;
                    for (SearchPacketsPacketDTO packetInList : searchPacketsPacketDTOs){
                        if (scannedId.equals(packetInList.getPacketId())){
                            packet = packetInList;
                        }
                    }
                    packet.setFound(true);
                    //Set state of gui
                    scannedHolder.rowView.setBackgroundResource(R.drawable.bg_found);
                    scannedHolder.disableControls();
                    //Check if this is the last packet and act accordingly
                    doLastPacketCheck(packet, scannedHolder);
                } else {
                    //No success:
                    //Show toast:
                    String toastText = String.format("Scanned code did not match packet id %s", scannedHolder.getPacketId());
                    toastTool.createToast(getActivity(), toastText).show();
                }
            }
        }
    }

    private void init(Bundle savedInstance) {

        if (searchPacketsPacketDTOs == null) {
            System.setProperty("http.keepAlive", "false");
            List<PacketDTO> packetDTOs = null;
            try {
                packetDTOs = new GetRoundPacketsTask().execute(roundId).get();
                searchPacketsPacketDTOs = new ArrayList<>(
                        SearchPacketsPacketDTO.mapCollectionPacketDTOToDecorator(packetDTOs));
            } catch (InterruptedException e) {
                //TODO handle this
                e.printStackTrace();
            } catch (ExecutionException e) {
                //TODO handle this
                e.printStackTrace();
            }
        }

        List<SearchPacketListItemHolder> holders = new ArrayList<>();
        for (final SearchPacketsPacketDTO packet : searchPacketsPacketDTOs) {
            final SearchPacketListItemHolder holder = new SearchPacketListItemHolder(savedInstance);
            holder.setTxtPacketIdText(packet.getPacketId());
            if (packet.getFound()) {
                holder.rowView.setBackgroundResource(R.drawable.bg_found);
                holder.disableControls();
            } else if (packet.getLost()) {
                holder.rowView.setBackgroundResource(R.drawable.bg_lost);
                holder.disableControls();
            }

            holder.btnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.switchConfirmVisually.isChecked()) {
                        ApplicationContext.getInstance().putBean("scannedHolder", holder);
                        //TODO test this flow on cellphone......
                        //Need to scan qr-code:
                        //Prepare Intent for scanner app
                        Intent intent = new Intent(ACTION_SCAN);
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        //Start scanner app activity
                        startActivityForResult(intent, 0);
                    } else {
                        //No scanning needed, just mark as found
                        foundPacket(packet.getPacketId());
                    }
                }

                public void foundPacket(String scannedId) {
                    String toastText = String.format("Packet Found: %s", scannedId);
                    //Show toast
                    toastTool.createToast(getActivity(), toastText).show();
                    //Mark the packet as found
                    packet.setFound(true);
                    //Set state of gui
                    holder.rowView.setBackgroundResource(R.drawable.bg_found);
                    holder.disableControls();
                    //Check if this is the last packet and act accordingly
                    doLastPacketCheck(packet, holder);
                }
            });

            holder.rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (isPacketTreated(packet)) {
                        holder.rowView.setBackgroundResource(0);
                        packet.setFound(false);
                        packet.setLost(false);
                        holder.enableControls();
                    } else {
                        holder.rowView.setBackgroundResource(R.drawable.bg_lost);
                        packet.setLost(true);
                        holder.disableControls();
                    }
                    doLastPacketCheck(packet, holder);
                    return true;
                }
            });

            holders.add(holder);
        }
        searchPacketsAdapter =
                new SearchPacketsAdapter(searchPacketsPacketDTOs, holders);
        lvwPackets.setAdapter(searchPacketsAdapter);
    }

    public class SearchPacketListItemHolder implements Serializable {
        private View rowView;
        private TextView txtPacketIdText;
        private Switch switchConfirmVisually;
        private Button btnScan;

        public SearchPacketListItemHolder(Bundle savedInstance) {
            rowView = getLayoutInflater(savedInstance).inflate(R.layout.search_packets_list_item, null);
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

        public String getPacketId(){
            return txtPacketIdText.getText().toString();
        }
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
                getActivity(), roundId, searchPacketsPacketDTOs);
        DialogInterface.OnClickListener noListener = new GoToNextStepNoListener(
                currentPacket, holder
        );

        dialogTool.yesNoDialog(
                getActivity(),
                title,
                message,
                yesListener,
                noListener)
                .show();
    }

    private boolean allPacketsAreTreated() {
        for (SearchPacketsPacketDTO packet : searchPacketsPacketDTOs) {
            if (!isPacketTreated(packet)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPacketTreated(SearchPacketsPacketDTO packet) {
        return packet.getFound() || packet.getLost();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("searchPacketsPacketDTOs", searchPacketsPacketDTOs);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            searchPacketsPacketDTOs = savedInstanceState.getParcelableArrayList("searchPacketsPacketDTOs");
        }
    }
}
