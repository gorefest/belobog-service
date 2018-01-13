package io.crowdcode.belobog.actuator.ui.controller;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import io.crowdcode.belobog.actuator.ui.service.impl.ConfigurationService;
import io.crowdcode.belobog.actuator.ui.service.impl.TemplateResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ThymeleafSvgController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    GPIOService gpioService;

    @Autowired
    TemplateResolutionService templateResolutionService;

    @RequestMapping(value = "/image")
    public String getSvg(Model model, @RequestParam(required = false) String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
        configurationService.prepareStandardModel(model);
        templateResolutionService.prepareImageTemplate(model);
        return "image";
    }

}
