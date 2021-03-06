package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**

 * Created by RDEAX37 on 21/04/2016.
 */
public class UpdateStateRoundStartedTask extends AbstractAsyncTask<Object, Void, Boolean> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 2;

    private AppWebService appService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        appService = context.getBean(Constants.APP_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createResult(false);
        }

        try {
            return createResult(appService.roundStarted((String) params[0], (Long) params[1]));
        } catch (Exception e) {
            return createResult(e);
        }
    }
}
