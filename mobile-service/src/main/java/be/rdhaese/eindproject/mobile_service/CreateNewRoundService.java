package be.rdhaese.eindproject.mobile_service;


import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class CreateNewRoundService {

    private static final String URI = "http://10.0.3.2:8080/round/new?amountOfPackets=";
    public String getNewRound(int amountOfPackets){
        System.out.println("HI FROM SERVICE");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate.getForEntity(String.format("%s%s", URI, amountOfPackets), String.class).getBody();
    }
}
