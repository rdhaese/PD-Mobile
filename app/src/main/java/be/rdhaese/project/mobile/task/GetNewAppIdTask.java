package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;
import android.util.Log;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class GetNewAppIdTask extends AbstractAsyncTask<Void, Void, String> {

    private AppWebService appService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appService = context.getBean(Constants.APP_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        try {
            return createResult(appService.getNewId());
        } catch (Exception e){
            return createResult(e);
        }
    }
}
