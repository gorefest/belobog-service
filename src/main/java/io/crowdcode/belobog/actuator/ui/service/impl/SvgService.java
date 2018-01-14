package io.crowdcode.belobog.actuator.ui.service.impl;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.AbstractContext;

@Service
public class SvgService {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    GPIOService gpioService;

    public void prepareSvgContext(AbstractContext context){
        int numberOfSlots = configurationService.getNumberOfSlots();
        boolean[] activeSlots = configurationService.getActiveSlots();
        Integer[] slot2Pin = configurationService.getSlot2Pin();
        String[] slotEnabledStyles = configurationService.getSlotEnabledStyles();
        String[] slotDisabledStyles = configurationService.getSlotDisabledStyles();
        for (int i = 0; i < numberOfSlots; i++){
            if (activeSlots[i]){
                Integer pin = slot2Pin[i];
                boolean isSet = gpioService.isGPIOEnabled(pin);
                String activeState="slot"+(i+1)+".style";
                if (isSet) {
                    context.setVariable(activeState, slotEnabledStyles[i]);
                } else {
                    context.setVariable(activeState, slotDisabledStyles[i]);
                }
            }
        }
    }


    public String prepareSvg(String context){
        int numberOfSlots = configurationService.getNumberOfSlots();
        boolean[] activeSlots = configurationService.getActiveSlots();
        Integer[] slot2Pin = configurationService.getSlot2Pin();
        String[] slotEnabledStyles = configurationService.getSlotEnabledStyles();
        String[] slotDisabledStyles = configurationService.getSlotDisabledStyles();
        String[] slotLabels = configurationService.getSlotLabels();
        for (int i = 0; i < numberOfSlots; i++){
            if (activeSlots[i]){
                Integer pin = slot2Pin[i];
                boolean isSet = gpioService.isGPIOEnabled(pin);
                String activeState="${slot"+(i+1)+".style}";
                String state="${slot"+(i+1)+".state}";
                if (isSet) {
                    context = context.replace(activeState, slotEnabledStyles[i]);
                    context = context.replace(state, "ENABLED");
                } else {
                    context = context.replace(activeState, slotDisabledStyles[i]);
                    context = context.replace(state, "DISABLED");
                }

                context = context.replace("${slot"+(i+1)+".title}", slotLabels[i]);

            }
        }
        return context;
    }
}
