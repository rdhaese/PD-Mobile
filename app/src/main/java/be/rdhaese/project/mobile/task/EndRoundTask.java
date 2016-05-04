package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 7/04/2016.
 */
public class EndRoundTask extends AbstractAsyncTask<Long,Void, Boolean> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 1;

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(Long... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createResult(false);
        }

        try {
            return createResult(roundService.endRound(params[0]));
        } catch (Exception e){
            return createResult(e);
        }
    }
}
