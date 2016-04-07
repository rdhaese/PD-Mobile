package be.rdhaese.project.mobile.application;

import android.app.Application;

import be.rdhaese.packetdelivery.mobile.service.DeliveryRoundServiceProxyRestWebService;
import be.rdhaese.project.mobile.context.ApplicationContext;

/**
 * Created by RDEAX37 on 3/04/2016.
 */
public class ApplicationExtension extends Application {

    @Override
    public void onCreate() {
        ApplicationContext context = ApplicationContext.getInstance();
        context.putBean("roundService", new DeliveryRoundServiceProxyRestWebService());
        super.onCreate();
    }
}
