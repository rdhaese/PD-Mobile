package be.rdhaese.project.mobile.decorator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import be.rdhaese.packetdelivery.dto.PacketDTO;

/**
 * Created by RDEAX37 on 7/04/2016.
 */
public class ParcelablePacketDTODecorator extends PacketDTO implements Parcelable {

    PacketDTO packetDTO;

    protected ParcelablePacketDTODecorator(){}

    public ParcelablePacketDTODecorator(PacketDTO packetDTO){
        this.packetDTO = packetDTO;
    }

    public static Collection<ParcelablePacketDTODecorator> mapCollectionToDecorator(Collection<PacketDTO> packetDTOs){
        Collection<ParcelablePacketDTODecorator> parcelablePacketDTODecorators = new ArrayList<>();
        for (PacketDTO packetDTO : packetDTOs){
            parcelablePacketDTODecorators.add(new ParcelablePacketDTODecorator(packetDTO));
        }
        return parcelablePacketDTODecorators;
    }

    public static Collection<PacketDTO> mapCollectionToDTO(Collection<ParcelablePacketDTODecorator> parcelablePacketDTODecorators){
        Collection<? extends PacketDTO> packetDTOs = new ArrayList<>(parcelablePacketDTODecorators);
        return (Collection<PacketDTO>) packetDTOs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ParcelablePacketDTODecorator that = (ParcelablePacketDTODecorator) o;

        return !(packetDTO != null ? !packetDTO.equals(that.packetDTO) : that.packetDTO != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (packetDTO != null ? packetDTO.hashCode() : 0);
        return result;
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

    public PacketDTO getParcelablePacketDTODecorator() {
        return packetDTO;
    }

    public void setParcelablePacketDTODecorator(PacketDTO packetDTO) {
        this.packetDTO = packetDTO;
    }

    protected ParcelablePacketDTODecorator(Parcel in) {
        packetDTO = (PacketDTO) in.readValue(PacketDTO.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(packetDTO);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParcelablePacketDTODecorator> CREATOR = new Parcelable.Creator<ParcelablePacketDTODecorator>() {
        @Override
        public ParcelablePacketDTODecorator createFromParcel(Parcel in) {
            return new ParcelablePacketDTODecorator(in);
        }

        @Override
        public ParcelablePacketDTODecorator[] newArray(int size) {
            return new ParcelablePacketDTODecorator[size];
        }
    };
}