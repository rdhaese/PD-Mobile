package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.packetdelivery.dto.AppStateDTO;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class GetAppStateTask extends AbstractAsyncTask <String, Void, AppStateDTO> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 1;

    private AppWebService appService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appService = context.getBean(Constants.APP_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<AppStateDTO> doInBackground(String... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createNullResult();
        }

        try {
            return createResult(appService.getAppState(params[0]));
        } catch (Exception e){
            return createResult(e);
        }
    }
}
