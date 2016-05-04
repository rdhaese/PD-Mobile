package be.rdhaese.project.mobile.task;

import android.util.Log;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.LongLatDTO;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 10/04/2016.
 */
public class AddLocationUpdateTask extends AbstractAsyncTask<Object, Void, Boolean> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 2;
    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context =ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }


    @Override
    protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createResult(false);
        }

        Long roundId = (Long) params[0];
        LongLatDTO longLatDTO = (LongLatDTO) params[1];

        try {
            return createResult(
                    roundService.addLocationUpdate(roundId, longLatDTO));
        } catch (Exception e){
            return createResult(e);
        }
    }
}
