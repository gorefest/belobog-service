package io.crowdcode.belobog.actuator.ui.service.impl;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DefaultGPIOEnforcerService {

    Logger logger = LoggerFactory.getLogger(DefaultGPIOEnforcerService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    GPIOService gpioService;

    public void setDefaults(){
        boolean active[] = configurationService.getActiveSlots();
        boolean enabled[] = configurationService.getEnabledSlots();
        Integer[] pins = configurationService.getSlot2Pin();
        for (int i = 0; i < configurationService.getNumberOfSlots(); i++) {
            if (active[i]){
                if (enabled[i]){
                    if (gpioService.setEnabled(pins[i])) {
                        logger.warn("SET SLOT "+(i+1)+" TO TRUE ON PIN "+pins[i]);
                    } else {
                        logger.error("FAILED TO SET SLOT "+(i+1)+" TO TRUE ON PIN "+pins[i]+"! THIS MUST BE CHECKED");
                    }
                } else {
                    if (gpioService.setDisabled(pins[i])) {
                        logger.warn("SET SLOT "+(i+1)+" TO FALSE ON PIN "+pins[i]);
                    } else {
                        logger.error("FAILED TO SET SLOT "+(i+1)+" TO FALSE ON PIN "+pins[i]+"! THIS MUST BE CHECKED");
                    }
                }
            }
        }
    }

}
