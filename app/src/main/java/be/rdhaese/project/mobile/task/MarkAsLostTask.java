package be.rdhaese.project.mobile.task;

import android.os.AsyncTask;
import android.util.Log;

import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 6/04/2016.
 */
public class MarkAsLostTask extends AsyncTask<Object, Void, Boolean> {

    DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean("roundService");
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        if (params.length == 0) {
            return false;
        }
        Long roundId = (Long) params[0];
        PacketDTO packetDTO = (PacketDTO) params[1];

        String message = String.format(
                "Marking packet [%s] for round [%s] as lost",
                packetDTO.getPacketId(),
                roundId
        );
        Log.d(getClass().getSimpleName(), message);

        return roundService.markAsLost(roundId, packetDTO);
    }
}
