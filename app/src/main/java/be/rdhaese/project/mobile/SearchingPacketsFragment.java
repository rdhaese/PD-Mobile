package be.rdhaese.project.mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepNoListener;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepYesListener;
import be.rdhaese.project.mobile.task.GetRoundPacketsTask;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class SearchingPacketsFragment extends RoboFragment {

    private ArrayList<SearchPacketsPacketDTO> searchPacketsPacketDTOs;

    private DeliveryRoundWebService roundService;
    private DialogTool dialogTool;
    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
        dialogTool = context.getBean(Constants.DIALOG_TOOL_KEY);
        toastTool = context.getBean(Constants.TOAST_TOOL_KEY);
    }

    @InjectView(R.id.lvwPackets)
    private ListView lvwPackets;

    @InjectExtra(Constants.ROUND_ID_KEY)
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
        try {
            init(savedInstanceState);
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
                SearchPacketListItemHolder scannedHolder = ApplicationContext.getInstance().getBean(Constants.SCANNED_HOLDER_KEY);
                if (scannedId.equals(scannedHolder.getPacketId())) {
                    String toastText = String.format("%s %s", getString(R.string.packet_found),  scannedId);
                    //Show toast
                    toastTool.createToast(getActivity(), toastText).show();
                    //Mark the packet as found
                    SearchPacketsPacketDTO packet = null;
                    for (SearchPacketsPacketDTO packetInList : searchPacketsPacketDTOs) {
                        if (scannedId.equals(packetInList.getPacketId())) {
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
                    String toastText = String.format("%s %s", getString(R.string.scanned_code_did_not_match), scannedHolder.getPacketId());
                    toastTool.createToast(getActivity(), toastText).show();
                }
            }
        }
    }

    private void init(Bundle savedInstance) throws Exception {
        if (searchPacketsPacketDTOs == null) {
            AsyncTaskResult<List<PacketDTO>> roundPacketResult = new GetRoundPacketsTask().execute(roundId).get();
            if (roundPacketResult.hasException()){
                throw roundPacketResult.getException();
            }

            searchPacketsPacketDTOs = new ArrayList<>(
                    SearchPacketsPacketDTO.mapCollectionPacketDTOToDecorator(roundPacketResult.getResult()));
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
                        ApplicationContext.getInstance().putBean(Constants.SCANNED_HOLDER_KEY, holder);
                        //Need to scan qr-code:
                        //Prepare Intent for scanner app
                        Intent intent = new Intent(Constants.ACTION_SCAN);
                        intent.setPackage(Constants.PACKAGE_SCAN);
                        intent.putExtra(Constants.EXTRA_SCAN_MODE, Constants.EXTRA_QR_CODE_MODE);
                        //Start scanner app activity
                        startActivityForResult(intent, Constants.SCAN_REQUEST_CODE);
                    } else {
                        //No scanning needed, just mark as found
                        foundPacket(packet.getPacketId());
                    }
                }

                public void foundPacket(String scannedId) {
                    String toastText = String.format("%s %s", getString(R.string.packet_found), scannedId);
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

    private void doLastPacketCheck(SearchPacketsPacketDTO currentPacket, SearchPacketListItemHolder holder) {
        if (allPacketsAreTreated()) {
            showConfirmContinueToNextActivityDialog(currentPacket, holder);
        }
    }

    private void showConfirmContinueToNextActivityDialog(final SearchPacketsPacketDTO currentPacket, final SearchPacketListItemHolder holder) {
        String title = getString(R.string.go_to_next_step);
        String message = getString(R.string.sure_info_in_list_correct);
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
        outState.putParcelableArrayList(Constants.SEARCH_PACKETS_LIST_KEY, searchPacketsPacketDTOs);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            searchPacketsPacketDTOs = savedInstanceState.getParcelableArrayList(Constants.SEARCH_PACKETS_LIST_KEY);
        }
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

        public String getPacketId() {
            return txtPacketIdText.getText().toString();
        }
    }
}
