package be.rdhaese.project.mobile.task;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.AddressDTO;
import be.rdhaese.project.mobile.constants.Constants;
import be.rdhaese.project.mobile.context.ApplicationContext;
import be.rdhaese.project.mobile.task.result.AsyncTaskResult;

/**
 * Created by RDEAX37 on 30/04/2016.
 */
public class GetCompanyAddressTask extends AbstractAsyncTask<Void, Void, AddressDTO> {

    private DeliveryRoundWebService roundService;

    {
        ApplicationContext context = ApplicationContext.getInstance();
        roundService = context.getBean(Constants.ROUND_SERVICE_KEY);
    }

    @Override
    protected AsyncTaskResult<AddressDTO> doInBackground(Void... params) {
        try {
            return createResult(roundService.getCompanyAddress());
        } catch (Exception e){
            return createResult(e);
        }
    }
}
