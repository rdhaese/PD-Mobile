package be.rdhaese.packetdelivery.mobile.service;


import com.sun.org.apache.xpath.internal.operations.Bool;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                backEndProperties.getNewRoundUrl(amountOfPackets),
                Long.class);
    }

    //@Override
    public List<PacketDTO> getPackets(Long roundId) {
        return Arrays.asList(
                restTemplate.getForEntity(
                        backEndProperties.getPacketsUrl(roundId),
                        PacketDTO[].class)
                        .getBody());
    }

    public Boolean markAsLost(Long roundId, PacketDTO packetDTO) {
        Map<String, Long> vars = new HashMap<String, Long>();
        vars.put("roundId", roundId);
        return restTemplate.postForObject(backEndProperties.getMarkAsLostUrl(), packetDTO, Boolean.class, vars);
    }
   // @Override
    public Boolean deliver(Long roundId, PacketDTO packetDTO) {
        return null;
    }

   // @Override
    public Boolean cannotDeliver(Long roundId, PacketDTO packetDTO, String s) {
        return null;
    }

   // @Override
    public Boolean addRemark(Long roundId, String s) {
        return null;
    }

   // @Override
    public Boolean addLocationUpdate(Long roundId, LongLatDTO longLatDTO) {
        return null;
    }

    @Override
    public Boolean endRound(Long roundId) {
        return restTemplate.getForObject(
                backEndProperties.getEndRoundUrl(roundId),
                Boolean.class);
    }
}
