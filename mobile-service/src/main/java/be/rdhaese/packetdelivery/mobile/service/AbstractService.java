package be.rdhaese.packetdelivery.mobile.service;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import be.rdhaese.packetdelivery.mobile.service.properties.BackEndProperties;

/**
 * Created by RDEAX37 on 28/04/2016.
 */
public class AbstractService {

    private static Integer TIMOUT = 1000 * 10; //10 seconds

    private RestTemplate restTemplate;
    private BackEndProperties backEndProperties;

    public AbstractService(){
        restTemplate = new MyRestTemplate(TIMOUT);
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

        try {
            backEndProperties = BackEndProperties.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected RestTemplate getRestTemplate(){
        return restTemplate;
    }

    protected BackEndProperties getBackEndProperties(){
        return backEndProperties;
    }

    private class MyRestTemplate extends RestTemplate {
        public MyRestTemplate(Integer timeout) {
            if (getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
                ((SimpleClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(timeout);
                ((SimpleClientHttpRequestFactory) getRequestFactory()).setReadTimeout(timeout);
            } else if (getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
                ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setReadTimeout(timeout);
                ((HttpComponentsClientHttpRequestFactory) getRequestFactory()).setConnectTimeout(timeout);
            }
        }
    }
}
