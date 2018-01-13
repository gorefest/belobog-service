package io.crowdcode.belobog.actuator.ui.service;

import java.time.LocalDateTime;

public interface GPIOService {
    LocalDateTime started = LocalDateTime.now();

    default LocalDateTime getStartTime() {
        return started;
    }

    boolean isGPIOEnabled(int pin);

    boolean setEnabled(int pin);

    boolean setDisabled(int pin);

    boolean disableAll();

    boolean enableAll();

}
