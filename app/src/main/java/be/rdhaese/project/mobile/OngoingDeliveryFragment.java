package be.rdhaese.project.mobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;


public class OngoingDeliveryFragment extends RoboFragment {

    @InjectView(R.id.txtPacketId)
    private TextView txtPacketId;
    @InjectView(R.id.txtCurrentPacketCount)
    private TextView txtCurrentPacketCount;
    @InjectView(R.id.txtPhoneNumber)
    private TextView txtPhoneNumber;
    @InjectView(R.id.txtStreet)
    private TextView txtStreet;
    @InjectView(R.id.txtNumber)
    private TextView txtNumber;
    @InjectView(R.id.txtMailbox)
    private TextView txtMailbox;
    @InjectView(R.id.txtPostalCode)
    private TextView txtPostalCode;
    @InjectView(R.id.txtCity)
    private TextView txtCity;
    
    @InjectView(R.id.btnAddRemark)
    private Button btnAddRemark;
    @InjectView(R.id.btnCannotDeliver)
    private Button btnCannotDeliver;
    @InjectView(R.id.btnDeliver)
    private Button btnDeliver;

    @InjectView(R.id.switchConfirmVisually)
    private Switch switchConfirmVisually;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ongoing_delivery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        //TODO
    }

}
