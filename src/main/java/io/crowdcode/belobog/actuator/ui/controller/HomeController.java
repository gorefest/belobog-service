package io.crowdcode.belobog.actuator.ui.controller;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import io.crowdcode.belobog.actuator.ui.service.impl.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home Controller.
 */
@Controller
public class HomeController {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    GPIOService gpioService;

    @RequestMapping("/")
    public String home(Model model) {
        configurationService.prepareStandardModel(model);
        return "redirect:/image";
    }


}
