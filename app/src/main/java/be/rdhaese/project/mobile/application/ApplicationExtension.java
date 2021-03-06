package be.rdhaese.project.mobile.application;

import android.app.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.concurrent.ExecutionException;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.packetdelivery.mobile.service.AppProxyRestWebService;
import be.rdhaese.packetdelivery.mobile.service.DeliveryRoundProxyRestWebService;
import be.rdhaese.project.mobile.R;
import be.rdhaese.project.mobile.app_id.AppIdTool;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.dialog.DialogTool;
import be.rdhaese.project.mobile.navigation.NavigationTool;
import be.rdhaese.project.mobile.task.GetNewAppIdTask;
import be.rdhaese.project.mobile.toast.ToastTool;

/**
 * Created by RDEAX37 on 3/04/2016.
 */
public class ApplicationExtension extends Application {

    @Override
    public void onCreate() {
        ApplicationContext context = ApplicationContext.getInstance();
        context.putBean(Constants.ROUND_SERVICE_KEY, new DeliveryRoundProxyRestWebService());
        context.putBean(Constants.APP_SERVICE_KEY, new AppProxyRestWebService());
        context.putBean(Constants.DIALOG_TOOL_KEY, new DialogTool());
        context.putBean(Constants.TOAST_TOOL_KEY, new ToastTool());
        context.putBean(Constants.APP_ID_TOOL_KEY, new AppIdTool());
        context.putBean(Constants.NAVIGATION_TOOL_KEY, new NavigationTool());

        super.onCreate();
    }


}
