package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 9/04/2016.
 */
public class DeliverTask extends AbstractAsyncTask<Object, Void, Boolean> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 2;

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(Object... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createResult(false);
        }

        Long roundId = (Long) params[0];
        PacketDTO currentPacket = (PacketDTO) params[1];

        try {
            return createResult(roundService.deliver(roundId, currentPacket));
        } catch (Exception e) {
            return createResult(e);
        }
    }
}
