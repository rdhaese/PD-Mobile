package be.rdhaese.project.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import be.rdhaese.project.mobile.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_home_screen)
public class HomeScreenActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void newRound(View view){
        Intent intent = new Intent(this, NumberOfPacketsActivity.class);
        startActivity(intent);
    }
}
