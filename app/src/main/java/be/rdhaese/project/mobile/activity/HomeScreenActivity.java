package be.rdhaese.project.mobile.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.packetdelivery.dto.AppStateDTO;
import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.app_id.AppIdTool;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.dialog.listener.DoNothingListener;
import be.rdhaese.project.mobile.task.GetAppStateTask;
import be.rdhaese.project.mobile.task.GetNewAppIdTask;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_home_screen)
public class HomeScreenActivity extends AbstractActivity {

    private AppIdTool appIdTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appIdTool = context.getBean("appIdTool");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        checkIfNecessaryAppsAreInstalled();
        checkIfAlreadyHasARound(); //TODO maybe in onCreate or similar
    }

    private void checkIfNecessaryAppsAreInstalled() {
        if ((!isAppInstalled(Constants.PACKAGE_MAPS))
                || (!isAppInstalled(Constants.PACKAGE_SCAN))) {
            dialogTool.yesDialog(
                    this,
                    "Necessary Apps Not Installed",
                    "Please install a scanner and navigation app before using the application. The app will terminate now.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HomeScreenActivity.this.finishAffinity();
                            System.exit(0);
                        }
                    }).show();
        }
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private void checkIfAlreadyHasARound() {
        String appId = appIdTool.getAppId(this);

        AppStateDTO appState = null;
        try {
            appState = new GetAppStateTask().execute(appId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();//TODO handle this
        } catch (ExecutionException e) {
            e.printStackTrace();//TODO handle this
        }

        if (appState == null){
            //Means the id that the app holds, is not known to the back end for some reason
            //Request new app id
            try {
                appId = new GetNewAppIdTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO handle this
            } catch (ExecutionException e) {
                e.printStackTrace(); //TODO handle this
            }
            //Save the new appId
            appIdTool.saveAppId(this,appId);

            //Get appstate again
            try {
                appState = new GetAppStateTask().execute(appId).get();
            } catch (InterruptedException e) {
                e.printStackTrace();//TODO handle this
            } catch (ExecutionException e) {
                e.printStackTrace();//TODO handle this
            }
        }

        if (appState.getRoundId() != null) {
            Intent intent = null;
            switch (appState.getActivity()) {
                case "searching":
                    intent = new Intent(this, SearchingPacketsActivity.class);
                    break;
                case "loading":
                    intent = new Intent(this, LoadingInActivity.class);
                    break;
                case "ongoing":
                    intent = new Intent(this, LoadingInActivity.class);
                    intent.putExtra("currentPacketIndex", appState.getCurrentPacketIndex());
                    break;
            }
            intent.putExtra("roundId", appState.getRoundId());
            startActivity(intent);

            String toastText = "Delivery round state loaded.";
            toastTool.createToast(this,toastText).show();
        }
    }
}
