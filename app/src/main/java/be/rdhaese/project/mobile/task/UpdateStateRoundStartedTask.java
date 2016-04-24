package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class UpdateStateRoundStartedTask extends AsyncTask<Object, Void, Boolean> {

    private AppWebService appService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appService = context.getBean("appService");
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        if (params.length < 2){
            return false;
        }
        return appService.roundStarted((String)params[0], (Long)params[1]);
    }
}
