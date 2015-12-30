package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import com.google.inject.Inject;

import be.rdhaese.eindproject.mobile_service.CreateNewRoundService;

/**
 * Created by RDEAX37 on 28/12/2015.
 */
public class GetNewRoundTask extends AsyncTask<Integer,Void,String> {

    CreateNewRoundService createNewRoundService = new CreateNewRoundService();

    @Override
    protected String doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }
        return createNewRoundService.getNewRound(params[0]);
    }
}
