package be.rdhaese.project.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import roboguice.inject.InjectView;

/**
 * Created by RDEAX37 on 27/02/2016.
 */
public class SearchPacketsAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;

    private Context context;
    private List<PacketDTO> packets;

    public SearchPacketsAdapter(SearchingPacketsActivity searchingPacketsActivity, List<PacketDTO> packets) {
        // TODO Auto-generated constructor stub
        context=searchingPacketsActivity;
        this.packets = packets;
        inflater = ( LayoutInflater )context.
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
     * @param position
     * @return always -1;
     */
    @Override
    public long getItemId(int position) {
        return -1;
    }

    public class SearchPacketListItemHolder
    {
        private View rowView;
        private TextView txtPacketid;
        private TextView txtPacketIdText;
        private Switch switchConfirmVisually;
        private Button btnScan;

        public SearchPacketListItemHolder(){
            View rowView = inflater.inflate(R.layout.search_packets_list_item, null);
            txtPacketid=(TextView) rowView.findViewById(R.id.txtPacketId);
            txtPacketIdText=(TextView) rowView.findViewById(R.id.txtPacketIdText);
            switchConfirmVisually = (Switch) rowView.findViewById(R.id.switchConfirmVisually);
            btnScan = (Button) rowView.findViewById(R.id.btnScan);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        SearchPacketListItemHolder holder = new SearchPacketListItemHolder();

        holder.txtPacketIdText.setText(packets.get(position).getPacketId());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}