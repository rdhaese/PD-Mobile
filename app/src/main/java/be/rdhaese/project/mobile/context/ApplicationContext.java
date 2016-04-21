package be.rdhaese.project.mobile.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RDEAX37 on 3/04/2016.
 */
public class ApplicationContext {

    private ApplicationContext(){
        beans = new HashMap<>();
    };

    private static ApplicationContext instance;

    public static ApplicationContext getInstance(){
        if (instance == null){
            instance = new ApplicationContext();
        }
        return instance;
    }

    private Map<String, Object> beans;

    public Map<String, Object> getBeans(){
        return beans;
    }

    public <T> T getBean(String key){
        Object bean = beans.get(key);
        if (bean == null){
            return null;
        }
        return (T) bean;
    }

    public void putBean(String key, Object bean){
        beans.put(key, bean);
    }
}
