package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class GetNewAppIdTask extends AsyncTask<Void, Void, String> {

    private AppWebService appService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appService = context.getBean("appService");
    }

    @Override
    protected String doInBackground(Void... params) {
        return appService.getNewId();
    }
}
