package be.rdhaese.eindproject.mobile_service.impl;


import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import be.rdhaese.eindproject.mobile_service.RoundService;
import be.rdhaese.packetdelivery.dto.PacketDTO;

public class RoundServiceImpl implements RoundService {

    private static final String URI_NEW_ROUND = "http://10.0.3.2:8080/round/new?amountOfPackets=";
    private static final String URI_GET_PACKETS = "http://10.0.3.2:8080/round/packets?roundId=";

    /**
     * @param amountOfPackets for the new round
     * @return the id of the new round
     */
    public Long getNewRound(int amountOfPackets){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate.getForEntity(String.format("%s%s", URI_NEW_ROUND, amountOfPackets), Long.class).getBody();
    }

    @Override
    public List<PacketDTO> getPackets(Long roundId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate.getForEntity(String.format("%s%s", URI_GET_PACKETS, roundId), List.class).getBody();
    }
}
