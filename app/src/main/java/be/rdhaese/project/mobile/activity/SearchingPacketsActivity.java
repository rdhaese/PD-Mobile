package be.rdhaese.project.mobile.activity;

import android.content.Intent;

import be.rdhaese.project.mobile.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_searching_packets)
public class SearchingPacketsActivity extends RoboFragmentActivity {

    private String previousScannedId = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                previousScannedId = data.getStringExtra("SCAN_RESULT");
            }
        }
    }

    public String getPreviousScannedId() {
        return previousScannedId;
    }
}
