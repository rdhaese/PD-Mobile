package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 28/12/2015.
 */
public class GetNewRoundTask extends AbstractAsyncTask<Integer, Void, Long> {
    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 1;

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<Long> doInBackground(Integer... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createNullResult();
        }

        try {
            return createResult(roundService.newRound(params[0]));
        } catch (Exception e) {
            return createResult(e);
        }
    }
}
