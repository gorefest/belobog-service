package io.crowdcode.belobog.actuator.ui.controller;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import io.crowdcode.belobog.actuator.ui.service.impl.ConfigurationService;
import io.crowdcode.belobog.actuator.ui.service.impl.SelfTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Self Test Controller to trigger a self test. Returns logview, which is going to open a web socket
 * on {@link io.crowdcode.belobog.actuator.ui.service.impl.LogFileService}, dumping all logs out
 * to the socket.
 */
@Controller
public class SelfTestController {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    SelfTestService selfTestService;


    @RequestMapping("/selftest")
    public String selftest(Model model) {
        configurationService.prepareStandardModel(model);
        selfTestService.scheduleSelfTest();
        return "logview";
    }


}
