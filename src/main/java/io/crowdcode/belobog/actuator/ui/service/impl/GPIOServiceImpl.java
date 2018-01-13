package io.crowdcode.belobog.actuator.ui.service.impl;

import com.pi4j.io.gpio.*;
import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GPIOServiceImpl implements GPIOService {

    public static final Logger logger = LoggerFactory.getLogger(GPIOServiceImpl.class);

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

        pins = new GpioPinDigitalOutput[16];


        pins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, label(0) , state(0));
        pins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, label(1) , state(1));
        pins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, label(2) , state(2));
        pins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, label(3) , state(3));
        pins[4] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, label(4) , state(4));
        pins[5] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, label(5) , state(5));
        pins[6] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, label(6) , state(6));
        pins[7] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, label(7) , state(7));
        pins[8] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, label(8) , state(8));
        pins[9] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, label(9) , state(9));
        pins[10] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, label(10) , state(10));
        pins[11] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, label(11) , state(11));
        pins[12] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, label(12) , state(12));
        pins[13] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, label(13) , state(13));
        pins[14] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, label(14) , state(14));
        pins[15] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, label(15) , state(15));


    }

    private PinState state(int index) {
        if (enabledPins != null && index <= enabledPins.length && enabledPins[index]) {
            return pins[index].getState();
        } else {
            return PinState.LOW;
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
        GpioPinDigitalOutput gpioPin = pins[pin];
        logger.info("PIN "+gpioPin.getName()+" FROM "+gpioPin.getState().toString()+" TO "+PinState.HIGH.toString());
        gpioPin.setState(PinState.HIGH);
        return true;
    }

    @Override
    public boolean setDisabled(int pin) {
        GpioPinDigitalOutput gpioPin = pins[pin];
        logger.info("PIN "+gpioPin.getName()+" FROM "+gpioPin.getState().toString()+" TO "+PinState.LOW.toString());
        gpioPin.setState(PinState.LOW);
        return true;
    }

    @Override
    public boolean disableAll() {
        int i=0;
        for (GpioPinDigitalOutput pin : pins) {
            if (enabledPins[i++]) {
                logger.info("PIN "+pin.getName()+" FROM "+pin.getState().toString()+" TO "+PinState.LOW.toString());
                pin.setState(PinState.LOW);
            } else {
                logger.info("PIN "+pin.getName()+" IS DISABLED AND SKIPPED");
            }
        }
        return true;
    }

    @Override
    public boolean enableAll() {
        int i=0;
        for (GpioPinDigitalOutput pin : pins) {
            if (enabledPins[i++]) {
                logger.info("PIN "+pin.getName()+" FROM "+pin.getState().toString()+" TO "+PinState.HIGH.toString());
                pin.setState(PinState.HIGH);
            } else {
                logger.info("PIN "+pin.getName()+" IS DISABLED AND SKIPPED");
            }
        }
        return true;
    }
}
