package be.rdhaese.packetdelivery.mobile.service;


import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import be.rdhaese.packetdelivery.back_end.web_service.interfaces.DeliveryRoundWebService;
import be.rdhaese.packetdelivery.dto.AddressDTO;
import be.rdhaese.packetdelivery.dto.LongLatDTO;
import be.rdhaese.packetdelivery.dto.PacketDTO;
import be.rdhaese.packetdelivery.mobile.service.properties.BackEndProperties;

//import be.rdhaese.packetdelivery.back_end.application.web_service.interfaces.DeliveryRoundWebService;

public class DeliveryRoundProxyRestWebService extends AbstractService implements DeliveryRoundWebService {

    /**
     * @param amountOfPackets for the new round
     * @return the id of the new round
     */
    @Override
    public Long newRound(int amountOfPackets) {
        return getRestTemplate().getForObject(
                getBackEndProperties().getNewRoundUrl(),
                Long.class, amountOfPackets);
    }

    @Override
    public List<PacketDTO> getPackets(Long roundId) throws Exception {
        return Arrays.asList(
                getRestTemplate().getForEntity(
                        getBackEndProperties().getPacketsUrl(),
                        PacketDTO[].class, roundId)
                        .getBody());
    }

    @Override
    public Boolean markAsLost(Long roundId, PacketDTO packetDTO) throws Exception {
        return getRestTemplate().postForObject(getBackEndProperties().getMarkAsLostUrl(), packetDTO, Boolean.class, roundId);
    }

     @Override
    public Boolean deliver(Long roundId, PacketDTO packetDTO) throws Exception {
        return getRestTemplate().postForObject(getBackEndProperties().getDeliverUrl(), packetDTO, Boolean.class, roundId);
    }

     @Override
    public Boolean cannotDeliver(Long roundId, String reason, PacketDTO packetDTO) throws Exception {
        return getRestTemplate().postForObject(getBackEndProperties().getCannotDeliverUrl(), packetDTO, Boolean.class, roundId, reason);
    }

     @Override
    public Boolean addRemark(Long roundId, String remark) {
        return getRestTemplate().getForObject(getBackEndProperties().getAddRemarkUrl(), Boolean.class, roundId, remark);
    }

     @Override
    public Boolean addLocationUpdate(Long roundId, LongLatDTO longLatDTO) {
        return getRestTemplate().postForObject(getBackEndProperties().getAddLocationUpdateUrl(), longLatDTO, Boolean.class, roundId);
    }

    @Override
    public Boolean endRound(Long roundId) {
        return getRestTemplate().getForObject(
                getBackEndProperties().getEndRoundUrl(),
                Boolean.class, roundId);
    }

    @Override
    public Boolean startRound(Long roundId) throws Exception {
        return getRestTemplate().getForObject(
                getBackEndProperties().getStartRoundUrl(),
                Boolean.class, roundId);
    }

    @Override
    public AddressDTO getCompanyAddress(){
        return getRestTemplate().getForObject(getBackEndProperties().getCompanyAddressUrl(), AddressDTO.class);
    }
}
