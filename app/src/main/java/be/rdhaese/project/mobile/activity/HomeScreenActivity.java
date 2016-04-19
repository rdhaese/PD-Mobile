package be.rdhaese.project.mobile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.StartRoundFragment;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_home_screen)
public class HomeScreenActivity extends RoboFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //TODO check all permissions that are necessary and ask them if they are not granted
        //TODO if they are not granted after asking: show a message that the app will be inusable and will terminate
    }
}
