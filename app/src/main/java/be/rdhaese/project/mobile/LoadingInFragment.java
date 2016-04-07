package be.rdhaese.project.mobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.OngoingDeliveryActivity;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class LoadingInFragment extends RoboFragment {

    @InjectView(R.id.btnScan)
    private Button btnScan;

    private Button btn

    @InjectExtra(value = "roundId", optional = false)
    private Long roundId;

    private List<PacketDTO> packets;

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
        Intent intent = getActivity().getIntent();
        packets = new ArrayList<>(
                SearchPacketsPacketDTO.mapCollectionToDTO(intent
                        .<SearchPacketsPacketDTO>getParcelableArrayListExtra("packets")));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v);
            }
        });
    }

    public void scan(View view) {
        Intent intent = new Intent(this.getActivity(), OngoingDeliveryActivity.class);
        startActivity(intent);
    }
}
