package be.rdhaese.packetdelivery.mobile.service;


import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.LongLatDTO;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.packetdelivery.mobile.service.properties.BackEndProperties;

public class DeliveryRoundServiceProxyRestWebService implements DeliveryRoundWebService {

    private BackEndProperties backEndProperties;
    private RestTemplate restTemplate;

    public DeliveryRoundServiceProxyRestWebService() {
        try {
            backEndProperties = new BackEndProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    }

    /**
     * @param amountOfPackets for the new round
     * @return the id of the new round
     */
    //@Override
    public Long newRound(int amountOfPackets) {
        return restTemplate.getForObject(
                backEndProperties.getNewRoundUrl(),
                Long.class, amountOfPackets);
    }

    //@Override
    public List<PacketDTO> getPackets(Long roundId) {
        return Arrays.asList(
                restTemplate.getForEntity(
                        backEndProperties.getPacketsUrl(),
                        PacketDTO[].class, roundId)
                        .getBody());
    }

    public Boolean markAsLost(Long roundId, PacketDTO packetDTO) {
        return restTemplate.postForObject(backEndProperties.getMarkAsLostUrl(), packetDTO, Boolean.class, roundId);
    }

    // @Override
    public Boolean deliver(Long roundId, PacketDTO packetDTO) {
        return restTemplate.postForObject(backEndProperties.getDeliverUrl(), packetDTO, Boolean.class, roundId);
    }

    // @Override
    public Boolean cannotDeliver(Long roundId, PacketDTO packetDTO, String reason) {
        return restTemplate.postForObject(backEndProperties.getCannotDeliverUrl(), packetDTO, Boolean.class, roundId, reason);
    }

    // @Override
    public Boolean addRemark(Long roundId, String remark) {
        return restTemplate.getForObject(backEndProperties.getAddRemarkUrl(), Boolean.class, roundId, remark);
    }

    // @Override
    public Boolean addLocationUpdate(Long roundId, LongLatDTO longLatDTO) {
        return restTemplate.postForObject(backEndProperties.getAddLocationUpdateUrl(), longLatDTO, Boolean.class, roundId);
    }

    //@Override
    public Boolean endRound(Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getEndRoundUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean startRound(Long roundId) {
       return restTemplate.getForObject(
               backEndProperties.getStartRoundUrl(),
               Boolean.class, roundId);
    }
}
