package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.eindproject.mobile_service.RoundService;
import be.rdhaese.eindproject.mobile_service.impl.RoundServiceImpl;

/**
 * Created by RDEAX37 on 28/12/2015.
 */
public class GetNewRoundTask extends AsyncTask<Integer,Void,Long> {
    //TODO better to inject dependency...
    RoundService roundService = new RoundServiceImpl();

    @Override
    protected Long doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }
        return roundService.getNewRound(params[0]);
    }
}
