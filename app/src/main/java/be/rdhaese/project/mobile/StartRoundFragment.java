package be.rdhaese.project.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.rdhaese.project.mobile.activity.NumberOfPacketsActivity;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.toast.ToastTool;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class StartRoundFragment extends RoboFragment {

    @InjectView(R.id.btnStartNewRound)
    private Button btnStartNewRound;

    @InjectExtra(value = "message", optional = true)
    private String message;

    @InjectExtra(value = "roundFinished", optional = true)
    private Boolean roundFinished = false;

    private Boolean messageShown = false;

    private ToastTool toastTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        toastTool = context.getBean("toastTool");
    }

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
        if ((message != null) && (!messageShown)) {
            toastTool.createToast(getActivity(), message).show();
            messageShown =true;
        }


        btnStartNewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound(v);
            }
        });

        if (roundFinished) {
//TODO test this:
            //TODO address to pd that comes from back end
            //set roundFinished to false so navigation only opens 1 time
            roundFinished = false;

            //Prepare navigation app
            //Setup address query
            String qry = String.format(
                    "google.navigation:q=%s+%s,+%s+%s",
                    "DagmoedStraat",
                    "77",
                    "9500",
                    "Schendelbeke");
            Uri gmmIntentUri = Uri.parse(qry);
            //Create intent
            final Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            //Show toast that navigation is going to start
            String toastText = "Navigation is starting...";
            toastTool.createToast(getActivity(), toastText).show();

            //Wait 3 seconds before starting navigation
            // so the courier has the time to understand what is happening
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Start navigation
                    startActivity(mapIntent);
                }
            }, 3000);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("roundFinished", roundFinished);
        outState.putBoolean("messageShown", messageShown);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            roundFinished = savedInstanceState.getBoolean("roundFinished");
            messageShown = savedInstanceState.getBoolean("messageShown");
        }
    }

    public void newRound(View view) {
        Intent intent = new Intent(getActivity(), NumberOfPacketsActivity.class);
        startActivity(intent);
    }
}
