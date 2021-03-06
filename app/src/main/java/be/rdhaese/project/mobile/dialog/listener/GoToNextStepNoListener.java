package be.rdhaese.project.mobile.dialog.listener;

import android.content.DialogInterface;

import be.rdhaese.project.mobile.SearchingPacketsFragment;
import be.rdhaese.project.mobile.decorator.SearchPacketsPacketDTO;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class GoToNextStepNoListener implements DialogInterface.OnClickListener  {

    private SearchPacketsPacketDTO currentPacket;
    private SearchingPacketsFragment.SearchPacketListItemHolder holder;

    public GoToNextStepNoListener(SearchPacketsPacketDTO currentPacket, SearchingPacketsFragment.SearchPacketListItemHolder holder) {
        this.currentPacket = currentPacket;
        this.holder = holder;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //undo last action
        holder.getRowView().setBackgroundResource(0);
        currentPacket.setFound(false);
        currentPacket.setLost(false);
        holder.enableControls();
    }
}
