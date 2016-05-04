package be.rdhaese.packetdelivery.mobile.service.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

/**
 * Created by RDEAX37 on 6/04/2016.
 */
public class BackEndProperties {

    private static final String PROPERTY_FILE_NAME = "back-end.properties";

    private static BackEndProperties instance;

    private Properties properties = new Properties();

    private BackEndProperties() throws IOException {
        try (InputStream propertiesAsInputStream =
                     getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            properties.load(propertiesAsInputStream);
        }
    }

    public static BackEndProperties getInstance() throws IOException {
        if (instance == null){
            instance = new BackEndProperties();
        }
        return instance;
    }

    public String getIp() {
        return properties.getProperty("ip");
    }

    public String getPort() {
        return properties.getProperty("port");
    }

    public String getServerPath() {
        return String.format("http://%s:%s", getIp(), getPort());
    }

    public String getWithServerPath(String propertyKey){
        return String.format(
                "%s/%s",
                getServerPath(),
                properties.getProperty(propertyKey)
        );
    }

    public String getNewRoundUrl() {
        return getWithServerPath("newRound");
    }

    public String getPacketsUrl() {
        return getWithServerPath("getPackets");
    }

    public String getMarkAsLostUrl() {
        return getWithServerPath("markAsLost");
    }

    public String getEndRoundUrl() {
        return getWithServerPath("endRound");
    }

    public String getStartRoundUrl() {
        return getWithServerPath("startRound");
    }

    public String getAddRemarkUrl() {
        return getWithServerPath("addRemark");
    }

    public String getCannotDeliverUrl(){
        return getWithServerPath("cannotDeliver");
    }

    public String getDeliverUrl(){
        return getWithServerPath("deliver");
    }

    public String getAddLocationUpdateUrl() {
        return getWithServerPath("addLocationUpdate");
    }

    public String getCompanyAddressUrl() {
        return getWithServerPath("companyAddress");
    }

    public String getStateNewIdUrl() { return getWithServerPath("state.new"); }

    public String getStateForAppIdUrl() {return getWithServerPath("state.get");}

    public String getStateRoundStartedUrl() {return getWithServerPath("state.roundStarted");}

    public String getStateLoadingInUrl() {return getWithServerPath("state.loadingIn");}

    public String getStateNextPacketUrl() {return getWithServerPath("state.nextPacket");}

    public String getStateOngoingDeliveryUrl() {return getWithServerPath("state.ongoingDelivery");}

    public String getStateRoundEndedUrl() {return getWithServerPath("state.roundEnded");}

}
