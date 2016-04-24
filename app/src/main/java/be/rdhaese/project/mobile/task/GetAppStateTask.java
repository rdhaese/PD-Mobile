package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.packetdelivery.dto.AppStateDTO;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class GetAppStateTask extends AsyncTask <String, Void, AppStateDTO> {

    private AppWebService appService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appService = context.getBean("appService");
    }

    @Override
    protected AppStateDTO doInBackground(String... params) {
        if (params.length == 0){
            return null;
        }
        return appService.getAppState(params[0]);
    }
}
