package be.rdhaese.project.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.adapter.SearchPacketsAdapter;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.task.GetRoundPacketsTask;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


public class SearchingPacketsFragment extends RoboFragment {

    private DeliveryRoundWebService roundService;
    private ArrayList<SearchPacketsPacketDTO> searchPacketsPacketDTOs;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @InjectView(R.id.lvwPackets)
    private ListView lvwPackets;

    @InjectExtra(value = "roundId", optional = false)
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
        init();
    }

    private void init() {
        if (searchPacketsAdapter == null) {
            System.setProperty("http.keepAlive", "false");
            List<PacketDTO> packetDTOs = null;
            try {
                packetDTOs = new GetRoundPacketsTask().execute(roundId).get();
            } catch (InterruptedException e) {
                //TODO handle this
                e.printStackTrace();
            } catch (ExecutionException e) {
                //TODO handle this
                e.printStackTrace();
            }
            List<SearchPacketsPacketDTO> packets =
                    new ArrayList<>(SearchPacketsPacketDTO.mapCollectionToDecorator(packetDTOs));
            searchPacketsAdapter =
                    new SearchPacketsAdapter((SearchingPacketsActivity) this.getActivity(),
                            packets,roundId);
        }
        lvwPackets.setAdapter(searchPacketsAdapter);
    }
}
