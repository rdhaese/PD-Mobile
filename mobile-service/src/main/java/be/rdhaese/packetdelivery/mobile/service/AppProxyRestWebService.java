package be.rdhaese.packetdelivery.mobile.service;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.packetdelivery.dto.AppStateDTO;
import be.rdhaese.packetdelivery.mobile.service.properties.BackEndProperties;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class AppProxyRestWebService extends AbstractService implements AppWebService{

    @Override
    public String getNewId()  throws Exception  {
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateNewIdUrl(),
                String.class);
    }

    @Override
    public AppStateDTO getAppState(String appId) throws Exception {
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateForAppIdUrl(),
                AppStateDTO.class, appId);
    }

    @Override
    public Boolean roundStarted(String appId, Long roundId)  throws Exception{
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateRoundStartedUrl(),
                Boolean.class, appId, roundId);
    }

    @Override
    public Boolean loadingIn(Long roundId)  throws Exception{
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateLoadingInUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean nextPacket(Long roundId) throws Exception {
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateNextPacketUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean ongoingDelivery(Long roundId) throws Exception {
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateOngoingDeliveryUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean roundEnded(Long roundId) throws Exception {
        return getRestTemplate().getForObject(
                getBackEndProperties().getStateRoundEndedUrl(),
                Boolean.class, roundId);
    }

    protected class MyRestTemplate extends RestTemplate {
        public MyRestTemplate(int timeout) {
            if (getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
                ((SimpleClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(timeout);
                ((SimpleClientHttpRequestFactory) getRequestFactory()).setReadTimeout(timeout);
            } else if (getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
                ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setReadTimeout(timeout);
                ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(timeout);
            }
        }
    }
}
