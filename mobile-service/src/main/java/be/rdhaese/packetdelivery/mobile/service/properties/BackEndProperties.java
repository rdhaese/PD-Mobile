package be.rdhaese.packetdelivery.mobile.service.properties;

import org.codehaus.jackson.map.deser.SettableBeanProperty;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.zip.InflaterInputStream;

/**
 * Created by RDEAX37 on 6/04/2016.
 */
public class BackEndProperties {

    private static final String PROPERTY_FILE_NAME = "back-end.properties";

    private Properties backEndProperties = new Properties();

    public BackEndProperties() throws IOException {
        try (InputStream propertiesAsInputStream =
                     getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            backEndProperties.load(propertiesAsInputStream);
        }
    }

    public String getIp() {
        return backEndProperties.getProperty("ip");
    }

    public String getPort() {
        return backEndProperties.getProperty("port");
    }

    public String getServerPath() {
        return String.format("http://%s:%s", getIp(), getPort());
    }

    public String getNewRoundUrl(int amountOfPackets) {
        return String.format(
                "%s/%s%s",
                getServerPath(),
                backEndProperties.getProperty("newRound"),
                amountOfPackets);
    }

    public String getPacketsUrl(Long roundId) {
        return String.format(
                "%s/%s%s",
                getServerPath(),
                backEndProperties.getProperty("getPackets"),
                roundId);
    }

    public String getMarkAsLostUrl() {
        return String.format(
                "%s/%s",
                getServerPath(),
                backEndProperties.getProperty("markAsLost"));
    }

    public String getEndRoundUrl(Long roundId) {
        return String.format(
                "%s/%s%s",
                getServerPath(),
                backEndProperties.getProperty("endRound"),
                roundId);
    }

    public String getStartRoundUrl(Long roundId) {
        return String.format(
                "%s/%s%s",
                getServerPath(),
                backEndProperties.getProperty("startRound"),
                roundId);
    }

    public String getAddRemarkUrl(String remark) {
        return String.format(
                "%s/%s%s",
                getServerPath(),
                backEndProperties.getProperty("addRemark"),
                remark);
    }

    public String getCannotDeliverUrl(String reason){
        return String.format(
                "%s/%s%s",
                getServerPath(),
                backEndProperties.getProperty("cannotDeliver"),
                reason
        );
    }

    public String getDeliverUrl(){
        return String.format(
                "%s/%s",
                getServerPath(),
                backEndProperties.getProperty("deliver")
        );
    }

    public String getAddLocationUpdateUrl() {
        return String.format(
                "%s/%s",
                getServerPath(),
                backEndProperties.getProperty("addLocationUpdate"));
    }
}
