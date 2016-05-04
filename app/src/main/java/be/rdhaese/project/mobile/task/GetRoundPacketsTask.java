package be.rdhaese.project.mobile.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.List;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.activity.SearchingPacketsActivity;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 27/02/2016.
 */
public class GetRoundPacketsTask extends AbstractAsyncTask<Object,Void,List<PacketDTO>> {

    public static final Integer CORRECT_AMOUNT_OF_PARAMS = 2;

    private DeliveryRoundWebService roundService;
    private  ProgressDialog progressDialog;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected AsyncTaskResult<List<PacketDTO>> doInBackground(Object... params) {
        if (isAmountOfParamsIncorrect(CORRECT_AMOUNT_OF_PARAMS, params)){
            return createNullResult();
        }

        try {
            return createResult(roundService.getPackets((Long) params[1]));
        } catch (Exception e){
            return createResult(e);
        }
    }
}
