package be.rdhaese.project.mobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import be.rdhaese.eindproject.mobile_service.RoundService;
import be.rdhaese.eindproject.mobile_service.impl.RoundServiceImpl;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.R;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_searching_packets)
public class SearchingPacketsActivity extends AppCompatActivity {

    //TODO better to inject dependency...
    private RoundService roundService = new RoundServiceImpl();

    @InjectView(R.id.lvwPackets)
    private ListView lvwPackets;

    @InjectExtra("roundId")
    private Long roundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        List<PacketDTO> packets = roundService.getPackets(roundId);
        //TODO fill listview
        ArrayAdapter<PacketDTO> arrayAdapter = new ArrayAdapter<PacketDTO>(this, )
        lvwPackets.setAdapter(ne);
    }

    public void testNextActivity(View view){
        Intent intent = new Intent(this, LoadingInActivity.class);
        startActivity(intent);
    }
}
