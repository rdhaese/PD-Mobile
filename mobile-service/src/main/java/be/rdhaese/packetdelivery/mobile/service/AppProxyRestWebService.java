package be.rdhaese.packetdelivery.mobile.service;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.AppWebService;
import be.rdhaese.packetdelivery.dto.AppStateDTO;
import be.rdhaese.packetdelivery.mobile.service.properties.BackEndProperties;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class AppProxyRestWebService implements AppWebService{

    private BackEndProperties backEndProperties;
    private RestTemplate restTemplate;

    public AppProxyRestWebService() {
        try {
            backEndProperties = BackEndProperties.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    }

    @Override
    public String getNewId() {
        return restTemplate.getForObject(
                backEndProperties.getStateNewIdUrl(),
                String.class);
    }

    @Override
    public AppStateDTO getAppState(String appId) {
        return restTemplate.getForObject(
                backEndProperties.getStateForAppIdUrl(),
                AppStateDTO.class, appId);
    }

    @Override
    public Boolean roundStarted(String appId, Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getStateRoundStartedUrl(),
                Boolean.class, appId, roundId);
    }

    @Override
    public Boolean loadingIn(Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getStateLoadingInUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean nextPacket(Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getStateNextPacketUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean ongoingDelivery(Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getStateOngoingDeliveryUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean roundEnded(Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getStateRoundEndedUrl(),
                Boolean.class, roundId);
    }
}
