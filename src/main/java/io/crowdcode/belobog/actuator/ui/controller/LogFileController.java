package io.crowdcode.belobog.actuator.ui.controller;

import io.crowdcode.belobog.actuator.ui.model.LogEntry;
import io.crowdcode.belobog.actuator.ui.service.impl.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Logfile Controller. This
 */
@Controller
public class LogFileController {

    @Autowired
    ConfigurationService configurationService;
    
    @MessageMapping("/log")
    @SendTo("/topic/log")
    public LogEntry log() {
        return new LogEntry(new Date().toString());
    }


    @RequestMapping("/logs")
    public String home(Model model) {
        configurationService.prepareStandardModel(model);
        return "logs";
    }


    @RequestMapping("/logview")
    public String logview(Model model) {
        configurationService.prepareStandardModel(model);
        return "logview";
    }
}
