package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import java.io.IOException;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.mobile.service.DeliveryRoundServiceProxyRestWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 28/12/2015.
 */
public class GetNewRoundTask extends AsyncTask<Integer,Void,Long> {
        DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Long doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }
        Long id = null;
            id = roundService.newRound(params[0]);
        return id;
    }
}
