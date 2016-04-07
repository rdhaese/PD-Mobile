package be.rdhaese.project.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.NumberPicker;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.task.GetNewRoundTask;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_number_of_packets)
public class NumberOfPacketsActivity extends RoboFragmentActivity {

}
