package be.rdhaese.project.mobile.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import java.io.FileNotFoundException;
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
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_home_screen)
public class HomeScreenActivity extends AbstractActivity {

    public static final String ACTIVITY_SEARCHING = "searching";
    public static final String ACTIVITY_LOADING = "loading";
    public static final String ACTIVITY_ONGOING = "ongoing";

    private AppIdTool appIdTool;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appIdTool = context.getBean(Constants.APP_ID_TOOL_KEY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        checkIfNecessaryAppsAreInstalled();
        try {
            checkIfAlreadyHasARound();
        } catch (Exception e) {
            dialogTool.fatalBackEndExceptionDialog(this).show();
        }
    }

    private void checkIfNecessaryAppsAreInstalled() {
        if ((!isAppInstalled(Constants.PACKAGE_MAPS))
                || (!isAppInstalled(Constants.PACKAGE_SCAN))) {
            dialogTool.yesDialog(
                    this,
                    getString(R.string.necessary_apps_not_installed),
                    getString(R.string.please_install_apps),
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

    private void checkIfAlreadyHasARound() throws Exception {
        String appId = appIdTool.getAppId(this);

        AsyncTaskResult<AppStateDTO> appStateResult = new GetAppStateTask().execute(appId).get();

        if (appStateResult.hasNullResult()) {
            //Means the id that the app holds, is not known to the back end for some reason
            //Request new app id
            AsyncTaskResult<String> appIdResult = new GetNewAppIdTask().execute().get();
            if (appIdResult.hasException()) {
                throw appIdResult.getException();
            }

            //Save the new appId
            appIdTool.saveAppId(this, appIdResult.getResult());

            //Get appstate again
            appStateResult = new GetAppStateTask().execute(appIdResult.getResult()).get();
        }

        if (appStateResult.hasException()) {
            throw appStateResult.getException();
        }

        AppStateDTO appState = appStateResult.getResult();
        if (appState.getRoundId() != null) {
            Intent intent = null;
            switch (appState.getActivity()) {
                case ACTIVITY_SEARCHING:
                    intent = new Intent(this, SearchingPacketsActivity.class);
                    break;
                case ACTIVITY_LOADING:
                    intent = new Intent(this, LoadingInActivity.class);
                    intent.putExtra(Constants.CURRENT_PACKET_INDEX_KEY, appState.getCurrentPacketIndex());
                    break;
                case ACTIVITY_ONGOING:
                    intent = new Intent(this, OngoingDeliveryActivity.class);
                    intent.putExtra(Constants.CURRENT_PACKET_INDEX_KEY, appState.getCurrentPacketIndex());
                    break;
            }
            intent.putExtra(Constants.ROUND_ID_KEY, appState.getRoundId());
            startActivity(intent);

            String toastText = getString(R.string.state_loaded);
            toastTool.createToast(this, toastText).show();
        }
    }

}
