package io.crowdcode.belobog.actuator.ui.controller;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import io.crowdcode.belobog.actuator.ui.service.impl.ConfigurationService;
import io.crowdcode.belobog.actuator.ui.service.impl.TemplateResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * Slot Controller controlling a slot mapped to a gpio pin (e.g. a relay or a switch).
 *
 */
@Controller
public class SlotController {

    @Autowired
    GPIOService gpioService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    TemplateResolutionService templateResolutionService;

    @RequestMapping("/switch/{slot}")
    public String switchSlot(Model model, @PathVariable("slot") String slot) throws IOException {
        configurationService.prepareStandardModel(model);

        Integer slotnumber = (Integer.valueOf(slot.replace("slot","")))-1;

        Integer pin = configurationService.getSlot2Pin()[slotnumber];

        if (gpioService.isGPIOEnabled(pin)) {
            gpioService.setDisabled(pin);
        } else {
            gpioService.setEnabled(pin);
        }

        configurationService.prepareStandardModel(model);
        templateResolutionService.prepareImageTemplate(model);
        return "redirect:/image";
    }

}
