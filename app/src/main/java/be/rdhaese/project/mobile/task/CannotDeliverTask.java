package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 9/04/2016.
 */
public class CannotDeliverTask extends AsyncTask<Object, Void, Boolean> {

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        if (params.length != 3){
            return false;
        }
        Long roundId = (Long) params[0];
        PacketDTO currentPacket = (PacketDTO) params[1];
        String reason = params[2].toString();

        if ((reason == null) || ((reason = reason.trim()).isEmpty())){
            return false;
        }

        return roundService.cannotDeliver(roundId, currentPacket, reason);
    }
}
