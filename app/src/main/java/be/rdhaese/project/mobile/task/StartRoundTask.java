package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 8/04/2016.
 */
public class StartRoundTask extends AsyncTask<Long,Void,Boolean> {

    DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        if (params.length == 0){
            return false;
        }
        Boolean roundStartedInBackEnd = roundService.startRound(params[0]);
        if (roundStartedInBackEnd){
            //TODO start thread that sends location updates
        }
        return roundStartedInBackEnd;
    }
}
