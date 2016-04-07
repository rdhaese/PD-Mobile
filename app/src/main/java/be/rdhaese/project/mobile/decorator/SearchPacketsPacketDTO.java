package be.rdhaese.project.mobile.decorator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import be.rdhaese.packetdelivery.dto.PacketDTO;

public class SearchPacketsPacketDTO extends PacketDTO implements Parcelable {

    private PacketDTO packetDTO;
    private Boolean found = false;
    private Boolean lost = false;

    public static Collection<SearchPacketsPacketDTO> mapCollectionToDecorator(Collection<PacketDTO> packetDTOs){
        Collection<SearchPacketsPacketDTO> searchPacketsPacketDTOs = new ArrayList<>();
        for (PacketDTO packetDTO : packetDTOs){
            searchPacketsPacketDTOs.add(new SearchPacketsPacketDTO(packetDTO));
        }
        return searchPacketsPacketDTOs;
    }

    public static Collection<PacketDTO> mapCollectionToDTO(Collection<SearchPacketsPacketDTO> searchPacketsPacketDTOs){
        Collection<? extends PacketDTO> packetDTOs = new ArrayList<>(searchPacketsPacketDTOs);
        return (Collection<PacketDTO>) packetDTOs;
    }

    public SearchPacketsPacketDTO(PacketDTO packetDTO){
        this.packetDTO = packetDTO;
    }

    public String getPacketId() {
        return packetDTO.getPacketId();
    }

    public void setPacketId(String packetId) {
        packetDTO.setPacketId(packetId);
    }

    public String getPacketStatus() {
        return packetDTO.getPacketStatus();
    }

    public void setPacketStatus(String packetStatus) {
        packetDTO.setPacketStatus(packetStatus);
    }

    public Date getStatusChangedOn() {
        return packetDTO.getStatusChangedOn();
    }

    public void setStatusChangedOn(Date statusChangedOn) {
        packetDTO.setStatusChangedOn(statusChangedOn);
    }

    public String getClientName() {
        return packetDTO.getClientName();
    }

    public void setClientName(String clientName) {
        packetDTO.setClientName(clientName);
    }

    public String getClientPhone() {
        return packetDTO.getClientPhone();
    }

    public void setClientPhone(String clientPhone) {
        packetDTO.setClientPhone(clientPhone);
    }

    public String getClientEmail() {
        return packetDTO.getClientEmail();
    }

    public void setClientEmail(String clientEmail) {
        packetDTO.setClientEmail(clientEmail);
    }

    public String getClientStreet() {
        return packetDTO.getClientStreet();
    }

    public void setClientStreet(String clientStreet) {
        packetDTO.setClientStreet(clientStreet);
    }

    public String getClientNumber() {
        return packetDTO.getClientNumber();
    }

    public void setClientNumber(String clientNumber) {
        packetDTO.setClientNumber(clientNumber);
    }

    public String getClientMailbox() {
        return packetDTO.getClientMailbox();
    }

    public void setClientMailbox(String clientMailbox) {
        packetDTO.setClientMailbox(clientMailbox);
    }

    public String getClientCity() {
        return packetDTO.getClientCity();
    }

    public void setClientCity(String clientCity) {
        packetDTO.setClientCity(clientCity);
    }

    public String getClientPostalCode() {
        return packetDTO.getClientPostalCode();
    }

    public void setClientPostalCode(String clientPostalCode) {
        packetDTO.setClientPostalCode(clientPostalCode);
    }

    public String getDeliveryName() {
        return packetDTO.getDeliveryName();
    }

    public void setDeliveryName(String deliveryName) {
        packetDTO.setDeliveryName(deliveryName);
    }

    public String getDeliveryPhone() {
        return packetDTO.getDeliveryPhone();
    }

    public void setDeliveryPhone(String deliveryPhone) {
        packetDTO.setDeliveryPhone(deliveryPhone);
    }

    public String getDeliveryEmail() {
        return packetDTO.getDeliveryEmail();
    }

    public void setDeliveryEmail(String deliveryEmail) {
        packetDTO.setDeliveryEmail(deliveryEmail);
    }

    public String getDeliveryStreet() {
        return packetDTO.getDeliveryStreet();
    }

    public void setDeliveryStreet(String deliveryStreet) {
        packetDTO.setClientStreet(deliveryStreet);
    }

    public String getDeliveryNumber() {
        return packetDTO.getDeliveryNumber();
    }

    public void setDeliveryNumber(String deliveryNumber) {
        packetDTO.setDeliveryNumber(deliveryNumber);
    }

    public String getDeliveryMailbox() {
        return packetDTO.getDeliveryMailbox();
    }

    public void setDeliveryMailbox(String deliveryMailbox) {
        packetDTO.setDeliveryMailbox(deliveryMailbox);
    }

    public String getDeliveryCity() {
        return packetDTO.getDeliveryCity();
    }

    public void setDeliveryCity(String deliveryCity) {
        packetDTO.setDeliveryCity(deliveryCity);
    }

    public String getDeliveryPostalCode() {
        return packetDTO.getDeliveryPostalCode();
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        packetDTO.setDeliveryPostalCode(deliveryPostalCode);
    }

    public String getDeliveryRegionName() {
        return packetDTO.getDeliveryRegionName();
    }

    public void setDeliveryRegionName(String deliveryRegionName) {
        packetDTO.setDeliveryRegionName(deliveryRegionName);
    }

    public String getDeliveryRegionCode() {
        return packetDTO.getDeliveryRegionCode();
    }

    public void setDeliveryRegionCode(String deliveryRegionCode) {
        packetDTO.setDeliveryRegionCode(deliveryRegionCode);
    }

    public PacketDTO getPacketDTO() {
        return packetDTO;
    }

    public void setPacketDTO(PacketDTO packetDTO) {
        this.packetDTO = packetDTO;
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }

    public Boolean getLost() {
        return lost;
    }

    public void setLost(Boolean lost) {
        this.lost = lost;
    }

    protected SearchPacketsPacketDTO(Parcel in) {
        packetDTO = (PacketDTO) in.readValue(PacketDTO.class.getClassLoader());
        byte foundVal = in.readByte();
        found = foundVal == 0x02 ? null : foundVal != 0x00;
        byte lostVal = in.readByte();
        lost = lostVal == 0x02 ? null : lostVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(packetDTO);
        if (found == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (found ? 0x01 : 0x00));
        }
        if (lost == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (lost ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SearchPacketsPacketDTO> CREATOR = new Parcelable.Creator<SearchPacketsPacketDTO>() {
        @Override
        public SearchPacketsPacketDTO createFromParcel(Parcel in) {
            return new SearchPacketsPacketDTO(in);
        }

        @Override
        public SearchPacketsPacketDTO[] newArray(int size) {
            return new SearchPacketsPacketDTO[size];
        }
    };
}