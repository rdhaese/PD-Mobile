package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.LongLatDTO;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 10/04/2016.
 */
public class AddLocationUpdateTask extends AsyncTask<Object, Void, Boolean> {

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context =ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }


    @Override
    protected Boolean doInBackground(Object... params) {
        if (params.length != 2){
            return false;
        }

        Long roundId = (Long) params[0];
        LongLatDTO longLatDTO = (LongLatDTO) params[1];

        return roundService.addLocationUpdate(roundId, longLatDTO);
    }
}
