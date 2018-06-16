package io.crowdcode.belobog.actuator.ui.controller;

import io.crowdcode.belobog.actuator.ui.model.PinInfo;
import io.crowdcode.belobog.actuator.ui.model.SlotInfo;
import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import io.crowdcode.belobog.actuator.ui.service.impl.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Home Controller.
 */
@Controller
public class PinsController {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    GPIOService gpioService;

    @RequestMapping("/pins")
    public String home(Model model) {
        configurationService.prepareStandardModel(model);

        Integer numberOfSlots = configurationService.getNumberOfSlots();
        model.addAttribute("numberOfSlots", numberOfSlots);

        List<SlotInfo> info = new ArrayList<>();
        for (Integer i = 0; i < numberOfSlots;i++){
            SlotInfo slotInfo = configurationService.createSlotInfo(i);
            info.add(slotInfo);
        }

        model.addAttribute("slotInfos", info);


        List<PinInfo> mappedPins = configurationService.getPinMap();

        model.addAttribute("mappedPins",mappedPins);

        return "pins";
    }


}
