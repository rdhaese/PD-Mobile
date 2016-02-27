package be.rdhaese.project.mobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import be.rdhaese.project.mobile.R;

public class LoadingInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_in);
    }

    public void scan(View view){
        Intent intent = new Intent(this, OngoingDeliveryActivity.class);
        startActivity(intent);
    }
}
