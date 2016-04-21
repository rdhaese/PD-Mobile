package be.rdhaese.project.mobile.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.Serializable;
import java.util.List;

import be.rdhaese.project.mobile.SearchingPacketsFragment;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;

/**
 * Created by RDEAX37 on 27/02/2016.
 */
public class SearchPacketsAdapter extends BaseAdapter implements Serializable {

    private List<SearchPacketsPacketDTO> packets;
    private List<SearchingPacketsFragment.SearchPacketListItemHolder> holders;

    public SearchPacketsAdapter(List<SearchPacketsPacketDTO> packets, List<SearchingPacketsFragment.SearchPacketListItemHolder> holders) {
        this.packets = packets;
        this.holders = holders;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView != null)
                return convertView;
        return holders.get(position).getRowView();
    }

}