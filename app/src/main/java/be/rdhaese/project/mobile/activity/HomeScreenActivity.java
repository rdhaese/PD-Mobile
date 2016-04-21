package be.rdhaese.project.mobile.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import be.rdhaese.project.mobile.R;
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
