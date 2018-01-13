package io.crowdcode.belobog.actuator.ui.service.impl;

import com.pi4j.io.gpio.*;
import io.crowdcode.belobog.actuator.ui.service.GPIOService;


public class GPIOServiceImpl implements GPIOService {

    private final ConfigurationService configurationService;

    private GpioController gpio;
    private GpioPinDigitalOutput[] pins;
    private boolean[] enabledPins;

    public GPIOServiceImpl(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        enabledPins = configurationService.getEnabledPins();
    }

    public void postConstruct() {
        // create gpio controller
        gpio = GpioFactory.getInstance();

        pins = new GpioPinDigitalOutput[8];


        pins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, label(0) , state(0));
        pins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, label(1) , state(1));
        pins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, label(2) , state(2));
        pins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, label(3) , state(3));
        pins[4] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, label(4) , state(4));
        pins[5] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, label(5) , state(5));
        pins[6] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, label(6) , state(6));
        pins[7] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, label(7) , state(7));


    }

    private PinState state(int index) {
        if (enabledPins == null || index > enabledPins.length || !enabledPins[index]) {
            return PinState.LOW;
        } else {
            return PinState.HIGH;
        }
    }

    private String label(int index) {
        String[] slotLabels = configurationService.getSlotLabels();
        if (slotLabels == null ||  index > slotLabels.length  || slotLabels[index] == null || slotLabels[index].trim().isEmpty()){
            return "PIN "+index;
        } else {
            return slotLabels[index];
        }
    }

    @Override
    public boolean isGPIOEnabled(int pin) {
        return pins[pin].isHigh();
    }

    @Override
    public boolean setEnabled(int pin) {
        pins[pin].setState(PinState.HIGH);
        return true;
    }

    @Override
    public boolean setDisabled(int pin) {
        pins[pin].setState(PinState.LOW);
        return true;
    }

    @Override
    public boolean disableAll() {
        int i=0;
        for (GpioPinDigitalOutput pin : pins) {
            if (enabledPins[i++]) {
                pin.setState(PinState.LOW);
            }
        }
        return true;
    }

    @Override
    public boolean enableAll() {
        int i=0;
        for (GpioPinDigitalOutput pin : pins) {
            if (enabledPins[i++]) {
                pin.setState(PinState.HIGH);
            }
        }
        return true;
    }
}
