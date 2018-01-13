package io.crowdcode.belobog.actuator.ui.service.impl;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

public class GPIOFakeServiceImpl implements GPIOService {

    LocalDateTime started = LocalDateTime.now();
    boolean[] fakeStates;


    @Autowired
    ConfigurationService configurationService;

    @PostConstruct
    public void postConstruct(){
        fakeStates=new boolean[configurationService.numberOfSlots*3];
    }



    public boolean isGPIOEnabled(int pin){
        return fakeStates[pin];
    }

    @Override
    public boolean setEnabled(int pin) {
        try {
            fakeStates[pin] = true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean setDisabled(int pin) {
        try {
            fakeStates[pin] = false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean disableAll() {
        return setAll(false);
    }

    @Override
    public boolean enableAll() {
        return setAll(true);
    }

    private boolean setAll(boolean state) {
        try {
            for (int i =0; i<fakeStates.length; i++) {
                fakeStates[i] = state;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[FAKE-SERVICE]"+super.toString();
    }
}
