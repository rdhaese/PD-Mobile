package be.rdhaese.eindproject.mobile_service;

import java.util.List;

import be.rdhaese.packetdelivery.dto.PacketDTO;

/**
 * Created by RDEAX37 on 5/01/2016.
 */
public interface RoundService {
    Long getNewRound(int amountOfPackets);
    List<PacketDTO> getPackets(Long roundId);
}
