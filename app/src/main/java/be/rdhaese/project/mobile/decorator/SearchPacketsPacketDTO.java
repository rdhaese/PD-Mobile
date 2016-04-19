package be.rdhaese.project.mobile.decorator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import be.rdhaese.packetdelivery.dto.PacketDTO;

public class SearchPacketsPacketDTO extends ParcelablePacketDTODecorator implements Parcelable {

    private ParcelablePacketDTODecorator parcelablePacketDTODecorator;
    private Boolean found = false;
    private Boolean lost = false;

    public static Collection<SearchPacketsPacketDTO> mapCollectionPacketDTOToDecorator(Collection<PacketDTO> packetDTOs){
        Collection<SearchPacketsPacketDTO> searchPacketsPacketDTOs = new ArrayList<>();
        for (PacketDTO packetDTO : packetDTOs){
            searchPacketsPacketDTOs.add(new SearchPacketsPacketDTO(new ParcelablePacketDTODecorator(packetDTO)));
        }
        return searchPacketsPacketDTOs;
    }

    public static Collection<PacketDTO> mapCollectionSearchPacketsToDTO(Collection<SearchPacketsPacketDTO> searchPacketsPacketDTOs){
        Collection<? extends PacketDTO> packetDTOs = new ArrayList<>(searchPacketsPacketDTOs);
        return (Collection<PacketDTO>) packetDTOs;
    }

    public static Collection<ParcelablePacketDTODecorator> mapCollectionSearchPacketsToParcelableDTO(Collection<SearchPacketsPacketDTO> searchPacketsPacketDTOs){
        Collection<? extends ParcelablePacketDTODecorator> packetDTOs = new ArrayList<>(searchPacketsPacketDTOs);
        return (Collection<ParcelablePacketDTODecorator>) packetDTOs;
    }

    public SearchPacketsPacketDTO(ParcelablePacketDTODecorator parcelablePacketDTODecorator){
        super(parcelablePacketDTODecorator);
        this.parcelablePacketDTODecorator = parcelablePacketDTODecorator;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SearchPacketsPacketDTO that = (SearchPacketsPacketDTO) o;

        if (parcelablePacketDTODecorator != null ? !parcelablePacketDTODecorator.equals(that.parcelablePacketDTODecorator) : that.parcelablePacketDTODecorator != null)
            return false;
        if (found != null ? !found.equals(that.found) : that.found != null) return false;
        return !(lost != null ? !lost.equals(that.lost) : that.lost != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parcelablePacketDTODecorator != null ? parcelablePacketDTODecorator.hashCode() : 0);
        result = 31 * result + (found != null ? found.hashCode() : 0);
        result = 31 * result + (lost != null ? lost.hashCode() : 0);
        return result;
    }

    public String getPacketId() {
        return parcelablePacketDTODecorator.getPacketId();
    }

    public void setPacketId(String packetId) {
        parcelablePacketDTODecorator.setPacketId(packetId);
    }

    public String getPacketStatus() {
        return parcelablePacketDTODecorator.getPacketStatus();
    }

    public void setPacketStatus(String packetStatus) {
        parcelablePacketDTODecorator.setPacketStatus(packetStatus);
    }

    public Date getStatusChangedOn() {
        return parcelablePacketDTODecorator.getStatusChangedOn();
    }

    public void setStatusChangedOn(Date statusChangedOn) {
        parcelablePacketDTODecorator.setStatusChangedOn(statusChangedOn);
    }

    public String getClientName() {
        return parcelablePacketDTODecorator.getClientName();
    }

    public void setClientName(String clientName) {
        parcelablePacketDTODecorator.setClientName(clientName);
    }

    public String getClientPhone() {
        return parcelablePacketDTODecorator.getClientPhone();
    }

    public void setClientPhone(String clientPhone) {
        parcelablePacketDTODecorator.setClientPhone(clientPhone);
    }

    public String getClientEmail() {
        return parcelablePacketDTODecorator.getClientEmail();
    }

    public void setClientEmail(String clientEmail) {
        parcelablePacketDTODecorator.setClientEmail(clientEmail);
    }

    public String getClientStreet() {
        return parcelablePacketDTODecorator.getClientStreet();
    }

    public void setClientStreet(String clientStreet) {
        parcelablePacketDTODecorator.setClientStreet(clientStreet);
    }

    public String getClientNumber() {
        return parcelablePacketDTODecorator.getClientNumber();
    }

    public void setClientNumber(String clientNumber) {
        parcelablePacketDTODecorator.setClientNumber(clientNumber);
    }

    public String getClientMailbox() {
        return parcelablePacketDTODecorator.getClientMailbox();
    }

    public void setClientMailbox(String clientMailbox) {
        parcelablePacketDTODecorator.setClientMailbox(clientMailbox);
    }

    public String getClientCity() {
        return parcelablePacketDTODecorator.getClientCity();
    }

    public void setClientCity(String clientCity) {
        parcelablePacketDTODecorator.setClientCity(clientCity);
    }

    public String getClientPostalCode() {
        return parcelablePacketDTODecorator.getClientPostalCode();
    }

    public void setClientPostalCode(String clientPostalCode) {
        parcelablePacketDTODecorator.setClientPostalCode(clientPostalCode);
    }

    public String getDeliveryName() {
        return parcelablePacketDTODecorator.getDeliveryName();
    }

    public void setDeliveryName(String deliveryName) {
        parcelablePacketDTODecorator.setDeliveryName(deliveryName);
    }

    public String getDeliveryPhone() {
        return parcelablePacketDTODecorator.getDeliveryPhone();
    }

    public void setDeliveryPhone(String deliveryPhone) {
        parcelablePacketDTODecorator.setDeliveryPhone(deliveryPhone);
    }

    public String getDeliveryEmail() {
        return parcelablePacketDTODecorator.getDeliveryEmail();
    }

    public void setDeliveryEmail(String deliveryEmail) {
        parcelablePacketDTODecorator.setDeliveryEmail(deliveryEmail);
    }

    public String getDeliveryStreet() {
        return parcelablePacketDTODecorator.getDeliveryStreet();
    }

    public void setDeliveryStreet(String deliveryStreet) {
        parcelablePacketDTODecorator.setClientStreet(deliveryStreet);
    }

    public String getDeliveryNumber() {
        return parcelablePacketDTODecorator.getDeliveryNumber();
    }

    public void setDeliveryNumber(String deliveryNumber) {
        parcelablePacketDTODecorator.setDeliveryNumber(deliveryNumber);
    }

    public String getDeliveryMailbox() {
        return parcelablePacketDTODecorator.getDeliveryMailbox();
    }

    public void setDeliveryMailbox(String deliveryMailbox) {
        parcelablePacketDTODecorator.setDeliveryMailbox(deliveryMailbox);
    }

    public String getDeliveryCity() {
        return parcelablePacketDTODecorator.getDeliveryCity();
    }

    public void setDeliveryCity(String deliveryCity) {
        parcelablePacketDTODecorator.setDeliveryCity(deliveryCity);
    }

    public String getDeliveryPostalCode() {
        return parcelablePacketDTODecorator.getDeliveryPostalCode();
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        parcelablePacketDTODecorator.setDeliveryPostalCode(deliveryPostalCode);
    }

    public String getDeliveryRegionNameNl() {
        return parcelablePacketDTODecorator.getDeliveryRegionNameNl();
    }

    public void setDeliveryRegionNameNl(String deliveryRegionName) {
        parcelablePacketDTODecorator.setDeliveryRegionNameNl(deliveryRegionName);
    }

    public String getDeliveryRegionNameFr() {
        return parcelablePacketDTODecorator.getDeliveryRegionNameFr();
    }

    public void setDeliveryRegionNameFr(String deliveryRegionName) {
        parcelablePacketDTODecorator.setDeliveryRegionNameFr(deliveryRegionName);
    }

    public String getDeliveryRegionNameDe() {
        return parcelablePacketDTODecorator.getDeliveryRegionNameDe();
    }

    public void setDeliveryRegionNameDe(String deliveryRegionName) {
        parcelablePacketDTODecorator.setDeliveryRegionNameDe(deliveryRegionName);
    }

    public String getDeliveryRegionNameEn() {
        return parcelablePacketDTODecorator.getDeliveryRegionNameEn();
    }

    public void setDeliveryRegionNameEn(String deliveryRegionName) {
        parcelablePacketDTODecorator.setDeliveryRegionNameEn(deliveryRegionName);
    }

    public String getDeliveryRegionCode() {
        return parcelablePacketDTODecorator.getDeliveryRegionCode();
    }

    public void setDeliveryRegionCode(String deliveryRegionCode) {
        parcelablePacketDTODecorator.setDeliveryRegionCode(deliveryRegionCode);
    }

    public ParcelablePacketDTODecorator getParcelablePacketDTODecorator() {
        return parcelablePacketDTODecorator;
    }

    public void setParcelablePacketDTODecorator(ParcelablePacketDTODecorator parcelablePacketDTODecorator) {
        this.parcelablePacketDTODecorator = parcelablePacketDTODecorator;
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
        parcelablePacketDTODecorator = (ParcelablePacketDTODecorator) in.readValue(PacketDTO.class.getClassLoader());
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
        dest.writeValue(parcelablePacketDTODecorator);
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