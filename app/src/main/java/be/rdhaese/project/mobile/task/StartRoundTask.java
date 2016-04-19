package be.rdhaese.project.mobile.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.location.LocationUpdateService;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class StartRoundTask extends AsyncTask<Long, Void, Boolean> {

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Boolean doInBackground(final Long... params) {
        if (params.length == 0) {
            return false;
        }
        final Long roundId = params[0];

        return roundService.startRound(roundId);
    }
}
