package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 9/04/2016.
 */
public class AddRemarkTask extends AsyncTask<Object,Void,Boolean> {

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        if (params.length != 2){
            return false;
        }
        Long roundId = (Long) params[0];
        String remark = (String) params[1];

        if ((remark == null) || ((remark = remark.trim()).isEmpty())){
            return false;
        }

        return roundService.addRemark(roundId, remark);
    }
}
