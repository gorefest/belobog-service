package io.crowdcode.belobog.actuator.ui.service.impl;

import io.crowdcode.belobog.actuator.ui.model.PinInfo;
import io.crowdcode.belobog.actuator.ui.model.SlotInfo;
import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.context.AbstractContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    @Value("${application.messages.welcome}")
    private String applicationWelcome;

    @Value("${application.messages.uptime}")
    private String applicationUptime;

    @Value("${application.template}")
    private String template;

    @Value("${application.numberOfPins}")
    Integer numberOfPins;

    @Value("${application.numberOfGPIOSlots}")
    Integer numberOfSlots;

    @Value("${slot.default.enabled.style}")
    String defaultEnabledStyle;

    @Value("${slot.default.disabled.style}")
    String defaultDisabledStyle;

    @Value("${slot.default.isActivated}")
    Boolean defaultIsActivated;

    @Value("${slot.default.isEnabled}")
    Boolean defaultIsEnabled;

    @Value("${slot.default.invertHiLo}")
    Boolean defaultInvertedHiLo;

    String[] slotEnabledStyles;
    String[] slotDisabledStyles;
    String[] slotLabels;
    boolean[] activeSlots;
    boolean[] enabledSlots;
    Integer[] slot2Pin;
    boolean[] enabledPins;
    boolean[] activePins;
    boolean[] invertHiLo;
    boolean[] invertHiLoPins;

    @Autowired
    private Environment env;

    @PostConstruct
    public void postConstruct(){

        slotDisabledStyles=new String[numberOfSlots];
        slotEnabledStyles=new String[numberOfSlots];
        slotLabels = new String[numberOfSlots];
        activeSlots = new boolean[numberOfSlots];
        slot2Pin = new Integer[numberOfSlots];
        enabledSlots = new boolean[numberOfSlots];
        invertHiLo=new boolean[numberOfSlots];

        enabledPins=new boolean[128];
        activePins=new boolean[128];
        invertHiLoPins=new boolean[128];

        for (int i = 1; i <= numberOfSlots; i++) {
            slotEnabledStyles[i-1]  =getProperty("slot"+i+".enabled.style", defaultEnabledStyle);
            slotDisabledStyles[i-1] =getProperty("slot"+i+".disabled.style", defaultDisabledStyle);
            activeSlots[i-1] = Boolean.valueOf(getProperty("slot"+i+".isActivated", Boolean.valueOf(defaultIsActivated).toString()));
            slot2Pin[i-1] = Integer.valueOf(getProperty("slot"+i+".gpioPin","-1"));
            slotLabels[i-1] = getProperty("slot"+i+".title","slot "+i);
            enabledSlots[i-1] = Boolean.valueOf(getProperty("slot"+i+".isEnabled", Boolean.valueOf(defaultIsEnabled).toString()));
            invertHiLo[i-1] = Boolean.valueOf(getProperty("slot"+i+".invertHiLo", Boolean.valueOf(defaultInvertedHiLo).toString()));

            Integer pin = slot2Pin[i-1];

            // map slot attributes to specific GPIO pins
            boolean enabled = enabledSlots[i-1];
            enabledPins[pin] = enabled;

            boolean active = activeSlots[i-1];
            activePins[pin] = active;

            boolean invert = invertHiLo[i-1];
            invertHiLoPins[pin] = invert;

        }

    }

    private String getProperty(String s, String fallback) {
        Object value = env.getProperty(s);
        if (value == null) { return fallback; }
        return value.toString();
    }


    public String getApplicationWelcome() {
        return applicationWelcome;
    }

    public String getApplicationUptime() {
        return applicationUptime;
    }

    public String getTemplate() {
        return template;
    }




    public void prepareStandardModel(Model model) {
        model.addAttribute("title", getApplicationWelcome());

        String uptime = String.format(getApplicationUptime(), GPIOService.started);
        model.addAttribute("message", uptime);

        model.addAttribute("date", new Date());

        model.addAttribute("template",getTemplate());

    }

    public Integer getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(Integer numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public String getDefaultEnabledStyle() {
        return defaultEnabledStyle;
    }

    public String getDefaultDisabledStyle() {
        return defaultDisabledStyle;
    }

    public Boolean getDefaultisActivated() {
        return defaultIsActivated;
    }

    public String[] getSlotEnabledStyles() {
        return slotEnabledStyles;
    }

    public String[] getSlotDisabledStyles() {
        return slotDisabledStyles;
    }

    public String[] getSlotLabels() {
        return slotLabels;
    }

    public boolean[] getActiveSlots() {
        return activeSlots;
    }

    public void setActiveSlots(boolean[] activeSlots) {
        this.activeSlots = activeSlots;
    }

    public Integer[] getSlot2Pin() {
        return slot2Pin;
    }

    public boolean[] getEnabledSlots() {
        return enabledSlots;
    }

    public boolean[] getActivePins() {
        return activePins;
    }

    public boolean[] getInvertHiLoPins() {
        return invertHiLoPins;
    }

    public SlotInfo createSlotInfo(Integer number) {
        SlotInfo result = new SlotInfo();
        result.setActivated(activePins[slot2Pin[number]]);
        result.setEnabled(enabledSlots[number]);
        result.setInvertHiLo(invertHiLo[number]);
        result.setSlotNumber(number);
        result.setTitle(slotLabels[number]);
        result.setPinNumber(slot2Pin[number]);

        return result;
    }

    public List<PinInfo> getPinMap() {
        List<PinInfo> result = new ArrayList<>();
        boolean[] pins = new boolean[numberOfPins];

        for (int i = 0; i < numberOfSlots; i++) {
            pins[slot2Pin[i]] = true;
        }

        int i = 1;
        for (boolean pin : pins) {
            PinInfo info = new PinInfo();
            info.setPin(i++);
            info.setEnabled(pin);
            result.add(info);
        }
        return result;
    }
}
