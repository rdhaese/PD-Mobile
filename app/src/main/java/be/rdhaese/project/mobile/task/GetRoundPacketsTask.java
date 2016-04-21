package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;

import java.util.List;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 27/02/2016.
 */
public class GetRoundPacketsTask extends AsyncTask<Long,Void,List<PacketDTO>> {
    DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected List<PacketDTO> doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }
        try {
            return roundService.getPackets(params[0]);
        } catch (Exception e){
            e.printStackTrace();
            //TODO handle this
        }
        return null;
    }
}
