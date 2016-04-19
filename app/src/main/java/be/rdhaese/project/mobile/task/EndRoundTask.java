package be.rdhaese.project.mobile.task;

import android.content.Intent;
import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.location.LocationUpdateService;

/**
 * Created by RDEAX37 on 7/04/2016.
 */
public class EndRoundTask extends AsyncTask<Long,Void, Boolean> {

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        if (params.length == 0) {
            return false;
        }

       return roundService.endRound(params[0]);
    }
}
