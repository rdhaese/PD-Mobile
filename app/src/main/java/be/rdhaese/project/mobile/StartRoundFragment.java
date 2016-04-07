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

import be.rdhaese.project.mobile.activity.NumberOfPacketsActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class StartRoundFragment extends RoboFragment {

    @InjectView(R.id.btnStartNewRound)
    private Button btnStartNewRound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_round, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        btnStartNewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound(v);
            }
        });
    }

    public void newRound(View view){
        Intent intent = new Intent(getActivity(), NumberOfPacketsActivity.class);
        startActivity(intent);
    }
}
