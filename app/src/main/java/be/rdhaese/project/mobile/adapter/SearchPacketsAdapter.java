package be.rdhaese.project.mobile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.SearchingPacketsFragment;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepNoListener;
import be.rdhaese.project.mobile.dialog.listener.GoToNextStepYesListener;
import be.rdhaese.project.mobile.toast.ToastTool;

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