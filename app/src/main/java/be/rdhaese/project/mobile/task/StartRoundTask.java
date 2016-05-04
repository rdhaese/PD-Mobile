package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class StartRoundTask extends AbstractAsyncTask<Long, Void, Boolean> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 1;

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(final Long... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createResult(false);
        }

        Long roundId = params[0];

        try {
            return createResult(roundService.startRound(roundId));
        } catch (Exception e){
            return createResult(e);
        }
    }
}
