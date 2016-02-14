package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.eindproject.mobile_service.CreateNewRoundService;
import be.rdhaese.eindproject.mobile_service.impl.CreateNewRoundServiceImpl;

/**
 * Created by RDEAX37 on 28/12/2015.
 */
public class GetNewRoundTask extends AsyncTask<Integer,Void,String> {
    //TODO better to inject dependency...
    CreateNewRoundService createNewRoundService = new CreateNewRoundServiceImpl();

    @Override
    protected String doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }
        return createNewRoundService.getNewRound(params[0]);
    }
}
